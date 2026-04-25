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

insert into video(id, category_id, video_name, title, cover, video_url, intro, author_name, duration_seconds, qi_value, create_time)
values
(108, 1, 'baduanjin', '八段锦·练功房跟练一', '/static/video/baduanjin-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/baduanjin/baduanjin.mp4', '适合练功房入阵后的基础跟练，节奏稳定，便于观察动作与呼吸配合。', '官方名家', 645, 1320, now()),
(109, 1, 'baduanjin1', '八段锦·练功房跟练二', '/static/video/baduanjin-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/baduanjin/baduanjin1.mp4', '适合肩颈舒缓与日常跟练，动作展示更完整。', '官方名家', 728, 1380, now()),
(110, 1, 'baduanjin2', '八段锦·练功房跟练三', '/static/video/baduanjin-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/baduanjin/baduanjin2.mp4', '适合较长时长的完整练习，便于配合法阵训练流程使用。', '官方名家', 2135, 1450, now()),
(119, 2, 'taiji1', '太极拳·练功房跟练一', '/static/video/taiji-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/taijiquan/taiji1.mp4', '适合静心调息与慢节奏入阵，便于配合动作评分逐步跟练。', '云溪道人', 388, 1010, now()),
(120, 2, 'taiji2', '太极拳·练功房跟练二', '/static/video/taiji-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/taijiquan/taiji2.mp4', '适合居家练功房完整流程使用，节奏更稳，便于配合呼吸检测。', '云溪道人', 852, 1070, now()),
(111, 3, 'wuqinxi1', '五禽戏·练功房跟练一', '/static/video/wuqinxi-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/wuqinxi/wuqinxi1.mp4', '适合初次入阵时跟练，节奏舒展，便于观察体态。', '山野隐士', 1508, 980, now()),
(112, 3, 'wuqinxi2', '五禽戏·练功房跟练二', '/static/video/wuqinxi-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/wuqinxi/wuqinxi2.mp4', '适合强身舒展，演示更清晰，便于结合姿态模型检测。', '山野隐士', 822, 1030, now()),
(113, 3, 'wuqinxi3', '五禽戏·练功房跟练三', '/static/video/wuqinxi-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/wuqinxi/wuqinxi3.mp4', '适合短时训练与节奏对齐，便于练功房快速起练。', '山野隐士', 395, 1080, now()),
(114, 3, 'wuqinxi4', '五禽戏·练功房跟练四', '/static/video/wuqinxi-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/wuqinxi/wuqinxi4.mp4', '适合较完整的练功房流程，配合呼吸与动作评分使用。', '山野隐士', 962, 1120, now()),
(115, 4, 'yijinjin1', '易筋经·练功房跟练一', '/static/video/yijinjing-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/yijinjing/yijinjin1.mp4', '适合入门跟练，强调发力与筋骨舒展的基础节奏。', '少林教习', 385, 1380, now()),
(116, 4, 'yijinjin2', '易筋经·练功房跟练二', '/static/video/yijinjing-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/yijinjing/yijinjin2.mp4', '适合配合姿态检测进行训练，动作段落更清晰。', '少林教习', 900, 1430, now()),
(117, 4, 'yijinjin3', '易筋经·练功房跟练三', '/static/video/yijinjing-cover.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/yijinjing/yijinjin3.mp4', '适合较完整的练功房流程，便于结合呼吸节奏与发力控制。', '少林教习', 765, 1480, now())
on duplicate key update
category_id = values(category_id),
video_name = values(video_name),
title = values(title),
cover = values(cover),
video_url = values(video_url),
intro = values(intro),
author_name = values(author_name),
duration_seconds = values(duration_seconds),
qi_value = values(qi_value);
