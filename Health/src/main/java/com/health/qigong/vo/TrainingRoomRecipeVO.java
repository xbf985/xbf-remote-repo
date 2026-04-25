package com.health.qigong.vo;

import lombok.Data;

import java.util.List;

@Data
public class TrainingRoomRecipeVO {
    private Long id;
    private String name;
    private String cover;
    private List<String> effectTags;
    private String ingredientsPreview;
}
