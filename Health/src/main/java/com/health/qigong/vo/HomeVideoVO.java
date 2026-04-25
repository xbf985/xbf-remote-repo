package com.health.qigong.vo;

import lombok.Data;

@Data
public class HomeVideoVO {
    private Long id;
    private Long categoryId;
    private String title;
    private String cover;
    private String mediaUrl;
    private String effect;
    private Long duration;
    private String authorName;
    private Long qiValue;
    private Boolean liked;
    private Boolean collected;
}
