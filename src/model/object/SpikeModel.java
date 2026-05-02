package model.object;

import java.awt.Rectangle;

import static core.GameConfig.SCALE;

public class SpikeModel {
    public static final int DIR_UP = 0;
    public static final int DIR_RIGHT = 1;
    public static final int DIR_DOWN = 2;
    public static final int DIR_LEFT = 3;

    private int x, y;
    private int width, height;
    private Rectangle hitbox;
    private int hbOffsetX, hbOffsetY, hbWidth, hbHeight;
    private int direction;

    public SpikeModel(int x, int y, int width, int height, int direction) {
        this.x = x;
        this.y = y;
        this.width = hbWidth = width;
        this.height = hbHeight = height;
        this.direction = direction;
        hbOffsetX = hbOffsetY = 0;
        this.hitbox = new Rectangle(x, y, width, height);
        this.setHitBox(0, (int) (16 * SCALE), (int) (32 * SCALE), (int) (32 * SCALE));
        changeHitbox();
    }

    private void changeHitbox() {
        switch (direction) {
            case DIR_UP -> this.setHitBox(0, (int) (16 * SCALE), (int) (32 * SCALE), (int) (16 * SCALE));
            case DIR_RIGHT -> this.setHitBox(0, 0, (int) (16 * SCALE), (int) (32 * SCALE));
            case DIR_DOWN -> this.setHitBox(0, 0, (int) (32 * SCALE), (int) (16 * SCALE));
            case DIR_LEFT -> this.setHitBox((int) (16 * SCALE), 0, (int) (16 * SCALE), (int) (32 * SCALE));
            default -> this.setHitBox(0, (int) (16 * SCALE), (int) (32 * SCALE), (int) (16 * SCALE));
        }
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

    public int getDirection() {
        return direction;
    }

}
