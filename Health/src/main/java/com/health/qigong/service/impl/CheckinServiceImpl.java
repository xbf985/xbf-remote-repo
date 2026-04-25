package com.health.qigong.service.impl;

import com.health.qigong.entity.Checkin;
import com.health.qigong.mapper.CheckinMapper;
import com.health.qigong.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CheckinServiceImpl implements CheckinService {

    @Autowired
    private CheckinMapper checkinMapper;

    public void checkin(long userId) {
        LocalDate today = LocalDate.now();
        Checkin old = checkinMapper.selectByUserAndDate(userId, today);
        if (old != null) {
            return;
        }

        Checkin checkin = new Checkin();
        checkin.setUserId(userId);
        checkin.setCheckDate(today);
        checkin.setCreateTime(LocalDateTime.now());
        checkinMapper.insert(checkin);
    }
}
