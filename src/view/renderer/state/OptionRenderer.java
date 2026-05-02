package view.renderer.state;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import model.state.SettingsModel;
import view.assets.ResourceManager;
import view.renderer.state.ui.AudioButton;
import view.renderer.state.ui.MenuButton;

import static core.GameConfig.*;
import static util.AssetsPath.*;

public class OptionRenderer {
    private BufferedImage backgroundImg;
    private BufferedImage optionBoardImg;

    private BufferedImage[][] audioImgs = new BufferedImage[2][3];
    private BufferedImage[][] easyImgs = new BufferedImage[1][3];
    private BufferedImage[][] mediumImgs = new BufferedImage[1][3];
    private BufferedImage[][] hardImgs = new BufferedImage[1][3];
    private BufferedImage[][] homeImgs = new BufferedImage[1][3];

    private AudioButton[] audioButtons = new AudioButton[2];
    private MenuButton[] diffButtons = new MenuButton[3];
    private MenuButton homeBtn;

    private int boardWidth, boardHeight;
    private int boardX, boardY;

    public OptionRenderer() {
        loadResource();
        initCoordinate();
    }

    private void loadResource() {
        backgroundImg = ResourceManager.loadImg(optionBG);
        optionBoardImg = ResourceManager.loadImg(optionBoard);

        audioImgs[0] = ResourceManager.loadSprite(soundButton, 3, 42, 42);
        audioImgs[1] = ResourceManager.loadSprite(muteButton, 3, 42, 42);

        easyImgs[0] = ResourceManager.loadSprite(easyButton, 3, 56, 56);
        mediumImgs[0] = ResourceManager.loadSprite(mediumButton, 3, 56, 56);
        hardImgs[0] = ResourceManager.loadSprite(hardButton, 3, 56, 56);
        homeImgs[0] = ResourceManager.loadSprite(homeButton, 3, 56, 56);
    }

    private void initCoordinate() {
        boardWidth = (int) (282 * UI_SCALE);
        boardHeight = (int) (405 * UI_SCALE);
        boardX = GAME_WIDTH / 2 - boardWidth / 2;
        boardY = GAME_HEIGHT / 2 - boardHeight / 2;

        int audioSize = (int) (42 * UI_SCALE);
        int normalSize = (int) (56 * UI_SCALE);

        int audioX = GAME_WIDTH / 2 + (int) (40 * UI_SCALE);
        int audioStartY = boardY + (int) (110 * UI_SCALE);
        int audioGap = (int) (5 * UI_SCALE);

        audioButtons[0] = new AudioButton(audioX, audioStartY, audioSize, audioSize, audioImgs);
        audioButtons[1] = new AudioButton(audioX, audioStartY + audioSize + audioGap, audioSize, audioSize, audioImgs);

        int diffY = boardY + (int) (235 * UI_SCALE);
        int diffGap = (int) (15 * UI_SCALE);
        int totalDiffWidth = 3 * normalSize + 2 * diffGap;
        int diffStartX = GAME_WIDTH / 2 - totalDiffWidth / 2;

        diffButtons[0] = new MenuButton(diffStartX, diffY, normalSize, normalSize, 0, easyImgs);
        diffButtons[1] = new MenuButton(diffStartX + normalSize + diffGap, diffY, normalSize, normalSize, 0,
                mediumImgs);
        diffButtons[2] = new MenuButton(diffStartX + 2 * (normalSize + diffGap), diffY, normalSize, normalSize, 0,
                hardImgs);

        int homeY = boardY + boardHeight - (int) (95 * UI_SCALE);
        int homeX = GAME_WIDTH / 2 - normalSize / 2;

        homeBtn = new MenuButton(homeX, homeY, normalSize, normalSize, 0, homeImgs);
    }

    public void render(Graphics g, SettingsModel settingsModel) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(optionBoardImg, boardX, boardY, boardWidth, boardHeight, null);
        audioButtons[0].setMuted(settingsModel.isMusicMuted());
        audioButtons[1].setMuted(settingsModel.isSFXMuted());
        for (AudioButton btn : audioButtons) {
            btn.draw(g);
        }
        for (int i = 0; i < 3; i++) {
            if (settingsModel.getDifficult() == i) {
                diffButtons[i].setPressed(true);
            } else if (!diffButtons[i].isHovered()) {
                diffButtons[i].setPressed(false);
            }
            diffButtons[i].draw(g);
        }
        homeBtn.draw(g);
    }

    public AudioButton[] getAudioButtons() {
        return audioButtons;
    }

    public MenuButton[] getDiffButtons() {
        return diffButtons;
    }

    public MenuButton getHomeButton() {
        return homeBtn;
    }
    
}