package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class TrainingRoomConfigVO {
    private List<TrainingRoomFeatureVO> featureOptions;
    private List<TrainingRoomOptionItemVO> sceneOptions;
    private List<TrainingRoomOptionItemVO> goalOptions;
    private List<TrainingRoomOptionItemVO> methodOptions;
    private List<TrainingRoomOptionItemVO> masterOptions;
}
