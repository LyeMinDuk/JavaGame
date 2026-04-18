package view.renderer.state;

import static core.GameConfig.GAME_HEIGHT;
import static core.GameConfig.GAME_WIDTH;
import static core.GameConfig.SCALE;
import static util.AssetsPath.muteButton;
import static util.AssetsPath.optionBG;
import static util.AssetsPath.optionBoard;
import static util.AssetsPath.soundButton;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;

public class OptionRenderer {
    private BufferedImage backgroundImg;
    private BufferedImage optionBoardImg;
    private BufferedImage[][] buttonImg = new BufferedImage[3][2];
    private BufferedImage[] homeButtonImg = new BufferedImage[3];
    private int pressed = -1;
    private int hovered = -1;
    private final int boardWidth = (int) (282 * SCALE), boardHeight = (int) (405 * SCALE);
    private final int butWidth = 50, butHeight = 50;
    private int boardX, boardY, butX;
    private int[] butY = new int[2];
    private boolean isMusicMuted = false;
    private boolean isSFXMuted = false;

    public OptionRenderer() {
        loadResource();
        initCordinate();
    }

    private void loadResource() {
        backgroundImg = ResourceManager.loadImg(optionBG);
        optionBoardImg = ResourceManager.loadImg(optionBoard);
        buttonImg[0] = ResourceManager.loadSprite(soundButton, 3, 42, 42);
        buttonImg[1] = ResourceManager.loadSprite(muteButton, 3, 42, 42);

    }

    private void initCordinate() {
        boardX = GAME_WIDTH / 2 - boardWidth / 2;
        boardY = GAME_HEIGHT / 2 - boardHeight / 2;
        butX = GAME_WIDTH / 2 + 60;
        int startY = 203;
        for (int i = 0; i < 2; ++i) {
            butY[i] = startY + i * (butHeight + 15);
        }
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(optionBoardImg, boardX, boardY, boardWidth, boardHeight, null);
        
        for (int i = 0; i < 2; ++i) {
            int butState = 0;
            if (i == pressed) {
                butState = 2;
            } else if (i == hovered) {
                butState = 1;
            }
            int imgIdx = 0;
            if (i == 0) {
                imgIdx = isMusicMuted ? 1 : 0;
            } else if (i == 1) {
                imgIdx = isSFXMuted ? 1 : 0;
            }
            g.drawImage(buttonImg[imgIdx][butState], butX, butY[i], butWidth, butHeight, null);
        }
    }

    public int getButtonIndexAt(int x, int y) {
        for (int i = 0; i < 2; i++) {
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

    public boolean isMusicMuted() {
        return isMusicMuted;
    }

    public boolean isSFXMuted() {
        return isSFXMuted;
    }

    public void setMusicMuted(boolean isMusicMuted) {
        this.isMusicMuted = isMusicMuted;
    }

    public void setSFXMuted(boolean isSFXMuted) {
        this.isSFXMuted = isSFXMuted;
    }

}
