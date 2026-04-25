insert into video(id, category_id, title, cover, video_url, intro, author_name, duration_seconds, qi_value, create_time)
values
(101, 1, '国家体育总局八段锦演示', '/static/video/baduanjin-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/1/baduanjin_1.mp4', '动作规范清晰，适合每日跟练入门。', '官方名家', 728, 1250, now()),
(105, 1, '八段锦进阶跟练', '/static/video/baduanjin-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/1/baduanjin_2.mp4', '适合已有基础的完整跟练版本。', '官方名家', 726, 1180, now()),
(102, 2, '太极拳晨修基础课', '/static/video/taiji-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/2/taijiquan_1.mp4', '节奏柔和稳当，适合清晨舒展身心。', '云溪道人', 852, 980, now()),
(107, 2, '太极拳舒缓跟练', '/static/video/taiji-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/2/taijiquan_2.mp4', '短时跟练版本，适合放松调息。', '云溪道人', 388, 860, now()),
(103, 3, '五禽戏导引跟练', '/static/video/wuqinxi-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/3/wuqinxi_1.mp4', '舒展筋骨，疏通气机，适合户外修习。', '山野隐士', 823, 860, now()),
(104, 4, '易筋经入门正练', '/static/video/yijinjing-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/4/yijinjing_1.mp4', '内外兼修，循序渐进体会筋骨发力。', '少林教习', 765, 1320, now()),
(106, 4, '易筋经进阶练习', '/static/video/yijinjing-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/4/yijinjing_2.mp4', '适合继续打磨发力与呼吸节奏。', '少林教习', 890, 1400, now())
on duplicate key update
category_id = values(category_id),
title = values(title),
cover = values(cover),
video_url = values(video_url),
intro = values(intro),
author_name = values(author_name),
duration_seconds = values(duration_seconds),
qi_value = values(qi_value);
