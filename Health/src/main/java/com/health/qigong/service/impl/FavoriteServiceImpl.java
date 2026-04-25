package com.health.qigong.service.impl;

import com.health.qigong.dto.FavoriteDTO;
import com.health.qigong.entity.CommunityPost;
import com.health.qigong.entity.Favorite;
import com.health.qigong.entity.Recipe;
import com.health.qigong.entity.Video;
import com.health.qigong.mapper.CommunityPostMapper;
import com.health.qigong.mapper.FavoriteMapper;
import com.health.qigong.mapper.RecipeMapper;
import com.health.qigong.mapper.VideoMapper;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.vo.HistoryItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private CommunityPostMapper communityPostMapper;

    @Override
    public void add(Long userId, FavoriteDTO dto) {
        Favorite old = favoriteMapper.selectOne(userId, dto.getContentId(), dto.getContentType());
        if (old != null) {
            return;
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setContentId(dto.getContentId());
        favorite.setContentType(dto.getContentType());
        favorite.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite);
    }

    @Override
    public void remove(Long userId, FavoriteDTO dto) {
        favoriteMapper.deleteByUserAndContent(userId, dto.getContentId(), dto.getContentType());
    }

    @Override
    public List<HistoryItemVO> list(Long userId) {
        List<Favorite> list = favoriteMapper.selectByUserId(userId);
        List<HistoryItemVO> result = new ArrayList<>();
        for (Favorite item : list) {
            HistoryItemVO vo = buildHistoryItem(item.getContentId(), item.getContentType(), item.getCreateTime());
            if (vo != null) {
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public boolean exists(Long userId, Long contentId, Integer contentType) {
        return favoriteMapper.selectOne(userId, contentId, contentType) != null;
    }

    @Override
    public int count(Long contentId, Integer contentType) {
        Integer count = favoriteMapper.countByContent(contentId, contentType);
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

        if (contentType == 2) {
            Recipe recipe = recipeMapper.getbyId(contentId);
            if (recipe == null) {
                return null;
            }
            vo.setItemSubType("recipe");
            vo.setTitle(recipe.getName());
            vo.setCover(recipe.getCover());
            vo.setSubtitle(recipe.getEffect());
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
