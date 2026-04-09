package model.entity;

import java.awt.Rectangle;

import static util.PlayerStateIndex.*;

public class PlayerModel extends EntityModel {
    private double dx, dy;
    private boolean moving = false;
    private boolean jumping = false;
    private boolean attacking = false;
    private boolean hitting = false;
    private boolean falling = false;

    private int state = IDLE;

    public PlayerModel(double x, double y, int width, int height) {
        super(x, y, width, height);

    }

    public void requestJump() {
        if (!jumping) {
            dy += 10;
            jumping = true;
        }
    }

    public void refreshState() {
        if (hitting) {
            state = HIT;
        }
        if (attacking) {
            state = ATTACK;
        }
        if (jumping) {
            state = JUMP;
        } else if (falling) {
            state = FALL;
        }
        if (moving) {
            state = RUN;
        } else {
            state = IDLE;
        }
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

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public int getState() {
        return state;
    }

}
