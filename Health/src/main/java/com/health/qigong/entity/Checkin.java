package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Checkin {
    private Long id;
    private Long userId;
    private LocalDate checkDate;
    private LocalDateTime createTime;
}
