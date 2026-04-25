package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Favorite {
    private Long id;
    private Long userId;
    private Long contentId;
    private Integer contentType;
    private LocalDateTime createTime;
}
