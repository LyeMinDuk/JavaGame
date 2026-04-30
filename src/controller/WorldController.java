package controller;

import controller.entity.EnemyController;
import controller.entity.PlayerController;
import controller.object.SpikeController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import model.state.GameState;
import model.state.GameStateModel;
import model.state.SettingsModel;

import static core.GameConfig.*;

public class WorldController {
    private MapModel map;
    private CameraModel camera;
    private PlayerController playerController;
    private PhysicsController physics;
    private EnemyController enemyController;
    private SpikeController spikeController;
    private GameStateModel gameState;

    public WorldController(MapModel map, CameraModel camera, PlayerController playerController,
            GameStateModel gameState, SettingsModel settingsModel) {
        this.map = map;
        this.camera = camera;
        this.playerController = playerController;
        this.gameState = gameState;
        this.enemyController = new EnemyController(map, settingsModel.getDifficult());
        spikeController = new SpikeController(map);
        this.physics = new PhysicsController(map, enemyController);
    }

    public void update(PlayerModel player, AudioController audio) {
        if (gameState.getGameState() == GameState.PLAYING) {
            playerController.update(player, enemyController.getListEnemy(), audio);
            enemyController.updateAllEnemy(player);
            physics.apply(player);
            spikeController.update(player);
            for (EnemyModel enemy : enemyController.getListEnemy()) {
                physics.apply(enemy);
            }
            player.refreshState();
            if (map.getBossCheckpoint() != -1) {
                int barrierPixX = map.getBossCheckpoint() * TILE_SIZE;
                if (enemyController.getListEnemy().size() == 1
                        && player.getX() >= barrierPixX + GAME_WIDTH) {
                    camera.setMinOffsetX(barrierPixX);
                }
            }
            camera.update((int) player.getX(), (int) player.getY(), map.getTileWide(), map.getTileHigh());
        }
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }

    public SpikeController getSpikeController() {
        return spikeController;
    }
}
