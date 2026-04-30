package view.renderer.state;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import model.object.SpikeModel;
import view.assets.ResourceManager;
import view.renderer.MapRenderer;
import view.renderer.entity.enemy.EnemyManager;
import view.renderer.entity.enemy.EnemyRenderer;
import view.renderer.entity.PlayerRenderer;
import view.renderer.hud.HealthBarRenderer;
import view.renderer.hud.ManaBarRenderer;
import view.renderer.object.SpikeRenderer;

import static core.GameConfig.*;
import static util.AssetsPath.mapTexture;
import static util.AssetsPath.playingBG;

public class PlayingRenderer {
    private BufferedImage[] bgLayers;
    private final double[] parallaxFactors = { 0.0, 0.15, 0.3, 0.5, 0.7 };
    private CameraModel camera;
    private MapModel map;
    private PlayerModel player;

    private MapRenderer mapRenderer;
    private PlayerRenderer playerRenderer;
    private EnemyRenderer enemyRenderer;
    private HealthBarRenderer healthBarRenderer;
    private ManaBarRenderer manaBarRenderer;
    private SpikeRenderer spikeRenderer;

    private List<EnemyModel> enemies;
    private List<SpikeModel> spikes;

    public PlayingRenderer(CameraModel camera, MapModel map, PlayerModel player, int levelIdx, List<EnemyModel> enemies,
            List<SpikeModel> spikes) {
        this.camera = camera;
        this.map = map;
        this.player = player;
        this.enemies = enemies;
        this.spikes = spikes;

        loadMapTexture();
        loadPlayerAnimation();
        initRenderer();
        loadResource(levelIdx);
    }

    private void loadResource(int levelIdx) {
        int frame = 0;
        switch (levelIdx) {
            case 0 -> frame = 5;
            case 1 -> frame = 4;
            // case 2 -> frame = 5;
            // case 0 -> frame = 5;
        }
        bgLayers = new BufferedImage[frame];
        
        for (int i = 0; i < bgLayers.length; ++i) {
            bgLayers[i] = ResourceManager.loadImg(playingBG + (levelIdx + 1) + "/" + (i + 1) + ".png");
        }
    }

    public void loadMapTexture() {
        BufferedImage outside = ResourceManager.loadImg(mapTexture);

        BufferedImage[] tiles = new BufferedImage[48];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                tiles[i * 12 + j] = outside.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
        mapRenderer = new MapRenderer(tiles);
    }

    private void initRenderer() {
        enemyRenderer = new EnemyManager();
        spikeRenderer = new SpikeRenderer();
        healthBarRenderer = new HealthBarRenderer();
        manaBarRenderer = new ManaBarRenderer();
    }

    private void loadPlayerAnimation() {
        playerRenderer = new PlayerRenderer();
    }

    public void update() {
        playerRenderer.update(player);
        enemyRenderer.updateAll(enemies);
    }

    public void render(Graphics g) {
        int xOffset = camera.getOffsetX();
        int yOffset = camera.getOffsetY();
        drawBackground(g, xOffset);
        mapRenderer.render(g, map, xOffset, yOffset);
        spikeRenderer.render(g, spikes, xOffset, yOffset);
        enemyRenderer.renderAll(g, enemies, xOffset, yOffset);
        playerRenderer.render(g, player, xOffset, yOffset);
        healthBarRenderer.render(g, player);
        manaBarRenderer.render(g, player);
    }

    private void drawBackground(Graphics g, int xOffset) {
        for (int i = 0; i < bgLayers.length; ++i) {
            int xMove = (int) (xOffset * parallaxFactors[i]);
            int xStart = -(xMove % GAME_WIDTH);
            g.drawImage(bgLayers[i], xStart, 0, GAME_WIDTH, GAME_HEIGHT, null);
            g.drawImage(bgLayers[i], xStart + GAME_WIDTH, 0, GAME_WIDTH, GAME_HEIGHT, null);
        }
    }

}
