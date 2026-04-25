delete from training_room_session where id in (9001);
delete from training_record where training_room_session_id in (9001);
delete from training_recipe_rule where id between 8101 and 8110;
delete from training_advice_template where id between 7101 and 7130;
delete from training_room_plan where id between 501 and 510;
delete from recipe where id in (205, 206);

insert into recipe(id, name, cover, category_id, effect, ingredients, steps, create_time)
values
(205, '当归黄芪炖乌鸡', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/205_danggui_huangqi_chicken.jpg', 2, '益气补血,固本培元', '乌鸡半只，黄芪15g，当归10g，红枣6枚，姜片适量', '乌鸡处理干净后先焯水去浮沫，再与黄芪、当归、红枣和姜片一同放入砂锅中，加入足量清水。先用大火煮开，再转小火慢炖约一个半小时，让药材和鸡肉的香味充分融合，最后按个人口味少量加盐调和，汤色浓润时饮用更适合训练后调补。', now()),
(206, '百合陈皮静心饮', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/206_baihe_chenpi_tea.jpg', 4, '宁心调息,润肺和中', '百合20g，陈皮5g，莲子15g，冰糖适量', '百合、莲子先用清水略微浸泡，陈皮洗净后切成细丝备用。锅中加入适量清水，先放入莲子煮至微微软化，再加入百合和陈皮一起小火煮二十分钟左右，待香气慢慢散出后加入少许冰糖调味，煮到茶饮清润回甘时即可温热饮用。', now());

insert into training_room_plan(
    id, title, scene_code, goal_code, master_code, method_code, video_id, intro, mantra,
    estimated_duration_seconds, estimated_calories, support_posture_model, support_breath_model, priority, create_time
)
values
(501, '案牍舒颈八段锦·官方定式', 'office', 'relaxNeck', 'official', 'baduanjin', 101, '适合久坐案前之人，动作清晰，容易跟练。', '同修共鸣', 720, 135, 1, 1, 90, now()),
(502, '居家静心太极拳·归真调息', 'home', 'calmMind', 'selfCultivation', 'taijiquan', 102, '节奏柔和，适合居家安神与收束杂念。', '返璞归真', 900, 160, 1, 1, 86, now()),
(503, '露天强身五禽戏·宗师导引', 'outdoor', 'fitness', 'taijiMaster', 'wuqinxi', 103, '户外舒展筋骨，兼顾体能与气机流转。', '山风入息', 840, 170, 1, 1, 88, now()),
(504, '学堂通脉易筋经·正宗入门', 'classroom', 'qiFlow', 'official', 'yijinjing', 104, '适合集体修习，循序渐进体会筋骨与经脉发力。', '筋骨同鸣', 960, 180, 1, 1, 92, now()),
(505, '居家柔和八段锦·舒肩松颈', 'home', 'relaxNeck', 'taijiMaster', 'baduanjin', 101, '重点舒缓肩颈，适合晚间或工作后修习。', '缓息沉肩', 680, 128, 1, 1, 80, now()),
(506, '天光理气五禽戏·自在养元', 'outdoor', 'qiFlow', 'selfCultivation', 'wuqinxi', 103, '借户外开阔之势打开胸廓，调匀呼吸。', '气行周身', 780, 150, 1, 1, 78, now());

insert into training_advice_template(id, dimension, issue_code, goal_code, method_code, min_score, max_score, advice_text, priority)
values
(7101, 'OVERALL', null, null, null, 90, 100, '本次修习节奏稳定，动作与呼吸配合较为圆融。', 100),
(7102, 'OVERALL', null, null, null, 80, 89, '本次整体表现平稳，继续保持专注与从容即可更进一步。', 90),
(7103, 'OVERALL', null, null, null, 0, 79, '本次修习已完成，下次可更注意起承转合的连贯与放松。', 80),
(7111, 'POSTURE', 'NECK_TENSE', 'relaxNeck', null, 0, 100, '肩颈仍稍有提紧，开势前可先沉肩坠肘，再带动双臂展开。', 100),
(7112, 'POSTURE', 'CENTER_UNSTABLE', null, null, 0, 100, '重心转换稍快，可先稳住下盘，再衔接下一式。', 96),
(7113, 'POSTURE', 'ARM_NOT_EXTENDED', null, 'baduanjin', 0, 100, '展臂略短，意念可再向外送一点，动作会更舒展。', 94),
(7114, 'POSTURE', null, null, null, 85, 100, '姿态整体舒展平衡，继续保持松而不散的架势。', 70),
(7121, 'BREATH', 'BREATH_SHALLOW', 'qiFlow', null, 0, 100, '呼吸稍浅，收式前可让吸气再沉一分，意守丹田。', 100),
(7122, 'BREATH', 'RHYTHM_UNEVEN', null, null, 0, 100, '呼吸节奏有些起伏，试着让吐纳更柔和地贴合动作。', 96),
(7123, 'BREATH', 'EXHALE_HURRIED', null, null, 0, 100, '呼气略急，下落或收势阶段可再放慢半拍。', 94),
(7124, 'BREATH', null, null, null, 85, 100, '吐纳节奏较稳，与动作配合自然。', 70);

insert into training_recipe_rule(id, recipe_id, goal_code, method_code, issue_code, season_category, min_score, max_score, priority)
values
(8101, 205, 'fitness', null, null, null, 0, 100, 95),
(8102, 205, 'relaxNeck', 'baduanjin', 'NECK_TENSE', 1, 0, 100, 110),
(8103, 201, 'qiFlow', null, 'BREATH_SHALLOW', 1, 0, 100, 108),
(8104, 206, 'calmMind', null, 'RHYTHM_UNEVEN', null, 0, 100, 108),
(8105, 203, 'calmMind', null, null, null, 0, 100, 100),
(8106, 204, 'fitness', null, 'CENTER_UNSTABLE', null, 0, 100, 102),
(8107, 202, 'relaxNeck', null, null, null, 0, 100, 92),
(8108, 201, 'qiFlow', 'wuqinxi', null, null, 0, 100, 96);

insert into training_room_session(
    id, user_id, plan_id, video_id, recipe_id, scene_code, goal_code, master_code, method_code,
    enable_score, enable_posture_model, enable_breath_model, status, duration_seconds, calories_burned,
    posture_score, breath_score, total_score, posture_issue_codes, breath_issue_codes,
    practice_video_url, practice_video_cover, practice_video_duration_seconds, summary_text,
    advice_text, recipe_reason, start_time, end_time, create_time
)
values
(9001, 1, 501, 101, 205, 'office', 'relaxNeck', 'official', 'baduanjin',
 1, 1, 1, 'COMPLETED', 760, 148, 92, 94, 93, 'NECK_TENSE', 'BREATH_SHALLOW',
 '/uploads/training-room/demo/practice-9001.mp4', '/static/video/baduanjin-cover.jpg', 760,
 '本次八段锦修习与舒缓肩颈的目标较为契合，整体完成度不错。',
 '本次修习节奏稳定，动作与呼吸配合较为圆融。肩颈仍稍有提紧，呼吸也略浅，收势时可再沉肩延息。',
 '根据你本次舒缓肩颈的诉求，以及训练中出现的肩颈紧张与呼吸偏浅反馈，特为你荐此一膳。',
 date_sub(now(), interval 1 day), date_sub(now(), interval 1 day) + interval 13 minute, date_sub(now(), interval 1 day));

insert into training_record(
    user_id, training_room_session_id, training_name, duration, calories,
    practice_video_url, practice_video_cover, practice_video_duration_seconds, create_time
)
values
(1, 9001, '八段锦·舒缓肩颈', 13, 148,
 '/uploads/training-room/demo/practice-9001.mp4', '/static/video/baduanjin-cover.jpg', 760, date_sub(now(), interval 1 day) + interval 13 minute);
