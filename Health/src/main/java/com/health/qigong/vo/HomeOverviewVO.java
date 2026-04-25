package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class HomeOverviewVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private String level;
    private Integer practiceDays;
    private Integer todayCalories;
    private Integer qiValue;
    private Integer nextLevelQi;
    private List<String> commonCourses;
}
