package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class AuthorProfileVO {
    private Long authorId;
    private String authorName;
    private String avatar;
    private String realm;
    private String verifiedLabel;
    private String bio;
    private String location;
    private Boolean followed;
    private Integer followingCount;
    private Integer followerCount;
    private Integer totalSupportCount;
    private String currentTab;
    private List<CommunityTabVO> tabs;
    private List<CommunityFeedItemVO> works;
}
