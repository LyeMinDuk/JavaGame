package controller.entity.enemy;

import java.util.ArrayList;
import java.util.List;

import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.DemonSlimeModel;
import model.entity.enemy.EnemyModel;
import model.entity.enemy.SharkModel;
import model.entity.enemy.SkeletonModel;

import static core.GameConfig.*;

public class EnemyController {
    private List<EnemyModel> listEnemy = new ArrayList<>();

    public EnemyController(MapModel map, int difficult) {
        spawnEnemy(map, difficult);
    }

    private void spawnEnemy(MapModel map, int difficult) {
        int normalHp, normalDmg, bossHp, bossDmg;
        switch (difficult) {
            case 0 -> {
                normalHp = 10;
                normalDmg = 1;
                bossHp = 100;
                bossDmg = 10;
            }
            case 2 -> {
                normalHp = 60;
                normalDmg = 10;
                bossHp = 1000;
                bossDmg = 20;
            }
            default -> {
                normalHp = 100;
                normalDmg = 100;
                bossHp = 3000;
                bossDmg = 1000;
            }
        }

        for (int[] spawn : map.getEnemySpawns()) {
            int tileX = spawn[0];
            int tileY = spawn[1];
            int type = spawn[2];
            switch (type) {
                case MapModel.ENEMY_TYPE_SHARK -> {
                    double x = tileX * TILE_SIZE;
                    double y = (tileY - 1) * TILE_SIZE;
                    listEnemy.add(new SharkModel(x, y, (int) (34 * SCALE), (int) (30 * SCALE), normalHp, normalDmg));
                }
                case MapModel.ENEMY_TYPE_SKELETON -> {
                    double x = tileX * TILE_SIZE;
                    double y = (tileY - 1) * TILE_SIZE;
                    listEnemy.add(new SkeletonModel(x, y, (int) (96 * SCALE), (int) (64 * SCALE), normalHp, normalDmg));
                }
                case MapModel.ENEMY_TYPE_DEMON_SLIME -> {
                    double x = tileX * TILE_SIZE;
                    double y = (tileY - 4) * TILE_SIZE; // boss cao hơn
                    listEnemy.add(new DemonSlimeModel(x, y, (int) (256 * SCALE), (int) (128 * SCALE), bossHp, bossDmg));
                }
            }
        }
    }

    public void updateAllEnemy(PlayerModel player) {
        for (EnemyModel enemy : listEnemy) {
            if (!enemy.isAlive())
                continue;
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