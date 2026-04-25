package com.health.qigong.service;

import com.health.qigong.dto.TrainingRecordDTO;
import com.health.qigong.entity.TrainingRecord;
import com.health.qigong.vo.TrainingRecordVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TrainingRecordService {
    void add(long userId, TrainingRecordDTO dto);

    List<TrainingRecordVO> list(long userId);

    List<TrainingRecord> listEntities(long userId);
}
