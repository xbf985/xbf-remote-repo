package com.health.qigong.service.impl;

import com.health.qigong.dto.LikeDTO;
import com.health.qigong.entity.CommunityPost;
import com.health.qigong.entity.Likes;
import com.health.qigong.entity.Video;
import com.health.qigong.mapper.CommunityPostMapper;
import com.health.qigong.mapper.LikesMapper;
import com.health.qigong.mapper.VideoMapper;
import com.health.qigong.service.LikesService;
import com.health.qigong.vo.HistoryItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LikesServiceImpl implements LikesService {

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CommunityPostMapper communityPostMapper;

    @Override
    public void add(long userId, LikeDTO dto) {
        Likes old = likesMapper.selectOne(userId, dto.getContentId(), dto.getContentType());
        if (old != null) {
            return;
        }
        Likes like = new Likes();
        like.setUserId(userId);
        like.setContentId(dto.getContentId());
        like.setContentType(dto.getContentType());
        like.setCreateTime(LocalDateTime.now());
        likesMapper.insert(like);
    }

    @Override
    public void remove(long userId, LikeDTO dto) {
        likesMapper.remove(userId, dto.getContentId(), dto.getContentType());
    }

    @Override
    public List<HistoryItemVO> list(long userId) {
        List<Likes> list = likesMapper.selectByUserId(userId);
        List<HistoryItemVO> result = new ArrayList<>();
        for (Likes item : list) {
            HistoryItemVO vo = buildHistoryItem(item.getContentId(), item.getContentType(), item.getCreateTime());
            if (vo != null) {
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public boolean exists(long userId, Long contentId, Integer contentType) {
        return likesMapper.selectOne(userId, contentId, contentType) != null;
    }

    @Override
    public int count(Long contentId, Integer contentType) {
        Integer count = likesMapper.countByContent(contentId, contentType);
        return count == null ? 0 : count;
    }

    private HistoryItemVO buildHistoryItem(Long contentId, Integer contentType, LocalDateTime time) {
        HistoryItemVO vo = new HistoryItemVO();
        vo.setContentId(contentId);
        vo.setContentType(contentType);
        vo.setTime(time);

        if (contentType == 1) {
            Video video = videoMapper.selectById(contentId);
            if (video == null) {
                return null;
            }
            vo.setItemSubType("officialVideo");
            vo.setTitle(video.getTitle());
            vo.setCover(video.getCover());
            vo.setVideoUrl(video.getVideoUrl());
            vo.setSubtitle(video.getAuthorName());
            return vo;
        }

        if (contentType == 3) {
            CommunityPost post = communityPostMapper.selectById(contentId);
            if (post == null) {
                return null;
            }
            vo.setItemSubType("VIDEO".equalsIgnoreCase(post.getPostType()) ? "communityVideo" : "communityPost");
            vo.setTitle(post.getTitle());
            vo.setCover(post.getCover());
            if ("VIDEO".equalsIgnoreCase(post.getPostType())) {
                vo.setVideoUrl(post.getMediaUrl());
            }
            vo.setSubtitle(post.getSummary());
            return vo;
        }
        return null;
    }
}
