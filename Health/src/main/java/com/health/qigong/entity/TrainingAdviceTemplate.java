package com.health.qigong.entity;

import lombok.Data;

@Data
public class TrainingAdviceTemplate {
    private Long id;
    private String dimension;
    private String issueCode;
    private String goalCode;
    private String methodCode;
    private Integer minScore;
    private Integer maxScore;
    private String adviceText;
    private Integer priority;
}
