// DemonSlimeModel.java
package model.entity.enemy;

import java.awt.Rectangle;
import model.entity.PlayerModel;
import util.enemy.EnemyStateIndex.DemonSlime;

import static core.GameConfig.SCALE;
import static util.enemy.EnemyAIState.*;
import static util.enemy.EnemyStateIndex.DemonSlime;

public class DemonSlimeModel extends BossModel {
    private final int SIGHT_RANGE = 500;
    private final int ATTACK_RANGE = 120; // Tầm đánh xa hơn bình thường vì Boss to
    private long lastAttackTime = 0;
    private final long ATTACK_COOLDOWN = 2000; 

    // --- CẤU HÌNH ATTACK ---
    // Animation có 15 frame (từ 0 đến 14). Đòn chém giáng xuống ở khoảng frame 11-13
    private final int atkStartFrame = 11;
    private final int atkEndFrame = 13;
    private boolean hasDealtDamage = false; // Cờ đánh dấu để sát thương không bị trừ liên tục

    public DemonSlimeModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage, "Demon Slime");
        this.aiState = IDLE;
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
            refreshState();
            return;
        }

        // CHÌA KHÓA 1: Không đổi hướng mặt khi ĐANG ĐÁNH (tránh lỗi moonwalk trượt băng)
        if (aiState != ATTACK) {
            facingRight = (player.getX() >= this.x);
        }

        double dist = Math.abs(player.getX() - this.x);

        // CHÌA KHÓA 2: KHÓA TRẠNG THÁI (STATE LOCK)
        if (aiState == ATTACK) {
            // Boss phải đứng im khi ra chiêu
            dx = 0; 
            
            // Xử lý logic sát thương
            tryAttack(player);

            // Kiểm tra xem animation đã chạy đến frame cuối cùng chưa (15 frame -> index 14)
            if (aniIndex >= 14) {
                aiState = IDLE; // Chém xong mới cho phép đổi trạng thái
            }
        } 
        // BƯỚC 3: CÁC TRẠNG THÁI KHÁC
        else {
            if (dist <= ATTACK_RANGE && System.currentTimeMillis() - lastAttackTime > ATTACK_COOLDOWN) {
                aiState = ATTACK;
                lastAttackTime = System.currentTimeMillis();
                hasDealtDamage = false; // Reset cờ sát thương cho cú chém mới
                dx = 0;
            } 
            else if (dist <= SIGHT_RANGE) {
                aiState = CHASE;
                this.x += facingRight ? moveSpeed : -moveSpeed;
            } 
            else {
                aiState = IDLE;
            }
        }

        syncHitbox();
        refreshState();
    }

    // --- LOGIC GÂY SÁT THƯƠNG ---
    private void tryAttack(PlayerModel player) {
        // Chỉ xét gây sát thương khi: Chưa gây dame VÀ đang ở đúng frame vung rìu xuống
        if (!hasDealtDamage && aniIndex >= atkStartFrame && aniIndex <= atkEndFrame) {
            Rectangle atkBox = getAttackBox();
            if (atkBox.intersects(player.getHitbox())) {
                player.takeDamage(damage);
                hasDealtDamage = true; // Bật cờ lên để các frame sau không trừ máu nữa
            }
        }
    }

    // --- TÍNH TOÁN VÙNG ĐÁNH (ATTACK BOX) ---
    public Rectangle getAttackBox() {
        Rectangle hb = getHitbox();
        // Vùng sát thương của Boss to hơn quái thường
        int atkW = 100; 
        int atkH = 100;
        int atkOffset = 20;

        // Nếu quay phải thì vẽ bục bên phải, quay trái thì đẩy tọa độ sang trái
        int ax = facingRight ? hb.x + hb.width + atkOffset : hb.x - atkW - atkOffset;
        int ay = hb.y + (hb.height - atkH); // Canh ở nửa dưới của Boss

        return new Rectangle(ax, ay, atkW, atkH);
    }
}