package controller;

import controller.entity.PlayerController;
import controller.entity.enemy.EnemyController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import model.state.GameState;
import model.state.GameStateModel;
import model.state.SettingsModel;

public class WorldController {
    private MapModel map;
    private CameraModel camera;
    private PlayerController playerController;
    private PhysicsController physics;
    private EnemyController enemyController;
    private GameStateModel gameState;

    public WorldController(MapModel map, CameraModel camera, PlayerController playerController,
            GameStateModel gameState, SettingsModel settingsModel) {
        this.map = map;
        this.camera = camera;
        this.playerController = playerController;
        this.gameState = gameState;
        this.enemyController = new EnemyController(map, settingsModel.getDifficult());
        this.physics = new PhysicsController(map, enemyController);
    }

    public void update(PlayerModel player) {
        if (gameState.getGameState() == GameState.PLAYING) {
            playerController.update(player, enemyController.getListEnemy());
            enemyController.updateAllEnemy(player);

            physics.apply(player);
            for (EnemyModel enemy : enemyController.getListEnemy()) {
                physics.apply(enemy);
            }
            player.refreshState();

            // --- THÊM LOGIC KHÓA CAMERA Ở ĐÂY ---
            if (map.getBossCheckpoint() != -1) {
                int barrierPixX = map.getBossCheckpoint() * core.GameConfig.TILE_SIZE;

                // Kiểm tra: Đã hết quái thường VÀ Player đã bước qua chốt chặn
                // Lưu ý: Nếu trong listEnemy của bạn có cả Boss, thì phải tự viết hàm
                // areNormalEnemiesCleared()
                // thay cho lệnh kiểm tra size() == 0 nhé!
                if (enemyController.getListEnemy().size() == 0 && player.getX() >= barrierPixX + core.GameConfig.GAME_WIDTH) {
                    // Khóa biên trái của Camera đúng bằng tọa độ của bức tường tàng hình
                    // Tùy vào thẩm mỹ, bạn có thể trừ đi một chút (ví dụ: barrierPixX - 100)
                    // để người chơi không bị đứng sát mép màn hình bên trái.
                    camera.setMinOffsetX(barrierPixX);
                }
            }

            // Camera update (nó sẽ tự động bị chặn bởi minOffsetX mới)
            camera.update((int) player.getX(), (int) player.getY(), map.getTileWide(), map.getTileHigh());
        }
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
