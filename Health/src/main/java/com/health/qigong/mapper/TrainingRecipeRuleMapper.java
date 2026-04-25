package com.health.qigong.mapper;

import com.health.qigong.entity.TrainingRecipeRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrainingRecipeRuleMapper {

    @Select("""
            select id,
                   recipe_id as recipeId,
                   goal_code as goalCode,
                   method_code as methodCode,
                   issue_code as issueCode,
                   season_category as seasonCategory,
                   min_score as minScore,
                   max_score as maxScore,
                   priority
            from training_recipe_rule
            order by priority desc, id asc
            """)
    List<TrainingRecipeRule> listAll();
}
