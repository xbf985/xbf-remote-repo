package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingRoomPlan {
    private Long id;
    private String title;
    private String sceneCode;
    private String goalCode;
    private String masterCode;
    private String methodCode;
    private Long videoId;
    private String intro;
    private String mantra;
    private Integer estimatedDurationSeconds;
    private Integer estimatedCalories;
    private Boolean supportPostureModel;
    private Boolean supportBreathModel;
    private Integer priority;
    private LocalDateTime createTime;
}
