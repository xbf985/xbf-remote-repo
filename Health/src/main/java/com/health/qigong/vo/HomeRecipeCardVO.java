package com.health.qigong.vo;

import lombok.Data;

@Data
public class HomeRecipeCardVO {
    private Long id;
    private String name;
    private String cover;
    private String effect;
    private String ingredients;
    private Long collectCount;
    private Boolean collected;
}
