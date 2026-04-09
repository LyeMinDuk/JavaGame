package view.renderer;

import static util.AssetsPath.playerAttack;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.PlayerRenderer;
import static util.AssetsPath.*;

public class GameRenderer {
    private MapModel map;
    private PlayerModel player;
    private CameraModel camera;

    private ResourceManager assets = new ResourceManager();
    private MapRenderer mapRenderer;
    private PlayerRenderer playerRenderer;

    public GameRenderer(MapModel map, PlayerModel player, CameraModel camera) {
        this.map = map;
        this.player = player;
        this.camera = camera;

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
        // playerRenderer.update(player);
    }

    public void render(Graphics g) {
        int xOffset = camera.getXOffset();
        mapRenderer.render(g, map, xOffset);
        // playerRenderer.render(g, player, xOffset);
    }
}
