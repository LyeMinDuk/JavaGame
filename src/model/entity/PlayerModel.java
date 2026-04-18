package model.entity;

import static util.PlayerStateIndex.*;

import java.awt.Rectangle;

import static core.GameConfig.*;

public class PlayerModel extends EntityModel {
    private int aniIndex = -1;
    private boolean facingRight = true;
    private boolean moving = false;
    private boolean jumping = false;
    private boolean atking = false;
    private boolean hurted = false;
    private boolean hitted = false;
    private int state = IDLE;

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
        if (atking)
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

    // @Override
    // public int getWidth() {
    //     if (state == JUMP)
    //         return (int) (36 * SCALE);
    //     return width;
    // }

    // @Override
    // public int getHeight() {
    //     if (state == JUMP)
    //         return (int) (44 * SCALE);
    //     return height;
    // }

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

    public boolean isHitted() {
        return hitted;
    }

    public void setHitted(boolean hitted) {
        this.hitted = hitted;
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

}
