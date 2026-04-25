package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class MeCenterVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private String realm;
    private String profileTag;
    private String currentQiState;
    private Integer practiceDays;
    private Integer breathHours;
    private Integer qiValue;
    private String bio;
    private List<MeAchievementVO> achievements;
}
