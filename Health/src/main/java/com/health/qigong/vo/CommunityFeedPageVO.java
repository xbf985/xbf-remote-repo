package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommunityFeedPageVO {
    private String currentTab;
    private String keyword;
    private List<CommunityTabVO> tabs;
    private List<CommunityFeedItemVO> items;
}
