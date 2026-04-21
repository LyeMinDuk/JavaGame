package model.entity.enemy;

import java.awt.Rectangle;
import model.entity.PlayerModel;
import util.enemy.EnemyStateIndex.Shark;

import static core.GameConfig.*;
import static util.enemy.EnemyAIState.*;
import static util.enemy.EnemyStateIndex.*;

public class SharkModel extends EnemyModel {
    private final double detectRange = TILE_SIZE * 3;
    private final double attackRange = TILE_SIZE * 1;
    private final long attackCD = 1000;
    private long lastAttackTime = 0;
    private final int atkStartFrame = 3;
    private final int atkEndFrame = 4;

    public SharkModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
        this.moveSpeed = 0.5 * SCALE;
        this.patrolLeftX = x - (100 * SCALE);
        this.patrolRightX = x + (100 * SCALE);
        // this.setHitBox((int) (7 * SCALE), (int) (5 * SCALE), (int) (18 * SCALE), (int) (23 * SCALE));
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

        // --- CHÌA KHÓA: Cập nhật hướng nhìn TRƯỚC KHI tính toán ---
        // Để hàm getAttackBox() sinh ra vùng đánh ở đúng phía nó đang nhìn
        if (aiState != ATTACK) {
            facingRight = distX > 0;
        }

        // --- BƯỚC 1: KHÓA TRẠNG THÁI ATTACK ---
        if (aiState == ATTACK) {
            dx = 0;
            tryAttack(player);
            if (aniIndex > atkEndFrame) {
                aiState = IDLE;
            }
        }
        // --- BƯỚC 2: TÍNH TOÁN STATE (CHASE / PATROL) ---
        else {
            // Loại bỏ hoàn toàn attackRange áng chừng.
            // Dùng chính Box vật lý để kiểm tra xem "Cắn có tới không?"
            Rectangle simulatedAtkBox = getAttackBox();
            boolean canHitPlayer = simulatedAtkBox.intersects(playerBox);

            if (canHitPlayer && now - lastAttackTime >= attackCD) {
                // Chỉ ra đòn khi CHẮC CHẮN chạm tới mục tiêu
                aiState = ATTACK;
                dx = 0;
            } else if (absX <= detectRange) {
                // --- CHASE STATE ---
                double nextDx = distX > 0 ? moveSpeed : -moveSpeed;
                double nextX = x + nextDx;

                if (nextX < patrolLeftX || nextX > patrolRightX) {
                    // Chạm biên nhưng vẫn chưa tới tầm cắn -> Đứng im gầm gừ
                    dx = 0;
                    aiState = IDLE;
                } else {
                    // Còn trong biên -> Tiếp tục đuổi
                    aiState = CHASE;
                    dx = nextDx;
                }
            } else {
                // --- PATROL STATE ---
                patrol();
                // Khi tuần tra, cập nhật lại hướng mặt theo gia tốc dx
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
        if (aiState != ATTACK)
            return;
        long now = System.currentTimeMillis();
        int frame = aniIndex;
        if (now - lastAttackTime >= attackCD) {
            Rectangle atkBox = getAttackBox();
            if (frame >= atkStartFrame && frame <= atkEndFrame) {
                if (atkBox.intersects(player.getHitbox())) {
                    player.takeDamage(damage);
                }
                lastAttackTime = now;
            }
        }
    }

    public Rectangle getAttackBox() {
        Rectangle hb = getHitbox();
        int atkW = 28, atkH = 40, atkOffset = 5;
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