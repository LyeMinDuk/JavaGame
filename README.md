# Java Game – 2D ACTION RPG (Java)

Dự án game 2D viết bằng Java, tập trung vào cơ chế điều khiển nhân vật, chiến đấu theo lớp nhân vật, AI quái vật và kiến trúc MVC.  
Game chạy theo game loop riêng, render bằng Java Swing và có hệ thống âm thanh, lưu/đọc cấu hình.

## 🎮 Tổng quan
- Thể loại: 2D RPG / Action
- Nền tảng: Java Desktop (Swing)
- Điểm nổi bật:
  - Kiến trúc MVC rõ ràng (Model – View – Controller)
  - Nhiều loại quái và boss với AI riêng
  - Hệ thống kỹ năng (Ultimate / Special / Attack)
  - Lưu cấu hình người chơi (âm thanh, độ khó, premium)

## 🧱 Kiến trúc MVC
- **Model**: Player, Enemy, Map, State, Settings  
- **View**: Render game, UI, HUD, animation, assets  
- **Controller**: Input, World, Physics, State controllers  

## ⚙️ Game Loop
- Vòng lặp update + render tách biệt
- Xử lý input, physics, AI theo từng frame
- Giữ FPS ổn định (cấu hình trong `GameConfig`)

## 📄 Báo cáo
Xem báo cáo chi tiết tại:  
**https://lmsutceduvn-my.sharepoint.com/:f:/g/personal/duc241230709_lms_utc_edu_vn/IgBgsGU5OLtxT5XZiGaK_6HVAQaYKIHqN2TpSOXu0Blxg1E?e=ScaT8M**

## ▶️ Hướng dẫn chạy
### Cách 1: Chạy trực tiếp
Chạy class `App.java`.

### Cách 2: Biên dịch bằng javac
```bash
javac App.java
java App
```

## 📂 Cấu trúc thư mục
```
core/          (game loop, cấu hình)
controller/    (logic điều khiển)
model/         (dữ liệu)
view/          (render, UI)
util/          (hằng số, index)
```

---

**Tác giả:** LyeMinDuk  
**Ngôn ngữ:** Java
