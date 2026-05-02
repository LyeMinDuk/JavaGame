package controller.object;

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
        for (int[] p : map.getSpikeSpawns()) {
            int x = p[0] * TILE_SIZE;
            int y = p[1] * TILE_SIZE;
            listSpike.add(new SpikeModel(x, y, (int) (32 * SCALE), (int) (32 * SCALE), p[2]));
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
