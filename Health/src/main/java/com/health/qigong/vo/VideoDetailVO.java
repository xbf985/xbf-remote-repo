package com.health.qigong.vo;

import lombok.Data;

@Data
public class VideoDetailVO {
    private Long id;
    private Long categoryId;
    private String title;
    private String cover;
    private String videoUrl;
    private String intro;
    private String authorName;
    private Long durationSeconds;
    private Long qiValue;
    private Boolean liked;
    private Boolean collected;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
}
