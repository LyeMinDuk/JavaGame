package model.entity;

import java.awt.Rectangle;

public abstract class EntityModel {
    protected int curHealth;
    protected int maxHealth;
    protected double x, y;
    protected double dx, dy;
    protected int width, height;
    protected Rectangle hitbox;
    protected int hbOffsetX, hbOffsetY, hbWidth, hbHeight;
    protected boolean alive;
    protected boolean onGround;

    public EntityModel(double x, double y, int width, int height, int maxHealth) {
        this.x = x;
        this.y = y;
        this.width = hbWidth = width;
        this.height = hbHeight = height;
        hbOffsetX = hbOffsetY = 0;
        this.maxHealth = maxHealth;
        this.curHealth = maxHealth;
        alive = true;
        hitbox = new Rectangle();
        syncHitbox();
    }

    protected void syncHitbox() {
        hitbox.x = (int) x + hbOffsetX;
        hitbox.y = (int) y + hbOffsetY;
        hitbox.width = hbWidth;
        hitbox.height = hbHeight;
    }

    protected void setHitBox(int offsetX, int offsetY, int width, int height) {
        this.hbOffsetX = offsetX;
        this.hbOffsetY = offsetY;
        this.hbWidth = width;
        this.hbHeight = height;
        syncHitbox();
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        syncHitbox();
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        syncHitbox();
    }

    public int getCurHealth() {
        return curHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHbOffsetX() {
        return hbOffsetX;
    }

    public int getHbOffsetY(){
        return hbOffsetY;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
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

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
