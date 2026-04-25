package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingRoomSession {
    private Long id;
    private Long userId;
    private Long planId;
    private Long videoId;
    private Long recipeId;
    private String sceneCode;
    private String goalCode;
    private String masterCode;
    private String methodCode;
    private Boolean enableScore;
    private Boolean enablePostureModel;
    private Boolean enableBreathModel;
    private String status;
    private Integer durationSeconds;
    private Integer caloriesBurned;
    private Integer postureScore;
    private Integer breathScore;
    private Integer totalScore;
    private String postureIssueCodes;
    private String breathIssueCodes;
    private String practiceVideoUrl;
    private String practiceVideoCover;
    private Integer practiceVideoDurationSeconds;
    private String summaryText;
    private String adviceText;
    private String recipeReason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
}
