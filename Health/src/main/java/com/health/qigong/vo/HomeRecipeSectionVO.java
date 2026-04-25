package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class HomeRecipeSectionVO {
    private Long categoryId;
    private String categoryName;
    private String seasonalTips;
    private List<HomeRecipeCardVO> recipes;
}
