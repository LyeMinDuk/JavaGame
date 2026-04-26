package view.renderer.state;

import static core.GameConfig.*;
import static util.AssetsPath.mapTexture;
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
import view.renderer.entity.enemy.EnemyManager;
import view.renderer.entity.PlayerRenderer;
import view.renderer.entity.enemy.SharkRenderer;
import view.renderer.hud.HealthBarRenderer;
import view.renderer.hud.ManaBarRenderer;

public class PlayingRenderer {
    private BufferedImage[] bgLayers;
    private final double[] parallaxFactors = {0.0, 0.15, 0.3, 0.5, 0.7};
    private CameraModel camera;
    private MapModel map;
    private PlayerModel player;
    private MapRenderer mapRenderer;
    private PlayerRenderer playerRenderer;
    private EnemyRenderer enemyRenderer;
    private EnemyController enemyController;
    private HealthBarRenderer healthBarRenderer;
    private ManaBarRenderer manaBarRenderer;

    public PlayingRenderer(CameraModel camera, MapModel map, PlayerModel player, EnemyController enemyController) {
        this.camera = camera;
        this.map = map;
        this.player = player;
        this.enemyController = enemyController;
        loadMapTexture();

        loadPlayerAnimation();
        initEnemyRenderer();
        healthBarRenderer = new HealthBarRenderer();
        manaBarRenderer = new ManaBarRenderer();
        loadResource();
    }

    private void loadResource() {
        bgLayers = new BufferedImage[5];

        for(int i = 0; i < bgLayers.length; ++i){
            bgLayers[i] = ResourceManager.loadImg(playingBG + (i + 1) + ".png");
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

    private void initEnemyRenderer() {
        enemyRenderer = new EnemyManager();
    }

    private void loadPlayerAnimation() {
        playerRenderer = new PlayerRenderer();
    }

    public void update() {
        playerRenderer.update(player);
        enemyRenderer.updateAll(enemyController.getListEnemy());
    }

    public void render(Graphics g) {
        int xOffset = camera.getOffsetX();
        int yOffset = camera.getOffsetY();
        drawBackground(g, xOffset);
        mapRenderer.render(g, map, xOffset, yOffset);
        playerRenderer.render(g, player, xOffset, yOffset);
        enemyRenderer.renderAll(g, enemyController.getListEnemy(), xOffset, yOffset);
        healthBarRenderer.render(g, player);
        manaBarRenderer.render(g, player);
    }

    private void drawBackground(Graphics g, int xOffset) {
        for(int i = 0; i < bgLayers.length; ++i){
            // Tính toán quãng đường di chuyển của layer này
            int xMove = (int) (xOffset * parallaxFactors[i]);

            // Dùng Modulo để tìm vị trí x bắt đầu vẽ. 
            // Dấu trừ vì camera đi sang phải thì cảnh vật phải lùi về bên trái
            int xStart = -(xMove % GAME_WIDTH);

            // Vẽ ảnh thứ nhất
            g.drawImage(bgLayers[i], xStart, 0, GAME_WIDTH, GAME_HEIGHT, null);
            
            // Vẽ ảnh thứ hai nối tiếp ngay sau đuôi ảnh thứ nhất (xStart + GAME_WIDTH)
            // để lấp đầy khoảng trống khi ảnh thứ nhất trôi qua
            g.drawImage(bgLayers[i], xStart + GAME_WIDTH, 0, GAME_WIDTH, GAME_HEIGHT, null);
            
            // (Tùy chọn) Nếu game cho phép lùi camera ra số âm, bạn có thể cần vẽ 
            // thêm 1 ảnh ở xStart - GAME_WIDTH để tránh bị rỗng hình bên trái.
            // g.setColor(new Color(0, 0, 0, 120));
            // g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }
    }

}
