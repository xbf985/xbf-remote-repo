package com.health.qigong.service;

import com.health.qigong.dto.VideoCommentDTO;
import com.health.qigong.vo.HomeCourseCategoryVO;
import com.health.qigong.vo.HomeVideoVO;
import com.health.qigong.vo.VideoCommentVO;
import com.health.qigong.vo.VideoDetailVO;

import java.util.List;

public interface VideoService {

    List<HomeCourseCategoryVO> listCategories();

    List<HomeVideoVO> listByCategory(Long userId, Long categoryId);

    VideoDetailVO getDetail(Long userId, Long videoId);

    List<VideoCommentVO> listComments(Long videoId);

    void addComment(Long userId, Long videoId, VideoCommentDTO dto);
}
