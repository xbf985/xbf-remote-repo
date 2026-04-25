package com.health.qigong.service;

import com.health.qigong.dto.LikeDTO;
import com.health.qigong.vo.HistoryItemVO;
import org.springframework.stereotype.Service;
import java.util.List;


public interface LikesService {
    void add(long userId, LikeDTO dto);

    void remove(long userId, LikeDTO dto);

    List<HistoryItemVO> list(long userId);

    boolean exists(long userId, Long contentId, Integer contentType);

    int count(Long contentId, Integer contentType);
}
