package view.renderer.entity.enemy;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.GolemModel;
import model.entity.enemy.SharkModel;
import model.entity.enemy.SkeletonModel;
import model.entity.enemy.boss.CthuluModel;
import model.entity.enemy.boss.DemonSlimeModel;
import model.entity.enemy.boss.FrostGuardianModel;
import model.entity.enemy.boss.MinotaurModel;
import view.renderer.entity.enemy.boss.CthuluRenderer;
import view.renderer.entity.enemy.boss.DemonSlimeRenderer;
import view.renderer.entity.enemy.boss.FrostGuardianRenderer;
import view.renderer.entity.enemy.boss.MinotaurRenderer;

public class EnemyManager extends EnemyRenderer {
    private Map<Class<?>, EnemyRenderer> renderers = new HashMap<>();

    public EnemyManager() {
        renderers.put(SharkModel.class, new SharkRenderer());
        renderers.put(SkeletonModel.class, new SkeletonRenderer());
        renderers.put(DemonSlimeModel.class, new DemonSlimeRenderer());
        renderers.put(MinotaurModel.class, new MinotaurRenderer());
        renderers.put(CthuluModel.class, new CthuluRenderer());
        renderers.put(FrostGuardianModel.class, new FrostGuardianRenderer());
        renderers.put(GolemModel.class, new GolemRenderer());
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