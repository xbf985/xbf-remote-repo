package com.health.qigong.vo;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HomeCheckinCalendarVO {
    private Integer year;
    private Integer month;
    private Integer continuousDays;
    private Integer totalDays;
    private Integer qiPoints;
    private Integer ranking;
    private Boolean todayChecked;
    private List<LocalDate> checkinDates;
}
