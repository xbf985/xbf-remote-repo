import math
import os
from pathlib import Path

import oss2
from PIL import Image, ImageDraw, ImageFilter, ImageFont


OUTPUT_DIR = Path("c:/Users/谢泊锋/IdeaProjects/Health/tmp/community-seed-assets")
PUBLIC_BASE = os.getenv("OSS_PUBLIC_BASE_URL", "https://sky-tae-out.oss-cn-beijing.aliyuncs.com").rstrip("/")
FONT_PATH = Path("C:/Windows/Fonts/msyh.ttc")
FONT_BOLD_PATH = Path("C:/Windows/Fonts/msyhbd.ttc")

AVATARS = [
    {"slug": "official", "label": "官", "bg": "#D96C4B", "fg": "#FFF8F1"},
    {"slug": "yunxi", "label": "云", "bg": "#4E7D73", "fg": "#F7F9F4"},
    {"slug": "shanye", "label": "隐", "bg": "#7F6A59", "fg": "#FFF6EB"},
    {"slug": "wuming", "label": "无", "bg": "#6C5B7B", "fg": "#FFF9FD"},
    {"slug": "qingyu", "label": "清", "bg": "#6D8C94", "fg": "#F5FBFC"},
    {"slug": "shenguan", "label": "观", "bg": "#8A5A7D", "fg": "#FFF7FC"},
    {"slug": "aning", "label": "宁", "bg": "#B96A44", "fg": "#FFF8F0"},
    {"slug": "wanzhao", "label": "晚", "bg": "#506D8A", "fg": "#F8FAFF"},
    {"slug": "xuanzhi", "label": "玄", "bg": "#5F6F52", "fg": "#FAFCF4"},
]

POSTS = [
    {
        "slug": "321_official_baduanjin_morning",
        "title": "早八前练一遍八段锦，肩颈真的会慢慢松下来",
        "subline": "打工人晨练",
        "accent": "#C95F46",
        "bg": "#F8F0E8",
        "type": "video",
        "tag": "八段锦",
    },
    {
        "slug": "322_wuming_dorm_breath",
        "title": "宿舍也能做的五分钟静心小流程，越练越能稳住心气",
        "subline": "晚间记录",
        "accent": "#6C5B7B",
        "bg": "#F7F3FB",
        "type": "post",
        "tag": "静心",
    },
    {
        "slug": "323_yunxi_taiji_day7",
        "title": "太极晨练第七天，动作慢下来以后，气息反而更稳",
        "subline": "晨练打卡",
        "accent": "#4E7D73",
        "bg": "#EEF7F3",
        "type": "video",
        "tag": "太极拳",
    },
    {
        "slug": "324_shenguan_cloudhands",
        "title": "今天终于把云手走顺了，原来不是越快越好",
        "subline": "同修心得",
        "accent": "#8A5A7D",
        "bg": "#F8F0F6",
        "type": "post",
        "tag": "太极拳",
    },
    {
        "slug": "325_shanye_wuqinxi_lunch",
        "title": "午休后跟练一段五禽戏，腰背会一点点被打开",
        "subline": "午间跟练",
        "accent": "#7F6A59",
        "bg": "#F6F0EA",
        "type": "video",
        "tag": "五禽戏",
    },
    {
        "slug": "326_qingyu_baduanjin_bow",
        "title": "打工人练八段锦最有感的一招，是左右开弓这一式",
        "subline": "动作拆解",
        "accent": "#6D8C94",
        "bg": "#EEF6F8",
        "type": "post",
        "tag": "八段锦",
    },
    {
        "slug": "327_official_baduanjin_count",
        "title": "国家体育总局口令版八段锦，跟着练不容易乱节奏",
        "subline": "口令跟练",
        "accent": "#D96C4B",
        "bg": "#FAEFE8",
        "type": "video",
        "tag": "八段锦",
    },
    {
        "slug": "328_wanzhao_yijinjing_heat",
        "title": "练完易筋经以后，小腿发热是真的会出现，不是错觉",
        "subline": "夜练记录",
        "accent": "#506D8A",
        "bg": "#EEF3F9",
        "type": "post",
        "tag": "易筋经",
    },
    {
        "slug": "329_yunxi_taiji_home",
        "title": "居家跟练太极十二分钟，心会慢下来，动作也会更稳",
        "subline": "居家修习",
        "accent": "#4E7D73",
        "bg": "#EEF7F3",
        "type": "video",
        "tag": "太极拳",
    },
    {
        "slug": "330_xuanzhi_roommate_wuqinxi",
        "title": "第一次把五禽戏发给室友，她居然真的跟着练起来了",
        "subline": "分享日常",
        "accent": "#5F6F52",
        "bg": "#F3F8EF",
        "type": "post",
        "tag": "五禽戏",
    },
    {
        "slug": "331_shanye_yijinjing_begin",
        "title": "易筋经入门别急着用蛮力，先把呼吸节奏带顺",
        "subline": "跟练视频",
        "accent": "#7F6A59",
        "bg": "#F7F1EB",
        "type": "video",
        "tag": "易筋经",
    },
    {
        "slug": "332_wuming_standing_today",
        "title": "今天的修习记录：站桩十分钟，比昨天更能静下来一点",
        "subline": "修行日记",
        "accent": "#6C5B7B",
        "bg": "#F6F2FA",
        "type": "post",
        "tag": "站桩",
    },
]


