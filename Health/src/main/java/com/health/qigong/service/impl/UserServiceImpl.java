package com.health.qigong.service.impl;

import com.health.qigong.entity.User;
import com.health.qigong.exception.BusinessException;
import com.health.qigong.mapper.CheckinMapper;
import com.health.qigong.mapper.TrainingRecordMapper;
import com.health.qigong.mapper.UserMapper;
import com.health.qigong.service.UserService;
import com.health.qigong.vo.UserProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper usermapper;

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private TrainingRecordMapper trainingRecordMapper;

    public User login(String username, String password) {
        User user = usermapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    public void register(User user) {
        usermapper.insert(user);
    }

    public void updatePassword(Long id, String password) {
        usermapper.updatePassword(id, password);
    }

    public void updatePhone(Long id, String phone) {
        usermapper.updatePhone(id, phone);
    }

    public User findByUsername(String username) {
        return usermapper.findByUsername(username);
    }

    @Override
    public User getById(Long userId) {
        return usermapper.selectById(userId);
    }

    @Override
    public void updateProfile(User user) {
        usermapper.updateProfile(user);
    }

    public UserProfileVO getProfile(long userId) {
        User user = usermapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }

        Integer practiceDays = checkinMapper.countDaysByUserId(userId);
        Integer totalDuration = trainingRecordMapper.sumDurationByUserId(userId);

        UserProfileVO vo = new UserProfileVO();
        vo.setUserId(user.getId());
        vo.setNickname(user.getUsername());
        vo.setAvatar(user.getAvatar());
        vo.setPracticeDays(practiceDays == null ? 0 : practiceDays);
        vo.setTotalDuration(totalDuration == null ? 0 : totalDuration);
        vo.setLevel(calcLevel(totalDuration));
        return vo;
    }

    private String calcLevel(Integer totalDuration) {
        int duration = totalDuration == null ? 0 : totalDuration;
        if (duration < 100) {
            return "BEGINNER";
        }
        if (duration < 300) {
            return "BASIC";
        }
        if (duration < 600) {
            return "INTERMEDIATE";
        }
        if (duration < 1000) {
            return "ADVANCED";
        }
        if (duration < 1500) {
            return "EXPERT";
        }
        return "MASTER";
    }
}
