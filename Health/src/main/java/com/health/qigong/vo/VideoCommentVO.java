package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoCommentVO {
    private Long id;
    private Long videoId;
    private Long userId;
    private String nickname;
    private String avatar;
    private String content;
    private LocalDateTime createTime;
}
