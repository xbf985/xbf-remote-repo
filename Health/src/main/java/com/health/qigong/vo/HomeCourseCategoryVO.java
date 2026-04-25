package com.health.qigong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeCourseCategoryVO {
    private Long id;
    private String name;
    private String tag;
    private String slogan;
}
