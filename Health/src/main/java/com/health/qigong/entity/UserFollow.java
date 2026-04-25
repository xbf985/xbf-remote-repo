package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFollow {
    private Long id;
    private Long userId;
    private Long targetUserId;
    private LocalDateTime createTime;
}
