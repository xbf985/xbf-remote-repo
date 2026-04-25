delete from content_view_record
where content_type = 3
  and content_id in (301,302,303,304,305,306,307,308,309,310,311,312,321,322,323,324,325,326,327,328,329,330,331,332);

delete from community_comment
where post_id in (301,302,303,304,305,306,307,308,309,310,311,312,321,322,323,324,325,326,327,328,329,330,331,332);

delete from favorite
where content_type = 3
  and content_id in (301,302,303,304,305,306,307,308,309,310,311,312,321,322,323,324,325,326,327,328,329,330,331,332);

delete from likes
where content_type = 3
  and content_id in (301,302,303,304,305,306,307,308,309,310,311,312,321,322,323,324,325,326,327,328,329,330,331,332);

delete from community_post
where id in (301,302,303,304,305,306,307,308,309,310,311,312,321,322,323,324,325,326,327,328,329,330,331,332);

delete from user_follow
where user_id in (19,20,21,22,23)
   or target_user_id in (19,20,21,22,23);

delete from users
where userid in (19,20,21,22,23)
   or id in (24,25,26,27,28);

update users
set avatar = 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_official.png',
    bio = '分享正宗功法跟练、动作拆解和练功节奏，适合入门与日常陪练。',
    location = '终南书院',
    realm = '大圆满',
    verified_label = '官方认证宗门'
where id = 13;

update users
set avatar = 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_yunxi.png',
    bio = '晨修太极，夜录心得，喜欢把每次呼吸和转腕的细节记下来。',
    location = '青城山',
    realm = '元婴',
    verified_label = '清修道人'
where id = 14;

update users
set avatar = 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_shanye.png',
    bio = '山中常练五禽戏与易筋经，喜欢把安静但有用的练法分享出来。',
    location = '终南山',
    realm = '结丹',
    verified_label = '山门修士'
where id = 15;

update users
set avatar = 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_wuming.png',
    bio = '晨起吐纳，暮时静坐，愿在平常练习里看到一点真实变化。',
    location = '武当山',
    realm = '筑基期·玖',
    verified_label = '归元修者'
where id = 18;

