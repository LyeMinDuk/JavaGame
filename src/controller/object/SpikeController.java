package controller.object;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.MapModel;
import model.entity.PlayerModel;
import model.object.SpikeModel;

import static core.GameConfig.SCALE;
import static core.GameConfig.TILE_SIZE;

public class SpikeController {
    private List<SpikeModel> listSpike = new ArrayList<>();

    public SpikeController(MapModel map) {
        spawnObject(map);
    }

    private void spawnObject(MapModel map) {
        for (Point p : map.getSpikeSpawns()) {
            int x = p.x * TILE_SIZE;
            int y = p.y * TILE_SIZE;
            listSpike.add(new SpikeModel(x, y, (int) (32 * SCALE), (int) (32 * SCALE)));
        }
    }

    public void update(PlayerModel player) {
        for (SpikeModel spike : listSpike) {
            if(spike.getHitbox().intersects(player.getHitbox())){
                player.takeDamage(player.getMaxHealth());
            }
        }
    }

    public List<SpikeModel> getListSpike() {
        return listSpike;
    }

}
