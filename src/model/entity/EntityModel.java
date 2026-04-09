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

    public void move(int dx, int dy){
        this.x += dx;
        this.y += dy;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
