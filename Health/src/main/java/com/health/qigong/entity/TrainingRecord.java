package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingRecord {
    private Long id;
    private Long userId;
    private Long trainingRoomSessionId;
    private String trainingName;
    private Integer duration;
    private int calories;
    private String practiceVideoUrl;
    private String practiceVideoCover;
    private Integer practiceVideoDurationSeconds;
    private LocalDateTime createTime;
}
