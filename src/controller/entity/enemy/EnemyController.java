package controller.entity.enemy;

import static core.GameConfig.SCALE;
import static core.GameConfig.TILE_SIZE;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.DemonSlimeModel;
import model.entity.enemy.EnemyModel;
import model.entity.enemy.SharkModel;
import model.entity.enemy.SkeletonModel;

public class EnemyController {
    private List<EnemyModel> listEnemy = new ArrayList<>();

    public EnemyController(MapModel map, int difficult) {
        spawnEnemy(map, difficult);
    }

    private void spawnEnemy(MapModel map, int difficult) {
        int enemyHp = 10;
        int enemyDamage = 1;

        switch (difficult) {
            case 0 -> { // Easy
                enemyHp = 10;
                enemyDamage = 1;
            }
            case 1 -> { // Medium
                enemyHp = 60;
                enemyDamage = 10;
            }
            case 2 -> { // Hard
                enemyHp = 100;
                enemyDamage = 100;
            }
        }
        for (Point point : map.getEnemyLocation()) {
            double enemyX = point.x * TILE_SIZE;
            double enemyY = (point.y - 1) * TILE_SIZE;

            listEnemy.add(new SharkModel(enemyX, enemyY, (int) (34 * SCALE), (int) (30 * SCALE), enemyHp, enemyDamage));
        }

        Point bossPoint = map.getBossLocation();
        if (bossPoint != null) {
            double bossX = bossPoint.x * TILE_SIZE;
            double bossY = (bossPoint.y - 4) * TILE_SIZE; // Trừ y nhiều hơn vì boss bự (cao 128px)

            listEnemy.add(new DemonSlimeModel(bossX, bossY, (int) (256 * SCALE), (int) (128 * SCALE), 1000, 50));
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
