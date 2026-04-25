import argparse
import os
from pathlib import Path

import oss2
import pymysql


VIDEO_MAPPING = {
    "baduanjin": 101,
    "taijiquan": 102,
    "wuqinxi": 103,
    "yijinjing": 104,
}


def require_env(name: str) -> str:
    value = os.getenv(name, "").strip()
    if not value:
        raise SystemExit(f"Missing environment variable: {name}")
    return value


def upload_file(bucket, local_path: Path, object_key: str) -> str:
    with local_path.open("rb") as file_obj:
        bucket.put_object(object_key, file_obj)
    public_base = require_env("OSS_PUBLIC_BASE_URL").rstrip("/")
    return f"{public_base}/{object_key}"


def main():
    parser = argparse.ArgumentParser(description="Upload homepage qigong videos to OSS and update DB")
    parser.add_argument("--baduanjin", required=True)
    parser.add_argument("--taijiquan", required=True)
    parser.add_argument("--wuqinxi", required=True)
    parser.add_argument("--yijinjing", required=True)
    args = parser.parse_args()

    files = {
        "baduanjin": Path(args.baduanjin),
        "taijiquan": Path(args.taijiquan),
        "wuqinxi": Path(args.wuqinxi),
        "yijinjing": Path(args.yijinjing),
    }
    for label, path in files.items():
        if not path.exists() or not path.is_file():
            raise SystemExit(f"{label} file not found: {path}")

    endpoint = require_env("OSS_ENDPOINT")
    bucket_name = require_env("OSS_BUCKET")
    access_key_id = require_env("OSS_ACCESS_KEY_ID")
    access_key_secret = require_env("OSS_ACCESS_KEY_SECRET")

    auth = oss2.Auth(access_key_id, access_key_secret)
    bucket = oss2.Bucket(auth, endpoint, bucket_name)

    uploaded_urls = {}
    for label, path in files.items():
        object_key = f"homepage-videos/{label}/{path.name}"
        uploaded_urls[label] = upload_file(bucket, path, object_key)

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
        with connection.cursor() as cursor:
            for label, record_id in VIDEO_MAPPING.items():
                cursor.execute(
                    "update video set video_url=%s where id=%s",
                    (uploaded_urls[label], record_id),
                )
        connection.commit()
    finally:
        connection.close()

    for label, url in uploaded_urls.items():
        print(f"{label}: {url}")


if __name__ == "__main__":
    main()
