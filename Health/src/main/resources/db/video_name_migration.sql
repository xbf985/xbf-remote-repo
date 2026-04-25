set @add_video_name_column = (
    select if(
        exists(
            select 1
            from information_schema.columns
            where table_schema = database()
              and table_name = 'video'
              and column_name = 'video_name'
        ),
        'select 1',
        'alter table video add column video_name varchar(100) null after category_id'
    )
);
prepare stmt from @add_video_name_column;
execute stmt;
deallocate prepare stmt;
