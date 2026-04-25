package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunityPost {
    private Long id;
    private Long authorId;
    private String postType;
    private String title;
    private String summary;
    private String cover;
    private String mediaUrl;
    private String content;
    private String practiceTag;
    private LocalDateTime createTime;
}
