package model.entity.enemy;

import model.entity.PlayerModel;

public class BossModel extends EnemyModel {
    // Bạn có thể thiết lập các thông số riêng cho Boss (máu trâu hơn, hit box lớn hơn...)
    public BossModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
    }

    @Override
    public void updateAi(PlayerModel player) {
        // ... Logic AI của Boss (tương tự hoặc phức tạp hơn SharkModel) ...
    }

    @Override
    public void refreshState() {
        // ... Cập nhật animation state cho Boss ...
    }
}