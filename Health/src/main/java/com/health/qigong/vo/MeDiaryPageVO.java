package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class MeDiaryPageVO {
    private String currentTab;
    private Integer totalRecordCount;
    private Integer totalFavoriteCount;
    private Integer totalViewCount;
    private Integer totalCheckinDays;
    private Integer totalTrainingCount;
    private Integer totalCalories;
    private Integer totalDuration;
    private List<MeDiaryTabVO> tabs;
    private List<MeDiaryItemVO> items;
}
