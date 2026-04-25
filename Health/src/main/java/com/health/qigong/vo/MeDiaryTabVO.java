package com.health.qigong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeDiaryTabVO {
    private String code;
    private String label;
    private Integer count;
}
