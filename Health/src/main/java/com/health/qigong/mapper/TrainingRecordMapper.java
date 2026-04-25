package com.health.qigong.mapper;

import com.health.qigong.entity.TrainingRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TrainingRecordMapper {

    @Select("select ifnull(sum(duration), 0) from training_record where user_id=#{userId}")
    Integer sumDurationByUserId(Long userId);

    @Insert("""
            insert into training_record(
                user_id,
                training_room_session_id,
                training_name,
                duration,
                calories,
                practice_video_url,
                practice_video_cover,
                practice_video_duration_seconds,
                create_time
            ) values(
                #{userId},
                #{trainingRoomSessionId},
                #{trainingName},
                #{duration},
                #{calories},
                #{practiceVideoUrl},
                #{practiceVideoCover},
                #{practiceVideoDurationSeconds},
                #{createTime}
            )
            """)
    void insert(TrainingRecord record);

    @Select("""
            select id,
                   user_id as userId,
                   training_room_session_id as trainingRoomSessionId,
                   training_name as trainingName,
                   duration,
                   calories,
                   practice_video_url as practiceVideoUrl,
                   practice_video_cover as practiceVideoCover,
                   practice_video_duration_seconds as practiceVideoDurationSeconds,
                   create_time as createTime
            from training_record
            where user_id=#{userId}
            order by create_time desc
            """)
    List<TrainingRecord> selectByUserId(long userId);

    @Select("select ifnull(sum(calories), 0) from training_record where user_id=#{userId} and date(create_time)=#{day}")
    Integer sumTodayCaloriesByUserId(@Param("userId") Long userId, @Param("day") LocalDate day);
}
