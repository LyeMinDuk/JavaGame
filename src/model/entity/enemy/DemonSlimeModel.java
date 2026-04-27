package model.entity.enemy;

import java.awt.Rectangle;
import model.entity.PlayerModel;
import util.enemy.EnemyStateIndex.DemonSlime;

import static core.GameConfig.SCALE;
import static core.GameConfig.TILE_SIZE;
import static util.enemy.EnemyAIState.*;

public class DemonSlimeModel extends EnemyModel {
    private final int SIGHT_RANGE = 500;
    private final long ATTACK_COOLDOWN = 2000;
    private long lastAttackTime = 0;

    private final int atkStartFrame = 9;
    private final int atkEndFrame = 11;
    private boolean hasDealtDamage = false;

    // Deadzone đủ lớn để tránh flip liên tục khi player đứng gần tâm boss
    private final double FLIP_DEADZONE = TILE_SIZE * 0.5;

    public DemonSlimeModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
        this.aiState = IDLE;
        this.moveSpeed = 0.6 * SCALE;
        this.setHitBox((int) (81 * SCALE), (int) (52 * SCALE), (int) (70 * SCALE), (int) (75 * SCALE));
    }

    @Override
    public void refreshState() {
        switch (aiState) {
            case IDLE -> aniState = DemonSlime.IDLE;
            case CHASE, PATROL -> aniState = DemonSlime.RUN;
            case ATTACK -> aniState = DemonSlime.ATTACK;
            case HURT -> aniState = DemonSlime.HURT;
            case DIE -> aniState = DemonSlime.DIE;
        }
    }

    @Override
    public void updateAi(PlayerModel player) {
        if (aiState == DIE) {
            refreshState();
            return;
        }

        if (aiState == HURT) {
            if (System.currentTimeMillis() > hurtUntil) {
                aiState = IDLE;
            }
            dx = 0;
            refreshState();
            return;
        }

        Rectangle playerBox = player.getHitbox();
        double centerPlayer = playerBox.x + playerBox.width / 2.0;
        double centerEnemy = getHitbox().x + getHitbox().width / 2.0;
        double distX = centerPlayer - centerEnemy;
        double absX = Math.abs(distX);

        // FIX: chỉ đổi hướng khi khoảng cách đủ lớn (deadzone) VÀ không đang tấn công
        if (aiState != ATTACK && absX > FLIP_DEADZONE) {
            facingRight = distX > 0;
        }

        if (aiState == ATTACK) {
            dx = 0;
            tryAttack(player);
            // Khi animation ATTACK chạy xong (frame cuối), về IDLE
            // DemonSlimeRenderer set loop=false nên aniIndex sẽ dừng ở frame cuối
            if (aniIndex >= DEMON_SLIME_ATK_LAST_FRAME()) {
                aiState = IDLE;
            }
        } else {
            Rectangle simulatedAtkBox = getAttackBox();
            boolean canHitPlayer = simulatedAtkBox.intersects(playerBox);

            if (canHitPlayer) {
                if (System.currentTimeMillis() - lastAttackTime > ATTACK_COOLDOWN) {
                    aiState = ATTACK;
                    lastAttackTime = System.currentTimeMillis();
                    hasDealtDamage = false; // FIX: bỏ comment, reset đúng chỗ
                    dx = 0;
                } else {
                    aiState = IDLE;
                    dx = 0;
                }
            } else if (absX <= SIGHT_RANGE) {
                aiState = CHASE;
                dx = distX > 0 ? moveSpeed : -moveSpeed;
                this.x += dx;
            } else {
                aiState = IDLE;
                dx = 0;
            }
        }

        syncHitbox();
        refreshState();
    }

    // Frame cuối của animation ATTACK (15 frames, index 0-14)
    private int DEMON_SLIME_ATK_LAST_FRAME() {
        return 14;
    }

    private void tryAttack(PlayerModel player) {
        if (!hasDealtDamage && aniIndex >= atkStartFrame && aniIndex <= atkEndFrame) {
            Rectangle atkBox = getAttackBox();
            if (atkBox.intersects(player.getHitbox())) {
                player.takeDamage(damage);
                hasDealtDamage = true;
            }
        }
    }

    @Override
    public Rectangle getAttackBox() {
        Rectangle hb = getHitbox();
        int atkW = 100;
        int atkH = 100;
        int atkOffset = 20;
        int ax = facingRight ? hb.x + hb.width + atkOffset : hb.x - atkW - atkOffset;
        int ay = hb.y + (hb.height - atkH);
        return new Rectangle(ax, ay, atkW, atkH);
    }
}