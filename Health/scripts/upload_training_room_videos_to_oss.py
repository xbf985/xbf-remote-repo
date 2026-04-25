import argparse
import os
import subprocess
from pathlib import Path

import oss2
import pymysql


VIDEO_DEFINITIONS = [
    {
        "id": 108,
        "category_id": 1,
        "method_code": "baduanjin",
        "video_name": "baduanjin",
        "title": "八段锦·练功房跟练一",
        "cover": "/static/video/baduanjin-cover.jpg",
        "intro": "适合练功房入阵后的基础跟练，节奏稳定，便于观察动作与呼吸配合。",
        "author_name": "官方名家",
        "qi_value": 1320,
    },
    {
        "id": 109,
        "category_id": 1,
        "method_code": "baduanjin",
        "video_name": "baduanjin1",
        "title": "八段锦·练功房跟练二",
        "cover": "/static/video/baduanjin-cover.jpg",
        "intro": "适合肩颈舒缓与日常跟练，动作展示更完整。",
        "author_name": "官方名家",
        "qi_value": 1380,
    },
    {
        "id": 110,
        "category_id": 1,
        "method_code": "baduanjin",
        "video_name": "baduanjin2",
        "title": "八段锦·练功房跟练三",
        "cover": "/static/video/baduanjin-cover.jpg",
        "intro": "适合较长时长的完整练习，便于配合法阵训练流程使用。",
        "author_name": "官方名家",
        "qi_value": 1450,
    },
    {
        "id": 119,
        "category_id": 2,
        "method_code": "taijiquan",
        "video_name": "taiji1",
        "title": "太极拳·练功房跟练一",
        "cover": "/static/video/taiji-cover.jpg",
        "intro": "适合静心调息与慢节奏入阵，便于配合动作评分逐步跟练。",
        "author_name": "云溪道人",
        "qi_value": 1010,
    },
    {
        "id": 120,
        "category_id": 2,
        "method_code": "taijiquan",
        "video_name": "taiji2",
        "title": "太极拳·练功房跟练二",
        "cover": "/static/video/taiji-cover.jpg",
        "intro": "适合居家练功房完整流程使用，节奏更稳，便于配合呼吸检测。",
        "author_name": "云溪道人",
        "qi_value": 1070,
    },
    {
        "id": 111,
        "category_id": 3,
        "method_code": "wuqinxi",
        "video_name": "wuqinxi1",
        "title": "五禽戏·练功房跟练一",
        "cover": "/static/video/wuqinxi-cover.jpg",
        "intro": "适合初次入阵时跟练，节奏舒展，便于观察体态。",
        "author_name": "山野隐士",
        "qi_value": 980,
    },
    {
        "id": 112,
        "category_id": 3,
        "method_code": "wuqinxi",
        "video_name": "wuqinxi2",
        "title": "五禽戏·练功房跟练二",
        "cover": "/static/video/wuqinxi-cover.jpg",
        "intro": "适合强身舒展，演示更清晰，便于结合姿态模型检测。",
        "author_name": "山野隐士",
        "qi_value": 1030,
    },
    {
        "id": 113,
        "category_id": 3,
        "method_code": "wuqinxi",
        "video_name": "wuqinxi3",
        "title": "五禽戏·练功房跟练三",
        "cover": "/static/video/wuqinxi-cover.jpg",
        "intro": "适合短时训练与节奏对齐，便于练功房快速起练。",
        "author_name": "山野隐士",
        "qi_value": 1080,
    },
    {
        "id": 114,
        "category_id": 3,
        "method_code": "wuqinxi",
        "video_name": "wuqinxi4",
        "title": "五禽戏·练功房跟练四",
        "cover": "/static/video/wuqinxi-cover.jpg",
        "intro": "适合较完整的练功房流程，配合呼吸与动作评分使用。",
        "author_name": "山野隐士",
        "qi_value": 1120,
    },
    {
        "id": 115,
        "category_id": 4,
        "method_code": "yijinjing",
        "video_name": "yijinjin1",
        "title": "易筋经·练功房跟练一",
        "cover": "/static/video/yijinjing-cover.jpg",
        "intro": "适合入门跟练，强调发力与筋骨舒展的基础节奏。",
        "author_name": "少林教习",
        "qi_value": 1380,
    },
    {
        "id": 116,
        "category_id": 4,
        "method_code": "yijinjing",
        "video_name": "yijinjin2",
        "title": "易筋经·练功房跟练二",
        "cover": "/static/video/yijinjing-cover.jpg",
        "intro": "适合配合姿态检测进行训练，动作段落更清晰。",
        "author_name": "少林教习",
        "qi_value": 1430,
    },
    {
        "id": 117,
        "category_id": 4,
        "method_code": "yijinjing",
        "video_name": "yijinjin3",
        "title": "易筋经·练功房跟练三",
        "cover": "/static/video/yijinjing-cover.jpg",
        "intro": "适合较完整的练功房流程，便于结合呼吸节奏与发力控制。",
        "author_name": "少林教习",
        "qi_value": 1480,
    },
]


def require_env(name: str) -> str:
    value = os.getenv(name, "").strip()
    if not value:
        raise SystemExit(f"Missing environment variable: {name}")
    return value


