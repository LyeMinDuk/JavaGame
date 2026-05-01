package model;

import java.util.List;
import model.entity.enemy.EnemyModel;

public class QuestModel {
    private int totalEnemies;
    private int totalBosses;
    private int killedEnemies;
    private int killedBosses;

    public void initTotals(List<EnemyModel> enemies) {
        totalEnemies = 0;
        totalBosses = 0;
        killedEnemies = 0;
        killedBosses = 0;
        for (EnemyModel e : enemies) {
            if (e.isBoss()) {
                totalBosses++;
            } else {
                totalEnemies++;
            }
        }
    }

    public void onEnemyRemoved(EnemyModel enemy) {
        if (enemy.isBoss()) {
            killedBosses++;
        } else {
            killedEnemies++;
        }
    }

    public int getTotalEnemies() {
        return totalEnemies;
    }

    public int getTotalBosses() {
        return totalBosses;
    }

    public int getKilledEnemies() {
        return killedEnemies;
    }

    public int getKilledBosses() {
        return killedBosses;
    }
}