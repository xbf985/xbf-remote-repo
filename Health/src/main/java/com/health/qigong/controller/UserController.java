package com.health.qigong.controller;

import com.health.qigong.dto.LoginDTO;
import com.health.qigong.dto.RegisterDTO;
import com.health.qigong.dto.UpdatePasswordDTO;
import com.health.qigong.dto.UpdatePhoneDTO;
import com.health.qigong.entity.User;
import com.health.qigong.result.Result;
import com.health.qigong.service.UserService;
import com.health.qigong.utils.JwtUtils;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.UserProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userservice;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO request) {
        User user = userservice.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return Result.error(400, "Invalid username or password");
        }
        String token = JwtUtils.generateToken(user.getId());
        return Result.success(token);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO request) {
        User dbUser = userservice.findByUsername(request.getUsername());
        if (dbUser != null) {
            return Result.error(409, "Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        userservice.register(user);
        return Result.successMsg("Register success");
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody UpdatePasswordDTO request) {
        Long userId = UserContext.getUserId();
        userservice.updatePassword(userId, request.getPassword());
        return Result.successMsg("Password updated");
    }

    @PutMapping("/phone")
    public Result<Void> updatePhone(@RequestBody UpdatePhoneDTO request) {
        Long userId = UserContext.getUserId();
        userservice.updatePhone(userId, request.getPhone());
        return Result.successMsg("Phone updated");
    }

    @GetMapping("/profile")
    public Result<UserProfileVO> profile() {
        long userId = UserContext.getUserId();
        return Result.success(userservice.getProfile(userId));
    }
}
