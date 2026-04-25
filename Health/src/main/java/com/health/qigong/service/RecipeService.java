package com.health.qigong.service;

import com.health.qigong.entity.Recipe;
import com.health.qigong.vo.RecipeCardVO;
import com.health.qigong.vo.RecipeDetailVO;
import org.springframework.stereotype.Service;
import java.util.List;


public interface RecipeService {
    List<Recipe> list(long categoryId);

    Recipe getbyId(long id);

    List<RecipeCardVO> listCards(Long userId, long categoryId);

    RecipeDetailVO getDetail(Long userId, long id);
}
