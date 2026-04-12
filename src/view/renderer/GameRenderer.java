package view.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.PlayerRenderer;
import view.renderer.hud.HealthBarRenderer;

import static util.AssetsPath.*;
import static util.PlayerStateIndex.*;
import static core.GameConfig.*;

public class GameRenderer {
    private MapModel map;
    private PlayerModel player;
    private CameraModel camera;

    private MapRenderer mapRenderer;
    private PlayerRenderer playerRenderer;
    private HealthBarRenderer healthBarRenderer;

    public GameRenderer(MapModel map, PlayerModel player, CameraModel camera) {
        this.map = map;
        this.player = player;
        this.camera = camera;
        // healthBarRenderer = new HealthBarRenderer();
        loadMapTexture();

        loadPlayerAnimation();
    }

    private void loadPlayerAnimation() {
        Animation[] playerAnimation = new Animation[MAX_STATE];
        playerAnimation[IDLE] = new Animation(ResourceManager.loadSprite(playerIdle, PLAYER_FRAME.get(IDLE), 64, 40),
                24);
        playerAnimation[RUN] = new Animation(ResourceManager.loadSprite(playerRun, PLAYER_FRAME.get(RUN), 64, 40), 20);
        playerAnimation[JUMP] = new Animation(ResourceManager.loadSprite(playerJump, PLAYER_FRAME.get(JUMP), 64, 40),
                40);
        playerAnimation[HURT] = new Animation(ResourceManager.loadSprite(playerHit, PLAYER_FRAME.get(HURT), 64, 40),
                30);
        playerAnimation[FALL] = new Animation(ResourceManager.loadSprite(playerFall, PLAYER_FRAME.get(FALL), 64, 40),
                30);
        playerAnimation[ATTACK] = new Animation(
                ResourceManager.loadSprite(playerAttack, PLAYER_FRAME.get(ATTACK), 64, 40), 18);

        playerRenderer = new PlayerRenderer(playerAnimation);
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
    }

    public void render(Graphics g) {
        int xOffset = camera.getXOffset();
        mapRenderer.render(g, map, xOffset);
        playerRenderer.render(g, player, xOffset);
        // healthBarRenderer.render(g, player);
    }
}
