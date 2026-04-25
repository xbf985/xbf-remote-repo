package com.health.qigong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainingRoomOptionItemVO {
    private String code;
    private String label;
    private String description;
    private Integer sort;
}
