package com.health.qigong.entity;

import lombok.Data;

@Data
public class TrainingRecipeRule {
    private Long id;
    private Long recipeId;
    private String goalCode;
    private String methodCode;
    private String issueCode;
    private Long seasonCategory;
    private Integer minScore;
    private Integer maxScore;
    private Integer priority;
}
