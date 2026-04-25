package com.health.qigong.service.impl;

import com.health.qigong.dto.TrainingRoomCompleteDTO;
import com.health.qigong.dto.TrainingRoomEnterDTO;
import com.health.qigong.entity.Recipe;
import com.health.qigong.entity.TrainingAdviceTemplate;
import com.health.qigong.entity.TrainingRecord;
import com.health.qigong.entity.TrainingRecipeRule;
import com.health.qigong.entity.TrainingRoomPlan;
import com.health.qigong.entity.TrainingRoomSession;
import com.health.qigong.entity.Video;
import com.health.qigong.exception.BusinessException;
import com.health.qigong.mapper.RecipeMapper;
import com.health.qigong.mapper.TrainingAdviceTemplateMapper;
import com.health.qigong.mapper.TrainingRecordMapper;
import com.health.qigong.mapper.TrainingRecipeRuleMapper;
import com.health.qigong.mapper.TrainingRoomPlanMapper;
import com.health.qigong.mapper.TrainingRoomSessionMapper;
import com.health.qigong.mapper.VideoMapper;
import com.health.qigong.service.TrainingRoomService;
import com.health.qigong.vo.TrainingRoomConfigVO;
import com.health.qigong.vo.TrainingRoomFeatureVO;
import com.health.qigong.vo.TrainingRoomHistoryItemVO;
import com.health.qigong.vo.TrainingRoomOptionItemVO;
import com.health.qigong.vo.TrainingRoomRecipeVO;
import com.health.qigong.vo.TrainingRoomSessionVO;
import com.health.qigong.vo.TrainingRoomSummaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrainingRoomServiceImpl implements TrainingRoomService {

    private static final List<TrainingRoomFeatureVO> FEATURE_OPTIONS = Arrays.asList(
            new TrainingRoomFeatureVO("score", "评分", true),
            new TrainingRoomFeatureVO("posture", "矫正", false),
            new TrainingRoomFeatureVO("breath", "呼吸", false)
    );

    private static final List<TrainingRoomOptionItemVO> SCENE_OPTIONS = Arrays.asList(
            new TrainingRoomOptionItemVO("office", "案牍之所", "办公", 1),
            new TrainingRoomOptionItemVO("home", "雅室居所", "居家", 2),
            new TrainingRoomOptionItemVO("outdoor", "露天空地", "户外", 3),
            new TrainingRoomOptionItemVO("classroom", "书院学堂", "教室", 4)
    );

    private static final List<TrainingRoomOptionItemVO> GOAL_OPTIONS = Arrays.asList(
            new TrainingRoomOptionItemVO("relaxNeck", "舒缓肩颈", "放松筋骨", 1),
            new TrainingRoomOptionItemVO("calmMind", "宁心安神", "静心调息", 2),
            new TrainingRoomOptionItemVO("fitness", "强身健体", "固本培元", 3),
            new TrainingRoomOptionItemVO("qiFlow", "理气通脉", "吐纳归元", 4)
    );

    private static final List<TrainingRoomOptionItemVO> MASTER_OPTIONS = Arrays.asList(
            new TrainingRoomOptionItemVO("official", "官方正宗", "国家体育总局", 1),
            new TrainingRoomOptionItemVO("taijiMaster", "太极宗师", "陈家沟传人", 2),
            new TrainingRoomOptionItemVO("selfCultivation", "自我修心", "返璞归真", 3)
    );

    private static final List<TrainingRoomOptionItemVO> METHOD_OPTIONS = Arrays.asList(
            new TrainingRoomOptionItemVO("baduanjin", "八段锦", "古法修身", 1),
            new TrainingRoomOptionItemVO("taijiquan", "太极拳", "以柔克刚", 2),
            new TrainingRoomOptionItemVO("wuqinxi", "五禽戏", "舒展导引", 3),
            new TrainingRoomOptionItemVO("yijinjing", "易筋经", "筋骨同修", 4)
    );

    private static final Map<String, TrainingRoomOptionItemVO> SCENE_MAP = toMap(SCENE_OPTIONS);
    private static final Map<String, TrainingRoomOptionItemVO> GOAL_MAP = toMap(GOAL_OPTIONS);
    private static final Map<String, TrainingRoomOptionItemVO> MASTER_MAP = toMap(MASTER_OPTIONS);
    private static final Map<String, TrainingRoomOptionItemVO> METHOD_MAP = toMap(METHOD_OPTIONS);

    @Autowired
    private TrainingRoomPlanMapper trainingRoomPlanMapper;

    @Autowired
    private TrainingAdviceTemplateMapper trainingAdviceTemplateMapper;

    @Autowired
    private TrainingRecipeRuleMapper trainingRecipeRuleMapper;

    @Autowired
    private TrainingRoomSessionMapper trainingRoomSessionMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private TrainingRecordMapper trainingRecordMapper;

    @Override
    public TrainingRoomConfigVO getConfig() {
        TrainingRoomConfigVO config = new TrainingRoomConfigVO();
        config.setFeatureOptions(new ArrayList<>(FEATURE_OPTIONS));
        config.setSceneOptions(new ArrayList<>(SCENE_OPTIONS));
        config.setGoalOptions(new ArrayList<>(GOAL_OPTIONS));
        config.setMethodOptions(new ArrayList<>(METHOD_OPTIONS));
        config.setMasterOptions(new ArrayList<>(MASTER_OPTIONS));
        return config;
    }

    @Override
    public TrainingRoomSessionVO enter(Long userId, TrainingRoomEnterDTO dto) {
        if (dto == null) {
            throw new BusinessException(400, "Training selection cannot be empty");
        }

        String sceneCode = requireOption(dto.getSceneCode(), SCENE_MAP, "scene");
        String goalCode = requireOption(dto.getGoalCode(), GOAL_MAP, "goal");
        String masterCode = requireOption(dto.getMasterCode(), MASTER_MAP, "master");
        String methodCode = requireOption(dto.getMethodCode(), METHOD_MAP, "method");

        TrainingRoomPlan plan = matchPlan(sceneCode, goalCode, masterCode, methodCode);
        Video video = resolveRecommendedVideo(plan, sceneCode, goalCode, masterCode, methodCode);
        if (video == null) {
            throw new BusinessException(404, "Training video not found");
        }

        TrainingRoomSession session = new TrainingRoomSession();
        session.setUserId(userId);
        session.setPlanId(plan.getId());
        session.setVideoId(video.getId());
        session.setSceneCode(sceneCode);
        session.setGoalCode(goalCode);
        session.setMasterCode(masterCode);
        session.setMethodCode(methodCode);
        session.setEnableScore(dto.getEnableScore() == null || dto.getEnableScore());
        session.setEnablePostureModel(Boolean.TRUE.equals(dto.getEnablePostureModel()) && Boolean.TRUE.equals(plan.getSupportPostureModel()));
        session.setEnableBreathModel(Boolean.TRUE.equals(dto.getEnableBreathModel()) && Boolean.TRUE.equals(plan.getSupportBreathModel()));
        session.setStatus("ONGOING");
        session.setStartTime(LocalDateTime.now());
        session.setCreateTime(LocalDateTime.now());
        trainingRoomSessionMapper.insert(session);

        return toSessionVO(session, plan, video);
    }

    @Override
    public TrainingRoomSummaryVO complete(Long userId, Long sessionId, TrainingRoomCompleteDTO dto) {
        TrainingRoomSession session = requireSession(userId, sessionId);
        if ("COMPLETED".equalsIgnoreCase(session.getStatus())) {
            return buildSummary(session, null, null, null);
        }

        TrainingRoomPlan plan = trainingRoomPlanMapper.selectById(session.getPlanId());
        if (plan == null) {
            throw new BusinessException(404, "Training plan not found");
        }

        TrainingRoomCompleteDTO payload = dto == null ? new TrainingRoomCompleteDTO() : dto;
        int durationSeconds = positiveOrDefault(payload.getDurationSeconds(), plan.getEstimatedDurationSeconds(), 600);
        int caloriesBurned = positiveOrDefault(payload.getCaloriesBurned(), plan.getEstimatedCalories(), Math.max(60, durationSeconds / 6));
        int durationScore = buildDurationScore(durationSeconds, plan.getEstimatedDurationSeconds());
        int postureScore = buildDimensionScore(payload.getPostureScore(), durationScore, session.getEnablePostureModel());
        int breathScore = buildDimensionScore(payload.getBreathScore(), durationScore, session.getEnableBreathModel());
        int totalScore = buildTotalScore(durationScore, postureScore, breathScore, session.getEnablePostureModel(), session.getEnableBreathModel());

        List<String> postureIssues = normalizeIssues(payload.getPostureIssues());
        List<String> breathIssues = normalizeIssues(payload.getBreathIssues());
        List<String> adviceItems = buildAdviceItems(totalScore, postureScore, breathScore, postureIssues, breathIssues, session.getGoalCode(), session.getMethodCode());
        Recipe recipe = recommendRecipe(session.getGoalCode(), session.getMethodCode(), mergeIssues(postureIssues, breathIssues), totalScore);
        String recipeReason = buildRecipeReason(recipe, session.getGoalCode(), mergeIssues(postureIssues, breathIssues));
        String summaryText = buildSummaryText(totalScore, session.getGoalCode(), session.getMethodCode(), postureIssues, breathIssues);

        session.setStatus("COMPLETED");
        session.setDurationSeconds(durationSeconds);
        session.setCaloriesBurned(caloriesBurned);
        session.setPostureScore(postureScore);
        session.setBreathScore(breathScore);
        session.setTotalScore(totalScore);
        session.setPostureIssueCodes(String.join(",", postureIssues));
        session.setBreathIssueCodes(String.join(",", breathIssues));
        session.setPracticeVideoUrl(normalizeMediaUrl(payload.getPracticeVideoUrl()));
        session.setPracticeVideoCover(normalizeMediaUrl(payload.getPracticeVideoCover()));
        session.setPracticeVideoDurationSeconds(payload.getPracticeVideoDurationSeconds());
        session.setSummaryText(summaryText);
        session.setAdviceText(String.join("\n", adviceItems));
        session.setRecipeId(recipe == null ? null : recipe.getId());
        session.setRecipeReason(recipeReason);
        session.setEndTime(LocalDateTime.now());
        trainingRoomSessionMapper.updateCompletion(session);

        saveTrainingRecord(userId, session, caloriesBurned, durationSeconds);
        return buildSummary(session, plan, recipe, adviceItems);
    }

    @Override
    public List<TrainingRoomHistoryItemVO> history(Long userId) {
        List<TrainingRoomSession> sessions = trainingRoomSessionMapper.selectByUserId(userId);
        List<TrainingRoomPlan> plans = trainingRoomPlanMapper.listAll();
        Map<Long, TrainingRoomPlan> planMap = plans.stream().collect(Collectors.toMap(TrainingRoomPlan::getId, item -> item, (left, right) -> left));

        List<TrainingRoomHistoryItemVO> result = new ArrayList<>();
        for (TrainingRoomSession session : sessions) {
            TrainingRoomHistoryItemVO item = new TrainingRoomHistoryItemVO();
            TrainingRoomPlan plan = planMap.get(session.getPlanId());
            item.setSessionId(session.getId());
            item.setTitle(plan == null ? buildDefaultTitle(session.getMethodCode(), session.getGoalCode()) : plan.getTitle());
            item.setGoalLabel(optionLabel(GOAL_MAP, session.getGoalCode()));
            item.setMethodLabel(optionLabel(METHOD_MAP, session.getMethodCode()));
            item.setTotalScore(session.getTotalScore());
            item.setDurationSeconds(session.getDurationSeconds());
            item.setCaloriesBurned(session.getCaloriesBurned());
            item.setStatus(session.getStatus());
            item.setPracticeVideoUrl(session.getPracticeVideoUrl());
            item.setPracticeVideoCover(session.getPracticeVideoCover());
            item.setPracticeVideoDurationSeconds(session.getPracticeVideoDurationSeconds());
            item.setStartTime(session.getStartTime());
            result.add(item);
        }
        return result;
    }

    @Override
    public TrainingRoomSummaryVO getSummary(Long userId, Long sessionId) {
        TrainingRoomSession session = requireSession(userId, sessionId);
        if (!"COMPLETED".equalsIgnoreCase(session.getStatus())) {
            throw new BusinessException(400, "Training session is not completed yet");
        }
        return buildSummary(session, null, null, null);
    }

    private TrainingRoomSummaryVO buildSummary(TrainingRoomSession session,
                                               TrainingRoomPlan plan,
                                               Recipe recipe,
                                               List<String> adviceItems) {
        TrainingRoomPlan currentPlan = plan == null ? trainingRoomPlanMapper.selectById(session.getPlanId()) : plan;
        Recipe currentRecipe = recipe;
        if (currentRecipe == null && session.getRecipeId() != null) {
            currentRecipe = recipeMapper.getbyId(session.getRecipeId());
        }
        List<String> currentAdviceItems = adviceItems;
        if (currentAdviceItems == null) {
            currentAdviceItems = splitAdvice(session.getAdviceText());
        }

        TrainingRoomSummaryVO summary = new TrainingRoomSummaryVO();
        summary.setSessionId(session.getId());
        summary.setTitle(currentPlan == null ? buildDefaultTitle(session.getMethodCode(), session.getGoalCode()) : currentPlan.getTitle());
        summary.setSceneLabel(optionFullLabel(SCENE_MAP, session.getSceneCode()));
        summary.setGoalLabel(optionLabel(GOAL_MAP, session.getGoalCode()));
        summary.setMasterLabel(optionLabel(MASTER_MAP, session.getMasterCode()));
        summary.setMethodLabel(optionLabel(METHOD_MAP, session.getMethodCode()));
        summary.setTotalScore(session.getTotalScore());
        summary.setPostureScore(session.getPostureScore());
        summary.setBreathScore(session.getBreathScore());
        summary.setScoreLevel(resolveScoreLevel(session.getTotalScore()));
        summary.setSummaryText(session.getSummaryText());
        summary.setAdviceItems(currentAdviceItems);
        summary.setRecommendedRecipe(toRecipeVO(currentRecipe));
        summary.setRecipeReason(session.getRecipeReason());
        summary.setPracticeVideoUrl(session.getPracticeVideoUrl());
        summary.setPracticeVideoCover(session.getPracticeVideoCover());
        summary.setPracticeVideoDurationSeconds(session.getPracticeVideoDurationSeconds());
        summary.setDurationSeconds(session.getDurationSeconds());
        summary.setCaloriesBurned(session.getCaloriesBurned());
        summary.setCompletedAt(session.getEndTime());
        return summary;
    }

    private TrainingRoomSessionVO toSessionVO(TrainingRoomSession session, TrainingRoomPlan plan, Video video) {
        TrainingRoomSessionVO vo = new TrainingRoomSessionVO();
        vo.setSessionId(session.getId());
        vo.setPlanId(plan.getId());
        vo.setTitle(plan.getTitle());
        vo.setIntro(plan.getIntro());
        vo.setMantra(plan.getMantra());
        vo.setSceneLabel(optionFullLabel(SCENE_MAP, session.getSceneCode()));
        vo.setGoalLabel(optionLabel(GOAL_MAP, session.getGoalCode()));
        vo.setMethodLabel(optionLabel(METHOD_MAP, session.getMethodCode()));
        vo.setMasterLabel(optionLabel(MASTER_MAP, session.getMasterCode()));
        vo.setEnableScore(session.getEnableScore());
        vo.setEnablePostureModel(session.getEnablePostureModel());
        vo.setEnableBreathModel(session.getEnableBreathModel());
        vo.setVideoId(video.getId());
        vo.setVideoTitle(video.getTitle());
        vo.setVideoName("baduanjian3");
        vo.setVideoCover(video.getCover());
        vo.setVideoUrl(video.getVideoUrl());
        vo.setVideoDurationSeconds(video.getDurationSeconds());
        return vo;
    }

    private Video resolveRecommendedVideo(TrainingRoomPlan plan,
                                          String sceneCode,
                                          String goalCode,
                                          String masterCode,
                                          String methodCode) {
        Long categoryId = resolveMethodCategoryId(methodCode);
        if (categoryId != null) {
            List<Video> candidates = videoMapper.listByCategoryId(categoryId).stream()
                    .filter(item -> item.getVideoName() != null && !item.getVideoName().isBlank())
                    .sorted(Comparator.comparing(Video::getVideoName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
            if (!candidates.isEmpty()) {
                int index = Math.floorMod(Objects.hash(sceneCode, goalCode, masterCode, methodCode), candidates.size());
                return candidates.get(index);
            }
        }
        return videoMapper.selectById(plan.getVideoId());
    }

    private TrainingRoomPlan matchPlan(String sceneCode, String goalCode, String masterCode, String methodCode) {
        List<TrainingRoomPlan> plans = trainingRoomPlanMapper.listAll();
        if (plans.isEmpty()) {
            throw new BusinessException(404, "Training plan is not configured");
        }

        TrainingRoomPlan best = null;
        int bestScore = Integer.MIN_VALUE;
        for (TrainingRoomPlan plan : plans) {
            int score = 0;
            score += Objects.equals(plan.getMethodCode(), methodCode) ? 50 : 0;
            score += Objects.equals(plan.getGoalCode(), goalCode) ? 30 : 0;
            score += Objects.equals(plan.getSceneCode(), sceneCode) ? 15 : 0;
            score += Objects.equals(plan.getMasterCode(), masterCode) ? 10 : 0;
            score += defaultZero(plan.getPriority());
            if (score > bestScore) {
                bestScore = score;
                best = plan;
            }
        }

        if (best == null) {
            throw new BusinessException(404, "No suitable training plan found");
        }
        return best;
    }

    private Long resolveMethodCategoryId(String methodCode) {
        if ("baduanjin".equalsIgnoreCase(methodCode)) {
            return 1L;
        }
        if ("taijiquan".equalsIgnoreCase(methodCode)) {
            return 2L;
        }
        if ("wuqinxi".equalsIgnoreCase(methodCode)) {
            return 3L;
        }
        if ("yijinjing".equalsIgnoreCase(methodCode)) {
            return 4L;
        }
        return null;
    }

    private String requireOption(String code, Map<String, TrainingRoomOptionItemVO> optionMap, String fieldName) {
        if (code == null || code.isBlank() || !optionMap.containsKey(code)) {
            throw new BusinessException(400, "Training " + fieldName + " is invalid");
        }
        return code;
    }

    private TrainingRoomSession requireSession(Long userId, Long sessionId) {
        TrainingRoomSession session = trainingRoomSessionMapper.selectById(sessionId);
        if (session == null || !Objects.equals(session.getUserId(), userId)) {
            throw new BusinessException(404, "Training session not found");
        }
        return session;
    }

    private int buildDurationScore(int durationSeconds, Integer estimatedDurationSeconds) {
        int expected = positiveOrDefault(estimatedDurationSeconds, 600, 600);
        double ratio = Math.min(1.25d, durationSeconds * 1.0d / expected);
        return clampScore((int) Math.round(65 + ratio * 30));
    }

    private int buildDimensionScore(Integer modelScore, int durationScore, Boolean modelEnabled) {
        if (modelScore != null) {
            return clampScore(modelScore);
        }
        if (Boolean.TRUE.equals(modelEnabled)) {
            return clampScore(durationScore - 2);
        }
        return durationScore;
    }

    private int buildTotalScore(int durationScore, int postureScore, int breathScore, Boolean postureEnabled, Boolean breathEnabled) {
        if (Boolean.TRUE.equals(postureEnabled) && Boolean.TRUE.equals(breathEnabled)) {
            return clampScore((int) Math.round(durationScore * 0.2 + postureScore * 0.4 + breathScore * 0.4));
        }
        if (Boolean.TRUE.equals(postureEnabled)) {
            return clampScore((int) Math.round(durationScore * 0.3 + postureScore * 0.7));
        }
        if (Boolean.TRUE.equals(breathEnabled)) {
            return clampScore((int) Math.round(durationScore * 0.3 + breathScore * 0.7));
        }
        return clampScore(durationScore);
    }

    private List<String> buildAdviceItems(int totalScore,
                                          int postureScore,
                                          int breathScore,
                                          List<String> postureIssues,
                                          List<String> breathIssues,
                                          String goalCode,
                                          String methodCode) {
        List<TrainingAdviceTemplate> templates = trainingAdviceTemplateMapper.listAll();
        LinkedHashSet<String> advice = new LinkedHashSet<>();

        addTemplateAdvice(advice, templates, "OVERALL", null, totalScore, goalCode, methodCode);
        if (!postureIssues.isEmpty()) {
            for (String issueCode : postureIssues) {
                addTemplateAdvice(advice, templates, "POSTURE", issueCode, postureScore, goalCode, methodCode);
            }
        } else {
            addTemplateAdvice(advice, templates, "POSTURE", null, postureScore, goalCode, methodCode);
        }

        if (!breathIssues.isEmpty()) {
            for (String issueCode : breathIssues) {
                addTemplateAdvice(advice, templates, "BREATH", issueCode, breathScore, goalCode, methodCode);
            }
        } else {
            addTemplateAdvice(advice, templates, "BREATH", null, breathScore, goalCode, methodCode);
        }

        if (advice.isEmpty()) {
            advice.add("本次修习已完成，可继续保持动作与吐纳的稳定节奏。");
        }
        return advice.stream().limit(3).collect(Collectors.toList());
    }

    private void addTemplateAdvice(LinkedHashSet<String> advice,
                                   List<TrainingAdviceTemplate> templates,
                                   String dimension,
                                   String issueCode,
                                   int score,
                                   String goalCode,
                                   String methodCode) {
        TrainingAdviceTemplate template = findBestTemplate(templates, dimension, issueCode, score, goalCode, methodCode);
        if (template != null && template.getAdviceText() != null && !template.getAdviceText().isBlank()) {
            advice.add(template.getAdviceText().trim());
        }
    }

    private TrainingAdviceTemplate findBestTemplate(List<TrainingAdviceTemplate> templates,
                                                    String dimension,
                                                    String issueCode,
                                                    int score,
                                                    String goalCode,
                                                    String methodCode) {
        return templates.stream()
                .filter(item -> dimension.equalsIgnoreCase(item.getDimension()))
                .filter(item -> matchesIssue(item.getIssueCode(), issueCode))
                .filter(item -> matchesNullable(item.getGoalCode(), goalCode))
                .filter(item -> matchesNullable(item.getMethodCode(), methodCode))
                .filter(item -> withinRange(score, item.getMinScore(), item.getMaxScore()))
                .sorted((left, right) -> Integer.compare(templateWeight(right), templateWeight(left)))
                .findFirst()
                .orElse(null);
    }

    private Recipe recommendRecipe(String goalCode, String methodCode, List<String> issueCodes, int totalScore) {
        List<TrainingRecipeRule> rules = trainingRecipeRuleMapper.listAll();
        Long seasonCategory = currentSeasonCategory();

        TrainingRecipeRule bestRule = rules.stream()
                .filter(rule -> matchesNullable(rule.getGoalCode(), goalCode))
                .filter(rule -> matchesNullable(rule.getMethodCode(), methodCode))
                .filter(rule -> rule.getIssueCode() == null || rule.getIssueCode().isBlank() || issueCodes.contains(rule.getIssueCode().toUpperCase()))
                .filter(rule -> rule.getSeasonCategory() == null || Objects.equals(rule.getSeasonCategory(), seasonCategory))
                .filter(rule -> withinRange(totalScore, rule.getMinScore(), rule.getMaxScore()))
                .sorted((left, right) -> Integer.compare(recipeRuleWeight(right, issueCodes, seasonCategory), recipeRuleWeight(left, issueCodes, seasonCategory)))
                .findFirst()
                .orElse(null);

        if (bestRule != null) {
            Recipe recipe = recipeMapper.getbyId(bestRule.getRecipeId());
            if (recipe != null) {
                return recipe;
            }
        }

        List<Recipe> seasonalRecipes = recipeMapper.list(seasonCategory);
        if (!seasonalRecipes.isEmpty()) {
            return seasonalRecipes.get(0);
        }

        List<Recipe> fallbackRecipes = recipeMapper.list(1L);
        if (!fallbackRecipes.isEmpty()) {
            return fallbackRecipes.get(0);
        }
        return null;
    }

    private String buildRecipeReason(Recipe recipe, String goalCode, List<String> issueCodes) {
        if (recipe == null) {
            return "本次修习未匹配到合适膳食，可稍后再试。";
        }

        StringBuilder reason = new StringBuilder();
        reason.append("基于您今日所求“").append(optionLabel(GOAL_MAP, goalCode)).append("”");
        if (issueCodes.contains("BREATH_SHALLOW") || issueCodes.contains("RHYTHM_UNEVEN")) {
            reason.append("，结合本次吐纳反馈");
        } else if (issueCodes.contains("NECK_TENSE") || issueCodes.contains("CENTER_UNSTABLE")) {
            reason.append("，结合本次动作反馈");
        } else {
            reason.append("，结合本次修习状态");
        }
        reason.append("，特荐此膳以助调息复原。");
        return reason.toString();
    }

    private String buildSummaryText(int totalScore,
                                    String goalCode,
                                    String methodCode,
                                    List<String> postureIssues,
                                    List<String> breathIssues) {
        String methodLabel = optionLabel(METHOD_MAP, methodCode);
        String goalLabel = optionLabel(GOAL_MAP, goalCode);
        if (totalScore >= 90) {
            return "本次" + methodLabel + "修习节奏流畅，已较好贴合“" + goalLabel + "”之所求。";
        }
        if (!postureIssues.isEmpty() || !breathIssues.isEmpty()) {
            return "本次" + methodLabel + "修习已完成，整体基础稳定，但在“" + goalLabel + "”方向仍有细节可继续打磨。";
        }
        return "本次" + methodLabel + "修习已顺利完成，继续保持节奏与定力，效果会更稳。";
    }

    private void saveTrainingRecord(Long userId, TrainingRoomSession session, int caloriesBurned, int durationSeconds) {
        TrainingRecord record = new TrainingRecord();
        record.setUserId(userId);
        record.setTrainingRoomSessionId(session.getId());
        record.setTrainingName(optionLabel(METHOD_MAP, session.getMethodCode()) + "·" + optionLabel(GOAL_MAP, session.getGoalCode()));
        record.setDuration(Math.max(1, durationSeconds / 60));
        record.setCalories(Math.max(1, caloriesBurned));
        record.setPracticeVideoUrl(session.getPracticeVideoUrl());
        record.setPracticeVideoCover(session.getPracticeVideoCover());
        record.setPracticeVideoDurationSeconds(session.getPracticeVideoDurationSeconds());
        record.setCreateTime(session.getEndTime() == null ? LocalDateTime.now() : session.getEndTime());
        trainingRecordMapper.insert(record);
    }

    private TrainingRoomRecipeVO toRecipeVO(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        TrainingRoomRecipeVO vo = new TrainingRoomRecipeVO();
        vo.setId(recipe.getId());
        vo.setName(recipe.getName());
        vo.setCover(recipe.getCover());
        vo.setEffectTags(splitText(recipe.getEffect()));
        vo.setIngredientsPreview(buildIngredientsPreview(recipe.getIngredients()));
        return vo;
    }

    private String buildIngredientsPreview(String ingredients) {
        if (ingredients == null || ingredients.isBlank()) {
            return "";
        }
        return ingredients.length() > 36 ? ingredients.substring(0, 36) + "..." : ingredients;
    }

    private List<String> splitText(String value) {
        if (value == null || value.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split("[,，|；;]"))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toList());
    }

    private List<String> splitAdvice(String adviceText) {
        if (adviceText == null || adviceText.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(adviceText.split("\\r?\\n"))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toList());
    }

    private String normalizeMediaUrl(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private List<String> normalizeIssues(List<String> issues) {
        if (issues == null || issues.isEmpty()) {
            return Collections.emptyList();
        }
        return issues.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .map(String::toUpperCase)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> mergeIssues(List<String> left, List<String> right) {
        LinkedHashSet<String> merged = new LinkedHashSet<>();
        merged.addAll(left);
        merged.addAll(right);
        return new ArrayList<>(merged);
    }

    private int templateWeight(TrainingAdviceTemplate template) {
        int weight = defaultZero(template.getPriority()) * 100;
        if (template.getIssueCode() != null && !template.getIssueCode().isBlank()) {
            weight += 30;
        }
        if (template.getGoalCode() != null && !template.getGoalCode().isBlank()) {
            weight += 20;
        }
        if (template.getMethodCode() != null && !template.getMethodCode().isBlank()) {
            weight += 10;
        }
        return weight;
    }

    private int recipeRuleWeight(TrainingRecipeRule rule, List<String> issueCodes, Long seasonCategory) {
        int weight = defaultZero(rule.getPriority()) * 100;
        if (rule.getGoalCode() != null && !rule.getGoalCode().isBlank()) {
            weight += 30;
        }
        if (rule.getMethodCode() != null && !rule.getMethodCode().isBlank()) {
            weight += 20;
        }
        if (rule.getIssueCode() != null && !rule.getIssueCode().isBlank() && issueCodes.contains(rule.getIssueCode().toUpperCase())) {
            weight += 15;
        }
        if (rule.getSeasonCategory() != null && Objects.equals(rule.getSeasonCategory(), seasonCategory)) {
            weight += 10;
        }
        return weight;
    }

    private boolean matchesIssue(String templateIssueCode, String targetIssueCode) {
        if (targetIssueCode == null || targetIssueCode.isBlank()) {
            return templateIssueCode == null || templateIssueCode.isBlank();
        }
        return templateIssueCode != null && templateIssueCode.equalsIgnoreCase(targetIssueCode);
    }

    private boolean matchesNullable(String templateValue, String actualValue) {
        return templateValue == null || templateValue.isBlank() || templateValue.equalsIgnoreCase(actualValue);
    }

    private boolean withinRange(int score, Integer minScore, Integer maxScore) {
        if (minScore != null && score < minScore) {
            return false;
        }
        if (maxScore != null && score > maxScore) {
            return false;
        }
        return true;
    }

    private int positiveOrDefault(Integer value, Integer defaultValue, int fallback) {
        if (value != null && value > 0) {
            return value;
        }
        if (defaultValue != null && defaultValue > 0) {
            return defaultValue;
        }
        return fallback;
    }

    private int clampScore(int score) {
        return Math.max(0, Math.min(100, score));
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private String optionLabel(Map<String, TrainingRoomOptionItemVO> map, String code) {
        TrainingRoomOptionItemVO option = map.get(code);
        return option == null ? code : option.getLabel();
    }

    private String optionFullLabel(Map<String, TrainingRoomOptionItemVO> map, String code) {
        TrainingRoomOptionItemVO option = map.get(code);
        if (option == null) {
            return code;
        }
        if (option.getDescription() == null || option.getDescription().isBlank()) {
            return option.getLabel();
        }
        return option.getLabel() + "（" + option.getDescription() + "）";
    }

    private String buildDefaultTitle(String methodCode, String goalCode) {
        return optionLabel(METHOD_MAP, methodCode) + "·" + optionLabel(GOAL_MAP, goalCode);
    }

    private String resolveScoreLevel(Integer totalScore) {
        int score = defaultZero(totalScore);
        if (score >= 90) {
            return "气韵上佳";
        }
        if (score >= 80) {
            return "渐入佳境";
        }
        return "尚可精进";
    }

    private Long currentSeasonCategory() {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        if ((month == 2 && day >= 4) || (month == 3 && day < 5)) {
            return 1L;
        }
        if ((month == 3 && day >= 5) || month == 4 || (month == 5 && day < 5)) {
            return 2L;
        }
        if ((month == 5 && day >= 5) || month == 6 && day < 21) {
            return 3L;
        }
        if ((month == 6 && day >= 21) || month == 7 || (month == 8 && day < 7)) {
            return 4L;
        }
        if ((month == 8 && day >= 7) || month == 9 && day < 7) {
            return 5L;
        }
        if ((month == 9 && day >= 7) || month == 10 || (month == 11 && day < 7)) {
            return 6L;
        }
        if ((month == 11 && day >= 7) || month == 12 && day < 21) {
            return 7L;
        }
        return 8L;
    }

    private static Map<String, TrainingRoomOptionItemVO> toMap(List<TrainingRoomOptionItemVO> options) {
        Map<String, TrainingRoomOptionItemVO> map = new LinkedHashMap<>();
        for (TrainingRoomOptionItemVO option : options) {
            map.put(option.getCode(), option);
        }
        return map;
    }
}
