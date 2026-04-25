package com.health.qigong.service.impl;

import com.health.qigong.dto.CommunityCommentDTO;
import com.health.qigong.dto.CommunityPublishDTO;
import com.health.qigong.entity.CommunityComment;
import com.health.qigong.entity.CommunityPost;
import com.health.qigong.entity.ContentViewRecord;
import com.health.qigong.entity.User;
import com.health.qigong.entity.UserFollow;
import com.health.qigong.exception.BusinessException;
import com.health.qigong.mapper.CommunityCommentMapper;
import com.health.qigong.mapper.CommunityPostMapper;
import com.health.qigong.mapper.ContentViewRecordMapper;
import com.health.qigong.mapper.UserFollowMapper;
import com.health.qigong.mapper.UserMapper;
import com.health.qigong.service.CommunityService;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.service.LikesService;
import com.health.qigong.vo.AuthorProfileVO;
import com.health.qigong.vo.CommunityCommentVO;
import com.health.qigong.vo.CommunityDetailVO;
import com.health.qigong.vo.CommunityFeedItemVO;
import com.health.qigong.vo.CommunityFeedPageVO;
import com.health.qigong.vo.CommunityMinePageVO;
import com.health.qigong.vo.CommunityTabVO;
import com.health.qigong.vo.HistoryItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    private CommunityPostMapper communityPostMapper;

    @Autowired
    private CommunityCommentMapper communityCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private LikesService likesService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ContentViewRecordMapper contentViewRecordMapper;

    @Override
    public CommunityFeedPageVO getFeed(Long userId, String tab, String keyword) {
        List<CommunityPost> posts = communityPostMapper.selectAll();
        String normalizedTab = normalizeFeedTab(tab);

        CommunityFeedPageVO page = new CommunityFeedPageVO();
        page.setCurrentTab(normalizedTab);
        page.setKeyword(keyword);
        page.setTabs(buildFeedTabs(posts));
        page.setItems(filterPosts(posts, normalizedTab, keyword).stream()
                .map(post -> toFeedItem(userId, post))
                .collect(Collectors.toList()));
        return page;
    }

    @Override
    public CommunityMinePageVO getMinePage(Long userId, String tab) {
        List<CommunityPost> posts = communityPostMapper.selectByAuthorId(userId);
        String normalizedTab = normalizeFeedTab(tab);

        CommunityMinePageVO page = new CommunityMinePageVO();
        page.setCurrentTab(normalizedTab);
        page.setTabs(buildFeedTabs(posts));
        page.setItems(filterPosts(posts, normalizedTab, null).stream()
                .map(post -> toFeedItem(userId, post))
                .collect(Collectors.toList()));
        return page;
    }

    @Override
    public CommunityDetailVO getDetail(Long userId, Long postId) {
        CommunityPost post = getPost(postId);
        User author = getUser(post.getAuthorId());
        recordView(userId, postId, 3);

        CommunityDetailVO detail = new CommunityDetailVO();
        detail.setId(post.getId());
        detail.setPostType(post.getPostType());
        detail.setPracticeTag(post.getPracticeTag());
        detail.setTitle(post.getTitle());
        detail.setSummary(post.getSummary());
        detail.setCover(post.getCover());
        detail.setMediaUrl(post.getMediaUrl());
        detail.setContent(post.getContent());
        detail.setLikeCount(likesService.count(postId, 3));
        detail.setFavoriteCount(favoriteService.count(postId, 3));
        detail.setCommentCount(defaultZero(communityCommentMapper.countByPostId(postId)));
        detail.setLiked(likesService.exists(userId, postId, 3));
        detail.setCollected(favoriteService.exists(userId, postId, 3));
        detail.setAuthorId(author.getId());
        detail.setAuthorName(author.getUsername());
        detail.setAuthorAvatar(author.getAvatar());
        detail.setAuthorRealm(author.getRealm());
        detail.setAuthorFollowerCount(defaultZero(userFollowMapper.countFollowers(author.getId())));
        detail.setFollowed(isFollowed(userId, author.getId()));
        detail.setComments(getComments(postId));
        return detail;
    }

    @Override
    public List<CommunityCommentVO> getComments(Long postId) {
        return communityCommentMapper.selectByPostId(postId).stream()
                .map(this::toCommentVO)
                .collect(Collectors.toList());
    }

    @Override
    public void addComment(Long userId, Long postId, CommunityCommentDTO dto) {
        getPost(postId);
        if (dto == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new BusinessException(400, "Comment content cannot be empty");
        }

        CommunityComment comment = new CommunityComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent().trim());
        comment.setCreateTime(LocalDateTime.now());
        communityCommentMapper.insert(comment);
    }

    @Override
    public Long publish(Long userId, CommunityPublishDTO dto) {
        if (dto == null) {
            throw new BusinessException(400, "Publish data cannot be empty");
        }
        String postType = normalizePostType(dto.getPostType());
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new BusinessException(400, "Title cannot be empty");
        }
        if ("VIDEO".equals(postType) && (dto.getMediaUrl() == null || dto.getMediaUrl().trim().isEmpty())) {
            throw new BusinessException(400, "Video post must include mediaUrl");
        }
        if ("POST".equals(postType) && (dto.getContent() == null || dto.getContent().trim().isEmpty())) {
            throw new BusinessException(400, "Text post must include content");
        }

        CommunityPost post = new CommunityPost();
        post.setAuthorId(userId);
        post.setPostType(postType);
        post.setTitle(dto.getTitle().trim());
        post.setSummary(trimToNull(dto.getSummary()));
        post.setCover(trimToNull(dto.getCover()));
        post.setMediaUrl(trimToNull(dto.getMediaUrl()));
        post.setContent(trimToNull(dto.getContent()));
        post.setPracticeTag(trimToNull(dto.getPracticeTag()));
        post.setCreateTime(LocalDateTime.now());
        communityPostMapper.insert(post);
        return post.getId();
    }

    @Override
    public void followAuthor(Long userId, Long authorId) {
        if (userId.equals(authorId)) {
            return;
        }
        getUser(authorId);
        if (userFollowMapper.selectOne(userId, authorId) != null) {
            return;
        }

        UserFollow follow = new UserFollow();
        follow.setUserId(userId);
        follow.setTargetUserId(authorId);
        follow.setCreateTime(LocalDateTime.now());
        userFollowMapper.insert(follow);
    }

    @Override
    public void unfollowAuthor(Long userId, Long authorId) {
        userFollowMapper.delete(userId, authorId);
    }

    @Override
    public AuthorProfileVO getAuthorProfile(Long userId, Long authorId, String tab) {
        User author = getUser(authorId);
        String normalizedTab = normalizeAuthorTab(tab);
        List<CommunityPost> works = communityPostMapper.selectByAuthorId(authorId);
        List<HistoryItemVO> favorites = favoriteService.list(authorId).stream()
                .filter(item -> item.getContentType() == 3)
                .collect(Collectors.toList());

        AuthorProfileVO profile = new AuthorProfileVO();
        profile.setAuthorId(author.getId());
        profile.setAuthorName(author.getUsername());
        profile.setAvatar(author.getAvatar());
        profile.setRealm(author.getRealm());
        profile.setVerifiedLabel(author.getVerifiedLabel());
        profile.setBio(author.getBio());
        profile.setLocation(author.getLocation());
        profile.setFollowed(isFollowed(userId, authorId));
        profile.setFollowingCount(defaultZero(userFollowMapper.countFollowing(authorId)));
        profile.setFollowerCount(defaultZero(userFollowMapper.countFollowers(authorId)));
        profile.setTotalSupportCount(sumSupport(works));
        profile.setCurrentTab(normalizedTab);
        profile.setTabs(Arrays.asList(
                new CommunityTabVO("works", "卷宗", works.size()),
                new CommunityTabVO("favorites", "藏经阁", favorites.size())
        ));
        profile.setWorks("favorites".equals(normalizedTab)
                ? fromFavorites(userId, favorites)
                : works.stream().map(post -> toFeedItem(userId, post)).collect(Collectors.toList()));
        return profile;
    }

    @Override
    public List<CommunityFeedItemVO> getMyWorks(Long userId, String tab) {
        return filterPosts(communityPostMapper.selectByAuthorId(userId), normalizeFeedTab(tab), null).stream()
                .map(post -> toFeedItem(userId, post))
                .collect(Collectors.toList());
    }

    private List<CommunityTabVO> buildFeedTabs(List<CommunityPost> posts) {
        return Arrays.asList(
                new CommunityTabVO("all", "全集", posts.size()),
                new CommunityTabVO("video", "演武(视频)", countByType(posts, "VIDEO")),
                new CommunityTabVO("baduanjin", "八段锦", countByTag(posts, "八段锦")),
                new CommunityTabVO("taijiquan", "太极拳", countByTag(posts, "太极拳"))
        );
    }

    private int countByType(List<CommunityPost> posts, String postType) {
        return (int) posts.stream()
                .filter(post -> postType.equalsIgnoreCase(post.getPostType()))
                .count();
    }

    private int countByTag(List<CommunityPost> posts, String tag) {
        return (int) posts.stream()
                .filter(post -> tag.equalsIgnoreCase(safe(post.getPracticeTag())))
                .count();
    }

    private List<CommunityPost> filterPosts(List<CommunityPost> posts, String tab, String keyword) {
        return posts.stream()
                .filter(post -> matchesTab(post, tab))
                .filter(post -> matchesKeyword(post, keyword))
                .collect(Collectors.toList());
    }

    private boolean matchesTab(CommunityPost post, String tab) {
        if ("all".equals(tab)) {
            return true;
        }
        if ("video".equals(tab)) {
            return "VIDEO".equalsIgnoreCase(post.getPostType());
        }
        if ("baduanjin".equals(tab)) {
            return "八段锦".equalsIgnoreCase(safe(post.getPracticeTag()));
        }
        if ("taijiquan".equals(tab)) {
            return "太极拳".equalsIgnoreCase(safe(post.getPracticeTag()));
        }
        return true;
    }

    private boolean matchesKeyword(CommunityPost post, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        String lower = keyword.toLowerCase(Locale.ROOT);
        return safe(post.getTitle()).toLowerCase(Locale.ROOT).contains(lower)
                || safe(post.getSummary()).toLowerCase(Locale.ROOT).contains(lower)
                || safe(post.getPracticeTag()).toLowerCase(Locale.ROOT).contains(lower)
                || safe(post.getContent()).toLowerCase(Locale.ROOT).contains(lower);
    }

    private CommunityFeedItemVO toFeedItem(Long userId, CommunityPost post) {
        User author = getUser(post.getAuthorId());

        CommunityFeedItemVO item = new CommunityFeedItemVO();
        item.setId(post.getId());
        item.setPostType(post.getPostType());
        item.setPracticeTag(post.getPracticeTag());
        item.setTitle(post.getTitle());
        item.setSummary(post.getSummary());
        item.setCover(post.getCover());
        item.setMediaUrl(post.getMediaUrl());
        item.setAuthorId(author.getId());
        item.setAuthorName(author.getUsername());
        item.setAuthorAvatar(author.getAvatar());
        item.setLikeCount(likesService.count(post.getId(), 3));
        item.setFavoriteCount(favoriteService.count(post.getId(), 3));
        item.setCommentCount(defaultZero(communityCommentMapper.countByPostId(post.getId())));
        item.setLiked(likesService.exists(userId, post.getId(), 3));
        item.setCollected(favoriteService.exists(userId, post.getId(), 3));
        item.setCreateTime(post.getCreateTime());
        return item;
    }

    private CommunityCommentVO toCommentVO(CommunityComment comment) {
        User user = getUser(comment.getUserId());

        CommunityCommentVO vo = new CommunityCommentVO();
        vo.setId(comment.getId());
        vo.setPostId(comment.getPostId());
        vo.setUserId(comment.getUserId());
        vo.setNickname(user.getUsername());
        vo.setAvatar(user.getAvatar());
        vo.setContent(comment.getContent());
        vo.setCreateTime(comment.getCreateTime());
        return vo;
    }

    private List<CommunityFeedItemVO> fromFavorites(Long viewerUserId, List<HistoryItemVO> favorites) {
        return favorites.stream()
                .map(item -> communityPostMapper.selectById(item.getContentId()))
                .filter(post -> post != null)
                .map(post -> toFeedItem(viewerUserId, post))
                .collect(Collectors.toList());
    }

    private int sumSupport(List<CommunityPost> works) {
        int total = 0;
        for (CommunityPost post : works) {
            total += likesService.count(post.getId(), 3);
            total += favoriteService.count(post.getId(), 3);
        }
        return total;
    }

    private boolean isFollowed(Long userId, Long authorId) {
        return userId != null && userFollowMapper.selectOne(userId, authorId) != null;
    }

    private CommunityPost getPost(Long postId) {
        CommunityPost post = communityPostMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException(404, "Post not found");
        }
        return post;
    }

    private User getUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }
        return user;
    }

    private String normalizeFeedTab(String tab) {
        if (tab == null || tab.isBlank()) {
            return "all";
        }
        return switch (tab) {
            case "all", "video", "baduanjin", "taijiquan" -> tab;
            default -> "all";
        };
    }

    private String normalizeAuthorTab(String tab) {
        if (tab == null || tab.isBlank()) {
            return "works";
        }
        return switch (tab) {
            case "works", "favorites" -> tab;
            default -> "works";
        };
    }

    private String normalizePostType(String postType) {
        if (postType == null || postType.isBlank()) {
            return "POST";
        }
        if ("video".equalsIgnoreCase(postType)) {
            return "VIDEO";
        }
        return "POST";
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private String safe(String value) {
        return value == null ? "" : value;
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
