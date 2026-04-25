package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class FavoritePageVO {
    private String currentTab;
    private List<FavoriteTabVO> tabs;
    private List<HistoryItemVO> items;
}
