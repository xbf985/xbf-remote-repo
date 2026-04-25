package com.health.qigong.service;

import com.health.qigong.dto.UpdateCalorieGoalDTO;
import com.health.qigong.dto.UpdateProfileDTO;
import com.health.qigong.vo.MeCenterVO;
import com.health.qigong.vo.MeDiaryPageVO;
import com.health.qigong.vo.MeSettingsVO;

public interface MeService {

    MeCenterVO getCenter(Long userId);

    MeDiaryPageVO getDiary(Long userId, String tab);

    MeSettingsVO getSettings(Long userId);

    void updateCalorieGoal(Long userId, UpdateCalorieGoalDTO dto);

    void updateProfile(Long userId, UpdateProfileDTO dto);
}
