package model.entity.enemy;

import java.awt.Rectangle;
import model.entity.PlayerModel;

import static core.GameConfig.*;
import static util.enemy.EnemyAIState.*;
import static util.enemy.EnemyStateIndex.*;

public class SharkModel extends EnemyModel {
    private final double detectRange = TILE_SIZE * 3;
    private final double attackRange = TILE_SIZE * 1.5;
    private final long attackCD = 2000;
    private long lastAttackTime = 0;

    public SharkModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
        this.moveSpeed = 0.5 * SCALE;
        this.patrolLeftX = x - (100 * SCALE);
        this.patrolRightX = x + (100 * SCALE);
    }

    @Override
    public void updateAi(PlayerModel player) {
        long now = System.currentTimeMillis();
        if (aiState == DIE) {
            dx = dy = 0;
            refreshState();
            return;
        }
        if (aiState == HURT && now < hurtUntil) {
            dx = 0;
            refreshState();
            return;
        }
        if (curHealth <= 0) {
            aiState = DIE;
            dx = dy = 0;
            return;
        }
        double distX = player.getX() - x;
        double absX = Math.abs(distX);
        if (absX <= attackRange) {
            aiState = ATTACK;
            dx = 0;
            if (aiState == ATTACK && aiState != HURT && aiState != DIE) {
                tryAttack(player);
            }
        } else if (absX <= detectRange) {
            aiState = CHASE;
            double nextDx = distX > 0 ? moveSpeed : -moveSpeed;
            double nextX = x + nextDx;
            if (nextX < patrolLeftX || nextX > patrolRightX) {
                patrol();
            } else {
                dx = nextDx;
            }
        } else {
            patrol();
        }
        if (dx != 0) {
            facingRight = dx > 0;
        }
        refreshState();
    }

    @Override
    public void refreshState() {
        switch (aiState) {
            case PATROL, CHASE -> aniState = Shark.RUN;
            case ATTACK -> aniState = Shark.ATTACK;
            case HURT -> aniState = Shark.HURT;
            case DIE -> aniState = Shark.DIE;
            default -> aniState = Shark.IDLE;
        }
    }

    private void patrol() {
        aiState = PATROL;
        if (x <= patrolLeftX) {
            dx = moveSpeed;
        } else if (x >= patrolRightX) {
            dx = -moveSpeed;
        }
        if (dx == 0) {
            dx = moveSpeed;
        }
    }

    private void tryAttack(PlayerModel player) {
        long now = System.currentTimeMillis();
        if (now - lastAttackTime >= attackCD) {
            Rectangle atkBox = getAttackBox();
            if (atkBox.intersects(player.getHitbox())) {
                player.takeDamage(damage);
            }
            lastAttackTime = now;
        }
    }

    public Rectangle getAttackBox() {
        Rectangle hb = getHitbox();
        int atkW = 28, atkH = 40, atkOffset = 15;
        int x = facingRight ? hb.x + hb.width + atkOffset : hb.x - atkW - atkOffset;
        int y = hb.y + hb.height / 2 - atkH / 2;
        return new Rectangle(x, y, atkW, atkH);
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

}