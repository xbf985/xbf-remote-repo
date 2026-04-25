package com.health.qigong.controller;

import com.health.qigong.dto.CommunityCommentDTO;
import com.health.qigong.dto.CommunityPublishDTO;
import com.health.qigong.dto.FavoriteDTO;
import com.health.qigong.dto.LikeDTO;
import com.health.qigong.result.Result;
import com.health.qigong.service.CommunityService;
import com.health.qigong.service.FileStorageService;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.service.LikesService;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.AuthorProfileVO;
import com.health.qigong.vo.CommunityCommentVO;
import com.health.qigong.vo.CommunityDetailVO;
import com.health.qigong.vo.CommunityFeedItemVO;
import com.health.qigong.vo.CommunityFeedPageVO;
import com.health.qigong.vo.CommunityMinePageVO;
import com.health.qigong.vo.UploadFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/feed")
    public Result<CommunityFeedPageVO> feed(
            @RequestParam(value = "tab", defaultValue = "all") String tab,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Long userId = UserContext.getUserId();
        return Result.success(communityService.getFeed(userId, tab, keyword));
    }

    @GetMapping("/my-page")
    public Result<CommunityMinePageVO> myPage(@RequestParam(value = "tab", defaultValue = "all") String tab) {
        Long userId = UserContext.getUserId();
        return Result.success(communityService.getMinePage(userId, tab));
    }

    @GetMapping("/my-works")
    public Result<List<CommunityFeedItemVO>> myWorks(@RequestParam(value = "tab", defaultValue = "all") String tab) {
        Long userId = UserContext.getUserId();
        return Result.success(communityService.getMyWorks(userId, tab));
    }

    @PostMapping("/upload")
    public Result<UploadFileVO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "community") String folder
    ) {
        return Result.success(fileStorageService.store(file, folder));
    }

    @PostMapping("/posts")
    public Result<Long> publish(@RequestBody CommunityPublishDTO dto) {
        Long userId = UserContext.getUserId();
        return Result.success(communityService.publish(userId, dto));
    }

    @GetMapping("/posts/{id}")
    public Result<CommunityDetailVO> detail(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        return Result.success(communityService.getDetail(userId, id));
    }

    @GetMapping("/posts/{id}/comments")
    public Result<List<CommunityCommentVO>> comments(@PathVariable("id") Long id) {
        return Result.success(communityService.getComments(id));
    }

    @PostMapping("/posts/{id}/comments")
    public Result<Void> addComment(@PathVariable("id") Long id, @RequestBody CommunityCommentDTO dto) {
        Long userId = UserContext.getUserId();
        communityService.addComment(userId, id, dto);
        return Result.successMsg("Comment added");
    }

    @PostMapping("/posts/{id}/like")
    public Result<Void> like(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        LikeDTO dto = new LikeDTO();
        dto.setContentId(id);
        dto.setContentType(3);
        likesService.add(userId, dto);
        return Result.successMsg("Liked");
    }

    @PostMapping("/posts/{id}/unlike")
    public Result<Void> unlike(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        LikeDTO dto = new LikeDTO();
        dto.setContentId(id);
        dto.setContentType(3);
        likesService.remove(userId, dto);
        return Result.successMsg("Unliked");
    }

    @PostMapping("/posts/{id}/favorite")
    public Result<Void> favorite(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        FavoriteDTO dto = new FavoriteDTO();
        dto.setContentId(id);
        dto.setContentType(3);
        favoriteService.add(userId, dto);
        return Result.successMsg("Favorited");
    }

    @PostMapping("/posts/{id}/unfavorite")
    public Result<Void> unfavorite(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        FavoriteDTO dto = new FavoriteDTO();
        dto.setContentId(id);
        dto.setContentType(3);
        favoriteService.remove(userId, dto);
        return Result.successMsg("Unfavorited");
    }

    @GetMapping("/authors/{authorId}")
    public Result<AuthorProfileVO> author(
            @PathVariable("authorId") Long authorId,
            @RequestParam(value = "tab", defaultValue = "works") String tab
    ) {
        Long userId = UserContext.getUserId();
        return Result.success(communityService.getAuthorProfile(userId, authorId, tab));
    }

    @PostMapping("/authors/{authorId}/follow")
    public Result<Void> follow(@PathVariable("authorId") Long authorId) {
        Long userId = UserContext.getUserId();
        communityService.followAuthor(userId, authorId);
        return Result.successMsg("Followed");
    }

    @PostMapping("/authors/{authorId}/unfollow")
    public Result<Void> unfollow(@PathVariable("authorId") Long authorId) {
        Long userId = UserContext.getUserId();
        communityService.unfollowAuthor(userId, authorId);
        return Result.successMsg("Unfollowed");
    }
}
