package view.renderer.state;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;
import static util.AssetsPath.*;
import static core.GameConfig.*;

public class MenuRenderer {
    private BufferedImage backgroundImg;
    private BufferedImage menuBoardImg;
    private BufferedImage[][] buttonImgs = new BufferedImage[3][3];
    private int pressed = -1;
    private int hovered = -1;
    private final int boardWidth = (int) (282 * SCALE), boardHeight = (int) (350 * SCALE);
    private final int butWidth = (int) (140 * SCALE), butHeight = (int) (56 * SCALE);
    private int boardX, boardY, butX;
    private int[] butY = new int[3];

    public MenuRenderer() {
        loadResource();
        initCordinate();
    }

    private void loadResource() {
        backgroundImg = ResourceManager.loadImg(menuBG);
        menuBoardImg = ResourceManager.loadImg(menuBoard);
        buttonImgs[0] = ResourceManager.loadSprite(menuPlayButton, 3, 140, 56);
        buttonImgs[1] = ResourceManager.loadSprite(menuOptionButton, 3, 140, 56);
        buttonImgs[2] = ResourceManager.loadSprite(menuQuitButton, 3, 140, 56);
    }

    private void initCordinate() {
        boardX = GAME_WIDTH / 2 - boardWidth / 2;
        boardY = GAME_HEIGHT / 2 - boardHeight / 2;
        butX = GAME_WIDTH / 2 - butWidth / 2;
        int startY = 225;
        for (int i = 0; i < 3; ++i) {
            butY[i] = startY + i * (butHeight + 30);
        }
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(menuBoardImg, boardX, boardY, boardWidth, boardHeight, null);
        for (int i = 0; i < 3; ++i) {
            int butState = 0;
            if (i == pressed) {
                butState = 2;
            } else if (i == hovered) {
                butState = 1;
            }
            g.drawImage(buttonImgs[i][butState], butX, butY[i], butWidth, butHeight, null);
        }
    }

    public int getButtonIndexAt(int x, int y) {
        for (int i = 0; i < 3; i++) {
            if (x >= butX && x <= butX + butWidth &&
                    y >= butY[i] && y <= butY[i] + butHeight) {
                return i;
            }
        }
        return -1;
    }

    public void setHoveredButton(int idx) {
        hovered = idx;
    }

    public int getPressed() {
        return pressed;
    }

    public void setPressedButton(int idx) {
        pressed = idx;
    }

    public void resetButtonStates() {
        hovered = pressed = -1;
    }
}
