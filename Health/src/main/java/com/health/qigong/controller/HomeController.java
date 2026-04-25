package com.health.qigong.controller;

import com.health.qigong.result.Result;
import com.health.qigong.service.HomeService;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.HomeCheckinCalendarVO;
import com.health.qigong.vo.HomeCourseCategoryVO;
import com.health.qigong.vo.HomeOverviewVO;
import com.health.qigong.vo.HomeRecipeSectionVO;
import com.health.qigong.vo.HomeVideoVO;
import com.health.qigong.vo.HistoryItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/overview")
    public Result<HomeOverviewVO> overview() {
        Long userId = UserContext.getUserId();
        return Result.success(homeService.getOverview(userId));
    }

    @GetMapping("/checkin/calendar")
    public Result<HomeCheckinCalendarVO> checkinCalendar(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        Long userId = UserContext.getUserId();
        return Result.success(homeService.getCheckinCalendar(userId, year, month));
    }

    @GetMapping("/favorites")
    public Result<List<HistoryItemVO>> favorites(
            @RequestParam(value = "contentType", required = false) Integer contentType,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        Long userId = UserContext.getUserId();
        return Result.success(homeService.getFavorites(userId, contentType, limit));
    }

    @GetMapping("/recipes/seasonal")
    public Result<HomeRecipeSectionVO> seasonalRecipes(@RequestParam(value = "categoryId", defaultValue = "1") Long categoryId) {
        Long userId = UserContext.getUserId();
        return Result.success(homeService.getSeasonalRecipes(userId, categoryId));
    }

    @GetMapping("/courses")
    public Result<List<HomeCourseCategoryVO>> courses() {
        return Result.success(homeService.getCourseCategories());
    }

    @GetMapping("/courses/{categoryId}/videos")
    public Result<List<HomeVideoVO>> courseVideos(@PathVariable("categoryId") Long categoryId) {
        Long userId = UserContext.getUserId();
        return Result.success(homeService.getCourseVideos(userId, categoryId));
    }
}
