package com.health.qigong.controller;

import com.health.qigong.result.Result;
import com.health.qigong.service.LikesService;
import com.health.qigong.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.health.qigong.dto.LikeDTO;
import com.health.qigong.vo.HistoryItemVO;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikesController {

    @Autowired
    private LikesService likesService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody LikeDTO dto){
        long userId= UserContext.getUserId();
        likesService.add(userId,dto);
        return Result.success();
    }

    @PostMapping("/remove")
    public Result<Void> remove(@RequestBody LikeDTO dto){
        long userId=UserContext.getUserId();
        likesService.remove(userId,dto);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<HistoryItemVO>> list(){
        long userId=UserContext.getUserId();
        return Result.success(likesService.list(userId));
    }
}
