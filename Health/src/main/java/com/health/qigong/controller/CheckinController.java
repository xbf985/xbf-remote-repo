package com.health.qigong.controller;

import com.health.qigong.result.Result;
import com.health.qigong.service.CheckinService;
import com.health.qigong.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkin")
public class CheckinController {

    @Autowired
    private CheckinService checkinService;

    @PostMapping
    public Result<Void> checkin(){
        long userId= UserContext.getUserId();
       checkinService.checkin(userId);
       return Result.success();
    }
}
