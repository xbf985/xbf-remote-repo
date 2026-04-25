create table if not exists training_room_plan (
    id bigint primary key auto_increment,
    title varchar(120) not null,
    scene_code varchar(50) not null,
    goal_code varchar(50) not null,
    master_code varchar(50) not null,
    method_code varchar(50) not null,
    video_id bigint not null,
    intro varchar(255),
    mantra varchar(100),
    estimated_duration_seconds int default 600,
    estimated_calories int default 120,
    support_posture_model tinyint(1) default 1,
    support_breath_model tinyint(1) default 1,
    priority int default 0,
    create_time datetime default current_timestamp
);

create table if not exists training_advice_template (
    id bigint primary key auto_increment,
    dimension varchar(20) not null,
    issue_code varchar(50),
    goal_code varchar(50),
    method_code varchar(50),
    min_score int,
    max_score int,
    advice_text varchar(500) not null,
    priority int default 0
);

create table if not exists training_recipe_rule (
    id bigint primary key auto_increment,
    recipe_id bigint not null,
    goal_code varchar(50),
    method_code varchar(50),
    issue_code varchar(50),
    season_category bigint,
    min_score int,
    max_score int,
    priority int default 0
);

create table if not exists training_room_session (
    id bigint primary key auto_increment,
    user_id bigint not null,
    plan_id bigint not null,
    video_id bigint not null,
    recipe_id bigint,
    scene_code varchar(50) not null,
    goal_code varchar(50) not null,
    master_code varchar(50) not null,
    method_code varchar(50) not null,
    enable_score tinyint(1) default 1,
    enable_posture_model tinyint(1) default 0,
    enable_breath_model tinyint(1) default 0,
    status varchar(20) default 'ONGOING',
    duration_seconds int,
    calories_burned int,
    posture_score int,
    breath_score int,
    total_score int,
    posture_issue_codes varchar(255),
    breath_issue_codes varchar(255),
    practice_video_url varchar(255),
    practice_video_cover varchar(255),
    practice_video_duration_seconds int,
    summary_text varchar(500),
    advice_text text,
    recipe_reason varchar(500),
    start_time datetime default current_timestamp,
    end_time datetime,
    create_time datetime default current_timestamp
);

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'training_room_plan'
          and index_name = 'idx_training_room_plan_method'
    ),
    'select 1',
    'create index idx_training_room_plan_method on training_room_plan(method_code)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'training_room_plan'
          and index_name = 'idx_training_room_plan_goal'
    ),
    'select 1',
    'create index idx_training_room_plan_goal on training_room_plan(goal_code)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'training_room_session'
          and index_name = 'idx_training_room_session_user'
    ),
    'select 1',
    'create index idx_training_room_session_user on training_room_session(user_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'training_room_session'
          and index_name = 'idx_training_room_session_status'
    ),
    'select 1',
    'create index idx_training_room_session_status on training_room_session(status)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'training_recipe_rule'
          and index_name = 'idx_training_recipe_rule_goal'
    ),
    'select 1',
    'create index idx_training_recipe_rule_goal on training_recipe_rule(goal_code)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;
