package com.health.qigong.service;

import com.health.qigong.dto.CommunityCommentDTO;
import com.health.qigong.dto.CommunityPublishDTO;
import com.health.qigong.vo.AuthorProfileVO;
import com.health.qigong.vo.CommunityCommentVO;
import com.health.qigong.vo.CommunityDetailVO;
import com.health.qigong.vo.CommunityFeedItemVO;
import com.health.qigong.vo.CommunityFeedPageVO;
import com.health.qigong.vo.CommunityMinePageVO;

import java.util.List;

public interface CommunityService {

    CommunityFeedPageVO getFeed(Long userId, String tab, String keyword);

    CommunityMinePageVO getMinePage(Long userId, String tab);

    CommunityDetailVO getDetail(Long userId, Long postId);

    List<CommunityCommentVO> getComments(Long postId);

    void addComment(Long userId, Long postId, CommunityCommentDTO dto);

    Long publish(Long userId, CommunityPublishDTO dto);

    void followAuthor(Long userId, Long authorId);

    void unfollowAuthor(Long userId, Long authorId);

    AuthorProfileVO getAuthorProfile(Long userId, Long authorId, String tab);

    List<CommunityFeedItemVO> getMyWorks(Long userId, String tab);
}
