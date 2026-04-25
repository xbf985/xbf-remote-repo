package com.health.qigong.service.impl;

import com.health.qigong.entity.Checkin;
import com.health.qigong.entity.Recipe;
import com.health.qigong.entity.TrainingRecord;
import com.health.qigong.entity.User;
import com.health.qigong.mapper.CheckinMapper;
import com.health.qigong.mapper.RecipeMapper;
import com.health.qigong.mapper.TrainingRecordMapper;
import com.health.qigong.mapper.UserMapper;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.service.HomeService;
import com.health.qigong.service.VideoService;
import com.health.qigong.vo.HomeCheckinCalendarVO;
import com.health.qigong.vo.HomeCourseCategoryVO;
import com.health.qigong.vo.HomeOverviewVO;
import com.health.qigong.vo.HomeRecipeCardVO;
import com.health.qigong.vo.HomeRecipeSectionVO;
import com.health.qigong.vo.HomeVideoVO;
import com.health.qigong.vo.HistoryItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private TrainingRecordMapper trainingRecordMapper;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private VideoService videoService;

    @Override
    public HomeOverviewVO getOverview(Long userId) {
        User user = userMapper.selectById(userId);
        Integer practiceDays = checkinMapper.countDaysByUserId(userId);
        Integer totalDuration = trainingRecordMapper.sumDurationByUserId(userId);
        Integer todayCalories = trainingRecordMapper.sumTodayCaloriesByUserId(userId, LocalDate.now());
        Integer qiValue = buildQiValue(practiceDays, totalDuration);

        HomeOverviewVO overview = new HomeOverviewVO();
        overview.setUserId(userId);
        overview.setNickname(user == null ? "修行者" : user.getUsername());
        overview.setAvatar(user == null ? null : user.getAvatar());
        overview.setLevel(calcLevel(totalDuration));
        overview.setPracticeDays(defaultZero(practiceDays));
        overview.setTodayCalories(defaultZero(todayCalories));
        overview.setQiValue(qiValue);
        overview.setNextLevelQi(Math.max(0, nextLevelThreshold(qiValue) - qiValue));
        overview.setCommonCourses(buildCommonCourses(userId));
        return overview;
    }

    @Override
    public HomeCheckinCalendarVO getCheckinCalendar(Long userId, int year, int month) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = YearMonth.of(year, month).atEndOfMonth();
        List<Checkin> monthRecords = checkinMapper.selectByUserAndDateRange(userId, monthStart, monthEnd);
        List<LocalDate> checkinDates = monthRecords.stream()
                .map(Checkin::getCheckDate)
                .sorted()
                .collect(Collectors.toList());

        HomeCheckinCalendarVO calendar = new HomeCheckinCalendarVO();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setContinuousDays(countContinuousDays(userId));
        calendar.setTotalDays(defaultZero(checkinMapper.countDaysByUserId(userId)));
        calendar.setQiPoints(buildQiValue(calendar.getTotalDays(), trainingRecordMapper.sumDurationByUserId(userId)));
        calendar.setRanking(999);
        calendar.setTodayChecked(checkinDates.contains(LocalDate.now()));
        calendar.setCheckinDates(checkinDates);
        return calendar;
    }

    @Override
    public List<HistoryItemVO> getFavorites(Long userId, Integer contentType, Integer limit) {
        List<HistoryItemVO> items = favoriteService.list(userId);
        return items.stream()
                .filter(item -> contentType == null || contentType.equals(item.getContentType()))
                .limit(limit == null || limit < 1 ? 10L : limit.longValue())
                .collect(Collectors.toList());
    }

    @Override
    public HomeRecipeSectionVO getSeasonalRecipes(Long userId, Long categoryId) {
        List<Recipe> recipes = recipeMapper.list(categoryId);
        List<HomeRecipeCardVO> cards = new ArrayList<>();
        for (Recipe recipe : recipes) {
            HomeRecipeCardVO card = new HomeRecipeCardVO();
            card.setId(recipe.getId());
            card.setName(recipe.getName());
            card.setCover(recipe.getCover());
            card.setEffect(recipe.getEffect());
            card.setIngredients(recipe.getIngredients());
            card.setCollectCount((long) favoriteService.count(recipe.getId(), 2));
            card.setCollected(favoriteService.exists(userId, recipe.getId(), 2));
            cards.add(card);
        }

        HomeRecipeSectionVO section = new HomeRecipeSectionVO();
        section.setCategoryId(categoryId);
        section.setCategoryName(resolveSolarTermName(categoryId));
        section.setSeasonalTips(resolveSolarTermTips(categoryId));
        section.setRecipes(cards);
        return section;
    }

    @Override
    public List<HomeCourseCategoryVO> getCourseCategories() {
        return videoService.listCategories();
    }

    @Override
    public List<HomeVideoVO> getCourseVideos(Long userId, Long categoryId) {
        return videoService.listByCategory(userId, categoryId);
    }

    private int countContinuousDays(Long userId) {
        List<Checkin> records = checkinMapper.selectByUserId(userId);
        if (records.isEmpty()) {
            return 0;
        }

        List<LocalDate> dates = records.stream()
                .map(Checkin::getCheckDate)
                .distinct()
                .sorted((a, b) -> b.compareTo(a))
                .collect(Collectors.toList());

        int streak = 0;
        LocalDate cursor = LocalDate.now();
        if (!dates.contains(cursor) && dates.contains(cursor.minusDays(1))) {
            cursor = cursor.minusDays(1);
        }

        for (LocalDate date : dates) {
            if (date.equals(cursor)) {
                streak++;
                cursor = cursor.minusDays(1);
            } else if (date.isBefore(cursor)) {
                break;
            }
        }
        return streak;
    }

    private int buildQiValue(Integer practiceDays, Integer totalDuration) {
        return defaultZero(practiceDays) * 5 + defaultZero(totalDuration);
    }

    private int nextLevelThreshold(int qiValue) {
        if (qiValue < 100) {
            return 100;
        }
        if (qiValue < 300) {
            return 300;
        }
        if (qiValue < 600) {
            return 600;
        }
        if (qiValue < 1000) {
            return 1000;
        }
        if (qiValue < 1500) {
            return 1500;
        }
        return qiValue;
    }

    private String calcLevel(Integer totalDuration) {
        int duration = defaultZero(totalDuration);
        if (duration < 100) {
            return "筑基初期";
        }
        if (duration < 300) {
            return "筑基后期";
        }
        if (duration < 600) {
            return "结丹期";
        }
        if (duration < 1000) {
            return "元婴期";
        }
        if (duration < 1500) {
            return "化神期";
        }
        return "大乘期";
    }

    private List<String> buildCommonCourses(Long userId) {
        List<TrainingRecord> records = trainingRecordMapper.selectByUserId(userId);
        List<String> names = records.stream()
                .map(TrainingRecord::getTrainingName)
                .filter(name -> name != null && !name.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .limit(2)
                .collect(Collectors.toList());
        if (names.isEmpty()) {
            return Arrays.asList("八段锦", "太极拳");
        }
        return names;
    }

    private String resolveSolarTermName(Long categoryId) {
        if (categoryId == 1L) {
            return "立春";
        }
        if (categoryId == 2L) {
            return "惊蛰";
        }
        if (categoryId == 3L) {
            return "立夏";
        }
        if (categoryId == 4L) {
            return "夏至";
        }
        if (categoryId == 5L) {
            return "立秋";
        }
        if (categoryId == 6L) {
            return "白露";
        }
        if (categoryId == 7L) {
            return "立冬";
        }
        if (categoryId == 8L) {
            return "冬至";
        }
        return "时令养生";
    }

    private String resolveSolarTermTips(Long categoryId) {
        if (categoryId == 1L) {
            return "阳气初生，万物复苏，宜省酸增甘，养阳敛阴";
        }
        if (categoryId == 2L) {
            return "春雷始鸣，肝木渐旺，宜疏肝理气，兼顾健脾化湿";
        }
        if (categoryId == 3L) {
            return "阳盛渐显，湿热易生，宜健脾祛湿，益气生津";
        }
        if (categoryId == 4L) {
            return "阳极阴生，暑气最盛，宜清心安神，养阴生津";
        }
        if (categoryId == 5L) {
            return "燥气渐起，宜润肺养阴，少辛增酸，以防秋燥";
        }
        if (categoryId == 6L) {
            return "白露夜凉，宜润肺和胃，养阴止燥，兼顾脾胃";
        }
        if (categoryId == 7L) {
            return "万物潜藏，宜温补脾肾，养藏固本，少食寒凉";
        }
        if (categoryId == 8L) {
            return "阴极阳生，宜温阳补虚，养血驱寒，以蓄来春之气";
        }
        return "顺时而食，调养身心";
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }
}
