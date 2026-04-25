package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunityComment {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createTime;
}
