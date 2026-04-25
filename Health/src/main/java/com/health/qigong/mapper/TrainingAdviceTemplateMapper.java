package com.health.qigong.mapper;

import com.health.qigong.entity.TrainingAdviceTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrainingAdviceTemplateMapper {

    @Select("""
            select id,
                   dimension,
                   issue_code as issueCode,
                   goal_code as goalCode,
                   method_code as methodCode,
                   min_score as minScore,
                   max_score as maxScore,
                   advice_text as adviceText,
                   priority
            from training_advice_template
            order by priority desc, id asc
            """)
    List<TrainingAdviceTemplate> listAll();
}
