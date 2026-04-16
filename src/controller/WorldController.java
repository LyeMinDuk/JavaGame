package controller;

import controller.entity.PlayerController;
import controller.entity.enemy.EnemyController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;

public class WorldController {
    private MapModel map;
    private CameraModel camera;
    private PlayerController playerController;
    private PhysicsController physics;
    private EnemyController enemyController;

    public WorldController(MapModel map, CameraModel camera, PlayerController playerController) {
        this.map = map;
        this.camera = camera;
        this.playerController = playerController;
        this.physics = new PhysicsController(map);
        this.enemyController = new EnemyController(map);
    }

    public void update(PlayerModel player) {
        playerController.update(player, enemyController.getListEnemy());
        enemyController.updateAllEnemy(player);

        physics.apply(player);
        for (EnemyModel enemy : enemyController.getListEnemy()) {
            physics.apply(enemy);
        }
        player.refreshState();
        camera.update((int) player.getX(), map.getTileWide());
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
