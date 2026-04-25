package com.health.qigong.mapper;

import com.health.qigong.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecipeMapper {
    @Select("""
            select id,
                   name,
                   cover,
                   category_id as categoryId,
                   effect,
                   ingredients,
                   steps,
                   create_time as createTime
            from recipe
            where category_id=#{categoryId}
            order by create_time desc
            """)
    List<Recipe> list(long categoryId);

    @Select("""
            select id,
                   name,
                   cover,
                   category_id as categoryId,
                   effect,
                   ingredients,
                   steps,
                   create_time as createTime
            from recipe
            where id=#{id}
            """)
    Recipe getbyId(long id);
}
