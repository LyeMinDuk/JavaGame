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
    private boolean special = false;
    private int state = IDLE;

    private final double speed = 2.0 * SCALE;
    private final double jumpPow = -4 * SCALE;

    private long lastNormalAtk = 0;
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

    private static final long HURT_DURATION = 500;
    private long hurtUntil = 0;
    private boolean frozen = false;
    private long frozenUntil = 0;
    private static final long FROZEN_DURATION = 2000;
    private Rectangle atkbox;

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

    public boolean isNormalAtkReady() {
        return System.currentTimeMillis() - lastNormalAtk >= normalAtkCd;
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

    public Rectangle getDefaultAttackBox() {
        int atkW = (int) (18 * SCALE);
        int atkH = (int) (15 * SCALE);
        int atkOffset = (int) (-1 * SCALE);
        Rectangle hb = getHitbox();
        int atkX = isFacingRight() ? hb.x + hb.width + atkOffset : hb.x - atkW - atkOffset;
        int atkY = hb.y + (int) (13 * SCALE);
        return new Rectangle(atkX, atkY, atkW, atkH);
    }

    public void setLastNormalAttack(long lastNormalAtk) {
        this.lastNormalAtk = lastNormalAtk;
    }

    public void setLastSpecialCastTime(long lastSpecialCastTime) {
        this.lastSpecialCastTime = lastSpecialCastTime;
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

    public boolean isSpecial() {
        return special;
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

    public void setSpecial(boolean v) {
        special = v;
    }

    public void setAttackBox(Rectangle v) {
        atkbox = v;
    }

    public void setLastUltCastTime(long v) {
        lastUltCastTime = v;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public int getSpecialCost() {
        return specialCost;
    }

}