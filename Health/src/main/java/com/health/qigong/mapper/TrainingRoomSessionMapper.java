package com.health.qigong.mapper;

import com.health.qigong.entity.TrainingRoomSession;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TrainingRoomSessionMapper {

    @Insert("""
            insert into training_room_session(
                user_id,
                plan_id,
                video_id,
                scene_code,
                goal_code,
                master_code,
                method_code,
                enable_score,
                enable_posture_model,
                enable_breath_model,
                status,
                start_time,
                create_time
            ) values (
                #{userId},
                #{planId},
                #{videoId},
                #{sceneCode},
                #{goalCode},
                #{masterCode},
                #{methodCode},
                #{enableScore},
                #{enablePostureModel},
                #{enableBreathModel},
                #{status},
                #{startTime},
                #{createTime}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TrainingRoomSession session);

    @Select("""
            select id,
                   user_id as userId,
                   plan_id as planId,
                   video_id as videoId,
                   recipe_id as recipeId,
                   scene_code as sceneCode,
                   goal_code as goalCode,
                   master_code as masterCode,
                   method_code as methodCode,
                   enable_score as enableScore,
                   enable_posture_model as enablePostureModel,
                   enable_breath_model as enableBreathModel,
                   status,
                   duration_seconds as durationSeconds,
                   calories_burned as caloriesBurned,
                   posture_score as postureScore,
                   breath_score as breathScore,
                   total_score as totalScore,
                   posture_issue_codes as postureIssueCodes,
                   breath_issue_codes as breathIssueCodes,
                   practice_video_url as practiceVideoUrl,
                   practice_video_cover as practiceVideoCover,
                   practice_video_duration_seconds as practiceVideoDurationSeconds,
                   summary_text as summaryText,
                   advice_text as adviceText,
                   recipe_reason as recipeReason,
                   start_time as startTime,
                   end_time as endTime,
                   create_time as createTime
            from training_room_session
            where id = #{id}
            """)
    TrainingRoomSession selectById(Long id);

    @Select("""
            select id,
                   user_id as userId,
                   plan_id as planId,
                   video_id as videoId,
                   recipe_id as recipeId,
                   scene_code as sceneCode,
                   goal_code as goalCode,
                   master_code as masterCode,
                   method_code as methodCode,
                   enable_score as enableScore,
                   enable_posture_model as enablePostureModel,
                   enable_breath_model as enableBreathModel,
                   status,
                   duration_seconds as durationSeconds,
                   calories_burned as caloriesBurned,
                   posture_score as postureScore,
                   breath_score as breathScore,
                   total_score as totalScore,
                   posture_issue_codes as postureIssueCodes,
                   breath_issue_codes as breathIssueCodes,
                   practice_video_url as practiceVideoUrl,
                   practice_video_cover as practiceVideoCover,
                   practice_video_duration_seconds as practiceVideoDurationSeconds,
                   summary_text as summaryText,
                   advice_text as adviceText,
                   recipe_reason as recipeReason,
                   start_time as startTime,
                   end_time as endTime,
                   create_time as createTime
            from training_room_session
            where user_id = #{userId}
            order by start_time desc, id desc
            """)
    List<TrainingRoomSession> selectByUserId(Long userId);

    @Update("""
            update training_room_session
            set recipe_id = #{recipeId},
                status = #{status},
                duration_seconds = #{durationSeconds},
                calories_burned = #{caloriesBurned},
                posture_score = #{postureScore},
                breath_score = #{breathScore},
                total_score = #{totalScore},
                posture_issue_codes = #{postureIssueCodes},
                breath_issue_codes = #{breathIssueCodes},
                practice_video_url = #{practiceVideoUrl},
                practice_video_cover = #{practiceVideoCover},
                practice_video_duration_seconds = #{practiceVideoDurationSeconds},
                summary_text = #{summaryText},
                advice_text = #{adviceText},
                recipe_reason = #{recipeReason},
                end_time = #{endTime}
            where id = #{id}
            """)
    void updateCompletion(TrainingRoomSession session);
}
