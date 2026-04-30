package view.renderer.entity.enemy;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.MinotaurModel;
import model.entity.enemy.SharkModel;
import model.entity.enemy.SkeletonModel;
import model.entity.enemy.DemonSlimeModel;

public class EnemyManager extends EnemyRenderer {
    private Map<Class<? extends EnemyModel>, EnemyRenderer> renderers = new HashMap<>();

    public EnemyManager() {
        renderers.put(SharkModel.class, new SharkRenderer());
        renderers.put(SkeletonModel.class, new SkeletonRenderer());
        renderers.put(DemonSlimeModel.class, new DemonSlimeRenderer());
        renderers.put(MinotaurModel.class, new MinotaurRenderer());
    }

    @Override
    protected void update(EnemyModel enemy) {
        EnemyRenderer r = renderers.get(enemy.getClass());
        r.update(enemy);
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        EnemyRenderer r = renderers.get(enemy.getClass());
        r.render(g, enemy, x, y);
    }
}