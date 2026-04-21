package view.renderer.state.ui;

import java.awt.Graphics;
import java.awt.Rectangle;

// Dùng abstract class vì mỗi nút có thể có cách vẽ (draw) ảnh khác nhau
public abstract class UIButton {
    protected int x, y, width, height;
    protected Rectangle bounds;
    protected boolean hovered, pressed;

    public UIButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
    }

    // Nút nào cũng cần bắt va chạm chuột y hệt nhau
    public boolean isHit(int mouseX, int mouseY) {
        return bounds.contains(mouseX, mouseY);
    }

    // Mỗi loại nút sẽ tự định nghĩa cách nó được vẽ ra màn hình
    public abstract void draw(Graphics g);

    // Getters & Setters dùng chung
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void resetState() {
        hovered = false;
        pressed = false;
    }
}