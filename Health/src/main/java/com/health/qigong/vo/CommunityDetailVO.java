package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommunityDetailVO {
    private Long id;
    private String postType;
    private String practiceTag;
    private String title;
    private String summary;
    private String cover;
    private String mediaUrl;
    private String content;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Boolean liked;
    private Boolean collected;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private String authorRealm;
    private Integer authorFollowerCount;
    private Boolean followed;
    private List<CommunityCommentVO> comments;
}
