package view.renderer.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;
import view.renderer.state.ui.MenuButton;

import static core.GameConfig.*;
import static util.AssetsPath.*;

public class VictoryRenderer {
    private BufferedImage levelCompletedBoardImg;
    private BufferedImage gameCompletedBoardImg;

    private BufferedImage[][] homeImgs = new BufferedImage[1][3];
    private BufferedImage[][] nextImgs = new BufferedImage[1][3];

    private MenuButton homeBtn;
    private MenuButton nextBtn;
    private MenuButton homeBtnSingle;

    private int lvBoardWidth, lvBoardHeight;
    private int lvBoardX, lvBoardY;

    private int gameBoardWidth, gameBoardHeight;
    private int gameBoardX, gameBoardY;

    public VictoryRenderer() {
        loadResource();
        initCoordinate();
    }

    private void loadResource() {
        levelCompletedBoardImg = ResourceManager.loadImg(lvCompletedBoard);
        gameCompletedBoardImg = ResourceManager.loadImg(gameCompletedBoard);

        homeImgs[0] = ResourceManager.loadSprite(homeButton, 3, 56, 56);
        nextImgs[0] = ResourceManager.loadSprite(resumeButton, 3, 56, 56);
    }

    private void initCoordinate() {
        lvBoardWidth = (int) (224 * UI_SCALE);
        lvBoardHeight = (int) (204 * UI_SCALE);
        lvBoardX = GAME_WIDTH / 2 - lvBoardWidth / 2;
        lvBoardY = GAME_HEIGHT / 2 - lvBoardHeight / 2;

        int normalSize = (int) (56 * UI_SCALE);
        int gap = (int) (20 * UI_SCALE);
        int lvStartY = lvBoardY + (int) (120 * UI_SCALE);
        int startX = GAME_WIDTH / 2 - normalSize - gap / 2;

        homeBtn = new MenuButton(startX, lvStartY, normalSize, normalSize, 0, homeImgs);
        nextBtn = new MenuButton(startX + normalSize + gap, lvStartY, normalSize, normalSize, 0, nextImgs);
        gameBoardWidth = (int) (258 * UI_SCALE);
        gameBoardHeight = (int) (258 * UI_SCALE);
        gameBoardX = GAME_WIDTH / 2 - gameBoardWidth / 2;
        gameBoardY = GAME_HEIGHT / 2 - gameBoardHeight / 2;

        int gameStartY = gameBoardY + (int) (150 * UI_SCALE);
        int singleX = GAME_WIDTH / 2 - normalSize / 2;
        homeBtnSingle = new MenuButton(singleX, gameStartY, normalSize, normalSize, 0, homeImgs);
    }

    public void render(Graphics g, boolean isGameCompleted) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        if (isGameCompleted) {
            g.drawImage(gameCompletedBoardImg, gameBoardX, gameBoardY, gameBoardWidth, gameBoardHeight, null);
            homeBtnSingle.draw(g);
        } else {
            g.drawImage(levelCompletedBoardImg, lvBoardX, lvBoardY, lvBoardWidth, lvBoardHeight, null);
            homeBtn.draw(g);
            nextBtn.draw(g);
        }
    }

    public MenuButton getHomeBtn() {
        return homeBtn;
    }

    public MenuButton getNextBtn() {
        return nextBtn;
    }

    public MenuButton getHomeBtnSingle() {
        return homeBtnSingle;
    }
}