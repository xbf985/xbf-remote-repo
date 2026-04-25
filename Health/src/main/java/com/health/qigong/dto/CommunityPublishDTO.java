package com.health.qigong.dto;

import lombok.Data;

@Data
public class CommunityPublishDTO {
    private String postType;
    private String title;
    private String summary;
    private String cover;
    private String mediaUrl;
    private String content;
    private String practiceTag;
}
