package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class MeSettingsVO {
    private String username;
    private String phone;
    private Integer dailyCalorieGoal;
    private List<MeSettingEntryVO> accountItems;
    private List<MeSettingEntryVO> preferenceItems;
    private List<MeSettingEntryVO> commonItems;
}
