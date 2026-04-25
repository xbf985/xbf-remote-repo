delete from video_comment where video_id in (101, 102, 103, 104, 105, 106, 107);
delete from likes where content_type = 1 and content_id in (101, 102, 103, 104, 105, 106, 107);
delete from favorite where (content_type = 1 and content_id in (101, 102, 103, 104, 105, 106, 107))
   or (content_type = 2 and content_id in (201, 202, 203, 204, 207, 208, 209, 210));
delete from training_record where user_id = 1;
delete from checkin where user_id = 1;
delete from video where id in (101, 102, 103, 104, 105, 106, 107);
delete from recipe where id in (201, 202, 203, 204, 207, 208, 209, 210);
delete from users where userid = 1;

insert into users(userid, username, password, phone, avatar, create_time)
values (1, '清风道长', '123456', '13800000000', '/static/avatar/qingfeng.png', now());

update users
set bio = '晨起调息，夜半静修，记录每日修习与养生所得。',
    location = '武当山',
    realm = '筑基后期',
    verified_label = '归元修者'
where userid = 1;

insert into recipe(id, name, cover, category_id, effect, ingredients, steps, create_time)
values
(201, '玫瑰花疏肝茶', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/201_rose_tea.jpg', 1, '疏肝解郁,理气和中', '平阴玫瑰5g，新会陈皮3g，新疆红枣2颗', '先将陈皮用清水稍微冲洗后切成细丝，红枣去核备用，再把玫瑰花一同放入养生壶或带盖杯中。随后冲入约八百毫升沸水，加盖焖泡五到八分钟，让花香与果香慢慢释出；若口感偏淡可再静置片刻，待茶汤颜色微透后即可温热饮用。', now()),
(202, '荠菜猪肝明目汤', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/202_jicai_liver_soup.jpg', 1, '清热平肝,养血明目', '荠菜200g，鲜猪肝150g，姜丝适量', '先将荠菜择洗干净后焯水备用，猪肝切成薄片后用清水反复漂洗，再加入少许料酒和姜丝轻轻抓匀去腥。锅中加适量清水煮开后先下姜丝，再放入猪肝煮至变色，随后加入荠菜继续小火煮几分钟，最后少量调味即可出锅，汤色清亮时食用口感最佳。', now()),
(203, '百合莲子羹', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/203_lily_lotus_soup.jpg', 2, '宁心安神,润肺养阴', '百合20g，莲子20g，银耳半朵，冰糖适量', '银耳提前泡发后撕成小朵，莲子去芯洗净，百合稍微浸泡备用。将银耳、莲子先入锅加水慢炖，待汤底渐渐浓润后再放入百合继续煮二十分钟左右，最后根据口味加入少许冰糖化开，煮至羹体柔滑、入口清润时即可盛出。', now()),
(204, '山药薏米粥', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/204_shanyao_yimi_porridge.jpg', 3, '健脾益气,和胃养元', '山药100g，薏米30g，大米50g', '薏米提前浸泡一小时左右，大米淘洗干净，山药去皮后切成小丁备用。先将薏米与大米一同下锅熬煮，待米粒逐渐开花后加入山药继续小火慢熬十多分钟，期间适当搅拌防止粘底，煮至粥体绵软、山药熟透后即可食用，口感清淡又养胃。', now()),
(207, '银耳雪梨羹', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/207_snow_pear_soup.jpg', 5, '润肺生津,清燥和中', '雪梨1个，银耳半朵，百合15g，冰糖适量', '银耳提前泡发后撕成小朵，雪梨去皮去核切块，百合简单冲洗备用。将银耳先入锅煮至汤汁微稠，再放入雪梨和百合继续炖煮二十到三十分钟，待梨肉变软、汤水清甜时加入适量冰糖，稍煮片刻后即可关火，温热食用更能润燥生津。', now()),
(208, '山药百合润秋汤', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/208_runqiu_soup.jpg', 6, '润肺养阴,健脾和胃', '山药100g，百合20g，莲子15g，枸杞少许', '山药去皮后切段，百合和莲子清洗干净后稍作浸泡，准备少许枸杞最后点缀。锅中加清水后先下莲子煮一会儿，再放入山药和百合同煮约二十多分钟，待食材熟软、汤味渐浓时加入枸杞略煮两分钟即可，整道汤品清润平和，适合秋日调养。', now()),
(209, '黄芪羊肉暖身汤', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/209_yangrou_soup.jpg', 7, '温阳补虚,散寒益气', '羊肉250g，黄芪15g，生姜3片，胡椒少许', '羊肉切块后先冷水下锅焯去血沫，再捞出冲净备用，黄芪和生姜简单清洗即可。将羊肉、黄芪、生姜一同放入砂锅，加足量清水后先大火煮开，再转小火慢炖一小时左右，待肉质酥软、汤味渐浓时加入少许胡椒和盐调味，热饮更有暖身之感。', now()),
(210, '桂圆红枣养元饮', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/recipe-covers/210_guiyuan_hongzao_drink.jpg', 8, '温补气血,养心安神', '桂圆10枚，红枣6枚，枸杞10g，红糖适量', '桂圆去壳取肉，红枣洗净后去核，枸杞稍微冲洗备用。锅中加入适量清水，先放入桂圆和红枣煮十五分钟左右，让甜香慢慢释出，随后加入枸杞再煮两三分钟，最后依口味加入少许红糖调匀，待汤色红润、口感温和时即可饮用。', now());

insert into video(id, category_id, title, cover, video_url, intro, author_name, duration_seconds, qi_value, create_time)
values
(101, 1, '国家体育总局八段锦演示', '/static/video/baduanjin-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/1/baduanjin_1.mp4', '动作规范清晰，适合每日跟练入门。', '官方名家', 728, 1250, now()),
(105, 1, '八段锦进阶跟练', '/static/video/baduanjin-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/1/baduanjin_2.mp4', '适合已有基础的完整跟练版本。', '官方名家', 726, 1180, now()),
(102, 2, '太极拳晨修基础课', '/static/video/taiji-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/2/taijiquan_1.mp4', '节奏柔和稳当，适合清晨舒展身心。', '云溪道人', 852, 980, now()),
(107, 2, '太极拳舒缓跟练', '/static/video/taiji-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/2/taijiquan_2.mp4', '短时跟练版本，适合放松调息。', '云溪道人', 388, 860, now()),
(103, 3, '五禽戏导引跟练', '/static/video/wuqinxi-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/3/wuqinxi_1.mp4', '舒展筋骨，疏通气机，适合户外修习。', '山野隐士', 823, 860, now()),
(104, 4, '易筋经入门正练', '/static/video/yijinjing-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/4/yijinjing_1.mp4', '内外兼修，循序渐进体会筋骨发力。', '少林教习', 765, 1320, now()),
(106, 4, '易筋经进阶练习', '/static/video/yijinjing-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/4/yijinjing_2.mp4', '适合继续打磨发力与呼吸节奏。', '少林教习', 890, 1400, now());

insert into training_record(user_id, training_name, duration, calories, create_time)
values
(1, '八段锦', 30, 150, now()),
(1, '太极拳', 40, 220, now()),
(1, '八段锦', 35, 180, date_sub(now(), interval 1 day)),
(1, '五禽戏', 25, 120, date_sub(now(), interval 2 day));

insert into checkin(user_id, check_date, create_time)
values
(1, curdate(), now()),
(1, date_sub(curdate(), interval 1 day), date_sub(now(), interval 1 day)),
(1, date_sub(curdate(), interval 2 day), date_sub(now(), interval 2 day)),
(1, date_sub(curdate(), interval 4 day), date_sub(now(), interval 4 day)),
(1, date_sub(curdate(), interval 6 day), date_sub(now(), interval 6 day));

insert into favorite(user_id, content_id, content_type, create_time)
values
(1, 101, 1, now()),
(1, 201, 2, now());

insert into likes(user_id, content_id, content_type, create_time)
values
(1, 101, 1, now());

insert into video_comment(id, video_id, user_id, content, create_time)
values
(1001, 101, 1, '节奏很稳，早晨跟练特别顺。', now()),
(1002, 101, 1, '练完肩颈轻了不少，适合久坐之后来一遍。', date_sub(now(), interval 10 minute));
