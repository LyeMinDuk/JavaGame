package view.renderer.state.ui;

import java.awt.Graphics;
import java.awt.Rectangle;

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

    public boolean isHit(int mouseX, int mouseY) {
        return bounds.contains(mouseX, mouseY);
    }

    public abstract void draw(Graphics g);

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
        hovered = pressed = false;
    }

}