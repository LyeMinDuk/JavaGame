package controller.entity;

import java.util.Arrays;
import java.util.List;
import java.awt.Rectangle;

import controller.InputController;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;

import static util.enemy.EnemyAIState.*;

public class PlayerController {
    private final InputController input;
    private final int atkHitFrame = 3;
    private final int[] ultHitFrames = { 4, 9, 13 };
    private boolean[] ultFrameChecked = new boolean[17];

    public PlayerController(InputController input) {
        this.input = input;
    }

    public void update(PlayerModel player, List<EnemyModel> enemies) {
        player.regenMana();
        if (player.isUltimate()) {
            player.setDx(0);
            player.setMoving(false);
            handleUltimateDamage(player, enemies);
            return;
        }
        double dx = 0;
        if (input.isLeft() && !input.isRight()) {
            dx = -player.getSpeed();
        }
        if (input.isRight() && !input.isLeft()) {
            dx = player.getSpeed();
        }
        player.setFacingRight(dx > 0);
        player.setDx(dx);
        player.setMoving(dx != 0);
        if (input.isJump() && player.isOnGround()) {
            player.requestJump(player.getJumpPow());
        }
        handleUltimateInput(player);
        handleAttackInput(player, enemies);
        handleAttack(player, enemies);
    }

    private void handleUltimateInput(PlayerModel player) {
        if (input.isUltimate() && !player.isUltimate() && !player.isAtking() && player.isOnGround()) {
            if (player.isUltReady()) {
                if (player.useMana(player.getUltimateCost())) {
                    player.setUltimate(true);
                    player.setAniIndex(0);
                    player.setLastUltCastTime(System.currentTimeMillis());
                    Arrays.fill(ultFrameChecked, false);
                }
            }
        }
    }

    private void handleUltimateDamage(PlayerModel player, List<EnemyModel> enemies) {
        int frame = player.getAniIndex();
        Rectangle ultBox = player.getUltimateBox();
        boolean isHitFrame = false;
        for (int fr : ultHitFrames) {
            if (frame == fr) {
                isHitFrame = true;
                break;
            }
        }

        if (isHitFrame && !ultFrameChecked[frame]) {
            for (EnemyModel enemy : enemies) {
                if (enemy.isAlive() && ultBox.intersects(enemy.getHitbox())) {
                    enemy.takeDamage(player.getUltimateDamage());
                }
            }
            ultFrameChecked[frame] = true;
        }
    }

    private void handleAttackInput(PlayerModel player, List<EnemyModel> enemies) {
        long now = System.currentTimeMillis();
        if (input.isAttack() && !player.isAtking() && !player.isUltimate()
                && player.isNormalAtkReady()) {
            player.setAtking(true);
            player.setAniIndex(0);
            player.setLastNormalAttack(now);
        }
    }

    private void handleAttack(PlayerModel player, List<EnemyModel> enemies) {
        if (!player.isAtking())
            return;
        int frame = player.getAniIndex();
        Rectangle hb = player.getHitbox();
        int atkX = player.isFacingRight() ? hb.x + hb.width + player.getAtkOffset() : hb.x - player.getAtkW() - player.getAtkOffset();
        int atkY = hb.y + (hb.height - player.getAtkH()) / 2;
        Rectangle atkBox = new Rectangle(atkX, atkY, player.getAtkW(), player.getAtkH());
        player.setAttackBox(atkBox);
        if (frame == atkHitFrame) {
            for (EnemyModel enemy : enemies) {
                if (enemy.isAlive() && enemy.getAiState() != HURT && atkBox.intersects(enemy.getHitbox())) {
                    enemy.takeDamage(player.getDamage());
                    break;
                }
            }
        }
    }
}