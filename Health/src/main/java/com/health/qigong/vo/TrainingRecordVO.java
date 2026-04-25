package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingRecordVO {
    private Long trainingRoomSessionId;
    private String trainingName;
    private Integer duration;
    private Integer calories;
    private String practiceVideoUrl;
    private String practiceVideoCover;
    private Integer practiceVideoDurationSeconds;
    private LocalDateTime createTime;
}
