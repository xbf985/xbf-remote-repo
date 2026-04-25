package com.health.qigong.mapper;

import com.health.qigong.entity.CommunityComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommunityCommentMapper {

    @Insert("""
            insert into community_comment(post_id, user_id, content, create_time)
            values(#{postId}, #{userId}, #{content}, #{createTime})
            """)
    void insert(CommunityComment comment);

    @Select("""
            select id,
                   post_id as postId,
                   user_id as userId,
                   content,
                   create_time as createTime
            from community_comment
            where post_id = #{postId}
            order by create_time desc
            """)
    List<CommunityComment> selectByPostId(Long postId);

    @Select("select count(*) from community_comment where post_id = #{postId}")
    Integer countByPostId(@Param("postId") Long postId);
}
