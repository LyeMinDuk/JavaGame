package view.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.entity.enemy.EnemyController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import view.assets.ResourceManager;
import view.renderer.entity.EnemyRenderer;
import view.renderer.entity.PlayerRenderer;
import view.renderer.entity.enemy.SharkRenderer;
import view.renderer.entity.enemy.SkeletonRenderer;
import view.renderer.hud.HealthBarRenderer;

public class GameRenderer {
    private MapModel map;
    private PlayerModel player;
    private CameraModel camera;

    private MapRenderer mapRenderer;
    private PlayerRenderer playerRenderer;
    private EnemyRenderer enemyRenderer;
    private HealthBarRenderer healthBarRenderer;
    private EnemyController enemyController;

    public GameRenderer(MapModel map, PlayerModel player, CameraModel camera, EnemyController enemyController) {
        this.map = map;
        this.player = player;
        this.camera = camera;
        this.enemyController = enemyController;
        healthBarRenderer = new HealthBarRenderer();
        loadMapTexture();

        loadPlayerAnimation();

        enemyRenderer = new SharkRenderer();
    }

    private void loadPlayerAnimation() {
        playerRenderer = new PlayerRenderer();
    }

    public void loadMapTexture() {
        BufferedImage outside = ResourceManager.loadImg("/outside_sprites.png");

        BufferedImage[] tiles = new BufferedImage[48];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                tiles[i * 12 + j] = outside.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
        mapRenderer = new MapRenderer(tiles);
    }

    public void update() {
        playerRenderer.update(player);
        enemyRenderer.updateAll(enemyController.getListEnemy());
    }

    public void render(Graphics g) {
        int xOffset = camera.getXOffset();
        mapRenderer.render(g, map, xOffset);
        playerRenderer.render(g, player, xOffset);
        enemyRenderer.renderAll(g, enemyController.getListEnemy(), xOffset);
        healthBarRenderer.render(g, player);
    }
}