def require_env(name: str) -> str:
    value = os.getenv(name, "").strip()
    if not value:
        raise SystemExit(f"Missing environment variable: {name}")
    return value


def font(size: int, bold: bool = False) -> ImageFont.FreeTypeFont:
    path = FONT_BOLD_PATH if bold and FONT_BOLD_PATH.exists() else FONT_PATH
    if not path.exists():
        return ImageFont.load_default()
    return ImageFont.truetype(str(path), size)


def wrap_text(draw: ImageDraw.ImageDraw, text: str, target_width: int, current_font) -> list[str]:
    lines = []
    buffer = ""
    for char in text:
        candidate = buffer + char
        width = draw.textbbox((0, 0), candidate, font=current_font)[2]
        if width <= target_width or not buffer:
            buffer = candidate
            continue
        lines.append(buffer)
        buffer = char
    if buffer:
        lines.append(buffer)
    return lines


def draw_gradient_background(image: Image.Image, bg_color: str, accent_color: str) -> None:
    draw = ImageDraw.Draw(image)
    width, height = image.size
    for y in range(height):
        ratio = y / max(height - 1, 1)
        alpha = int(160 * (1 - ratio))
        draw.line(
            [(0, y), (width, y)],
            fill=hex_to_rgba(bg_color, 255),
            width=1,
        )
        draw.line(
            [(0, y), (width, y)],
            fill=hex_to_rgba(accent_color, alpha),
            width=1,
        )

    overlay = Image.new("RGBA", image.size, (0, 0, 0, 0))
    overlay_draw = ImageDraw.Draw(overlay)
    overlay_draw.ellipse((width - 460, -120, width + 80, 420), fill=hex_to_rgba(accent_color, 38))
    overlay_draw.rounded_rectangle((40, height - 220, width - 40, height - 60), radius=46, fill=(255, 255, 255, 150))
    overlay_draw.ellipse((40, 90, 220, 270), fill=hex_to_rgba("#FFFFFF", 120))
    overlay_draw.rounded_rectangle((72, 128, width - 72, 182), radius=26, fill=(255, 255, 255, 110))
    overlay_draw.rounded_rectangle((72, 200, 360, 250), radius=24, fill=(255, 255, 255, 95))
    image.alpha_composite(overlay)


def hex_to_rgba(hex_color: str, alpha: int) -> tuple[int, int, int, int]:
    hex_color = hex_color.lstrip("#")
    return tuple(int(hex_color[i:i + 2], 16) for i in (0, 2, 4)) + (alpha,)


