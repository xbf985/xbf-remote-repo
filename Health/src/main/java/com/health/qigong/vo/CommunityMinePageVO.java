package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommunityMinePageVO {
    private String currentTab;
    private List<CommunityTabVO> tabs;
    private List<CommunityFeedItemVO> items;
}
