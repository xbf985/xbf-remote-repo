# Docker 部署

这套配置的目标是：尽量少准备东西，先把项目跑起来。

## 1. 先准备

- 一台安装好 Docker 和 Docker Compose 的 Linux 服务器
- 本项目代码
- 阿里云 OSS 仍然可用

建议服务器：
- Ubuntu 22.04
- 2C4G 更稳

## 2. 配环境变量

在项目根目录复制：

```bash
cp .env.example .env
```

然后修改 `.env`：

```env
APP_PORT=8080

MYSQL_DATABASE=qigong
DB_USERNAME=root
DB_PASSWORD=你自己的数据库密码

APP_STORAGE_TYPE=oss
OSS_ENDPOINT=https://oss-cn-beijing.aliyuncs.com
OSS_BUCKET=sky-tae-out
OSS_PUBLIC_BASE_URL=https://sky-tae-out.oss-cn-beijing.aliyuncs.com
OSS_ACCESS_KEY_ID=你自己的OSS AccessKey ID
OSS_ACCESS_KEY_SECRET=你自己的OSS AccessKey Secret
```

## 3. 启动

在项目根目录执行：

```bash
docker compose up -d --build
```

第一次会做三件事：
- 拉取 MySQL 镜像
- 构建 Spring Boot 镜像
- 启动 `mysql` 和 `app`

## 4. 看运行状态

```bash
docker compose ps
```

看日志：

```bash
docker compose logs -f app
docker compose logs -f mysql
```

## 5. 初始化数据库

容器第一次启动后，MySQL 里只会自动创建数据库名，不会自动执行你的业务 SQL。

你需要把这些脚本依次导入到 `qigong` 数据库：

```text
src/main/resources/db/cleanup_obsolete_schema.sql
src/main/resources/db/homepage_schema.sql
src/main/resources/db/community_schema.sql
src/main/resources/db/me_schema.sql
src/main/resources/db/training_room_schema.sql
src/main/resources/db/training_room_video_migration.sql
src/main/resources/db/me_diary_migration.sql
src/main/resources/db/video_name_migration.sql
src/main/resources/db/homepage_seed.sql
src/main/resources/db/community_seed.sql
src/main/resources/db/me_seed.sql
src/main/resources/db/training_room_seed.sql
src/main/resources/db/training_room_video_oss_update.sql
```

最简单的做法：

```bash
docker exec -i qigong-mysql mysql -uroot -p你的数据库密码 qigong < src/main/resources/db/homepage_schema.sql
```

按上面的顺序一条条执行。

如果你嫌麻烦，后面我可以再帮你补一个一键初始化脚本。

## 6. 访问

启动成功后，接口地址就是：

```text
http://服务器IP:8080
```

例如：

```text
http://服务器IP:8080/user/login
```

## 7. 常用命令

重启：

```bash
docker compose restart
```

停止：

```bash
docker compose down
```

停止并删除数据库卷：

```bash
docker compose down -v
```

重新构建：

```bash
docker compose up -d --build
```

## 8. 这套方式适不适合你

适合。

因为你现在最重要的是：
- 快速部署
- 少折腾环境
- 后面还能顺手学 Docker

这套方案已经把最烦的环境差异收起来了：
- Java 用容器
- MySQL 用容器
- OSS 继续外部使用

你后面更新代码也简单：

```bash
git pull
docker compose up -d --build
```

## 9. 建议

正式上线前建议你再做两件事：
- 给后端再套一层 Nginx
- 给 OSS AccessKey 做一次轮换
