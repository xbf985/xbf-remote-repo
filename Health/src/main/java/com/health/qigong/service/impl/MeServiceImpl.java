package com.health.qigong.service.impl;

import com.health.qigong.dto.UpdateCalorieGoalDTO;
import com.health.qigong.dto.UpdateProfileDTO;
import com.health.qigong.entity.CommunityPost;
import com.health.qigong.entity.ContentViewRecord;
import com.health.qigong.entity.Recipe;
import com.health.qigong.entity.User;
import com.health.qigong.entity.UserSettings;
import com.health.qigong.entity.Video;
import com.health.qigong.exception.BusinessException;
import com.health.qigong.mapper.CheckinMapper;
import com.health.qigong.mapper.CommunityPostMapper;
import com.health.qigong.mapper.ContentViewRecordMapper;
import com.health.qigong.mapper.RecipeMapper;
import com.health.qigong.mapper.TrainingRecordMapper;
import com.health.qigong.mapper.UserSettingsMapper;
import com.health.qigong.mapper.VideoMapper;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.service.MeService;
import com.health.qigong.service.UserService;
import com.health.qigong.vo.HistoryItemVO;
import com.health.qigong.vo.MeAchievementVO;
import com.health.qigong.vo.MeCenterVO;
import com.health.qigong.vo.MeDiaryItemVO;
import com.health.qigong.vo.MeDiaryPageVO;
import com.health.qigong.vo.MeDiaryTabVO;
import com.health.qigong.vo.MeSettingEntryVO;
import com.health.qigong.vo.MeSettingsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MeServiceImpl implements MeService {

    @Autowired
    private UserService userService;

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private TrainingRecordMapper trainingRecordMapper;

    @Autowired
    private UserSettingsMapper userSettingsMapper;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ContentViewRecordMapper contentViewRecordMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private CommunityPostMapper communityPostMapper;

    @Override
    public MeCenterVO getCenter(Long userId) {
        User user = getUser(userId);
        int practiceDays = defaultZero(checkinMapper.countDaysByUserId(userId));
        int totalDuration = defaultZero(trainingRecordMapper.sumDurationByUserId(userId));
        int qiValue = practiceDays * 5 + totalDuration;

        MeCenterVO center = new MeCenterVO();
        center.setUserId(userId);
        center.setNickname(user.getUsername());
        center.setAvatar(user.getAvatar());
        center.setRealm(user.getRealm() == null ? calcRealm(totalDuration) : user.getRealm());
        center.setProfileTag(resolveProfileTag(totalDuration));
        center.setCurrentQiState(resolveQiState(qiValue));
        center.setPracticeDays(practiceDays);
        center.setBreathHours(totalDuration <= 0 ? 0 : (totalDuration + 59) / 60);
        center.setQiValue(qiValue);
        center.setBio(user.getBio());
        center.setAchievements(buildAchievements(practiceDays, totalDuration));
        return center;
    }

    @Override
    public MeDiaryPageVO getDiary(Long userId, String tab) {
        String currentTab = normalizeDiaryTab(tab);
        List<HistoryItemVO> favorites = favoriteService.list(userId);
        List<ContentViewRecord> viewRecords = contentViewRecordMapper.selectByUserId(userId);

        List<MeDiaryItemVO> allItems = new ArrayList<>();
        for (HistoryItemVO favorite : favorites) {
            allItems.add(fromFavorite(favorite));
        }
        for (ContentViewRecord viewRecord : viewRecords) {
            MeDiaryItemVO item = fromViewRecord(viewRecord);
            if (item != null) {
                allItems.add(item);
            }
        }

        allItems.sort(Comparator.comparing(MeDiaryItemVO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())));
        List<MeDiaryItemVO> filteredItems = allItems.stream()
                .filter(item -> matchesDiaryTab(item, currentTab))
                .collect(Collectors.toList());

        MeDiaryPageVO diary = new MeDiaryPageVO();
        diary.setCurrentTab(currentTab);
        diary.setTotalRecordCount(allItems.size());
        diary.setTotalFavoriteCount((int) allItems.stream().filter(item -> "FAVORITE".equals(item.getRecordType())).count());
        diary.setTotalViewCount((int) allItems.stream().filter(item -> "VIEW".equals(item.getRecordType())).count());
        diary.setTotalCheckinDays(0);
        diary.setTotalTrainingCount(0);
        diary.setTotalCalories(0);
        diary.setTotalDuration(0);
        diary.setTabs(buildDiaryTabs(allItems));
        diary.setItems(filteredItems);
        return diary;
    }

    @Override
    public MeSettingsVO getSettings(Long userId) {
        User user = getUser(userId);
        UserSettings settings = getOrCreateSettings(userId);

        MeSettingsVO vo = new MeSettingsVO();
        vo.setUsername(user.getUsername());
        vo.setPhone(user.getPhone());
        vo.setDailyCalorieGoal(settings.getDailyCalorieGoal());
        vo.setAccountItems(Arrays.asList(
                new MeSettingEntryVO("account", "账号管理", user.getUsername())
        ));
        vo.setPreferenceItems(Arrays.asList(
                new MeSettingEntryVO("dailyCalorieGoal", "每日卡路里目标", settings.getDailyCalorieGoal() + " 千卡")
        ));
        vo.setCommonItems(Arrays.asList(
                new MeSettingEntryVO("checkUpdate", "检查更新", ""),
                new MeSettingEntryVO("clearCache", "清除缓存", ""),
                new MeSettingEntryVO("faq", "常见问题", ""),
                new MeSettingEntryVO("privacy", "隐私政策", ""),
                new MeSettingEntryVO("thirdParty", "第三方共享清单", "")
        ));
        return vo;
    }

    @Override
    public void updateCalorieGoal(Long userId, UpdateCalorieGoalDTO dto) {
        if (dto == null || dto.getDailyCalorieGoal() == null || dto.getDailyCalorieGoal() < 100) {
            throw new BusinessException(400, "Daily calorie goal is invalid");
        }
        UserSettings settings = getOrCreateSettings(userId);
        userSettingsMapper.updateDailyCalorieGoal(settings.getUserId(), dto.getDailyCalorieGoal(), LocalDateTime.now());
    }

    @Override
    public void updateProfile(Long userId, UpdateProfileDTO dto) {
        User user = getUser(userId);
        if (dto == null) {
            throw new BusinessException(400, "Profile data cannot be empty");
        }
        if (dto.getNickname() != null && !dto.getNickname().isBlank()) {
            user.setUsername(dto.getNickname().trim());
        }
        if (dto.getAvatar() != null && !dto.getAvatar().isBlank()) {
            user.setAvatar(dto.getAvatar().trim());
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio().trim());
        }
        userService.updateProfile(user);
    }

    private MeDiaryItemVO fromFavorite(HistoryItemVO favorite) {
        MeDiaryItemVO item = new MeDiaryItemVO();
        item.setRecordType("FAVORITE");
        item.setItemType("FAVORITE");
        item.setContentId(favorite.getContentId());
        item.setContentType(favorite.getContentType());
        item.setItemSubType(favorite.getItemSubType());
        item.setTitle(favorite.getTitle());
        item.setDescription(switch (safe(favorite.getItemSubType())) {
            case "officialVideo" -> "已收藏名师影像";
            case "communityVideo" -> "已收藏同修影像";
            case "communityPost" -> "已收藏同修图文";
            case "recipe" -> "已收藏药膳食疗";
            default -> "已收藏修行内容";
        });
        item.setCover(favorite.getCover());
        item.setVideoUrl(favorite.getVideoUrl());
        item.setCreateTime(favorite.getTime());
        return item;
    }

    private MeDiaryItemVO fromViewRecord(ContentViewRecord record) {
        if (record.getContentType() == null) {
            return null;
        }

        MeDiaryItemVO item = new MeDiaryItemVO();
        item.setRecordType("VIEW");
        item.setItemType("VIEW");
        item.setContentId(record.getContentId());
        item.setContentType(record.getContentType());
        item.setCreateTime(record.getViewTime());
        item.setDuration(record.getDuration());

        if (Objects.equals(record.getContentType(), 1)) {
            Video video = videoMapper.selectById(record.getContentId());
            if (video == null) {
                return null;
            }
            item.setItemSubType("officialVideo");
            item.setTitle(video.getTitle());
            item.setDescription("已浏览名师影像");
            item.setCover(video.getCover());
            item.setVideoUrl(video.getVideoUrl());
            return item;
        }

        if (Objects.equals(record.getContentType(), 2)) {
            Recipe recipe = recipeMapper.getbyId(record.getContentId());
            if (recipe == null) {
                return null;
            }
            item.setItemSubType("recipe");
            item.setTitle(recipe.getName());
            item.setDescription("已浏览图文内容");
            item.setCover(recipe.getCover());
            return item;
        }

        if (Objects.equals(record.getContentType(), 3)) {
            CommunityPost post = communityPostMapper.selectById(record.getContentId());
            if (post == null) {
                return null;
            }
            boolean videoPost = "VIDEO".equalsIgnoreCase(post.getPostType());
            item.setItemSubType(videoPost ? "communityVideo" : "communityPost");
            item.setTitle(post.getTitle());
            item.setDescription(videoPost ? "已浏览同修影像" : "已浏览同修图文");
            item.setCover(post.getCover());
            item.setVideoUrl(videoPost ? post.getMediaUrl() : null);
            return item;
        }

        return null;
    }

    private List<MeDiaryTabVO> buildDiaryTabs(List<MeDiaryItemVO> items) {
        int masterVideoCount = (int) items.stream().filter(item -> "officialVideo".equals(item.getItemSubType())).count();
        int peerVideoCount = (int) items.stream().filter(item -> "communityVideo".equals(item.getItemSubType())).count();
        int peerPostCount = (int) items.stream().filter(item -> "communityPost".equals(item.getItemSubType()) || "recipe".equals(item.getItemSubType())).count();
        return Arrays.asList(
                new MeDiaryTabVO("all", "全部", items.size()),
                new MeDiaryTabVO("masterVideo", "名师影像", masterVideoCount),
                new MeDiaryTabVO("peerVideo", "同修影像", peerVideoCount),
                new MeDiaryTabVO("peerPost", "同修图文", peerPostCount)
        );
    }

    private boolean matchesDiaryTab(MeDiaryItemVO item, String tab) {
        if ("all".equals(tab)) {
            return true;
        }
        if ("masterVideo".equals(tab)) {
            return "officialVideo".equals(item.getItemSubType());
        }
        if ("peerVideo".equals(tab)) {
            return "communityVideo".equals(item.getItemSubType());
        }
        if ("peerPost".equals(tab)) {
            return "communityPost".equals(item.getItemSubType()) || "recipe".equals(item.getItemSubType());
        }
        return true;
    }

    private String normalizeDiaryTab(String tab) {
        if (tab == null || tab.isBlank()) {
            return "all";
        }
        return switch (tab) {
            case "all", "masterVideo", "peerVideo", "peerPost" -> tab;
            default -> "all";
        };
    }

    private User getUser(Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }
        return user;
    }

    private UserSettings getOrCreateSettings(Long userId) {
        UserSettings settings = userSettingsMapper.selectByUserId(userId);
        if (settings != null) {
            return settings;
        }
        UserSettings created = new UserSettings();
        created.setUserId(userId);
        created.setDailyCalorieGoal(1200);
        created.setCreateTime(LocalDateTime.now());
        created.setUpdateTime(LocalDateTime.now());
        userSettingsMapper.insert(created);
        return created;
    }

    private List<MeAchievementVO> buildAchievements(int practiceDays, int totalDuration) {
        return Arrays.asList(
                achievement("初", "初窥门径", practiceDays >= 1),
                achievement("基", "百日筑基", practiceDays >= 100),
                achievement("气", "气贯长虹", totalDuration >= 300),
                achievement("道", "道法自然", totalDuration >= 600),
                achievement("静", "心如止水", practiceDays >= 180),
                achievement("合", "天人合一", totalDuration >= 1200)
        );
    }

    private MeAchievementVO achievement(String title, String subtitle, boolean unlocked) {
        MeAchievementVO vo = new MeAchievementVO();
        vo.setTitle(title);
        vo.setSubtitle(subtitle);
        vo.setUnlocked(unlocked);
        return vo;
    }

    private String calcRealm(int totalDuration) {
        if (totalDuration < 100) {
            return "筑基期";
        }
        if (totalDuration < 300) {
            return "结丹期";
        }
        if (totalDuration < 600) {
            return "元婴期";
        }
        if (totalDuration < 1000) {
            return "化神期";
        }
        return "大乘期";
    }

    private String resolveProfileTag(int totalDuration) {
        if (totalDuration < 200) {
            return "修身养性";
        }
        if (totalDuration < 600) {
            return "吐纳调息";
        }
        return "内外兼修";
    }

    private String resolveQiState(int qiValue) {
        if (qiValue < 200) {
            return "平稳";
        }
        if (qiValue < 600) {
            return "上佳";
        }
        return "极佳";
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
