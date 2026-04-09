package model.entity;

import java.awt.Rectangle;

public abstract class EntityModel {
    protected double x, y;
    protected int width, height;
    protected Rectangle hitbox;
    protected boolean alive = true;

    public EntityModel(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitbox = new Rectangle((int) x, (int) y, width, height);
    }

    protected void syncHitbox() {
        hitbox.x = (int) x;
        hitbox.y = (int) y;
        hitbox.width = width;
        hitbox.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean isAlive() {
        return alive;
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        syncHitbox();
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
