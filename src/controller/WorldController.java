package controller;

import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;

public class WorldController {
    private MapModel map;
    private CameraModel camera;
    private final PhysicsController physics;

    public WorldController(MapModel map, CameraModel camera) {
        this.map = map;
        this.camera = camera;
        this.physics = new PhysicsController(map);
    }

    public void update(PlayerModel player) {
        physics.updatePlayer(player);
        player.refreshState();
        camera.update((int) player.getX(), map.getTileWide());
    }
}
