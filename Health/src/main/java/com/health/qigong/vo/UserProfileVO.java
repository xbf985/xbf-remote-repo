package com.health.qigong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer practiceDays;
    private Integer totalDuration;
    private String level;
}
