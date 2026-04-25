package com.health.qigong.mapper;

import com.health.qigong.entity.Checkin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CheckinMapper {

    @Select("select count(*) from checkin where user_id=#{userId}")
    Integer countDaysByUserId(Long userId);

    @Select("""
            select id,
                   user_id as userId,
                   check_date as checkDate,
                   create_time as createTime
            from checkin
            where user_id=#{userId} and check_date = #{today}
            """)
    Checkin selectByUserAndDate(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Insert("insert into checkin (user_id,check_date,create_time)values(#{userId},#{checkDate},#{createTime})")
    void insert(Checkin checkin);

    @Select("""
            select id,
                   user_id as userId,
                   check_date as checkDate,
                   create_time as createTime
            from checkin
            where user_id=#{userId}
            order by check_date desc
            """)
    List<Checkin> selectByUserId(Long userId);

    @Select("""
            select id,
                   user_id as userId,
                   check_date as checkDate,
                   create_time as createTime
            from checkin
            where user_id=#{userId} and check_date between #{startDate} and #{endDate}
            order by check_date asc
            """)
    List<Checkin> selectByUserAndDateRange(@Param("userId") Long userId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}
