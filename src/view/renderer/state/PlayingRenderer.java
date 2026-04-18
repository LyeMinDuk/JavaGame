package view.renderer.state;

import static core.GameConfig.*;
import static util.AssetsPath.playingBG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.entity.enemy.EnemyController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import view.assets.ResourceManager;
import view.renderer.MapRenderer;
import view.renderer.entity.EnemyRenderer;
import view.renderer.entity.PlayerRenderer;
import view.renderer.entity.enemy.SharkRenderer;
import view.renderer.hud.HealthBarRenderer;

public class PlayingRenderer {
    private BufferedImage backgroundImg;
    private CameraModel camera;
    private MapModel map;
    private PlayerModel player;
    private MapRenderer mapRenderer;
    private PlayerRenderer playerRenderer;
    private EnemyRenderer enemyRenderer;
    private EnemyController enemyController;
    private HealthBarRenderer healthBarRenderer;

    public PlayingRenderer(CameraModel camera, MapModel map, PlayerModel player, EnemyController enemyController) {
        this.camera = camera;
        this.map = map;
        this.player = player;
        this.enemyController = enemyController;
        loadMapTexture();

        loadPlayerAnimation();
        initEnemyRenderer();
        healthBarRenderer = new HealthBarRenderer();
        loadResource();
    }

    private void loadResource() {
        backgroundImg = ResourceManager.loadImg(playingBG);
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

    private void initEnemyRenderer() {
        enemyRenderer = new SharkRenderer();
    }

    private void loadPlayerAnimation() {
        playerRenderer = new PlayerRenderer();
    }

    public void update() {
        playerRenderer.update(player);
        enemyRenderer.updateAll(enemyController.getListEnemy());
    }

    public void render(Graphics g) {
        drawBackground(g);
        int xOffset = camera.getXOffset();
        mapRenderer.render(g, map, xOffset);
        playerRenderer.render(g, player, xOffset);
        enemyRenderer.renderAll(g, enemyController.getListEnemy(), xOffset);
        healthBarRenderer.render(g, player);
    }

    private void drawBackground(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        // g.setColor(new Color(0, 0, 0, 80));
        // g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

}
