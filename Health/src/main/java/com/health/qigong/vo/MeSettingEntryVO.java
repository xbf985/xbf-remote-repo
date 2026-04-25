package com.health.qigong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeSettingEntryVO {
    private String key;
    private String label;
    private String value;
}
