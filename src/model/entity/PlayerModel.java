package model.entity;

import java.awt.Rectangle;

import static core.GameConfig.*;
import static util.PlayerStateIndex.*;

public class PlayerModel extends EntityModel {
    private int aniIndex = -1;
    private boolean facingRight = true;
    private boolean moving = false;
    private boolean jumping = false;
    private boolean atking = false;
    private boolean hurted = false;
    private boolean ultimate = false;
    private int state = IDLE;

    private final double speed = 2.0 * SCALE;
    private final double jumpPow = -4 * SCALE;

    private final int atkW = (int) (14 * SCALE);
    private final int atkH = (int) (10 * SCALE);
    private final int atkOffset = (int) (3 * SCALE);
    private long lastNormalAtk = 0;
    private final long normalAtkCd = 500;

    private int curMana;
    private int maxMana;
    private int ultimateCost;
    private int ultimateDamage;
    private long lastManaRegenTime;
    private long lastUltCastTime = 0;
    private long ultCooldown;
    private static final long HURT_DURATION = 500;
    private long hurtUntil = 0;
    private Rectangle atkbox;

    public PlayerModel(double x, double y, int width, int height, int maxHealth) {
        super(x, y, width, height, maxHealth);
        this.setHitBox((int) (18 * SCALE), (int) (6 * SCALE), (int) (27 * SCALE), (int) (35 * SCALE));
    }

    public void requestJump(double jumpPow) {
        if (!jumping && onGround) {
            dy = jumpPow;
            jumping = true;
        }
    }

    public void refreshState() {
        if (ultimate)
            state = ULTIMATE;
        else if (hurted)
            state = HURT;
        else if (atking)
            state = ATTACK;
        else if (jumping)
            state = JUMP;
        else if (moving)
            state = RUN;
        else
            state = IDLE;

        if (hurted && System.currentTimeMillis() > hurtUntil) {
            hurted = false;
        }
    }

    public void takeDamage(int amount) {
        if (hurted)
            return;
        curHealth = Math.max(curHealth - amount, 0);
        if (curHealth == 0) {
            alive = false;
        } else {
            hurted = true;
            hurtUntil = System.currentTimeMillis() + HURT_DURATION;
        }
    }

    public void applyDifficult(int difficult) {
        switch (difficult) {
            case 0 -> {
                this.maxHealth = 10000;
                this.damage = 10000;
                this.maxMana = 10000;
                this.ultimateDamage = 50000;
                this.ultimateCost = 1;
                this.ultCooldown = 1000;
            }
            case 1 -> {
                this.maxHealth = 200;
                this.damage = 20;
                this.maxMana = 200;
                this.ultimateDamage = 60;
                this.ultimateCost = 50;
                this.ultCooldown = 5000;
            }
            case 2 -> {
                this.maxHealth = 50;
                this.damage = 10;
                this.maxMana = 100;
                this.ultimateDamage = 30;
                this.ultimateCost = 100;
                this.ultCooldown = 15000;
            }
        }
        this.curHealth = this.maxHealth;
        this.curMana = this.maxMana;
    }

    public boolean isUltReady() {
        return System.currentTimeMillis() - lastUltCastTime >= ultCooldown;
    }

    public boolean isNormalAtkReady() {
        return System.currentTimeMillis() - lastNormalAtk >= normalAtkCd;
    }

    public void regenMana() {
        long now = System.currentTimeMillis();
        if (now - lastManaRegenTime >= 1000) {
            curMana = Math.min(maxMana, curMana + 2);
            lastManaRegenTime = now;
        }
    }

    public boolean useMana(int amount) {
        if (curMana >= amount) {
            curMana -= amount;
            return true;
        }
        return false;
    }

    public Rectangle getUltimateBox() {
        int ultW = (int) (64 * SCALE);
        int ultH = (int) (64 * SCALE);
        int ultX = facingRight ? hitbox.x + hitbox.width : hitbox.x - ultW;
        int ultY = hitbox.y; // + (hitbox.height - ultH) / 2
        return new Rectangle(ultX, ultY, ultW, ultH);
    }

    public int getAtkW() {
        return atkW;
    }

    public int getAtkH() {
        return atkH;
    }

    public int getAtkOffset() {
        return atkOffset;
    }

    public void setLastNormalAttack(long lastNormalAtk) {
        this.lastNormalAtk = lastNormalAtk;
    }

    public int getUltimateCost() {
        return ultimateCost;
    }

    public double getSpeed() {
        return speed;
    }

    public double getJumpPow() {
        return jumpPow;
    }

    public int getCurMana() {
        return curMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getUltimateDamage() {
        return ultimateDamage;
    }

    public int getDamage() {
        return damage;
    }

    public int getState() {
        return state;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isAtking() {
        return atking;
    }

    public boolean isJumping() {
        return jumping;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public boolean isHurted() {
        return hurted;
    }

    public boolean isUltimate() {
        return ultimate;
    }

    public Rectangle getAttackBox() {
        return atkbox;
    }

    public void setAniIndex(int v) {
        aniIndex = v;
    }

    public void setMoving(boolean v) {
        moving = v;
    }

    public void setAtking(boolean v) {
        atking = v;
    }

    public void setJumping(boolean v) {
        jumping = v;
    }

    public void setFacingRight(boolean v) {
        facingRight = v;
    }

    public void setUltimate(boolean v) {
        ultimate = v;
    }

    public void setAttackBox(Rectangle v) {
        atkbox = v;
    }

    public void setLastUltCastTime(long v) {
        lastUltCastTime = v;
    }
}