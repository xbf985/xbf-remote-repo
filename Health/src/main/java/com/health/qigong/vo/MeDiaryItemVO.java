package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeDiaryItemVO {
    private String recordType;
    private Long contentId;
    private Integer contentType;
    private String itemSubType;
    private String itemType;
    private String title;
    private String description;
    private String cover;
    private String videoUrl;
    private Long trainingRoomSessionId;
    private Integer calories;
    private Integer duration;
    private String practiceVideoUrl;
    private String practiceVideoCover;
    private Integer practiceVideoDurationSeconds;
    private LocalDateTime createTime;
}
