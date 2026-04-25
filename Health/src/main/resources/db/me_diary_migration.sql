set @add_content_type = (
    select if(
        exists(
            select 1
            from information_schema.columns
            where table_schema = database()
              and table_name = 'content_view_record'
              and column_name = 'content_type'
        ),
        'select 1',
        'alter table content_view_record add column content_type int null after content_id'
    )
);
prepare stmt from @add_content_type;
execute stmt;
deallocate prepare stmt;

set @idx_user_time = (
    select if(
        exists(
            select 1
            from information_schema.statistics
            where table_schema = database()
              and table_name = 'content_view_record'
              and index_name = 'idx_content_view_record_user_time'
        ),
        'select 1',
        'create index idx_content_view_record_user_time on content_view_record(user_id, view_time)'
    )
);
prepare stmt from @idx_user_time;
execute stmt;
deallocate prepare stmt;
