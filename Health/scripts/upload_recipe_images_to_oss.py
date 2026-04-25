import io
import os
from pathlib import Path

import oss2
import pymysql
import requests
from PIL import Image


RECIPE_IMAGES = {
    201: {
        "name": "玫瑰花疏肝茶",
        "slug": "rose_tea",
        "source_url": "https://images.rawpixel.com/editor_1024/cHJpdmF0ZS9zdGF0aWMvaW1hZ2Uvd2Vic2l0ZS8yMDIyLTA0L2xyL3B4MTIxNzI3OS1pbWFnZS1rd3Z3NWV1My5qcGc.jpg",
    },
    202: {
        "name": "荠菜猪肝明目汤",
        "slug": "jicai_liver_soup",
        "source_url": "https://images.rawpixel.com/editor_1024/czNmcy1wcml2YXRlL3Jhd3BpeGVsX2ltYWdlcy93ZWJzaXRlX2NvbnRlbnQvbHIvcHgxNDEyMzgxLWltYWdlLWt3dnkwbnd3LmpwZw.jpg",
    },
    203: {
        "name": "百合莲子羹",
        "slug": "lily_lotus_soup",
        "source_url": "https://pd.w.org/2025/06/887685e768dda5237.82912937-2048x1536.jpg",
    },
    204: {
        "name": "山药薏米粥",
        "slug": "shanyao_yimi_porridge",
        "source_url": "https://upload.wikimedia.org/wikipedia/commons/c/c6/Porridge-muesli-fruits.jpg",
    },
    205: {
        "name": "当归黄芪炖乌鸡",
        "slug": "danggui_huangqi_chicken",
        "source_url": "https://live.staticflickr.com/4860/32622031308_385562c929_b.jpg",
    },
    206: {
        "name": "百合陈皮静心饮",
        "slug": "baihe_chenpi_tea",
        "source_url": "https://images.rawpixel.com/editor_1024/czNmcy1wcml2YXRlL3Jhd3BpeGVsX2ltYWdlcy93ZWJzaXRlX2NvbnRlbnQvbHIvbnM3MjI1LWltYWdlLWt3dnljNjl4LmpwZw.jpg",
    },
    207: {
        "name": "银耳雪梨羹",
        "slug": "snow_pear_soup",
        "source_url": "https://live.staticflickr.com/4266/34604129964_aca1f989a5_b.jpg",
    },
    208: {
        "name": "山药百合润秋汤",
        "slug": "runqiu_soup",
        "source_url": "https://images.rawpixel.com/editor_1024/czNmcy1wcml2YXRlL3Jhd3BpeGVsX2ltYWdlcy93ZWJzaXRlX2NvbnRlbnQvbHIvcHUyMzMzNDY5LWltYWdlLWt3eXJxdjJtLmpwZw.jpg",
    },
    209: {
        "name": "黄芪羊肉暖身汤",
        "slug": "yangrou_soup",
        "source_url": "https://images.rawpixel.com/editor_1024/czNmcy1wcml2YXRlL3Jhd3BpeGVsX2ltYWdlcy93ZWJzaXRlX2NvbnRlbnQvbHIvcHg5MDgyMzQtaW1hZ2Uta3d5bzZxdGwuanBn.jpg",
    },
    210: {
        "name": "桂圆红枣养元饮",
        "slug": "guiyuan_hongzao_drink",
        "source_url": "https://images.metmuseum.org/CRDImages/eg/original/25.3.52_view_2.jpg",
    },
}


def require_env(name: str) -> str:
    value = os.getenv(name, "").strip()
    if not value:
        raise SystemExit(f"Missing environment variable: {name}")
    return value


def fetch_image_bytes(url: str) -> bytes:
    response = requests.get(url, timeout=60, headers={"User-Agent": "Mozilla/5.0"})
    response.raise_for_status()
    return response.content


def to_jpeg_bytes(content: bytes) -> bytes:
    with Image.open(io.BytesIO(content)) as image:
        if image.mode not in ("RGB", "L"):
            image = image.convert("RGB")
        elif image.mode == "L":
            image = image.convert("RGB")
        output = io.BytesIO()
        image.save(output, format="JPEG", quality=90)
        return output.getvalue()


def upload_file(bucket: oss2.Bucket, object_key: str, content: bytes) -> str:
    bucket.put_object(
        object_key,
        content,
        headers={"Content-Type": "image/jpeg", "Cache-Control": "public, max-age=31536000"},
    )
    public_base = require_env("OSS_PUBLIC_BASE_URL").rstrip("/")
    return f"{public_base}/{object_key}"


def main() -> None:
    endpoint = require_env("OSS_ENDPOINT")
    bucket_name = require_env("OSS_BUCKET")
    access_key_id = require_env("OSS_ACCESS_KEY_ID")
    access_key_secret = require_env("OSS_ACCESS_KEY_SECRET")

    auth = oss2.Auth(access_key_id, access_key_secret)
    bucket = oss2.Bucket(auth, endpoint, bucket_name)

    uploaded_urls = {}
    for recipe_id, item in RECIPE_IMAGES.items():
        image_bytes = fetch_image_bytes(item["source_url"])
        jpeg_bytes = to_jpeg_bytes(image_bytes)
        object_key = f"recipe-covers/{recipe_id}_{item['slug']}.jpg"
        uploaded_urls[recipe_id] = upload_file(bucket, object_key, jpeg_bytes)
        print(f"uploaded {recipe_id} {item['name']} -> {uploaded_urls[recipe_id]}")

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
            for recipe_id, url in uploaded_urls.items():
                cursor.execute("update recipe set cover=%s where id=%s", (url, recipe_id))
        connection.commit()
    finally:
        connection.close()

    print("done")


if __name__ == "__main__":
    main()
