package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TrainingRoomSummaryVO {
    private Long sessionId;
    private String title;
    private String sceneLabel;
    private String goalLabel;
    private String methodLabel;
    private String masterLabel;
    private Integer totalScore;
    private Integer postureScore;
    private Integer breathScore;
    private String scoreLevel;
    private String summaryText;
    private List<String> adviceItems;
    private TrainingRoomRecipeVO recommendedRecipe;
    private String recipeReason;
    private String practiceVideoUrl;
    private String practiceVideoCover;
    private Integer practiceVideoDurationSeconds;
    private Integer durationSeconds;
    private Integer caloriesBurned;
    private LocalDateTime completedAt;
}
