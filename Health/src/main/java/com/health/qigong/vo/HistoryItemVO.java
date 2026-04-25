package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryItemVO {
    private Long contentId;
    private Integer contentType;
    private String itemSubType;
    private String title;
    private String cover;
    private String videoUrl;
    private String subtitle;
    private LocalDateTime time;
}
