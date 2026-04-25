package com.health.qigong.service;

import com.health.qigong.dto.TrainingRoomCompleteDTO;
import com.health.qigong.dto.TrainingRoomEnterDTO;
import com.health.qigong.vo.TrainingRoomConfigVO;
import com.health.qigong.vo.TrainingRoomHistoryItemVO;
import com.health.qigong.vo.TrainingRoomSessionVO;
import com.health.qigong.vo.TrainingRoomSummaryVO;

import java.util.List;

public interface TrainingRoomService {

    TrainingRoomConfigVO getConfig();

    TrainingRoomSessionVO enter(Long userId, TrainingRoomEnterDTO dto);

    TrainingRoomSummaryVO complete(Long userId, Long sessionId, TrainingRoomCompleteDTO dto);

    List<TrainingRoomHistoryItemVO> history(Long userId);

    TrainingRoomSummaryVO getSummary(Long userId, Long sessionId);
}
