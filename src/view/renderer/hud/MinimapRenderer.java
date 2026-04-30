package view.renderer.hud;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;

import view.assets.ResourceManager;

import static util.AssetsPath.minimap;
import static core.GameConfig.GAME_WIDTH;
import static core.GameConfig.SCALE;;

public class MinimapRenderer {
    private BufferedImage mapImg;
    private int drawWidth, drawHeight;
    private int drawX, drawY;

    public MinimapRenderer(int curMapIdx) {
        mapImg = ResourceManager.loadImg(minimap[curMapIdx]);
        drawWidth = GAME_WIDTH / 5; 
        double ratio = (double) mapImg.getHeight() / mapImg.getWidth();
        drawHeight = (int) (drawWidth * ratio);
        int pad = (int) (10 * SCALE);
        drawX = GAME_WIDTH - drawWidth - pad;
        drawY = pad;
    }

    public void render(Graphics g) {
        int pad = (int) (1 * SCALE);
        g.setColor(Color.WHITE);
        g.fillRect(drawX - pad, drawY - pad, drawWidth + (2 * pad), drawHeight + (2 * pad));
        g.drawImage(mapImg, drawX, drawY, drawWidth, drawHeight, null);
    }
}