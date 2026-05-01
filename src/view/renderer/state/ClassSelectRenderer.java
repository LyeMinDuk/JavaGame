package view.renderer.state;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;

import static core.GameConfig.*;
import static util.AssetsPath.*;

public class ClassSelectRenderer {
    private BufferedImage backgroundImg;
    private BufferedImage boardImg;
    private BufferedImage knightImg;
    private BufferedImage mageImg;

    private int boardW, boardH, boardX, boardY;

    public ClassSelectRenderer() {
        backgroundImg = ResourceManager.loadImg(menuBG);
        boardImg = ResourceManager.loadImg(menuBoard);
        knightImg = ResourceManager.loadImg(classSelectKnight);
        mageImg = ResourceManager.loadImg(classSelectMage);

        boardW = (int) (282 * UI_SCALE);
        boardH = (int) (350 * UI_SCALE);
        boardX = GAME_WIDTH / 2 - boardW / 2;
        boardY = GAME_HEIGHT / 2 - boardH / 2;
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(boardImg, boardX, boardY, boardW, boardH, null);
        int halfW = boardW / 2;
        int drawY = boardY + boardH / 2;
        g.drawImage(knightImg, boardX + 20, drawY, knightImg.getWidth(), knightImg.getHeight(), null);
        g.drawImage(mageImg, boardX + halfW, drawY, mageImg.getWidth(), mageImg.getHeight(), null);
    }

    public int getBoardX() {
        return boardX;
    }

    public int getBoardY() {
        return boardY;
    }

    public int getBoardW() {
        return boardW;
    }

    public int getBoardH() {
        return boardH;
    }
}