create table if not exists video (
    id bigint primary key auto_increment,
    category_id bigint not null,
    title varchar(100) not null,
    cover varchar(255),
    video_url varchar(255) not null,
    intro varchar(255),
    author_name varchar(50),
    duration_seconds bigint default 0,
    qi_value bigint default 0,
    create_time datetime default current_timestamp
);

create table if not exists video_comment (
    id bigint primary key auto_increment,
    video_id bigint not null,
    user_id bigint not null,
    content varchar(500) not null,
    create_time datetime default current_timestamp
);

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'video'
          and index_name = 'idx_video_category'
    ),
    'select 1',
    'create index idx_video_category on video(category_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

set @sql = if(
    exists(
        select 1
        from information_schema.statistics
        where table_schema = database()
          and table_name = 'video_comment'
          and index_name = 'idx_video_comment_video'
    ),
    'select 1',
    'create index idx_video_comment_video on video_comment(video_id)'
);
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;
