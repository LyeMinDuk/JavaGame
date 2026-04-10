package controller.entity;

import controller.InputController;
import model.entity.PlayerModel;

public class PlayerController {
    private final InputController input;
    private final double speed;
    private final double jumpVel;

    public PlayerController(InputController input, double speed, double jumpVel) {
        this.input = input;
        this.speed = speed;
        this.jumpVel = jumpVel;
    }

    public void update(PlayerModel player) {
        double dx = 0;
        boolean moving = false;

        if (input.isLeft() && !input.isRight()) {
            dx = -speed;
            moving = true;
            player.setFacingRight(false);
        }
        if (input.isRight() && !input.isLeft()) {
            dx = speed;
            moving = true;
            player.setFacingRight(true);
        }

        player.setDx(dx);
        player.setMoving(moving);

        if (input.isJump()) {
            player.requestJump(jumpVel);
        }
    }
}