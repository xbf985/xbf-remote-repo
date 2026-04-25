package com.health.qigong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteTabVO {
    private String key;
    private String label;
    private Integer count;
}
