delete from user_settings where user_id = 1;
delete from content_view_record where user_id = 1;

insert into user_settings(user_id, daily_calorie_goal, create_time, update_time)
values (1, 1200, now(), now());

update users
set username = '无名居士',
    avatar = '/static/avatar/self.png',
    bio = '晨起吐纳，暮时静坐，愿于平常中见功夫。',
    realm = '筑基期·玖'
where userid = 1;

insert into content_view_record(user_id, content_id, content_type, view_time, duration)
values
(1, 101, 1, date_sub(now(), interval 2 hour), 0),
(1, 201, 2, date_sub(now(), interval 5 hour), 0),
(1, 301, 3, date_sub(now(), interval 1 day), 0),
(1, 304, 3, date_sub(now(), interval 2 day), 0);
