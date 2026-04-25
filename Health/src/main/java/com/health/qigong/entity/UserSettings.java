package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSettings {
    private Long id;
    private Long userId;
    private Integer dailyCalorieGoal;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
