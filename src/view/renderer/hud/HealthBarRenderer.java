package view.renderer.hud;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import model.entity.PlayerModel;
import view.assets.ResourceManager;

import static core.GameConfig.*;
import static util.AssetsPath.*;

public class HealthBarRenderer {
    private final int x = 32, y = 32, width = (int) (200 * SCALE), height = (int) (25 * SCALE);

    private BufferedImage fillImg;
    private BufferedImage frameImg;
    private BufferedImage bgImg;

    public HealthBarRenderer() {
        fillImg = ResourceManager.loadImg(heathBarFill);
        frameImg = ResourceManager.loadImg(heathBarFrame);
        bgImg = ResourceManager.loadImg(heathBarBG);
    }

    public void render(Graphics g, PlayerModel player) {
        double percent = (double) player.getCurHealth() / player.getMaxHealth();
        percent = Math.max(0, Math.min(1, percent));
        int pad = 4;
        int drawPad = (int) Math.round(1 - percent) * pad;
        int drawWidth = (int) (percent * width);
        System.out.println(x + ' ' + drawPad);
        g.drawImage(bgImg, x, y, width, height, null);
        g.drawImage(fillImg, x + drawPad, y, drawWidth, height, null);
        g.drawImage(frameImg, x, y, width, height, null);
    }
}
