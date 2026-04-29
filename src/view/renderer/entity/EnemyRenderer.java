package view.renderer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import model.entity.enemy.EnemyModel;

import static view.renderer.entity.EntityRenderer.drawHB;

public abstract class EnemyRenderer {
    protected static final boolean debug = true;

    public void updateAll(List<EnemyModel> listEnemy) {
        for (EnemyModel enemy : listEnemy) {
            update(enemy);
        }
    }

    protected abstract void update(EnemyModel enemy);

    public void renderAll(Graphics g, List<EnemyModel> listEnemy, int xOffset, int yOffset) {
        for (EnemyModel enemy : listEnemy) {
            int x = (int) Math.round(enemy.getX() - xOffset);
            int y = (int) Math.round(enemy.getY() - yOffset);

            render(g, enemy, x, y);
            renderHealthBar(g, enemy, x, y);

            if (debug) {
                Rectangle atkBox = enemy.getAttackBox();
                g.drawRect(atkBox.x - xOffset, atkBox.y - yOffset, atkBox.width, atkBox.height);
                drawHB(g, enemy, xOffset, yOffset);
            }
        }
    }

    protected abstract void render(Graphics g, EnemyModel enemy, int x, int y);

    private void renderHealthBar(Graphics g, EnemyModel enemy, int x, int y) {
        int barW = enemy.getWidth();
        int hpW = (int) (barW * (enemy.getCurHealth() / (double) enemy.getMaxHealth()));
        int pad = (int) (4 * core.GameConfig.SCALE);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y - pad, barW, 4);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - pad, hpW, 4);
    }
}