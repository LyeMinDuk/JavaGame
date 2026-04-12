package model.entity;

import static util.PlayerStateIndex.*;

public class PlayerModel extends EntityModel {
    private double dx, dy;
    private boolean facingRight = true;
    private boolean moving = false;
    private boolean jumping = false;
    private boolean attacking = false;
    private boolean hitting = false;
    private boolean falling = false;

    private int state = IDLE;

    public PlayerModel(double x, double y, int width, int height, int maxHealth) {
        super(x, y, width, height, maxHealth);

    }

    public void requestJump(double jumpPow) {
        if (!jumping) {
            dy = jumpPow;
            jumping = true;
        }
    }

    public void refreshState() {
        if (hitting)
            state = HURT;
        else if (attacking)
            state = ATTACK;
        else if (jumping)
            state = JUMP;
        else if (falling)
            state = FALL;
        else if (moving)
            state = RUN;
        else
            state = IDLE;
    }

    public void takeDamage(int amount){
        if(amount <= 0 || !alive){
            return;
        }
        curHealth = Math.max(curHealth - amount, 0);
        if(curHealth == 0){
            alive = false;
        }
        hitting = true;

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

    public boolean isMoving() {
        return moving;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setHitting(boolean hitting) {
        this.hitting = hitting;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean isHitting() {
        return hitting;
    }

    public boolean isFalling() {
        return falling;
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
