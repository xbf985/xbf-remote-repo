package com.health.qigong.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String avatar;
    private String phone;
    private String bio;
    private String location;
    private String realm;
    private String verifiedLabel;
    private LocalDateTime createTime;
}
