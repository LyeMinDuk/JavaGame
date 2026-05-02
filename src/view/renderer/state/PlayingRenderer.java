package view.renderer.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import model.CameraModel;
import model.MapModel;
import model.QuestModel;
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
import view.renderer.hud.MinimapRenderer;
import view.renderer.object.SpikeRenderer;

import static core.GameConfig.*;
import static util.AssetsPath.mapTexture;
import static util.AssetsPath.playingBG;

public class PlayingRenderer {
    private BufferedImage[] bgLayers;
    private final double[] parallaxFactors = { 0.0, 0.15, 0.25, 0.4, 0.6, 0.8 };
    private CameraModel camera;
    private MapModel map;
    private PlayerModel player;

    private MapRenderer mapRenderer;
    private PlayerRenderer playerRenderer;
    private EnemyRenderer enemyRenderer;
    private HealthBarRenderer healthBarRenderer;
    private ManaBarRenderer manaBarRenderer;
    private MinimapRenderer minimapRenderer;
    private SpikeRenderer spikeRenderer;

    private List<EnemyModel> enemies;
    private List<SpikeModel> spikes;
    private QuestModel quest;

    public PlayingRenderer(CameraModel camera, MapModel map, PlayerModel player, int curMapIdx,
            List<EnemyModel> enemies, List<SpikeModel> spikes, QuestModel quest) {
        this.camera = camera;
        this.map = map;
        this.player = player;
        this.enemies = enemies;
        this.spikes = spikes;
        this.quest = quest;

        loadMapTexture();
        loadPlayerAnimation();
        initRenderer(curMapIdx);
        loadResource(curMapIdx);
    }

    private void loadResource(int levelIdx) {
        int frame = 0;
        switch (levelIdx) {
            case 0 -> frame = 6;
            case 1 -> frame = 6;
            case 2 -> frame = 6;
            case 3 -> frame = 5;
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

    private void initRenderer(int curMapIdx) {
        enemyRenderer = new EnemyManager();
        spikeRenderer = new SpikeRenderer();
        healthBarRenderer = new HealthBarRenderer();
        manaBarRenderer = new ManaBarRenderer();
        minimapRenderer = new MinimapRenderer(curMapIdx);
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

        playerRenderer.render(g, player, xOffset, yOffset);
        enemyRenderer.renderAll(g, enemies, xOffset, yOffset);

        healthBarRenderer.render(g, player);
        manaBarRenderer.render(g, player);
        minimapRenderer.render(g);
        renderQuest(g);
    }

    private void drawBackground(Graphics g, int xOffset) {
        for (int i = 0; i < bgLayers.length; ++i) {
            int xMove = (int) (xOffset * parallaxFactors[i]);
            int xStart = -(xMove % GAME_WIDTH);
            g.drawImage(bgLayers[i], xStart, 0, GAME_WIDTH, GAME_HEIGHT, null);
            g.drawImage(bgLayers[i], xStart + GAME_WIDTH, 0, GAME_WIDTH, GAME_HEIGHT, null);
        }
    }

    private void renderQuest(Graphics g) {
        Font old = g.getFont();
        g.setFont(old.deriveFont(Font.BOLD, 20));
        int questX = minimapRenderer.getDrawX();
        int questY = minimapRenderer.getDrawY() + minimapRenderer.getDrawHeight() + 25;
        g.setColor(Color.WHITE);
        g.drawString("Diệt quái (" + quest.getKilledEnemies() + "/" + quest.getTotalEnemies() + ")", questX, questY);
        g.drawString("Diệt boss (" + quest.getKilledBosses() + "/" + quest.getTotalBosses() + ")", questX, questY + 20);
    }
}
