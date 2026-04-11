package controller;

import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;

public class WorldController {
    private MapModel map;
    private CameraModel camera;
    private PhysicsController physics;

    public WorldController(MapModel map, CameraModel camera) {
        this.map = map;
        this.camera = camera;
        physics = new PhysicsController();
    }

    public void update(PlayerModel player) {
        physics.update(player, map);
        player.refreshState();
        camera.update((int) player.getX(), map.getTileWide());
    }
}
