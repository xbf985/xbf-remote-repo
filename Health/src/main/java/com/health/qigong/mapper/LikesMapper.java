package com.health.qigong.mapper;

import com.health.qigong.entity.Likes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LikesMapper {

    @Select("""
            select id,
                   user_id as userId,
                   content_id as contentId,
                   content_type as contentType,
                   create_time as createTime
            from likes
            where user_id=#{userId} and content_id=#{contentId} and content_type=#{contentType}
            limit 1
            """)
    Likes selectOne(@Param("userId") long userId, @Param("contentId") Long contentId, @Param("contentType") Integer contentType);

    @Insert("insert into likes(user_id,content_id,content_type,create_time)values (#{userId},#{contentId},#{contentType},#{createTime})")
    void insert(Likes like);

    @Delete("delete from likes where user_id=#{userId} and content_id=#{contentId} and content_type=#{contentType}")
    void remove(@Param("userId") long userId, @Param("contentId") Long contentId, @Param("contentType") Integer contentType);

    @Select("""
            select id,
                   user_id as userId,
                   content_id as contentId,
                   content_type as contentType,
                   create_time as createTime
            from likes
            where user_id=#{userId}
            order by create_time desc
            """)
    List<Likes> selectByUserId(long userId);

    @Select("select count(*) from likes where content_id=#{contentId} and content_type=#{contentType}")
    Integer countByContent(@Param("contentId") Long contentId, @Param("contentType") Integer contentType);
}
