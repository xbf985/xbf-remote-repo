package com.health.qigong.mapper;

import com.health.qigong.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Insert("insert into users(username,password,phone)values(#{username},#{password},#{phone})")
    void insert(User user);

    @Select("""
            select coalesce(userid, id) as id,
                   username,
                   password,
                   avatar,
                   phone,
                   bio,
                   coalesce(location, ip_location) as location,
                   realm,
                   verified_label as verifiedLabel,
                   create_time as createTime
            from users
            where username=#{username}
            limit 1
            """)
    User findByUsername(String username);

    @Update("""
            update users
            set password=#{password}
            where userid=#{id}
               or (userid is null and id=#{id})
            """)
    void updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("""
            update users
            set phone=#{phone}
            where userid=#{id}
               or (userid is null and id=#{id})
            """)
    void updatePhone(@Param("id") Long id, @Param("phone") String phone);

    @Update("""
            update users
            set username = #{username},
                avatar = #{avatar},
                bio = #{bio}
            where userid = #{id}
               or (userid is null and id = #{id})
            """)
    void updateProfile(User user);

    @Select("""
            select coalesce(userid, id) as id,
                   username,
                   password,
                   avatar,
                   phone,
                   bio,
                   coalesce(location, ip_location) as location,
                   realm,
                   verified_label as verifiedLabel,
                   create_time as createTime
            from users
            where userid=#{userId}
               or (userid is null and id=#{userId})
            limit 1
            """)
    User selectById(Long userId);
}
