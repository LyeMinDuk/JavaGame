package model.entity.enemy;

import java.awt.Rectangle;

import model.entity.EntityModel;
import model.entity.PlayerModel;

import static core.GameConfig.SCALE;
import static util.enemy.EnemyAIState.*;

public abstract class EnemyModel extends EntityModel {
    protected int aniIndex = -1;
    protected boolean facingRight = true;
    protected int aiState = PATROL;
    protected int aniState = IDLE;
    protected int lastState = -1;
    protected long hurtUntil = 0;
    protected long dieUntil = 0;
    private static final long HURT_DURATION = 800;
    private static final long DIE_DURATION = 1000;

    protected double patrolLeftX;
    protected double patrolRightX;
    protected double moveSpeed = 1.0 * SCALE;

    public EnemyModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth);
        this.damage = damage;
    }

    public void takeDamage(int amount) {
        if (curHealth <= 0) {
            return;
        }
        curHealth = Math.max(0, curHealth - amount);
        if (curHealth == 0) {
            aiState = DIE;
            dieUntil = System.currentTimeMillis() + DIE_DURATION;
        } else {
            aiState = HURT;
            hurtUntil = System.currentTimeMillis() + HURT_DURATION;
        }
    }

    public abstract Rectangle getAttackBox();

    public abstract void updateAi(PlayerModel player);

    public abstract void refreshState();

    public boolean canRemove() {
        return curHealth == 0 && System.currentTimeMillis() > dieUntil;
    }

    @Override
    public boolean isAlive() {
        return getCurHealth() > 0 || System.currentTimeMillis() <= dieUntil;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setLastState(int lastState) {
        this.lastState = lastState;
    }

    public int getLastState() {
        return lastState;
    }

    public int getAiState() {
        return aiState;
    }

    public int getAniState() {
        return aniState;
    }

    public double getPatrolLeftX() {
        return patrolLeftX;
    }

    public double getPatrolRightX() {
        return patrolRightX;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public void setAiState(int aiState) {
        this.aiState = aiState;
    }

    public void setAniState(int aniState) {
        this.aniState = aniState;
    }

}