package view.renderer.entity;

import static view.renderer.entity.EntityRenderer.drawHB;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SharkModel;

public abstract class EnemyRenderer {

    public void updateAll(List<EnemyModel> listEnemy) {
        for (EnemyModel enemy : listEnemy) {
            update(enemy);
        }
    }

    protected abstract void update(EnemyModel enemy);

    public void renderAll(Graphics g, List<EnemyModel> listEnemy, int xOffset, int yOffset) {
        for (EnemyModel enemy : listEnemy) {
            Rectangle hitbox = enemy.getHitbox();
            int x = (int) Math.round(enemy.getX() - xOffset);
            int y = (int) Math.round(enemy.getY() - yOffset);
            if (enemy instanceof SharkModel shark) {
                Rectangle atkBox = shark.getAttackBox();
                g.drawRect(atkBox.x - xOffset, atkBox.y - yOffset, atkBox.width, atkBox.height);
            }
            drawHB(g, enemy, xOffset, yOffset);
            render(g, enemy, x, y);
            renderHealthBar(g, enemy, x, y);
        }
    }

    protected abstract void render(Graphics g, EnemyModel enemy, int x, int y);

    private void renderHealthBar(Graphics g, EnemyModel enemy, int x, int y) {
        int barW = enemy.getWidth();
        int hpW = (int) (barW * (enemy.getCurHealth() / (double) enemy.getMaxHealth()));
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y - 8, barW, 4);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 8, hpW, 4);
    }
}