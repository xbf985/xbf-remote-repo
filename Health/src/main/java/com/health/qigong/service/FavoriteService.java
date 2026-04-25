package com.health.qigong.service;

import com.health.qigong.dto.FavoriteDTO;
import com.health.qigong.vo.HistoryItemVO;
import org.springframework.stereotype.Service;

import java.util.List;



public interface FavoriteService {
    void add(Long userId, FavoriteDTO dto);

    void remove(Long userId, FavoriteDTO dto);

    List<HistoryItemVO> list(Long userId);

    boolean exists(Long userId, Long contentId, Integer contentType);

    int count(Long contentId, Integer contentType);
}
