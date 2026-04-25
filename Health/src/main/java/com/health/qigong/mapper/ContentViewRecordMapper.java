package com.health.qigong.mapper;

import com.health.qigong.entity.ContentViewRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContentViewRecordMapper {

    @Insert("""
            insert into content_view_record(user_id, content_id, content_type, view_time, duration)
            values(#{userId}, #{contentId}, #{contentType}, #{viewTime}, #{duration})
            on duplicate key update
                content_type = values(content_type),
                view_time = values(view_time),
                duration = values(duration)
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ContentViewRecord record);

    @Select("""
            select id,
                   user_id as userId,
                   content_id as contentId,
                   content_type as contentType,
                   view_time as viewTime,
                   duration
            from content_view_record
            where user_id = #{userId}
            order by view_time desc, id desc
            """)
    List<ContentViewRecord> selectByUserId(Long userId);
}
