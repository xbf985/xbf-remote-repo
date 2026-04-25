package com.health.qigong.controller;

import com.health.qigong.dto.FavoriteDTO;
import com.health.qigong.dto.LikeDTO;
import com.health.qigong.dto.VideoCommentDTO;
import com.health.qigong.result.Result;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.service.LikesService;
import com.health.qigong.service.VideoService;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.HomeCourseCategoryVO;
import com.health.qigong.vo.HomeVideoVO;
import com.health.qigong.vo.VideoCommentVO;
import com.health.qigong.vo.VideoDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private LikesService likesService;

    @GetMapping("/categories")
    public Result<List<HomeCourseCategoryVO>> categories() {
        return Result.success(videoService.listCategories());
    }

    @GetMapping("/list")
    public Result<List<HomeVideoVO>> list(@RequestParam("categoryId") Long categoryId) {
        Long userId = UserContext.getUserId();
        return Result.success(videoService.listByCategory(userId, categoryId));
    }

    @GetMapping("/{id}")
    public Result<VideoDetailVO> detail(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        return Result.success(videoService.getDetail(userId, id));
    }

    @GetMapping("/{id}/comments")
    public Result<List<VideoCommentVO>> comments(@PathVariable("id") Long id) {
        return Result.success(videoService.listComments(id));
    }

    @PostMapping("/{id}/comments")
    public Result<Void> addComment(@PathVariable("id") Long id, @RequestBody VideoCommentDTO dto) {
        Long userId = UserContext.getUserId();
        videoService.addComment(userId, id, dto);
        return Result.successMsg("Comment added");
    }

    @PostMapping("/{id}/like")
    public Result<Void> like(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        LikeDTO dto = new LikeDTO();
        dto.setContentId(id);
        dto.setContentType(1);
        likesService.add(userId, dto);
        return Result.successMsg("Liked");
    }

    @PostMapping("/{id}/unlike")
    public Result<Void> unlike(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        LikeDTO dto = new LikeDTO();
        dto.setContentId(id);
        dto.setContentType(1);
        likesService.remove(userId, dto);
        return Result.successMsg("Unliked");
    }

    @PostMapping("/{id}/favorite")
    public Result<Void> favorite(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        FavoriteDTO dto = new FavoriteDTO();
        dto.setContentId(id);
        dto.setContentType(1);
        favoriteService.add(userId, dto);
        return Result.successMsg("Favorited");
    }

    @PostMapping("/{id}/unfavorite")
    public Result<Void> unfavorite(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        FavoriteDTO dto = new FavoriteDTO();
        dto.setContentId(id);
        dto.setContentType(1);
        favoriteService.remove(userId, dto);
        return Result.successMsg("Unfavorited");
    }
}
