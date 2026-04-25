package com.health.qigong.mapper;

import com.health.qigong.entity.TrainingRoomPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrainingRoomPlanMapper {

    @Select("""
            select id,
                   title,
                   scene_code as sceneCode,
                   goal_code as goalCode,
                   master_code as masterCode,
                   method_code as methodCode,
                   video_id as videoId,
                   intro,
                   mantra,
                   estimated_duration_seconds as estimatedDurationSeconds,
                   estimated_calories as estimatedCalories,
                   support_posture_model as supportPostureModel,
                   support_breath_model as supportBreathModel,
                   priority,
                   create_time as createTime
            from training_room_plan
            order by priority desc, id asc
            """)
    List<TrainingRoomPlan> listAll();

    @Select("""
            select id,
                   title,
                   scene_code as sceneCode,
                   goal_code as goalCode,
                   master_code as masterCode,
                   method_code as methodCode,
                   video_id as videoId,
                   intro,
                   mantra,
                   estimated_duration_seconds as estimatedDurationSeconds,
                   estimated_calories as estimatedCalories,
                   support_posture_model as supportPostureModel,
                   support_breath_model as supportBreathModel,
                   priority,
                   create_time as createTime
            from training_room_plan
            where id = #{id}
            """)
    TrainingRoomPlan selectById(Long id);
}
