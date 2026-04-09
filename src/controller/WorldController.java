package controller;

import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;

public class WorldController {
    private MapModel map;
    private CameraModel camera;

    public WorldController(MapModel map, CameraModel camera) {
        this.map = map;
        this.camera = camera;
    }

    public void update(PlayerModel player) {
        player.move((int) player.getDx(), (int) player.getDy());
        player.refreshState();
        camera.update((int) player.getX(), map.getTileWide());
    }
}
