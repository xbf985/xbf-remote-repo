package com.health.qigong.service;

import com.health.qigong.vo.HomeCheckinCalendarVO;
import com.health.qigong.vo.HomeCourseCategoryVO;
import com.health.qigong.vo.HomeOverviewVO;
import com.health.qigong.vo.HomeRecipeSectionVO;
import com.health.qigong.vo.HomeVideoVO;
import com.health.qigong.vo.HistoryItemVO;

import java.util.List;

public interface HomeService {

    HomeOverviewVO getOverview(Long userId);

    HomeCheckinCalendarVO getCheckinCalendar(Long userId, int year, int month);

    List<HistoryItemVO> getFavorites(Long userId, Integer contentType, Integer limit);

    HomeRecipeSectionVO getSeasonalRecipes(Long userId, Long categoryId);

    List<HomeCourseCategoryVO> getCourseCategories();

    List<HomeVideoVO> getCourseVideos(Long userId, Long categoryId);
}
