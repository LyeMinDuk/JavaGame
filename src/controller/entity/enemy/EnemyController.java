package controller.entity.enemy;

import static core.GameConfig.TILE_SIZE;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import model.entity.enemy.SharkModel;
import model.entity.enemy.SkeletonModel;

public class EnemyController {
    private List<EnemyModel> listEnemy = new ArrayList<>();

    public EnemyController(MapModel map) {
        spawnEnemy(map);
    }

    private void spawnEnemy(MapModel map) {
        for (Point point : map.getEnemyLocation()) {
            double enemyX = point.x * TILE_SIZE;
            double enemyY = (point.y - 1) * TILE_SIZE;

            listEnemy.add(new SharkModel(enemyX, enemyY, 34, 40, 50, 10));
        }
    }

    public void updateAllEnemy(PlayerModel player) {
        for (EnemyModel enemy : listEnemy) {
            if (!enemy.isAlive()) {
                continue;
            }
            enemy.updateAi(player);
        }
        removeDie();
    }

    public void removeDie() {
        for (int i = listEnemy.size() - 1; i >= 0; --i) {
            if (listEnemy.get(i).canRemove()) {
                listEnemy.remove(i);
            }
        }
    }

    public List<EnemyModel> getListEnemy() {
        return listEnemy;
    }
}
