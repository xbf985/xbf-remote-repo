package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingRoomHistoryItemVO {
    private Long sessionId;
    private String title;
    private String goalLabel;
    private String methodLabel;
    private Integer totalScore;
    private Integer durationSeconds;
    private Integer caloriesBurned;
    private String status;
    private String practiceVideoUrl;
    private String practiceVideoCover;
    private Integer practiceVideoDurationSeconds;
    private LocalDateTime startTime;
}
