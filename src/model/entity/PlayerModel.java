package model.entity;

import java.awt.Rectangle;

import static core.GameConfig.*;
import static util.PlayerStateIndex.*;

public class PlayerModel extends EntityModel {
    protected int aniIndex = -1;
    protected boolean facingRight = true;
    protected boolean moving = false;
    protected boolean jumping = false;
    protected boolean atking = false;
    protected boolean hurted = false;
    protected boolean ultimate = false;
    protected boolean special = false;
    protected int state = IDLE;
    protected final double speed = 2.0 * SCALE;
    protected final double jumpPow = -4 * SCALE;
    protected long lastNormalAtk = 0;
    protected long normalAtkCd = 500;
    protected int manaRegeneraion;

    protected int specialCost;
    protected int specialDamage;
    protected long lastSpecialCastTime = 0;
    protected long specialCooldown;

    protected int curMana;
    protected int maxMana;

    protected int ultimateCost;
    protected int ultimateDamage;
    protected long lastManaRegenTime;
    protected long lastUltCastTime = 0;
    protected long ultCooldown;

    protected static final long HURT_DURATION = 500;
    protected long hurtUntil = 0;
    protected boolean frozen = false;
    protected long frozenUntil = 0;
    protected static final long FROZEN_DURATION = 2000;
    protected Rectangle atkbox;

    public PlayerModel(double x, double y, int width, int height, int maxHealth) {
        super(x, y, width, height, maxHealth);
    }

    public void requestJump(double jumpPow) {
        if (!jumping && onGround) {
            dy = jumpPow;
            jumping = true;
        }
    }

    public void refreshState() {
        if (frozen && System.currentTimeMillis() > frozenUntil)
            frozen = false;
        if (frozen)
            state = FROZEN;
        else if (ultimate)
            state = ULTIMATE;
        else if (special)
            state = SPECIAL;
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
                this.maxHealth = 320;
                this.damage = 28;
                this.maxMana = 120;
                this.manaRegeneraion = 10;
                this.normalAtkCd = 500;
                this.ultimateDamage = 30;
                this.ultimateCost = 40;
                this.ultCooldown = 4000;
                this.specialDamage = 30;
                this.specialCost = 50;
                this.specialCooldown = 5000;
            }
            case 1 -> {
                this.maxHealth = 220;
                this.damage = 22;
                this.maxMana = 100;
                this.manaRegeneraion = 8;
                this.normalAtkCd = 550;
                this.ultimateDamage = 25;
                this.ultimateCost = 50;
                this.ultCooldown = 5000;
                this.specialDamage = 25;
                this.specialCost = 60;
                this.specialCooldown = 6000;
            }
            case 2 -> {
                this.maxHealth = 160;
                this.damage = 18;
                this.maxMana = 80;
                this.manaRegeneraion = 6;
                this.normalAtkCd = 600;
                this.ultimateDamage = 20;
                this.ultimateCost = 60;
                this.ultCooldown = 6000;
                this.specialDamage = 20;
                this.specialCost = 70;
                this.specialCooldown = 7000;
            }
        }
        this.curHealth = this.maxHealth;
        this.curMana = this.maxMana;
    }

    public boolean isNormalAtkReady() {
        return System.currentTimeMillis() - lastNormalAtk >= normalAtkCd;
    }

    public boolean isUltReady() {
        return System.currentTimeMillis() - lastUltCastTime >= ultCooldown;
    }

    public boolean isSpecialReady() {
        return System.currentTimeMillis() - lastSpecialCastTime >= specialCooldown;
    }

    public void regenMana() {
        long now = System.currentTimeMillis();
        if (now - lastManaRegenTime >= 1000) {
            curMana = Math.min(maxMana, curMana + manaRegeneraion);
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
        int ultY = hitbox.y;
        return new Rectangle(ultX, ultY, ultW, ultH);
    }

    public Rectangle getSpecialBox() {
        return getUltimateBox();
    }

    public int getSpecialDamage() {
        return specialDamage;
    }

    public void applyFrozen() {
        frozen = true;
        frozenUntil = System.currentTimeMillis() + FROZEN_DURATION;
        aniIndex = 0;
    }

    public Rectangle getNormalAttackBox() {
        int atkW = (int) (50 * SCALE);
        int atkH = (int) (15 * SCALE);
        int atkOffset = (int) (-34 * SCALE);
        int atkX = facingRight ? hitbox.x + hitbox.width + atkOffset : hitbox.x - atkW - atkOffset;
        int atkY = hitbox.y + (int) (13 * SCALE);
        return new Rectangle(atkX, atkY, atkW, atkH);
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isJumping() {
        return jumping;
    }

    public boolean isAtking() {
        return atking;
    }

    public boolean isHurted() {
        return hurted;
    }

    public boolean isUltimate() {
        return ultimate;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getState() {
        return state;
    }

    public double getSpeed() {
        return speed;
    }

    public double getJumpPow() {
        return jumpPow;
    }

    public int getSpecialCost() {
        return specialCost;
    }

    public int getCurMana() {
        return curMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getUltimateCost() {
        return ultimateCost;
    }

    public int getUltimateDamage() {
        return ultimateDamage;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setLastNormalAtk(long lastNormalAtk) {
        this.lastNormalAtk = lastNormalAtk;
    }

    public void setLastUltCastTime(long lastUltCastTime) {
        this.lastUltCastTime = lastUltCastTime;
    }

    public void setAtkbox(Rectangle atkbox) {
        this.atkbox = atkbox;
    }

    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setAtking(boolean atking) {
        this.atking = atking;
    }

    public void setUltimate(boolean ultimate) {
        this.ultimate = ultimate;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public void setLastSpecialCastTime(long lastSpecialCastTime) {
        this.lastSpecialCastTime = lastSpecialCastTime;
    }

}