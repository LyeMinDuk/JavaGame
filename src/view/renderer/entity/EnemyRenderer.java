package view.renderer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import model.entity.enemy.EnemyModel;

public abstract class EnemyRenderer {

    public void renderAll(Graphics g, List<EnemyModel> listEnemy, int xOffset){
        for (EnemyModel enemy : listEnemy) {
            int x = (int) enemy.getX() - xOffset;
            int y = (int) enemy.getY();

            render(g, enemy, x, y);
            renderHealthBar(g, enemy, x, y);
        }
    }

    protected abstract void render(Graphics g, EnemyModel enemy, int x, int y);

    private void renderHealthBar(Graphics g, EnemyModel enemy, int x, int y){
            int barW = enemy.getWidth();
            int hpW = (int) (barW * (enemy.getHealth() / (double) enemy.getMaxHealth()));
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y - 8, barW, 4);
            g.setColor(Color.GREEN);
            g.fillRect(x, y - 8, hpW, 4);
    }
}