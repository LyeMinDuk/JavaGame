package controller;

import controller.entity.enemy.EnemyController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;

public class WorldController {
    private MapModel map;
    private CameraModel camera;
    private PhysicsController physics;
    private EnemyController enemyController;

    public WorldController(MapModel map, CameraModel camera) {
        this.map = map;
        this.camera = camera;
        this.physics = new PhysicsController(map);
        this.enemyController = new EnemyController(map);
    }

    public void update(PlayerModel player) {
        physics.updatePlayer(player);
        enemyController.updateAllEnemy(player);
        player.refreshState();
        camera.update((int) player.getX(), map.getTileWide());
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
