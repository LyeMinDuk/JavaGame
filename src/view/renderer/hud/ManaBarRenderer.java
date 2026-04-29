package view.renderer.hud;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import model.entity.PlayerModel;
import view.assets.ResourceManager;

import static util.AssetsPath.*;

public class ManaBarRenderer {
    private final int x = 32, y = 60, width = 200, height = 25;

    private BufferedImage fillImg;
    private BufferedImage bgImg;

    public ManaBarRenderer() {
        fillImg = ResourceManager.loadImg(manaBarFill);
        bgImg = ResourceManager.loadImg(heathBarBG);
    }

    public void render(Graphics g, PlayerModel player) {
        double percent = (double) player.getCurMana() / player.getMaxMana();
        percent = Math.max(0, Math.min(1, percent));
        int pad = 4;
        int drawPad = (int) ((1 - percent) * pad);
        int drawWidth = (int) (percent * width);
        g.drawImage(bgImg, x, y, width, height, null);
        g.drawImage(fillImg, x + drawPad, y, drawWidth, height, null);
    }
}
