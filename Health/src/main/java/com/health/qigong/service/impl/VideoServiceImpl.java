package com.health.qigong.service.impl;

import com.health.qigong.dto.VideoCommentDTO;
import com.health.qigong.entity.ContentViewRecord;
import com.health.qigong.entity.User;
import com.health.qigong.entity.Video;
import com.health.qigong.entity.VideoComment;
import com.health.qigong.exception.BusinessException;
import com.health.qigong.mapper.ContentViewRecordMapper;
import com.health.qigong.mapper.UserMapper;
import com.health.qigong.mapper.VideoCommentMapper;
import com.health.qigong.mapper.VideoMapper;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.service.LikesService;
import com.health.qigong.service.VideoService;
import com.health.qigong.vo.HomeCourseCategoryVO;
import com.health.qigong.vo.HomeVideoVO;
import com.health.qigong.vo.VideoCommentVO;
import com.health.qigong.vo.VideoDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    private static final List<HomeCourseCategoryVO> VIDEO_CATEGORIES = Arrays.asList(
            new HomeCourseCategoryVO(1L, "八段锦", "修身", "调理脏腑，疏通经络"),
            new HomeCourseCategoryVO(2L, "太极拳", "悟道", "以柔克刚，内外兼修"),
            new HomeCourseCategoryVO(3L, "五禽戏", "长寿", "华佗所创，仿生导引"),
            new HomeCourseCategoryVO(4L, "易筋经", "淬体", "少林绝学，易筋洗髓")
    );

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoCommentMapper videoCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private ContentViewRecordMapper contentViewRecordMapper;

    @Override
    public List<HomeCourseCategoryVO> listCategories() {
        return VIDEO_CATEGORIES;
    }

    @Override
    public List<HomeVideoVO> listByCategory(Long userId, Long categoryId) {
        List<Video> videos = videoMapper.listByCategoryId(categoryId);
        List<HomeVideoVO> result = new ArrayList<>();
        for (Video video : videos) {
            HomeVideoVO item = new HomeVideoVO();
            item.setId(video.getId());
            item.setCategoryId(video.getCategoryId());
            item.setTitle(video.getTitle());
            item.setCover(video.getCover());
            item.setMediaUrl(video.getVideoUrl());
            item.setEffect(video.getIntro());
            item.setDuration(video.getDurationSeconds());
            item.setAuthorName(video.getAuthorName());
            item.setQiValue(video.getQiValue());
            item.setLiked(likesService.exists(userId, video.getId(), 1));
            item.setCollected(favoriteService.exists(userId, video.getId(), 1));
            result.add(item);
        }
        return result;
    }

    @Override
    public VideoDetailVO getDetail(Long userId, Long videoId) {
        Video video = videoMapper.selectById(videoId);
        if (video == null) {
            throw new BusinessException(404, "Video not found");
        }
        recordView(userId, videoId, 1);

        VideoDetailVO detail = new VideoDetailVO();
        detail.setId(video.getId());
        detail.setCategoryId(video.getCategoryId());
        detail.setTitle(video.getTitle());
        detail.setCover(video.getCover());
        detail.setVideoUrl(video.getVideoUrl());
        detail.setIntro(video.getIntro());
        detail.setAuthorName(video.getAuthorName());
        detail.setDurationSeconds(video.getDurationSeconds());
        detail.setQiValue(video.getQiValue());
        detail.setLiked(likesService.exists(userId, videoId, 1));
        detail.setCollected(favoriteService.exists(userId, videoId, 1));
        detail.setLikeCount(likesService.count(videoId, 1));
        detail.setFavoriteCount(favoriteService.count(videoId, 1));
        detail.setCommentCount(defaultZero(videoCommentMapper.countByVideoId(videoId)));
        return detail;
    }

    @Override
    public List<VideoCommentVO> listComments(Long videoId) {
        List<VideoComment> comments = videoCommentMapper.selectByVideoId(videoId);
        List<VideoCommentVO> result = new ArrayList<>();
        for (VideoComment comment : comments) {
            VideoCommentVO item = new VideoCommentVO();
            item.setId(comment.getId());
            item.setVideoId(comment.getVideoId());
            item.setUserId(comment.getUserId());
            item.setContent(comment.getContent());
            item.setCreateTime(comment.getCreateTime());
            User user = userMapper.selectById(comment.getUserId());
            item.setNickname(user == null ? "修行者" : user.getUsername());
            item.setAvatar(user == null ? null : user.getAvatar());
            result.add(item);
        }
        return result;
    }

    @Override
    public void addComment(Long userId, Long videoId, VideoCommentDTO dto) {
        if (dto == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new BusinessException(400, "Comment content cannot be empty");
        }
        Video video = videoMapper.selectById(videoId);
        if (video == null) {
            throw new BusinessException(404, "Video not found");
        }

        VideoComment comment = new VideoComment();
        comment.setVideoId(videoId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent().trim());
        comment.setCreateTime(LocalDateTime.now());
        videoCommentMapper.insert(comment);
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private void recordView(Long userId, Long contentId, Integer contentType) {
        if (userId == null || contentId == null || contentType == null) {
            return;
        }
        ContentViewRecord record = new ContentViewRecord();
        record.setUserId(userId);
        record.setContentId(contentId);
        record.setContentType(contentType);
        record.setDuration(0);
        record.setViewTime(LocalDateTime.now());
        contentViewRecordMapper.insert(record);
    }
}
