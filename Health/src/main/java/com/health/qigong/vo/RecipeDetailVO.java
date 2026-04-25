package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDetailVO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String cover;
    private List<String> effectTags;
    private String ingredients;
    private List<String> ingredientItems;
    private String steps;
    private List<String> stepItems;
    private Integer favoriteCount;
    private Boolean collected;
}
