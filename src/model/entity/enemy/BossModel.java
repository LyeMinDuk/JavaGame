// BossModel.java
package model.entity.enemy;

import model.entity.PlayerModel;

public abstract class BossModel extends EnemyModel {
    protected String bossName;

    public BossModel(double x, double y, int width, int height, int maxHealth, int damage, String bossName) {
        super(x, y, width, height, maxHealth, damage);
        this.bossName = bossName;
    }

    public abstract void refreshState();
}