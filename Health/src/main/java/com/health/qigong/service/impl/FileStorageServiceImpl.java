package com.health.qigong.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.health.qigong.exception.BusinessException;
import com.health.qigong.service.FileStorageService;
import com.health.qigong.vo.UploadFileVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.storage-type:local}")
    private String storageType;

    @Value("${app.upload-dir:${user.dir}/uploads}")
    private String uploadDir;

    @Value("${app.oss.endpoint:}")
    private String ossEndpoint;

    @Value("${app.oss.bucket:}")
    private String ossBucket;

    @Value("${app.oss.public-base-url:}")
    private String ossPublicBaseUrl;

    @Value("${app.oss.access-key-id:}")
    private String ossAccessKeyId;

    @Value("${app.oss.access-key-secret:}")
    private String ossAccessKeySecret;

    @Override
    public UploadFileVO store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "Upload file cannot be empty");
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String safeFolder = (folder == null || folder.isBlank()) ? "community" : folder.trim();
        String datePath = LocalDate.now().toString().replace("-", "");
        String fileName = UUID.randomUUID().toString().replace("-", "") + extension;
        String objectKey = safeFolder + "/" + datePath + "/" + fileName;

        if ("oss".equalsIgnoreCase(storageType)) {
            return storeToOss(file, safeFolder, fileName, objectKey);
        }

        Path targetDir = Paths.get(uploadDir, safeFolder, datePath);
        Path targetFile = targetDir.resolve(fileName);

        try {
            Files.createDirectories(targetDir);
            file.transferTo(targetFile.toFile());
        } catch (IOException e) {
            throw new BusinessException(500, "File upload failed");
        }

        UploadFileVO vo = new UploadFileVO();
        vo.setFileName(fileName);
        vo.setOriginalName(originalName);
        vo.setContentType(file.getContentType());
        vo.setSize(file.getSize());
        vo.setUrl("/uploads/" + objectKey);
        return vo;
    }

    private UploadFileVO storeToOss(MultipartFile file, String safeFolder, String fileName, String objectKey) {
        if (isBlank(ossEndpoint) || isBlank(ossBucket) || isBlank(ossAccessKeyId) || isBlank(ossAccessKeySecret)) {
            throw new BusinessException(500, "OSS configuration is incomplete");
        }

        OSS client = null;
        try (InputStream inputStream = file.getInputStream()) {
            client = new OSSClientBuilder().build(ossEndpoint, ossAccessKeyId, ossAccessKeySecret);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            if (!isBlank(file.getContentType())) {
                metadata.setContentType(file.getContentType());
            }
            client.putObject(ossBucket, objectKey, inputStream, metadata);

            UploadFileVO vo = new UploadFileVO();
            vo.setFileName(fileName);
            vo.setOriginalName(file.getOriginalFilename());
            vo.setContentType(file.getContentType());
            vo.setSize(file.getSize());
            vo.setUrl(buildPublicUrl(objectKey));
            return vo;
        } catch (IOException e) {
            throw new BusinessException(500, "OSS file upload failed");
        } catch (Exception e) {
            throw new BusinessException(500, "OSS file upload failed");
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }

    private String buildPublicUrl(String objectKey) {
        String baseUrl = ossPublicBaseUrl;
        if (!isBlank(baseUrl)) {
            return stripTrailingSlash(baseUrl) + "/" + objectKey;
        }

        String endpoint = stripScheme(ossEndpoint);
        return "https://" + ossBucket + "." + endpoint + "/" + objectKey;
    }

    private String stripScheme(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceFirst("^https?://", "");
    }

    private String stripTrailingSlash(String value) {
        if (value == null) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
