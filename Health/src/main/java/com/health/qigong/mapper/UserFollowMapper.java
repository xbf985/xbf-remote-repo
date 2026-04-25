package com.health.qigong.mapper;

import com.health.qigong.entity.UserFollow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserFollowMapper {

    @Insert("""
            insert into user_follow(user_id, target_user_id, create_time)
            values(#{userId}, #{targetUserId}, #{createTime})
            """)
    void insert(UserFollow follow);

    @Delete("""
            delete from user_follow
            where user_id = #{userId}
              and target_user_id = #{targetUserId}
            """)
    void delete(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);

    @Select("""
            select id,
                   user_id as userId,
                   target_user_id as targetUserId,
                   create_time as createTime
            from user_follow
            where user_id = #{userId}
              and target_user_id = #{targetUserId}
            limit 1
            """)
    UserFollow selectOne(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);

    @Select("select count(*) from user_follow where user_id = #{userId}")
    Integer countFollowing(Long userId);

    @Select("select count(*) from user_follow where target_user_id = #{targetUserId}")
    Integer countFollowers(Long targetUserId);
}
