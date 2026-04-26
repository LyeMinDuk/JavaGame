package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SharkModel;
import model.entity.enemy.SkeletonModel;
import model.entity.enemy.DemonSlimeModel; 
import view.renderer.entity.EnemyRenderer;

// THAY ĐỔI: Kế thừa EnemyRenderer
public class EnemyManager extends EnemyRenderer {
    // Khai báo các Renderer cụ thể
    private SharkRenderer sharkRenderer;
    private SkeletonRenderer skeletonRenderer;
    private DemonSlimeRenderer demonSlimeRenderer;

    public EnemyManager() {
        sharkRenderer = new SharkRenderer();
        skeletonRenderer = new SkeletonRenderer();
        demonSlimeRenderer = new DemonSlimeRenderer();
    }

    @Override
    protected void update(EnemyModel enemy) {
        // Phân loại quái để chuyển cho đúng Renderer xử lý update
        if (enemy instanceof SharkModel) {
            sharkRenderer.update(enemy);
        } else if (enemy instanceof SkeletonModel) {
            skeletonRenderer.update(enemy);
        } else if (enemy instanceof DemonSlimeModel) {
            demonSlimeRenderer.update(enemy);
        }
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        // Phân loại quái để chuyển cho đúng Renderer xử lý render ảnh
        if (enemy instanceof SharkModel) {
            sharkRenderer.render(g, enemy, x, y);
        } else if (enemy instanceof SkeletonModel) {
            skeletonRenderer.render(g, enemy, x, y);
        } else if (enemy instanceof DemonSlimeModel) {
            demonSlimeRenderer.render(g, enemy, x, y);
        }
    }
}