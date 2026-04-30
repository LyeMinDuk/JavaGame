package model.entity.enemy;

import java.awt.Rectangle;
import model.entity.PlayerModel;

import static core.GameConfig.*;
import static util.enemy.EnemyAIState.*;
import static util.enemy.EnemyStateIndex.Skeleton;;

public class SkeletonModel extends EnemyModel {
    private final double detectRange = TILE_SIZE * 3;
    private final long atkCD = 1000;
    private long lastAtkTime = 0;
    private final int atkStartFrame = 5;
    private final int atkEndFrame = 7;

    public static final int WHITE = 0;
    public static final int YELLOW = 1;
    private final int type;

    public SkeletonModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
        this.moveSpeed = 0.7 * SCALE;
        this.type = Math.random() < 0.5 ? WHITE : YELLOW;
        this.patrolLeftX = x - (TILE_SIZE * 5);
        this.patrolRightX = x + (TILE_SIZE * 5);
        this.setHitBox((int) (33 * SCALE), (int) (18 * SCALE), (int) (24 * SCALE), (int) (46 * SCALE));
    }

    @Override
    public void updateAi(PlayerModel player) {
        long now = System.currentTimeMillis();
        if (aiState == DIE || (aiState == HURT && now < hurtUntil)) {
            dx = dy = 0;
            refreshState();
            return;
        }

        Rectangle playerBox = player.getHitbox();
        double centerPlayer = (playerBox.x + playerBox.width) / 2.0;
        double centerEnemy = (x + width) / 2.0;
        double distX = centerPlayer - centerEnemy;
        double absX = Math.abs(distX);

        if (aiState != ATTACK) {
            facingRight = distX > 0;
        }
        if (aiState == ATTACK) {
            if (aniIndex >= 9) {
                aiState = IDLE;
            } else {
                tryAttack(player);
                refreshState();
                return;
            }
        } else {
            Rectangle atkBox = getAttackBox();
            boolean canHitPlayer = atkBox.intersects(playerBox);
            if (canHitPlayer) {
                if (now - lastAtkTime >= atkCD) {
                    aiState = ATTACK;
                    dx = 0;
                } else {
                    dx = 0;
                    aiState = IDLE;
                }
            } else if (absX <= detectRange) {
                double nextDx = distX > 0 ? moveSpeed : -moveSpeed;
                double nextX = x + nextDx;
                if (nextX < patrolLeftX || nextX > patrolRightX) {
                    dx = 0;
                    aiState = IDLE;
                } else {
                    aiState = CHASE;
                    dx = nextDx;
                }
            } else {
                patrol();
                if (dx != 0) {
                    facingRight = dx > 0;
                }
            }
        }
        refreshState();
    }

    @Override
    public void refreshState() {
        switch (aiState) {
            case PATROL, CHASE -> aniState = Skeleton.RUN;
            case ATTACK -> aniState = Skeleton.ATTACK;
            case HURT -> aniState = Skeleton.HURT;
            case DIE -> aniState = Skeleton.DIE;
            default -> aniState = Skeleton.IDLE;
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
        if (aiState != ATTACK)
            return;
        long now = System.currentTimeMillis();
        int frame = aniIndex;
        if (now - lastAtkTime >= atkCD) {
            Rectangle atkBox = getAttackBox();
            if (frame >= atkStartFrame && frame <= atkEndFrame) {
                if (atkBox.intersects(player.getHitbox())) {
                    player.takeDamage(damage);
                }
                lastAtkTime = now;
            }
        }
    }

    @Override
    public Rectangle getAttackBox() {
        Rectangle hb = getHitbox();
        int atkW = (int) (32 * SCALE);
        int atkH = (int) (15 * SCALE);
        int atkOffset = (int) (2 * SCALE);
        int x = facingRight ? hb.x + hb.width + atkOffset : hb.x - atkW - atkOffset;
        int y = hb.y + hb.height / 2 - atkH / 2;
        return new Rectangle(x, y, atkW, atkH);
    }

    public long getLastAtkTime() {
        return lastAtkTime;
    }

    public void setLastAtkTime(long lastAtkTime) {
        this.lastAtkTime = lastAtkTime;
    }

    public int getType() {
        return type;
    }

}