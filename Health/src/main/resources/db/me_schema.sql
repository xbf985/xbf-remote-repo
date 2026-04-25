create table if not exists user_settings (
    id bigint primary key auto_increment,
    user_id bigint not null,
    daily_calorie_goal int not null default 1200,
    create_time datetime default current_timestamp,
    update_time datetime default current_timestamp on update current_timestamp,
    unique key uk_user_settings_user_id (user_id)
);

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'user_settings'
          and index_name = 'idx_user_settings_user'
    ),
    'select 1',
    'create index idx_user_settings_user on user_settings(user_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;
