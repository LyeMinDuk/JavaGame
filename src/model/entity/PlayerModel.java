package model.entity;

import static util.PlayerStateIndex.*;

import java.awt.Rectangle;

import static core.GameConfig.*;

public class PlayerModel extends EntityModel {
    private int aniIndex = -1;
    private int damage;
    private boolean facingRight = true;
    private boolean moving = false;
    private boolean jumping = false;
    private boolean atking = false;
    private boolean hurted = false;
    private boolean ultimate = false;
    private int state = IDLE;
    private int curMana;
    private int maxMana;
    private int ultimateDamage;
    private long lastManaRegenTime;
    private long lastUltCastTime = 0;
    private long ultCooldownMs;

    private Rectangle atkbox;

    public PlayerModel(double x, double y, int width, int height, int maxHealth) {
        super(x, y, width, height, maxHealth);
        setHitBox((int) (18 * SCALE), (int) (6 * SCALE), (int) (27 * SCALE), (int) (35 * SCALE));
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
        else if (atking)
            state = ATTACK;
        else if (jumping)
            state = JUMP;
        else if (moving)
            state = RUN;
        else
            state = IDLE;
    }

    public void takeDamage(int amount) {
        if (amount <= 0 || !alive) {
            return;
        }
        curHealth = Math.max(curHealth - amount, 0);
        if (curHealth == 0) {
            alive = false;
        }
    }

    public void applyDifficult(int difficult) {
        switch (difficult) {
            case 0 -> {
                this.maxHealth = 10000;
                this.curHealth = 10000;
                this.damage = 10000;
                this.maxMana = 10000;
                this.ultimateDamage = 50000;
                this.ultCooldownMs = 1000;
            }
            case 1 -> {
                this.maxHealth = 100;
                this.curHealth = 100;
                this.damage = 20;
                this.maxMana = 100;
                this.ultimateDamage = 50;
                this.ultCooldownMs = 5000;
            }
            case 2 -> {
                this.maxHealth = 50;
                this.curHealth = 50;
                this.damage = 10;
                this.maxMana = 100; // Vẫn để maxMana 100 để đủ dùng skill
                this.ultimateDamage = 30;
                this.ultCooldownMs = 20000;
            }
        }
        this.curHealth = this.maxHealth;
        this.curMana = this.maxMana;
    }

    public boolean isUltReady() {
        // Nếu thời gian trôi qua kể từ lần cast cuối >= thời gian CD
        return System.currentTimeMillis() - lastUltCastTime >= ultCooldownMs;
    }

    public void setLastUltCastTime(long lastUltCastTime) {
        this.lastUltCastTime = lastUltCastTime;
    }

    public void regenMana() {
        long now = System.currentTimeMillis();
        // Cứ mỗi 5000ms (5 giây)
        if (now - lastManaRegenTime >= 5000) {
            if (curMana < maxMana) {
                curMana = Math.min(maxMana, curMana + 10);
            }
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

        // Ngay trước mặt: bên phải thì cộng chiều rộng hitbox player, bên trái thì trừ
        // đi chiều rộng của skill
        int ax = facingRight ? hitbox.x + hitbox.width : hitbox.x - ultW;

        // Căn giữa theo chiều dọc so với player
        int ay = hitbox.y + (hitbox.height - ultH) / 2;

        return new Rectangle(ax, ay, ultW, ultH);
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

    public void setUltimate(boolean ultimate) {
        this.ultimate = ultimate;
    }

    public boolean isUltimate() {
        return ultimate;
    }

    public Rectangle getAttackBox() {
        return atkbox;
    }

    public void setAttackBox(Rectangle atkbox) {
        this.atkbox = atkbox;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isAtking() {
        return atking;
    }

    public void setAtking(boolean atking) {
        this.atking = atking;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public int getState() {
        return state;
    }

    public int getDamage() {
        return damage;
    }

}
