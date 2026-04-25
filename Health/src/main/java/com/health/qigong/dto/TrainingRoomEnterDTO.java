package com.health.qigong.dto;

import lombok.Data;

@Data
public class TrainingRoomEnterDTO {
    private String sceneCode;
    private String goalCode;
    private String masterCode;
    private String methodCode;
    private Boolean enableScore;
    private Boolean enablePostureModel;
    private Boolean enableBreathModel;
}
