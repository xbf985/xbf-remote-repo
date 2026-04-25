package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Video {
    private Long id;
    private Long categoryId;
    private String videoName;
    private String title;
    private String cover;
    private String videoUrl;
    private String intro;
    private String authorName;
    private Long durationSeconds;
    private Long qiValue;
    private LocalDateTime createTime;
}
