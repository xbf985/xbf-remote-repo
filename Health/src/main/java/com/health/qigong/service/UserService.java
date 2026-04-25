package com.health.qigong.service;

import com.health.qigong.entity.User;
import org.springframework.stereotype.Service;
import com.health.qigong.vo.UserProfileVO;

public interface UserService {

    User login(String username, String password);

    void register(User user);

    void updatePassword(Long id, String password);

    void updatePhone(Long id, String phone);

    User findByUsername(String username);

    UserProfileVO getProfile(long userId);

    User getById(Long userId);

    void updateProfile(User user);


}
