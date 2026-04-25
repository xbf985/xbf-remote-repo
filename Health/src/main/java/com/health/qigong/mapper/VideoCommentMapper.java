package com.health.qigong.mapper;

import com.health.qigong.entity.VideoComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoCommentMapper {

    @Insert("""
            insert into video_comment(video_id, user_id, content, create_time)
            values(#{videoId}, #{userId}, #{content}, #{createTime})
            """)
    void insert(VideoComment comment);

    @Select("""
            select id,
                   video_id as videoId,
                   user_id as userId,
                   content,
                   create_time as createTime
            from video_comment
            where video_id = #{videoId}
            order by create_time desc
            """)
    List<VideoComment> selectByVideoId(Long videoId);

    @Select("select count(*) from video_comment where video_id = #{videoId}")
    Integer countByVideoId(@Param("videoId") Long videoId);
}
