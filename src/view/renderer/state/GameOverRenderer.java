package view.renderer.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;
import view.renderer.state.ui.MenuButton;

import static core.GameConfig.*;
import static util.AssetsPath.*;

public class GameOverRenderer {
    private BufferedImage gameOverBoardImg;

    private BufferedImage[][] homeImgs = new BufferedImage[1][3];
    private BufferedImage[][] restartImgs = new BufferedImage[1][3]; // Dùng chung nút "Play/Next" cho nút Try Again

    private MenuButton homeBtn;
    private MenuButton restartBtn;
    private int boardWidth, boardHeight;
    private int boardX, boardY;

    public GameOverRenderer() {
        loadResource();
        initCoordinate();
    }

    private void loadResource() {
        gameOverBoardImg = ResourceManager.loadImg(dieBoard);
        homeImgs[0] = ResourceManager.loadSprite(homeButton, 3, 56, 56);
        restartImgs[0] = ResourceManager.loadSprite(resumeButton, 3, 56, 56);
    }

    private void initCoordinate() {
        boardWidth = (int) (235 * UI_SCALE);
        boardHeight = (int) (225 * UI_SCALE);
        boardX = GAME_WIDTH / 2 - boardWidth / 2;
        boardY = GAME_HEIGHT / 2 - boardHeight / 2;

        int normalSize = (int) (56 * UI_SCALE);
        int gap = (int) (45 * UI_SCALE);
        int startY = boardY + (int) (95 * UI_SCALE);
        int startX = GAME_WIDTH / 2 - normalSize - gap / 2;

        homeBtn = new MenuButton(startX, startY, normalSize, normalSize, 0, homeImgs);
        restartBtn = new MenuButton(startX + normalSize + gap, startY, normalSize, normalSize, 0, restartImgs);
    }

    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.drawImage(gameOverBoardImg, boardX, boardY, boardWidth, boardHeight, null);
        homeBtn.draw(g);
        restartBtn.draw(g);
    }

    public MenuButton getHomeBtn() {
        return homeBtn;
    }

    public MenuButton getRestartBtn() {
        return restartBtn;
    }
    
}