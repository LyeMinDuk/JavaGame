package model.object;

import static core.GameConfig.SCALE;

import java.awt.Rectangle;

public class SpikeModel {
    protected int x, y;
    protected int width, height;
    protected Rectangle hitbox;
    protected int hbOffsetX, hbOffsetY, hbWidth, hbHeight;

    public SpikeModel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = hbWidth = width;
        this.height = hbHeight = height;
        hbOffsetX = hbOffsetY = 0;
        this.hitbox = new Rectangle(x, y, width, height);
        this.setHitBox(0, (int) (16 * SCALE), (int) (32 * SCALE), (int) (32 * SCALE));
    }

    protected void setHitBox(int offsetX, int offsetY, int width, int height) {
        this.hbOffsetX = offsetX;
        this.hbOffsetY = offsetY;
        this.hbWidth = width;
        this.hbHeight = height;
        syncHitbox();
    }

    protected void syncHitbox() {
        hitbox.x = (int) x + hbOffsetX;
        hitbox.y = (int) y + hbOffsetY;
        hitbox.width = hbWidth;
        hitbox.height = hbHeight;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
