package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunityCommentVO {
    private Long id;
    private Long postId;
    private Long userId;
    private String nickname;
    private String avatar;
    private String content;
    private LocalDateTime createTime;
}
