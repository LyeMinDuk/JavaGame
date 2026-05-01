package controller.entity;

import java.util.ArrayList;
import java.util.List;

import model.MapModel;
import model.QuestModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import model.entity.enemy.GolemModel;
import model.entity.enemy.SharkModel;
import model.entity.enemy.SkeletonModel;
import model.entity.enemy.boss.CthuluModel;
import model.entity.enemy.boss.DemonSlimeModel;
import model.entity.enemy.boss.FrostGuardianModel;
import model.entity.enemy.boss.MinotaurModel;
import util.enemy.EnemyStateIndex.Golem;

import static core.GameConfig.*;

public class EnemyController {
    private QuestModel quest;
    private List<EnemyModel> listEnemy = new ArrayList<>();

    public EnemyController(MapModel map, int difficult, QuestModel quest) {
        this.quest = quest;
        spawnEnemy(map, difficult);
        quest.initTotals(listEnemy);
    }

    private void spawnEnemy(MapModel map, int difficult) {
        int normalHp, normalDmg, bossHp, bossDmg;
        switch (difficult) {
            case 0 -> {
                normalHp = 20;
                normalDmg = 1;
                bossHp = 100;
                bossDmg = 10;
            }
            case 1 -> {
                normalHp = 80;
                normalDmg = 10;
                bossHp = 1000;
                bossDmg = 20;
            }
            case 2 -> {
                normalHp = 100;
                normalDmg = 100;
                bossHp = 3000;
                bossDmg = 1000;
            }
            default -> {
                normalHp = 80;
                normalDmg = 10;
                bossHp = 1000;
                bossDmg = 20;
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
                    listEnemy.add(new SkeletonModel(x, y, (int) (96 * SCALE), (int) (64 * SCALE), normalHp - 10,
                            normalDmg + 20));
                }
                case MapModel.ENEMY_TYPE_DEMON_SLIME -> {
                    double x = tileX * TILE_SIZE;
                    double y = (tileY - 4) * TILE_SIZE;
                    listEnemy.add(new DemonSlimeModel(x, y, (int) (256 * SCALE), (int) (128 * SCALE), bossHp, bossDmg));
                }
                case MapModel.ENEMY_TYPE_MINOTAUR -> {
                    double x = tileX * TILE_SIZE;
                    double y = (tileY - 4) * TILE_SIZE;
                    listEnemy.add(new MinotaurModel(x, y, (int) (256 * SCALE), (int) (128 * SCALE), bossHp, bossDmg));
                }
                case MapModel.ENEMY_TYPE_FROST_GUARDIAN -> {
                    double x = tileX * TILE_SIZE;
                    double y = (tileY - 4) * TILE_SIZE;
                    listEnemy.add(
                            new FrostGuardianModel(x, y, (int) (192 * SCALE), (int) (128 * SCALE), bossHp, bossDmg));
                }
                case MapModel.ENEMY_TYPE_CTHULU -> {
                    double x = tileX * TILE_SIZE;
                    double y = (tileY - 4) * TILE_SIZE;
                    listEnemy.add(new CthuluModel(x, y, (int) (192 * SCALE), (int) (112 * SCALE), bossHp, bossDmg));
                }
                case MapModel.ENEMY_TYPE_GOLEM -> {
                    double x = tileX * TILE_SIZE;
                    double y = (tileY - 1) * TILE_SIZE;
                    listEnemy.add(new GolemModel(x, y, (int) (90 * SCALE), (int) (64 * SCALE), normalHp + 30,
                            normalDmg - 20));
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
                quest.onEnemyRemoved(listEnemy.get(i));
                listEnemy.remove(i);
            }
        }
    }

    public List<EnemyModel> getListEnemy() {
        return listEnemy;
    }
}