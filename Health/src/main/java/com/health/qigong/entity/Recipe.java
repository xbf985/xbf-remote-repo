package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Recipe {

    private Long id;
    private String name;
    private String cover;
    private Long categoryId;
    private String effect;
    private String ingredients;
    private String steps;
    private LocalDateTime createTime;
}
