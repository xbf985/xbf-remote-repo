package com.health.qigong.mapper;

import com.health.qigong.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    Favorite selectOne(@Param("userId") Long userId, @Param("contentId") Long contentId, @Param("contentType") Integer contentType);

    void insert(Favorite favorite);

    void deleteByUserAndContent(@Param("userId") Long userId, @Param("contentId") Long contentId, @Param("contentType") Integer contentType);

    List<Favorite> selectByUserId(Long userId);

    Integer countByContent(@Param("contentId") Long contentId, @Param("contentType") Integer contentType);
}
