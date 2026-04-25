package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunityFeedItemVO {
    private Long id;
    private String postType;
    private String practiceTag;
    private String title;
    private String summary;
    private String cover;
    private String mediaUrl;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Boolean liked;
    private Boolean collected;
    private LocalDateTime createTime;
}
