package com.health.qigong.mapper;

import com.health.qigong.entity.UserSettings;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserSettingsMapper {

    @Select("""
            select id,
                   user_id as userId,
                   daily_calorie_goal as dailyCalorieGoal,
                   create_time as createTime,
                   update_time as updateTime
            from user_settings
            where user_id = #{userId}
            limit 1
            """)
    UserSettings selectByUserId(Long userId);

    @Insert("""
            insert into user_settings(user_id, daily_calorie_goal, create_time, update_time)
            values(#{userId}, #{dailyCalorieGoal}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserSettings settings);

    @Update("""
            update user_settings
            set daily_calorie_goal = #{dailyCalorieGoal},
                update_time = #{updateTime}
            where user_id = #{userId}
            """)
    void updateDailyCalorieGoal(@Param("userId") Long userId,
                                @Param("dailyCalorieGoal") Integer dailyCalorieGoal,
                                @Param("updateTime") java.time.LocalDateTime updateTime);
}
