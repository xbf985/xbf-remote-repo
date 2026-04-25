package com.health.qigong.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainingRoomCompleteDTO {
    private Integer durationSeconds;
    private Integer caloriesBurned;
    private Integer postureScore;
    private Integer breathScore;
    private List<String> postureIssues;
    private List<String> breathIssues;
    private String practiceVideoUrl;
    private String practiceVideoCover;
    private Integer practiceVideoDurationSeconds;
}
