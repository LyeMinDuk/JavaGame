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
            if (map.getBossCheckpoint() != -1) {
                int barrierPixX = map.getBossCheckpoint() * core.GameConfig.TILE_SIZE;
                if (enemyController.getListEnemy().size() == 1
                        && player.getX() >= barrierPixX + core.GameConfig.GAME_WIDTH) {
                    camera.setMinOffsetX(barrierPixX);
                }
            }
            camera.update((int) player.getX(), (int) player.getY(), map.getTileWide(), map.getTileHigh());
        }
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
