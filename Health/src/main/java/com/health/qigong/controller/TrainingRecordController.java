package com.health.qigong.controller;

import com.health.qigong.dto.TrainingRecordDTO;
import com.health.qigong.result.Result;
import com.health.qigong.service.TrainingRecordService;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.TrainingRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingRecordController {

    @Autowired
    private TrainingRecordService trainingRecordService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody TrainingRecordDTO dto){
        long userId= UserContext.getUserId();
        trainingRecordService.add(userId,dto);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<TrainingRecordVO>> list(){
        long userId=UserContext.getUserId();
        return Result.success(trainingRecordService.list(userId));
    }
}
