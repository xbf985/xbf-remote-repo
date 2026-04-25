package com.health.qigong.controller;

import com.health.qigong.dto.FavoriteDTO;
import com.health.qigong.result.Result;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.service.RecipeService;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.RecipeCardVO;
import com.health.qigong.vo.RecipeDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/list")
    public Result<List<RecipeCardVO>> list(@RequestParam("categoryId") long categoryId) {
        Long userId = UserContext.getUserId();
        return Result.success(recipeService.listCards(userId, categoryId));
    }

    @GetMapping("/{id}")
    public Result<RecipeDetailVO> detail(@PathVariable("id") long id) {
        Long userId = UserContext.getUserId();
        return Result.success(recipeService.getDetail(userId, id));
    }

    @PostMapping("/{id}/favorite")
    public Result<Void> favorite(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        FavoriteDTO dto = new FavoriteDTO();
        dto.setContentId(id);
        dto.setContentType(2);
        favoriteService.add(userId, dto);
        return Result.successMsg("Favorited");
    }

    @PostMapping("/{id}/unfavorite")
    public Result<Void> unfavorite(@PathVariable("id") Long id) {
        Long userId = UserContext.getUserId();
        FavoriteDTO dto = new FavoriteDTO();
        dto.setContentId(id);
        dto.setContentType(2);
        favoriteService.remove(userId, dto);
        return Result.successMsg("Unfavorited");
    }
}
