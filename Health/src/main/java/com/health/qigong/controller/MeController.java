package com.health.qigong.controller;

import com.health.qigong.dto.UpdateCalorieGoalDTO;
import com.health.qigong.dto.UpdateProfileDTO;
import com.health.qigong.result.Result;
import com.health.qigong.service.MeService;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.MeCenterVO;
import com.health.qigong.vo.MeDiaryPageVO;
import com.health.qigong.vo.MeSettingsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private MeService meService;

    @GetMapping("/center")
    public Result<MeCenterVO> center() {
        Long userId = UserContext.getUserId();
        return Result.success(meService.getCenter(userId));
    }

    @GetMapping("/diary")
    public Result<MeDiaryPageVO> diary(@RequestParam(value = "tab", defaultValue = "all") String tab) {
        Long userId = UserContext.getUserId();
        return Result.success(meService.getDiary(userId, tab));
    }

    @GetMapping("/settings")
    public Result<MeSettingsVO> settings() {
        Long userId = UserContext.getUserId();
        return Result.success(meService.getSettings(userId));
    }

    @PutMapping("/settings/calorie-goal")
    public Result<Void> updateCalorieGoal(@RequestBody UpdateCalorieGoalDTO dto) {
        Long userId = UserContext.getUserId();
        meService.updateCalorieGoal(userId, dto);
        return Result.successMsg("Calorie goal updated");
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody UpdateProfileDTO dto) {
        Long userId = UserContext.getUserId();
        meService.updateProfile(userId, dto);
        return Result.successMsg("Profile updated");
    }
}
