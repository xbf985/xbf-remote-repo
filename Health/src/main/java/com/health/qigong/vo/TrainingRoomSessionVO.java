package com.health.qigong.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TrainingRoomSessionVO {
    private Long sessionId;
    private Long planId;
    private String title;
    private String intro;
    private String mantra;
    private String sceneLabel;
    private String goalLabel;
    private String methodLabel;
    private String masterLabel;
    private Boolean enableScore;
    private Boolean enablePostureModel;
    private Boolean enableBreathModel;
    private Long videoId;
    private String videoTitle;
    @JsonProperty("video_name")
    private String videoName;
    private String videoCover;
    private String videoUrl;
    private Long videoDurationSeconds;
}
