set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'training_room_session'
          and column_name = 'practice_video_url'
    ),
    'select 1',
    'alter table training_room_session add column practice_video_url varchar(255) null after breath_issue_codes'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'training_room_session'
          and column_name = 'practice_video_cover'
    ),
    'select 1',
    'alter table training_room_session add column practice_video_cover varchar(255) null after practice_video_url'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'training_room_session'
          and column_name = 'practice_video_duration_seconds'
    ),
    'select 1',
    'alter table training_room_session add column practice_video_duration_seconds int null after practice_video_cover'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'training_record'
          and column_name = 'training_room_session_id'
    ),
    'select 1',
    'alter table training_record add column training_room_session_id bigint null after user_id'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'training_record'
          and column_name = 'practice_video_url'
    ),
    'select 1',
    'alter table training_record add column practice_video_url varchar(255) null after calories'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'training_record'
          and column_name = 'practice_video_cover'
    ),
    'select 1',
    'alter table training_record add column practice_video_cover varchar(255) null after practice_video_url'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'training_record'
          and column_name = 'practice_video_duration_seconds'
    ),
    'select 1',
    'alter table training_record add column practice_video_duration_seconds int null after practice_video_cover'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'training_record'
          and index_name = 'idx_training_record_session'
    ),
    'select 1',
    'create index idx_training_record_session on training_record(training_room_session_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;
