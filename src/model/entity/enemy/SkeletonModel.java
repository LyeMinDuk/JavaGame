package model.entity.enemy;

import model.entity.PlayerModel;

import static core.GameConfig.TILE_SIZE;
import static util.enemy.EnemyAIState.*;

public class SkeletonModel extends EnemyModel {
    private double detectRange = TILE_SIZE * 3;
    private double attackRange = TILE_SIZE * 1;
    private long attackCD = 900;
    private long lastAttackTime = 0;

    public SkeletonModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
        this.moveSpeed = 0.8;
    }

    @Override
    public void updateAI(PlayerModel player) {
        if (!alive) {
            state = DIE;
            dx = dy = 0;
            return;
        }

        double distX = player.getX() - x;
        double absX = Math.abs(distX);

        if (distX >= 0) {
            facingRight = true;
        } else {
            facingRight = false;
        }

        if (absX <= attackRange) {
            state = ATTACK;
            dx = dy = 0;
            tryAttack(player);
        } else if (absX <= detectRange) {
            state = CHASE;
            dx = distX > 0 ? moveSpeed : -moveSpeed;
            dy = 0;
        } else {
            patrol();
        }

        move(dx, dy);
    }

    private void patrol() {
        state = PATROL;

        if (x <= patrolLeftX) {
            setFacingRight(true);
        } else if (x >= patrolRightX) {
            setFacingRight(false);
        }

        dx = isFacingRight() ? moveSpeed : -moveSpeed;
        dy = 0;
    }

    private void tryAttack(PlayerModel player) {
        long now = System.currentTimeMillis();
        if (now - lastAttackTime >= attackCD) {
            player.takeDamage(damage);
            lastAttackTime = now;
        }
    }
}