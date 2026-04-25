package com.health.qigong.service.impl;

import com.health.qigong.dto.TrainingRecordDTO;
import com.health.qigong.entity.TrainingRecord;
import com.health.qigong.mapper.TrainingRecordMapper;
import com.health.qigong.service.TrainingRecordService;
import com.health.qigong.vo.TrainingRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingRecordServiceImpl implements TrainingRecordService {

    @Autowired
    private TrainingRecordMapper trainingRecordMapper;

    public void add(long userId, TrainingRecordDTO dto){
        TrainingRecord record=new TrainingRecord();
        record.setUserId(userId);
        record.setTrainingRoomSessionId(dto.getTrainingRoomSessionId());
        record.setTrainingName(dto.getTrainingName());
        record.setDuration(dto.getDuration());
        record.setCalories(dto.getCalories());
        record.setPracticeVideoUrl(dto.getPracticeVideoUrl());
        record.setPracticeVideoCover(dto.getPracticeVideoCover());
        record.setPracticeVideoDurationSeconds(dto.getPracticeVideoDurationSeconds());
        record.setCreateTime(LocalDateTime.now());
        trainingRecordMapper.insert(record);
    }


    public List<TrainingRecordVO> list(long userId) {
        List<TrainingRecord>list=trainingRecordMapper.selectByUserId(userId);
        List<TrainingRecordVO> result = new ArrayList<>();
        for(TrainingRecord item:list){
            TrainingRecordVO vo=new TrainingRecordVO();
            vo.setTrainingRoomSessionId(item.getTrainingRoomSessionId());
            vo.setTrainingName(item.getTrainingName());
            vo.setDuration(item.getDuration());
            vo.setCalories(item.getCalories());
            vo.setPracticeVideoUrl(item.getPracticeVideoUrl());
            vo.setPracticeVideoCover(item.getPracticeVideoCover());
            vo.setPracticeVideoDurationSeconds(item.getPracticeVideoDurationSeconds());
            vo.setCreateTime(item.getCreateTime());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<TrainingRecord> listEntities(long userId) {
        return trainingRecordMapper.selectByUserId(userId);
    }
}
