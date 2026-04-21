package controller.entity;

import java.util.Arrays;
import java.util.List;
import java.awt.Rectangle;

import controller.InputController;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;

import static core.GameConfig.*;
import static util.enemy.EnemyAIState.*;

public class PlayerController {
    private final InputController input;
    private final double speed = 2.0 * SCALE;
    private final double jumpForce = -4 * SCALE;

    private final int attackW = 28;
    private final int attackH = 20;
    private final int attackOffset = 6;
    private long lastNormalAttackMs = 0;
    private final long normalAttackCdMs = 500;
    private final int atkHitFrame = 3;
    private final int ultimateCost = 50;
    private final int ultHitFrame = 8;
    // --- CẤU HÌNH 3 HIT CHO ULTIMATE ---
    // Giả sử 3 nhịp chém rơi vào frame số 4, 8 và 12 (bạn tự chỉnh theo ảnh của bạn nhé)
    private final int[] ultHitFrames = {4, 9, 13}; 
    
    // Mảng đánh dấu frame đã gây sát thương chưa (kích thước 17 ứng với 17 frame của Ultimate)
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
            return; // Bỏ qua toàn bộ logic di chuyển/đánh thường bên dưới
        }
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

        // 3. LOGIC KỸ NĂNG
        handleUltimateInput(player);
        handleAttackInput(player, enemies);
        handleAttack(player, enemies);
    }

    // --- SỬA XỬ LÝ NHẤN PHÍM K (ULTIMATE) ---
    private void handleUltimateInput(PlayerModel player) {
        // Điều kiện: Nhấn phím K + không trên không + không đang bận cast chiêu khác
        if (input.isUltimate() && !player.isUltimate() && !player.isAtking() && player.isOnGround()) {

            // KIỂM TRA ĐIỀU KIỆN ĐỘC LẬP: MANA VÀ COOLDOWN
            if (player.isUltReady()) { // CD đã hồi xong
                if (player.useMana(ultimateCost)) { // Đủ mana
                    player.setUltimate(true);
                    player.setDx(0); // Đứng im
                    player.setAniIndex(0); // Reset frame để view vẽ từ đầu

                    // CẬP NHẬT THỜI ĐIỂM DÙNG KỸ NĂNG VÀO MODEL
                    player.setLastUltCastTime(System.currentTimeMillis());
                    Arrays.fill(ultFrameChecked, false);
                } 
            }
        }
    }

    private void handleUltimateDamage(PlayerModel player, List<EnemyModel> enemies) {
        int frame = player.getAniIndex();
        Rectangle ultBox = player.getUltimateBox();
        
        // 1. Kiểm tra xem frame hiện tại có phải là 1 trong 3 nhịp chém không?
        boolean isHitFrame = false;
        for (int f : ultHitFrames) {
            if (frame == f) {
                isHitFrame = true;
                break;
            }
        }

        // 2. Nếu là frame chém VÀ chưa được xét sát thương
        if (isHitFrame && !ultFrameChecked[frame]) {
            
            for (EnemyModel e : enemies) {
                // CHÚ Ý: Đã bỏ "e.getAiState() != HURT"
                // Việc bỏ đoạn check HURT này cho phép quái vật bị dính liên tiếp 3 nhát chém 
                // thay vì chỉ dính nhát đầu rồi "né" được 2 nhát sau nhờ hiệu ứng vô địch lúc bị thương.
                if (e.isAlive() && ultBox.intersects(e.getHitbox())) {
                    e.takeDamage(player.getUltimateDamage());
                }
            }
            
            // 3. Đánh dấu frame này đã xử lý xong, các tick sau của cùng frame này sẽ không bị trừ máu nữa
            ultFrameChecked[frame] = true;
        }
    }

    // --- SỬA XỬ LÝ NHẤN PHÍM J (ĐÁNH THƯỜNG) ---
    private void handleAttackInput(PlayerModel player, List<EnemyModel> enemies) {
        long now = System.currentTimeMillis();
        // Check CD riêng của đánh thường
        if (input.isAttack() && !player.isAtking() && !player.isUltimate()
                && (now - lastNormalAttackMs >= normalAttackCdMs)) {
            player.setAtking(true);
            player.setAniIndex(0); // Reset frame
            lastNormalAttackMs = now; // Cập nhật time đánh thường
        }
    }

    private void handleAttack(PlayerModel player, List<EnemyModel> enemies) {
        if (!player.isAtking())
            return;
        int frame = player.getAniIndex();
        Rectangle p = player.getHitbox();
        int ax = player.isFacingRight() ? p.x + p.width + attackOffset : p.x - attackW - attackOffset;
        int ay = p.y + (p.height - attackH) / 2;
        Rectangle atkBox = new Rectangle(ax, ay, attackW, attackH);
        player.setAttackBox(atkBox);
        if (frame == atkHitFrame) {
            for (EnemyModel e : enemies) {
                if (e.isAlive() && e.getAiState() != HURT && atkBox.intersects(e.getHitbox())) {
                    e.takeDamage(player.getDamage());
                    break;
                }
            }
        }
    }
}