insert into users(id, userid, username, password, phone, avatar, bio, location, realm, verified_label, create_time)
values
(24, 19, '清屿', '123456', '13800000019', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_qingyu.png', '记录适合上班族的短时练习，偏爱八段锦和站桩。', '临安', '筑基期·叁', '晨修同行', now()),
(25, 20, '沈观', '123456', '13800000020', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_shenguan.png', '练太极时最喜欢研究手眼身法步，慢慢体会稳和松。', '苏州', '筑基期·伍', '太极同修', now()),
(26, 21, '阿宁', '123456', '13800000021', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_aning.png', '练功不求猛，只求每天都能留出一点安静时间。', '杭州', '凝气境', '静心练习者', now()),
(27, 22, '晚照', '123456', '13800000022', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_wanzhao.png', '夜练易筋经，喜欢记录身体细微的发热和发力感。', '成都', '筑基期·贰', '夜修笔记', now()),
(28, 23, '玄枝', '123456', '13800000023', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/avatars/avatar_xuanzhi.png', '会把练功和日常生活串起来写，喜欢轻松一点的表达。', '南京', '凝气境', '同修手账', now());

insert into community_post(id, author_id, post_type, title, summary, cover, media_url, content, practice_tag, create_time)
values
(321, 19, 'VIDEO', '早八前练一遍八段锦，肩颈真的会慢慢松下来', '上班前跟练八分钟，脖子和肩背会先一步醒过来。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/321_official_baduanjin_morning.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/baduanjin/baduanjin.mp4', '这条视频是我最近最常放给新手的一版。节奏不会太赶，起势和收势都留了呼吸余地，适合早起没完全醒的时候跟着走一遍。办公室久坐的人练完以后，肩颈会明显松一点，整个人也更容易进入今天的状态。', '八段锦', date_sub(now(), interval 30 minute)),
(322, 23, 'POST', '宿舍也能做的五分钟静心小流程，越练越能稳住心气', '不需要铺垫太多空间，站直、调息、收心，五分钟也能练出安静感。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/322_wuming_dorm_breath.jpg', null, '我最近晚上回到宿舍以后，会先站定半分钟，把肩往下沉，目光自然垂下来，然后做三轮缓慢呼吸，再配合简短的托天和抱球动作。时间不长，但对清空脑子很有效。练完以后再去洗漱或者看书，整个人会比直接躺床上刷手机更安静。', '八段锦', date_sub(now(), interval 2 hour)),
(323, 20, 'VIDEO', '太极晨练第七天，动作慢下来以后，气息反而更稳', '以前总怕慢，后来才发现越慢越能看清自己的浮躁。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/323_yunxi_taiji_day7.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/taijiquan/taiji1.mp4', '这条是我最近晨修最顺的一次。没有刻意追求动作多漂亮，只是把呼吸和转腰尽量放在同一个节奏里。练到后半段的时候，手上的劲开始自然收住，脚底也更稳。太极拳对我来说最有用的地方，就是它会逼着人把心慢慢放下来。', '太极拳', date_sub(now(), interval 4 hour)),
(324, 20, 'POST', '今天终于把云手走顺了，原来不是越快越好', '之前总想做得流畅，后来才发现先稳住重心更重要。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/324_shenguan_cloudhands.jpg', null, '今天练到云手的时候，突然明白以前为什么总别扭。问题不是手不够圆，而是脚下和腰胯没配合好。后来故意放慢速度，把重心换得更清楚，再去带动手臂，动作反而顺了很多。很多时候不是做不到，只是太着急想要结果。', '太极拳', date_sub(now(), interval 6 hour)),
(325, 21, 'VIDEO', '午休后跟练一段五禽戏，腰背会一点点被打开', '工作日中段最适合练一小段，整个人会重新活过来。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/325_shanye_wuqinxi_lunch.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/wuqinxi/wuqinxi1.mp4', '这版五禽戏不追求强度，重点是把上身和腰背的紧绷慢慢抖开。尤其是上午坐太久以后，午休前后抽十分钟练一遍，腰背会舒服很多。动作看起来像舒展，真正难的是每一下都别抢，慢慢把气带进来。', '五禽戏', date_sub(now(), interval 8 hour)),
(326, 19, 'POST', '打工人练八段锦最有感的一招，是左右开弓这一式', '我原本以为最明显的是托天，后来发现真正能解肩背紧的是开弓。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/326_qingyu_baduanjin_bow.jpg', null, '如果只能选一式给久坐人群，我会选左右开弓。因为练的时候不仅肩背会被打开，连整个人的呼吸都会跟着拉长。我现在每次下班回家，练完整套之前都会先做两轮这一式，身体状态会明显从僵硬切回到可以练的状态。', '八段锦', date_sub(now(), interval 11 hour)),
(327, 22, 'VIDEO', '国家体育总局口令版八段锦，跟着练不容易乱节奏', '适合新手直接跟练，动作和口令都比较清楚。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/327_official_baduanjin_count.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/homepage-videos/1/baduanjin_1.mp4', '很多同修刚开始练八段锦时，容易一边看动作一边慌，最后气息和节奏全散掉。这版口令清楚、节奏稳定，比较适合直接照着走。跟练几次以后，再去琢磨每一式的发力和气口，体验会好很多。', '八段锦', date_sub(now(), interval 15 hour)),
(328, 22, 'POST', '练完易筋经以后，小腿发热是真的会出现，不是错觉', '只要发力不乱，热感通常会来得很自然。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/328_wanzhao_yijinjing_heat.jpg', null, '我以前看别人说练完会有发热感，总觉得太玄。最近连着练了几次易筋经，才发现只要姿势别太僵，呼吸也别乱顶，练到后面小腿和掌心真的会慢慢热起来。不是一下子冲上来的那种热，而是很细很稳的暖感。', '易筋经', date_sub(now(), interval 18 hour)),
(329, 20, 'VIDEO', '居家跟练太极十二分钟，心会慢下来，动作也会更稳', '晚上不想太激烈的时候，这种慢节奏跟练很舒服。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/329_yunxi_taiji_home.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/taijiquan/taiji2.mp4', '这条视频比较适合居家练习。空间不用太大，动作段落也比较平顺，从起势到收势没有很突兀的地方。很多人练太极最大的问题不是动作不会，而是心太快。这种慢下来以后，身体反而更容易跟上。', '太极拳', date_sub(now(), interval 22 hour)),
(330, 23, 'POST', '第一次把五禽戏发给室友，她居然真的跟着练起来了', '原本只是想分享一条有趣的视频，没想到她第二天又来问我。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/330_xuanzhi_roommate_wuqinxi.jpg', null, '我一直觉得五禽戏比很多人想象里更容易入门，因为动作有画面感，也不容易练得太枯燥。前两天顺手把视频发给室友，她照着练了一遍，第二天跟我说背居然没那么紧了。于是我们现在偶尔会一起练，气氛还挺好。', '五禽戏', date_sub(now(), interval 1 day)),
(331, 21, 'VIDEO', '易筋经入门别急着用蛮力，先把呼吸节奏带顺', '动作越想做“有劲”，越容易练得发僵。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/331_shanye_yijinjing_begin.jpg', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/training-room-videos/yijinjing/yijinjin1.mp4', '不少人一接触易筋经，就会下意识把动作做得很硬，觉得这样才像练功。其实入门阶段先把呼吸和动作节奏对上，比单纯发力重要得多。呼吸一稳，动作自然会更整，身体也不会越练越紧。', '易筋经', date_sub(date_sub(now(), interval 1 day), interval 4 hour)),
(332, 23, 'POST', '今天的修习记录：站桩十分钟，比昨天更能静下来一点', '不求立刻入定，只求今天比昨天更稳一点。', 'https://sky-tae-out.oss-cn-beijing.aliyuncs.com/community-seed/covers/332_wuming_standing_today.jpg', null, '今天站桩的时候，前五分钟脑子还是会乱飘，但比昨天少了很多急躁。后面慢慢把注意力放回脚下和呼吸，整个人确实能安住一点。现在越来越能接受“每天只进步一点点”这件事，本身就是一种很难得的稳定。', '八段锦', date_sub(date_sub(now(), interval 1 day), interval 7 hour));

insert into community_comment(id, post_id, user_id, content, create_time)
values
(2201, 321, 23, '这条真的适合通勤前练一遍，我前两天照着走完以后肩背舒服很多。', date_sub(now(), interval 20 minute)),
(2202, 321, 19, '口令清楚这一点太重要了，不然新手很容易一边看一边乱。', date_sub(now(), interval 16 minute)),
(2203, 323, 20, '太极真的是越慢越见功夫，看到后面那个转腰节奏很舒服。', date_sub(now(), interval 3 hour)),
(2204, 324, 20, '这段体会写得很准，很多时候不是手的问题，是脚下太飘。', date_sub(now(), interval 5 hour)),
(2205, 325, 23, '午休练这类视频真的很友好，练完回工位不会觉得累。', date_sub(now(), interval 7 hour)),
(2206, 326, 21, '左右开弓对久坐人真的很有感，我现在也会单独拎出来练。', date_sub(now(), interval 10 hour)),
(2207, 328, 21, '这个热感我最近也开始有了，特别是收式以后更明显。', date_sub(now(), interval 17 hour)),
(2208, 329, 23, '居家练这种慢节奏太极很合适，晚上练完睡前会更静。', date_sub(now(), interval 20 hour)),
(2209, 330, 22, '把功法分享给朋友一起练这件事，好像真的会更容易坚持。', date_sub(date_sub(now(), interval 1 day), interval 1 hour)),
(2210, 331, 20, '易筋经最怕一开始就抢劲，这条提醒很及时。', date_sub(date_sub(now(), interval 1 day), interval 3 hour)),
(2211, 332, 19, '看到“今天比昨天稳一点”这句，突然就不焦虑了。', date_sub(date_sub(now(), interval 1 day), interval 6 hour)),
(2212, 327, 20, '这版八段锦我收藏了，节奏很适合拿来反复跟。', date_sub(now(), interval 14 hour));

insert into likes(user_id, content_id, content_type, create_time)
values
(19, 321, 3, now()),
(20, 321, 3, now()),
(21, 321, 3, now()),
(23, 321, 3, now()),
(20, 323, 3, now()),
(22, 323, 3, now()),
(23, 323, 3, now()),
(19, 324, 3, now()),
(22, 324, 3, now()),
(21, 324, 3, now()),
(19, 325, 3, now()),
(23, 325, 3, now()),
(22, 325, 3, now()),
(20, 326, 3, now()),
(23, 326, 3, now()),
(21, 326, 3, now()),
(22, 327, 3, now()),
(20, 327, 3, now()),
(19, 327, 3, now()),
(23, 328, 3, now()),
(21, 328, 3, now()),
(20, 328, 3, now()),
(19, 329, 3, now()),
(20, 329, 3, now()),
(21, 329, 3, now()),
(22, 329, 3, now()),
(19, 330, 3, now()),
(22, 330, 3, now()),
(20, 331, 3, now()),
(22, 331, 3, now()),
(23, 331, 3, now()),
(19, 332, 3, now()),
(21, 332, 3, now()),
(22, 332, 3, now());

insert into favorite(user_id, content_id, content_type, create_time)
values
(20, 321, 3, now()),
(23, 321, 3, now()),
(21, 323, 3, now()),
(19, 323, 3, now()),
(19, 326, 3, now()),
(22, 325, 3, now()),
(20, 327, 3, now()),
(22, 329, 3, now()),
(23, 329, 3, now()),
(20, 331, 3, now()),
(23, 332, 3, now());

insert into user_follow(user_id, target_user_id, create_time)
values
(19, 20, now()),
(19, 21, now()),
(20, 19, now()),
(20, 22, now()),
(21, 20, now()),
(21, 23, now()),
(22, 19, now()),
(22, 21, now()),
(23, 19, now()),
(23, 20, now()),
(23, 22, now()),
(20, 21, now());
