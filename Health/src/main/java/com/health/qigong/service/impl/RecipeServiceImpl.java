package com.health.qigong.service.impl;

import com.health.qigong.entity.ContentViewRecord;
import com.health.qigong.entity.Recipe;
import com.health.qigong.exception.BusinessException;
import com.health.qigong.mapper.ContentViewRecordMapper;
import com.health.qigong.mapper.RecipeMapper;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.service.RecipeService;
import com.health.qigong.vo.RecipeCardVO;
import com.health.qigong.vo.RecipeDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ContentViewRecordMapper contentViewRecordMapper;

    @Override
    public List<Recipe> list(long categoryId) {
        return recipeMapper.list(categoryId);
    }

    @Override
    public Recipe getbyId(long id) {
        return recipeMapper.getbyId(id);
    }

    @Override
    public List<RecipeCardVO> listCards(Long userId, long categoryId) {
        return recipeMapper.list(categoryId).stream()
                .map(recipe -> toCard(userId, recipe))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDetailVO getDetail(Long userId, long id) {
        Recipe recipe = recipeMapper.getbyId(id);
        if (recipe == null) {
            throw new BusinessException(404, "Recipe not found");
        }
        recordView(userId, recipe.getId(), 2);

        RecipeDetailVO detail = new RecipeDetailVO();
        detail.setId(recipe.getId());
        detail.setCategoryId(recipe.getCategoryId());
        detail.setCategoryName(resolveSolarTermName(recipe.getCategoryId()));
        detail.setName(recipe.getName());
        detail.setCover(recipe.getCover());
        detail.setEffectTags(splitText(recipe.getEffect(), "[,，/|]"));
        detail.setIngredients(recipe.getIngredients());
        detail.setIngredientItems(splitText(recipe.getIngredients(), "[,，；;、]"));
        detail.setSteps(recipe.getSteps());
        detail.setStepItems(splitSteps(recipe.getSteps()));
        detail.setFavoriteCount(favoriteService.count(recipe.getId(), 2));
        detail.setCollected(favoriteService.exists(userId, recipe.getId(), 2));
        return detail;
    }

    private RecipeCardVO toCard(Long userId, Recipe recipe) {
        RecipeCardVO card = new RecipeCardVO();
        card.setId(recipe.getId());
        card.setName(recipe.getName());
        card.setCover(recipe.getCover());
        card.setEffectTags(splitText(recipe.getEffect(), "[,，/|]"));
        card.setIngredientsPreview(buildIngredientsPreview(recipe.getIngredients()));
        card.setCollectCount((long) favoriteService.count(recipe.getId(), 2));
        card.setCollected(favoriteService.exists(userId, recipe.getId(), 2));
        return card;
    }

    private String buildIngredientsPreview(String ingredients) {
        if (ingredients == null || ingredients.isBlank()) {
            return "";
        }
        return ingredients.length() > 40 ? ingredients.substring(0, 40) + "..." : ingredients;
    }

    private List<String> splitText(String value, String regex) {
        if (value == null || value.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(regex))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toList());
    }

    private List<String> splitSteps(String steps) {
        if (steps == null || steps.isBlank()) {
            return Collections.emptyList();
        }
        List<String> items = Arrays.stream(steps.split("\\r?\\n"))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toList());
        if (!items.isEmpty()) {
            return items;
        }
        return splitText(steps, "[；;]");
    }

    private String resolveSolarTermName(long categoryId) {
        if (categoryId == 1L) {
            return "立春";
        }
        if (categoryId == 2L) {
            return "惊蛰";
        }
        if (categoryId == 3L) {
            return "立夏";
        }
        if (categoryId == 4L) {
            return "夏至";
        }
        if (categoryId == 5L) {
            return "立秋";
        }
        if (categoryId == 6L) {
            return "白露";
        }
        if (categoryId == 7L) {
            return "立冬";
        }
        if (categoryId == 8L) {
            return "冬至";
        }
        return "时令养生";
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
