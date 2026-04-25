-- Remove obsolete homepage-unrelated tables and legacy columns.
-- Safe to run multiple times.

drop table if exists article;
drop table if exists music;
drop table if exists browse_history;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'users'
          and column_name = 'openid'
    ),
    'alter table users drop column openid',
    'select 1'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'users'
          and column_name = 'avater'
    ) and not exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'users'
          and column_name = 'avatar'
    ),
    'alter table users change column avater avatar varchar(255)',
    'select 1'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'recipe'
          and column_name = 'collect_count'
    ),
    'alter table recipe drop column collect_count',
    'select 1'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;