def parse_args():
    parser = argparse.ArgumentParser(description="Upload training-room videos to OSS and upsert DB records")
    parser.add_argument("--baduanjin", action="append", default=[], help="baduanjin local file path, can be passed multiple times")
    parser.add_argument("--taijiquan", action="append", default=[], help="taijiquan local file path, can be passed multiple times")
    parser.add_argument("--wuqinxi", action="append", default=[], help="wuqinxi local file path, can be passed multiple times")
    parser.add_argument("--yijinjing", action="append", default=[], help="yijinjing local file path, can be passed multiple times")
    return parser.parse_args()


def build_requested_files(args):
    requested = {
        "baduanjin": [Path(item) for item in args.baduanjin],
        "taijiquan": [Path(item) for item in args.taijiquan],
        "wuqinxi": [Path(item) for item in args.wuqinxi],
        "yijinjing": [Path(item) for item in args.yijinjing],
    }
    for method_code, paths in requested.items():
        for path in paths:
            if not path.exists() or not path.is_file():
                raise SystemExit(f"{method_code} file not found: {path}")
    return requested


def get_duration_seconds(local_path: Path) -> int:
    escaped = str(local_path).replace("'", "''")
    command = [
        "powershell",
        "-NoProfile",
        "-Command",
        (
            f"$path='{escaped}'; "
            "$shell=New-Object -ComObject Shell.Application; "
            "$folder=$shell.Namespace((Split-Path $path)); "
            "$item=$folder.ParseName((Split-Path $path -Leaf)); "
            "$folder.GetDetailsOf($item,27)"
        ),
    ]
    result = subprocess.run(command, capture_output=True, text=True, encoding="utf-8", check=True)
    raw = result.stdout.strip()
    if not raw:
        return 0
    hours, minutes, seconds = [int(part) for part in raw.split(":")]
    return hours * 3600 + minutes * 60 + seconds


def upload_file(bucket, local_path: Path, object_key: str) -> str:
    file_size = local_path.stat().st_size
    if file_size >= 10 * 1024 * 1024:
        oss2.resumable_upload(
            bucket,
            object_key,
            str(local_path),
            multipart_threshold=10 * 1024 * 1024,
            part_size=1024 * 1024,
            num_threads=1,
        )
    else:
        with local_path.open("rb") as file_obj:
            bucket.put_object(object_key, file_obj)
    public_base = require_env("OSS_PUBLIC_BASE_URL").rstrip("/")
    return f"{public_base}/{object_key}"


def ensure_video_name_column(connection):
    with connection.cursor() as cursor:
        cursor.execute("show columns from video like 'video_name'")
        if cursor.fetchone() is None:
            cursor.execute("alter table video add column video_name varchar(100) null after category_id")
    connection.commit()


def main():
    args = parse_args()
    requested_files = build_requested_files(args)

    auth = oss2.Auth(require_env("OSS_ACCESS_KEY_ID"), require_env("OSS_ACCESS_KEY_SECRET"))
    session = oss2.Session()
    session.session.trust_env = False
    bucket = oss2.Bucket(
        auth,
        require_env("OSS_ENDPOINT"),
        require_env("OSS_BUCKET"),
        session=session,
        connect_timeout=600,
        proxies={},
    )

    file_lookup = {}
    for method_code, paths in requested_files.items():
        for path in paths:
            file_lookup[path.stem.lower()] = (method_code, path)

    records = []
    for item in VIDEO_DEFINITIONS:
        key = item["video_name"].lower()
        if key not in file_lookup:
            continue
        method_code, path = file_lookup[key]
        object_key = f"training-room-videos/{method_code}/{path.name}"
        url = upload_file(bucket, path, object_key)
        record = dict(item)
        record["video_url"] = url
        record["duration_seconds"] = get_duration_seconds(path)
        records.append(record)

    if not records:
        raise SystemExit("No matching training-room videos were provided")

    connection = pymysql.connect(
        host="localhost",
        port=3306,
        user="root",
        password="@Xbf028430",
        database="qigong",
        charset="utf8mb4",
        autocommit=False,
    )
    try:
        ensure_video_name_column(connection)
        with connection.cursor() as cursor:
            for item in records:
                cursor.execute(
                    """
                    insert into video(
                        id, category_id, video_name, title, cover, video_url, intro, author_name, duration_seconds, qi_value, create_time
                    ) values (
                        %(id)s, %(category_id)s, %(video_name)s, %(title)s, %(cover)s, %(video_url)s, %(intro)s, %(author_name)s, %(duration_seconds)s, %(qi_value)s, now()
                    )
                    on duplicate key update
                        category_id = values(category_id),
                        video_name = values(video_name),
                        title = values(title),
                        cover = values(cover),
                        video_url = values(video_url),
                        intro = values(intro),
                        author_name = values(author_name),
                        duration_seconds = values(duration_seconds),
                        qi_value = values(qi_value)
                    """,
                    item,
                )
        connection.commit()
    finally:
        connection.close()

    for item in records:
        print(f"{item['video_name']}: {item['video_url']} ({item['duration_seconds']}s)")


if __name__ == "__main__":
    main()
