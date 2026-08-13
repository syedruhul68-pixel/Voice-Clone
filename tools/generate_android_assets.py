# tools/generate_android_assets.py
from PIL import Image, ImageFilter, ImageOps, ImageDraw
import os, sys

INPUT = "tools/assets/input_logo.png"  # Put your uploaded image here
OUT = "tools/android_assets"
os.makedirs(OUT, exist_ok=True)
os.makedirs(os.path.dirname(INPUT), exist_ok=True)

def make_circular_logo(src, size, glow_color=(124,0,255), glow_radius=26, outline_width=10):
    img = src.copy().convert("RGBA")
    w,h = img.size
    s = min(w,h)
    left = (w - s)//2; top = (h - s)//2
    img = img.crop((left, top, left+s, top+s)).resize((size, size), Image.LANCZOS)
    mask = Image.new("L", (size, size), 0)
    draw = ImageDraw.Draw(mask)
    draw.ellipse((0,0,size,size), fill=255)
    glow = Image.new("RGBA", (size, size), (0,0,0,0))
    glow_draw = ImageDraw.Draw(glow)
    glow_draw.ellipse((outline_width, outline_width, size-outline_width, size-outline_width), fill=glow_color+(200,))
    glow = glow.filter(ImageFilter.GaussianBlur(radius=glow_radius))
    out = Image.new("RGBA", (size, size), (0,0,0,0))
    out.paste(glow, (0,0), glow)
    out.paste(img, (0,0), mask)
    ring = Image.new("RGBA", (size, size), (0,0,0,0))
    ring_draw = ImageDraw.Draw(ring)
    ring_draw.ellipse((outline_width/2, outline_width/2, size-outline_width/2, size-outline_width/2), outline=glow_color+(180,), width=int(outline_width/2))
    ring = ring.filter(ImageFilter.GaussianBlur(radius=1.2))
    out = Image.alpha_composite(out, ring)
    return out

def make_blurred_background(src, out_size=(1920,1080), blur_radius=28, desat_amount=0.38, darken_alpha=180):
    w,h = src.size
    target_w, target_h = out_size
    target_ratio = target_w / target_h
    if (w / h) > target_ratio:
        new_w = int(h * target_ratio)
        left = (w - new_w)//2
        box = (left, 0, left+new_w, h)
    else:
        new_h = int(w / target_ratio)
        top = (h - new_h)//2
        box = (0, top, w, top+new_h)
    bg = src.crop(box).resize(out_size, Image.LANCZOS)
    gray = ImageOps.grayscale(bg).convert("RGB")
    blended = Image.blend(bg, gray, alpha=desat_amount)
    blurred = blended.filter(ImageFilter.GaussianBlur(radius=blur_radius))
    overlay = Image.new("RGBA", blurred.size, (0,0,0,darken_alpha))
    out = Image.alpha_composite(blurred.convert("RGBA"), overlay)
    tint = Image.new("RGBA", out.size, (120,0,255,30))
    out = Image.alpha_composite(out, tint)
    return out

if __name__ == "__main__":
    if not os.path.exists(INPUT):
        print("Place your source image at", INPUT)
        sys.exit(1)
    src = Image.open(INPUT).convert("RGBA")
    fg = make_circular_logo(src, 1024)
    bg = make_blurred_background(src, out_size=(1920,1080))
    fg.save(os.path.join(OUT, "ic_launcher_foreground_1024.png"))
    fg.resize((512,512), Image.LANCZOS).save(os.path.join(OUT, "ic_launcher_foreground_512.png"))
    fg.resize((192,192), Image.LANCZOS).save(os.path.join(OUT, "ic_launcher_foreground_192.png"))
    fg.resize((108,108), Image.LANCZOS).save(os.path.join(OUT, "ic_toolbar_logo_108.png"))
    bg.save(os.path.join(OUT, "background_1920x1080.png"))
    bg.resize((1280,720), Image.LANCZOS).save(os.path.join(OUT, "background_1280x720.png"))
    bg.resize((720,1280), Image.LANCZOS).save(os.path.join(OUT, "background_720x1280.png"))
    print("Saved assets to", OUT)
