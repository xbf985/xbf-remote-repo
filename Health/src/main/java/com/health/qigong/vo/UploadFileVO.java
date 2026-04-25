package com.health.qigong.vo;

import lombok.Data;

@Data
public class UploadFileVO {
    private String fileName;
    private String originalName;
    private String contentType;
    private Long size;
    private String url;
}
