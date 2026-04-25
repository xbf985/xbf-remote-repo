package com.health.qigong.service;

import com.health.qigong.vo.UploadFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    UploadFileVO store(MultipartFile file, String folder);
}
