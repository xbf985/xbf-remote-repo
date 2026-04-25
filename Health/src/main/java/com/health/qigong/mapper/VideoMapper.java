package com.health.qigong.mapper;

import com.health.qigong.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper {

    @Select("""
            select id,
                   category_id as categoryId,
                   video_name as videoName,
                   title,
                   cover,
                   video_url as videoUrl,
                   intro,
                   author_name as authorName,
                   duration_seconds as durationSeconds,
                   qi_value as qiValue,
                   create_time as createTime
            from video
            order by category_id asc, create_time desc
            """)
    List<Video> listAll();

    @Select("""
            select id,
                   category_id as categoryId,
                   video_name as videoName,
                   title,
                   cover,
                   video_url as videoUrl,
                   intro,
                   author_name as authorName,
                   duration_seconds as durationSeconds,
                   qi_value as qiValue,
                   create_time as createTime
            from video
            where category_id = #{categoryId}
            order by create_time desc
            """)
    List<Video> listByCategoryId(Long categoryId);

    @Select("""
            select id,
                   category_id as categoryId,
                   video_name as videoName,
                   title,
                   cover,
                   video_url as videoUrl,
                   intro,
                   author_name as authorName,
                   duration_seconds as durationSeconds,
                   qi_value as qiValue,
                   create_time as createTime
            from video
            where id = #{id}
            """)
    Video selectById(Long id);
}
