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

## ⌨️ Hướng dẫn Điều khiển
| Phím | Hành động |
|------|-----------|
| **A / D** | Di chuyển trái / phải |
| **Space** | Nhảy |
| **J** | Tấn công thường |
| **K** | Kỹ năng tối thượng |
| **L** | Kỹ năng đặc biệt |
| **P / ESC** | Tạm dừng |
| **H** | Mở bảng hướng dẫn |

## 🚀 Cài đặt & Chạy Game

### ✅ Yêu cầu hệ thống
- **JDK**: Java 17 trở lên  
- **IDE khuyên dùng**: IntelliJ IDEA / Eclipse / VS Code  

### ✅ Cách 1: Chạy bằng IDE
1. Clone repository về máy  
2. Mở thư mục dự án bằng IDE  
3. Chạy file `App.java`

### ✅ Cách 2: Chạy bằng Terminal
```bash
javac App.java
java App
```

## 📄 Tài liệu & Báo cáo
Chi tiết thiết kế hệ thống, sơ đồ lớp và phân tích luồng dữ liệu:  
🔗 **Báo cáo bài tập lớn**:  
https://lmsutceduvn-my.sharepoint.com/:f:/g/personal/duc241230709_lms_utc_edu_vn/IgBgsGU5OLtxT5XZiGaK_6HVAQaYKIHqN2TpSOXu0Blxg1E?e=ScaT8M

## 📂 Cấu trúc Thư mục Dự án
```
src/
├─ core/        # GameConfig, game loop chính
├─ controller/  # Input, Physics, AI, State controllers
├─ model/       # Entity, Map, Settings, State
├─ view/        # Renderer, UI, HUD, Assets
├─ util/        # Hằng số, index animation, assets paths
└─ App.java     # Entry point
```

---

## 👤 Thông tin
- **Tác giả:** Lê Minh Đức (LyeMinDuk)  
- **Trường:** ĐH Giao thông Vận tải (UTC) – Khoa CNTT  
- **Ngôn ngữ:** Java  
