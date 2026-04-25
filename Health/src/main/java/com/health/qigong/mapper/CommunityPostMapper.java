package com.health.qigong.mapper;

import com.health.qigong.entity.CommunityPost;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommunityPostMapper {

    @Insert("""
            insert into community_post(author_id, post_type, title, summary, cover, media_url, content, practice_tag, create_time)
            values(#{authorId}, #{postType}, #{title}, #{summary}, #{cover}, #{mediaUrl}, #{content}, #{practiceTag}, #{createTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CommunityPost post);

    @Select("""
            select id,
                   author_id as authorId,
                   post_type as postType,
                   title,
                   summary,
                   cover,
                   media_url as mediaUrl,
                   content,
                   practice_tag as practiceTag,
                   create_time as createTime
            from community_post
            order by create_time desc
            """)
    List<CommunityPost> selectAll();

    @Select("""
            select id,
                   author_id as authorId,
                   post_type as postType,
                   title,
                   summary,
                   cover,
                   media_url as mediaUrl,
                   content,
                   practice_tag as practiceTag,
                   create_time as createTime
            from community_post
            where author_id = #{authorId}
            order by create_time desc
            """)
    List<CommunityPost> selectByAuthorId(Long authorId);

    @Select("""
            select id,
                   author_id as authorId,
                   post_type as postType,
                   title,
                   summary,
                   cover,
                   media_url as mediaUrl,
                   content,
                   practice_tag as practiceTag,
                   create_time as createTime
            from community_post
            where id = #{id}
            """)
    CommunityPost selectById(Long id);

    @Select("""
            select count(*)
            from community_post
            where author_id = #{authorId}
            """)
    Integer countByAuthorId(Long authorId);

    @Select("""
            select count(*)
            from community_post
            where author_id = #{authorId}
              and post_type = #{postType}
            """)
    Integer countByAuthorIdAndType(@Param("authorId") Long authorId, @Param("postType") String postType);
}
