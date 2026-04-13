package model.entity.enemy;

import model.entity.EntityModel;
import model.entity.PlayerModel;

import static core.GameConfig.SCALE;
import static util.enemy.EnemyAIState.*;

import java.awt.Graphics;

public abstract class EnemyModel extends EntityModel {
    protected int damage;
    protected double dx, dy;

    protected boolean facingRight = true;
    protected int state = PATROL;

    protected double patrolLeftX;
    protected double patrolRightX;
    protected double moveSpeed = 1.0;

    public EnemyModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth);
        this.damage = damage;
        this.patrolLeftX = x - (100 * SCALE);
        this.patrolRightX = x + (100 * SCALE);
    }

    public void takeDamage(int amount) {
        if (!alive) {
            return;
        }
        setHealth(curHealth - amount);
        if (alive) {
            state = HURT;
        }
    }

    public void setHealth(int health) {
        curHealth = Math.max(0, health);
        if (curHealth == 0) {
            alive = false;
            state = DIE;
            dx = dy = 0;
        }
    }

    public abstract void updateAI(PlayerModel player);

    public int getHealth() {
        return curHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public int getState() {
        return state;
    }

    public double getPatrolLeftX() {
        return patrolLeftX;
    }

    public double getPatrolRightX() {
        return patrolRightX;
    }

    public void setDamage(int damage) {
        this.damage = Math.max(0, damage);
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public void setState(int state) {
        this.state = state;
    }

}