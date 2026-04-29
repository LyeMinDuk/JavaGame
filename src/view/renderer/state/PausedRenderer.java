package view.renderer.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import model.state.SettingsModel;
import view.assets.ResourceManager;
import view.renderer.state.ui.AudioButton;
import view.renderer.state.ui.MenuButton;

import static core.GameConfig.*;
import static util.AssetsPath.*;

public class PausedRenderer {
    private BufferedImage pausedBoardImg;

    private BufferedImage[][] audioImgs = new BufferedImage[2][3];
    private BufferedImage[][] homeImgs = new BufferedImage[1][3];
    private BufferedImage[][] resumeImgs = new BufferedImage[1][3];

    private AudioButton[] audioButtons = new AudioButton[2];
    private MenuButton homeBtn;
    private MenuButton resumeBtn;

    private int boardWidth, boardHeight;
    private int boardX, boardY;

    public PausedRenderer() {
        loadResource();
        initCoordinate();
    }

    private void loadResource() {
        pausedBoardImg = ResourceManager.loadImg(pausedBoard);

        audioImgs[0] = ResourceManager.loadSprite(soundButton, 3, 42, 42);
        audioImgs[1] = ResourceManager.loadSprite(muteButton, 3, 42, 42);
        homeImgs[0] = ResourceManager.loadSprite(homeButton, 3, 56, 56);
        resumeImgs[0] = ResourceManager.loadSprite(resumeButton, 3, 56, 56);
    }

    private void initCoordinate() {
        boardWidth = (int) (258 * UI_SCALE);
        boardHeight = (int) (389 * UI_SCALE);
        boardX = GAME_WIDTH / 2 - boardWidth / 2;
        boardY = GAME_HEIGHT / 2 - boardHeight / 2;

        int audioSize = (int) (42 * UI_SCALE);
        int normalSize = (int) (56 * UI_SCALE);

        int audioX = GAME_WIDTH / 2 + (int) (35 * UI_SCALE);
        int audioStartY = boardY + (int) (115 * UI_SCALE);
        int audioGap = (int) (7 * UI_SCALE);

        audioButtons[0] = new AudioButton(audioX, audioStartY, audioSize, audioSize, audioImgs);
        audioButtons[1] = new AudioButton(audioX, audioStartY + audioSize + audioGap, audioSize, audioSize, audioImgs);

        int btnY = boardY + boardHeight - (int) (90 * UI_SCALE);
        int btnGap = (int) (45 * UI_SCALE);
        int startX = GAME_WIDTH / 2 - normalSize - btnGap / 2;

        homeBtn = new MenuButton(startX, btnY, normalSize, normalSize, 0, homeImgs);
        resumeBtn = new MenuButton(startX + normalSize + btnGap, btnY, normalSize, normalSize, 0, resumeImgs);
    }

    public void render(Graphics g, SettingsModel settingsModel) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.drawImage(pausedBoardImg, boardX, boardY, boardWidth, boardHeight, null);
        audioButtons[0].setMuted(settingsModel.isMusicMuted());
        audioButtons[1].setMuted(settingsModel.isSFXMuted());
        for (AudioButton btn : audioButtons) {
            btn.draw(g);
        }
        homeBtn.draw(g);
        resumeBtn.draw(g);
    }

    public AudioButton[] getAudioButtons() {
        return audioButtons;
    }

    public MenuButton getHomeBtn() {
        return homeBtn;
    }

    public MenuButton getResumeBtn() {
        return resumeBtn;
    }
}