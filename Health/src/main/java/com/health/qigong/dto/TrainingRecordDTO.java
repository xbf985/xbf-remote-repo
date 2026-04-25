package com.health.qigong.dto;

import lombok.Data;

@Data
public class TrainingRecordDTO {
    private Long trainingRoomSessionId;
    private String trainingName;
    private Integer duration;
    private Integer calories;
    private String practiceVideoUrl;
    private String practiceVideoCover;
    private Integer practiceVideoDurationSeconds;
}
