package controller.entity;

import java.util.List;
import java.awt.Rectangle;

import controller.InputController;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;

import static core.GameConfig.*;
import static util.enemy.EnemyAIState.*;

public class PlayerController {
    private final InputController input;
    private final double speed = 3.0 * SCALE;
    private final double jumpForce = -4 * SCALE;

    private final int attackDamage = 15;
    private final int attackW = 28;
    private final int attackH = 20;
    private final int attackOffset = 6;
    private long lastAttackMs = 0;
    private final long attackCdMs = 250;
    private final int atkHitFrame = 2;

    public PlayerController(InputController input) {
        this.input = input;
    }

    public void update(PlayerModel player, List<EnemyModel> enemies) {
        double dx = 0;
        if (input.isLeft() && !input.isRight()) {
            dx = -speed;
            player.setFacingRight(false);
        }
        if (input.isRight() && !input.isLeft()) {
            dx = speed;
            player.setFacingRight(true);
        }
        player.setDx(dx);
        player.setMoving(dx != 0);
        if (input.isJump() && player.isOnGround()) {
            player.requestJump(jumpForce);
        }
        handleAttackInput(player, enemies);
        handleAttack(player, enemies);
    }

    private void handleAttackInput(PlayerModel player, List<EnemyModel> enemies) {
        if (!input.isAttack() || player.isAtking())
            return;

        long now = System.currentTimeMillis();
        if (now - lastAttackMs < attackCdMs)
            return;

        lastAttackMs = now;
        player.setAtking(true);
        player.setHitted(false);
    }

    private void handleAttack(PlayerModel player, List<EnemyModel> enemies) {
        if (!player.isAtking())
            return;
        int frame = player.getAniIndex();
        if (frame == atkHitFrame && !player.isHitted()) {
            Rectangle p = player.getHitbox();
            int ax = player.isFacingRight() ? p.x + p.width + attackOffset : p.x - attackW - attackOffset;
            int ay = p.y + (p.height - attackH) / 2;
            Rectangle atkBox = new Rectangle(ax, ay, attackW, attackH);
            for (EnemyModel e : enemies) {
                if (e.isAlive() && e.getAiState() != HURT && atkBox.intersects(e.getHitbox())) {
                    e.takeDamage(attackDamage);
                }
            }
            player.setHitted(true);
        }
    }
}