def create_avatar(slug: str, label: str, bg: str, fg: str) -> Path:
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    size = 512
    image = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    draw.ellipse((20, 20, size - 20, size - 20), fill=hex_to_rgba(bg, 255))
    draw.ellipse((52, 52, size - 52, size - 52), outline=hex_to_rgba("#FFFFFF", 120), width=4)
    draw.ellipse((100, 88, size - 100, size - 124), fill=hex_to_rgba("#FFFFFF", 28))
    text_font = font(220, bold=True)
    bbox = draw.textbbox((0, 0), label, font=text_font)
    x = (size - (bbox[2] - bbox[0])) / 2
    y = (size - (bbox[3] - bbox[1])) / 2 - 8
    draw.text((x, y), label, font=text_font, fill=fg)
    path = OUTPUT_DIR / f"avatar_{slug}.png"
    image.save(path)
    return path


def create_cover(post: dict) -> Path:
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    is_video = post["type"] == "video"
    height = 1260 if is_video else 1380
    image = Image.new("RGBA", (900, height), (255, 255, 255, 255))
    draw_gradient_background(image, post["bg"], post["accent"])
    draw = ImageDraw.Draw(image)

    tag_font = font(42, bold=True)
    title_font = font(82, bold=True)
    sub_font = font(34, bold=False)
    note_font = font(30, bold=False)

    draw.text((92, 118), post["subline"], font=tag_font, fill=post["accent"])
    draw.text((92, 208), f"#{post['tag']}", font=sub_font, fill="#7C736A")

    wrapped = wrap_text(draw, post["title"], 720, title_font)
    y = 314
    for line in wrapped[:4]:
        draw.text((92, y), line, font=title_font, fill="#2B2622")
        y += 108

    quote_text = "练得久了才发现，能坚持下来比一下子练很猛更重要。"
    quote_lines = wrap_text(draw, quote_text, 690, note_font)
    note_y = height - 200
    for line in quote_lines[:2]:
        draw.text((92, note_y), line, font=note_font, fill="#6A625B")
        note_y += 44

    if is_video:
        shadow = Image.new("RGBA", image.size, (0, 0, 0, 0))
        shadow_draw = ImageDraw.Draw(shadow)
        shadow_draw.ellipse((630, 84, 818, 272), fill=(0, 0, 0, 28))
        image.alpha_composite(shadow.filter(ImageFilter.GaussianBlur(18)))

        draw.ellipse((642, 92, 806, 256), fill=hex_to_rgba("#FFFFFF", 215), outline=hex_to_rgba(post["accent"], 80), width=3)
        triangle = [(706, 140), (706, 208), (760, 174)]
        draw.polygon(triangle, fill=post["accent"])
        draw.rounded_rectangle((92, height - 308, 330, height - 240), radius=26, fill=hex_to_rgba(post["accent"], 38))
        draw.text((118, height - 300), "点开可直接跟练", font=sub_font, fill=post["accent"])
    else:
        draw.rounded_rectangle((92, height - 320, 230, height - 246), radius=26, fill=hex_to_rgba(post["accent"], 34))
        draw.text((118, height - 308), "心得图文", font=sub_font, fill=post["accent"])

    path = OUTPUT_DIR / f"{post['slug']}.jpg"
    image.convert("RGB").save(path, quality=92)
    return path


def upload_file(bucket: oss2.Bucket, local_path: Path, object_key: str) -> str:
    with local_path.open("rb") as fp:
        bucket.put_object(
            object_key,
            fp,
            headers={"Cache-Control": "public, max-age=31536000"},
        )
    return f"{PUBLIC_BASE}/{object_key}"


def main() -> None:
    auth = oss2.Auth(require_env("OSS_ACCESS_KEY_ID"), require_env("OSS_ACCESS_KEY_SECRET"))
    bucket = oss2.Bucket(auth, require_env("OSS_ENDPOINT"), require_env("OSS_BUCKET"))

    print("avatar urls:")
    for item in AVATARS:
        avatar_path = create_avatar(item["slug"], item["label"], item["bg"], item["fg"])
        avatar_url = upload_file(bucket, avatar_path, f"community-seed/avatars/{avatar_path.name}")
        print(f"{item['slug']}={avatar_url}")

    print("cover urls:")
    for post in POSTS:
        cover_path = create_cover(post)
        cover_url = upload_file(bucket, cover_path, f"community-seed/covers/{cover_path.name}")
        print(f"{post['slug']}={cover_url}")


if __name__ == "__main__":
    main()
