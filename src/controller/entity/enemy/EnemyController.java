package controller.entity.enemy;

import static core.GameConfig.TILE_SIZE;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import model.entity.enemy.SkeletonModel;

public class EnemyController {
    private List<EnemyModel> listEnemy = new ArrayList<>();

    public EnemyController(MapModel map){
        spawnEnemy(map);
    }

    private void spawnEnemy(MapModel map) {
        for(Point point: map.getEnemyLocation()){
            double enemyX = point.x * TILE_SIZE;
            double enemyY = point.y * TILE_SIZE;

            listEnemy.add(new SkeletonModel(enemyX, enemyY, 96, 64, 50, 10));
        }
    }

    public void addEnemy(EnemyModel enemy) {
        listEnemy.add(enemy);
    }

    public void updateAllEnemy(PlayerModel player) {
        for (EnemyModel enemy : listEnemy) {
            enemy.updateAI(player);
        }
        removeDie();
    }

    public void removeDie() {
        for (int i = listEnemy.size() - 1; i >= 0; --i) {
            if (!listEnemy.get(i).isAlive()) {
                listEnemy.remove(i);
            }
        }
    }

    public List<EnemyModel> getListEnemy(){
        return listEnemy;
    }
}
