package model.entity.enemy;

import model.entity.EntityModel;
import model.entity.PlayerModel;
import util.EnemyStateIndex;

import static core.GameConfig.SCALE;
import static util.EnemyStateIndex.*;

public abstract class EnemyModel extends EntityModel {
    private int curHealth;
    private int maxHealth;
    private int damage;
    protected double dx, dy;

    protected boolean facingRight = true;
    protected int state = PATROL;

    protected double patrolLeftX;
    protected double patrolRightX;
    protected double moveSpeed = 1.0;

    public EnemyModel(double x, double y, int width, int height, int curHealth, int damage) {
        super(x, y, width, height);
        this.maxHealth = maxHealth;
        this.curHealth = curHealth;
        this.damage = damage;

        this.patrolLeftX = x - 100 * SCALE;
        this.patrolRightX = x + 100 * SCALE;
    }

    public void takeDamage(int amount) {
        if (!isAlive())
            return;
        setHealth(this.curHealth - Math.max(0, amount));
        if (isAlive()) {
            state = EnemyStateIndex.HIT;
        }
    }

    public void setHealth(int health) {
        this.curHealth = Math.max(0, health);
        if (this.curHealth == 0) {
            setAlive(false);
            state = DEAD;
            dx = dy = 0;
        }
    }

    public void setPatrolRange(double left, double right) {
        this.patrolLeftX = Math.min(left, right);
        this.patrolRightX = Math.max(left, right);
    }

    public void moveByVelocity() {
        move(dx, dy);
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

    public double getMoveSpeed() {
        return moveSpeed;
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

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
}