set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'users'
          and column_name = 'bio'
    ),
    'select 1',
    'alter table users add column bio varchar(255) null'
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
          and column_name = 'location'
    ),
    'select 1',
    'alter table users add column location varchar(100) null'
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
          and column_name = 'realm'
    ),
    'select 1',
    'alter table users add column realm varchar(50) null'
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
          and column_name = 'verified_label'
    ),
    'select 1',
    'alter table users add column verified_label varchar(100) null'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

create table if not exists community_post (
    id bigint primary key auto_increment,
    author_id bigint not null,
    post_type varchar(20) not null,
    title varchar(120) not null,
    summary varchar(255),
    cover varchar(255),
    media_url varchar(255),
    content text,
    practice_tag varchar(50),
    create_time datetime default current_timestamp
);

create table if not exists community_comment (
    id bigint primary key auto_increment,
    post_id bigint not null,
    user_id bigint not null,
    content varchar(500) not null,
    create_time datetime default current_timestamp
);

create table if not exists user_follow (
    id bigint primary key auto_increment,
    user_id bigint not null,
    target_user_id bigint not null,
    create_time datetime default current_timestamp
);

set @sql = if(
    exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'user_follow'
          and column_name = 'follow_user_id'
    ) and not exists(
        select 1
        from information_schema.columns
        where table_schema = database()
          and table_name = 'user_follow'
          and column_name = 'target_user_id'
    ),
    'alter table user_follow change column follow_user_id target_user_id bigint not null',
    'select 1'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'community_post'
          and index_name = 'idx_community_post_author'
    ),
    'select 1',
    'create index idx_community_post_author on community_post(author_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'community_post'
          and index_name = 'idx_community_post_tag'
    ),
    'select 1',
    'create index idx_community_post_tag on community_post(practice_tag)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'community_comment'
          and index_name = 'idx_community_comment_post'
    ),
    'select 1',
    'create index idx_community_comment_post on community_comment(post_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'user_follow'
          and index_name = 'idx_user_follow_user'
    ),
    'select 1',
    'create index idx_user_follow_user on user_follow(user_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'user_follow'
          and index_name = 'idx_user_follow_target'
    ),
    'select 1',
    'create index idx_user_follow_target on user_follow(target_user_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;
