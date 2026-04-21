package view.renderer.state;

import static core.GameConfig.GAME_HEIGHT;
import static core.GameConfig.GAME_WIDTH;
import static core.GameConfig.UI_SCALE;
import static util.AssetsPath.*; 

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import model.state.SettingsModel;
import view.assets.ResourceManager;
import view.renderer.state.ui.AudioButton;
import view.renderer.state.ui.MenuButton;

public class OptionRenderer {
    private BufferedImage backgroundImg;
    private BufferedImage optionBoardImg;

    // Mảng ảnh cho các nút
    private BufferedImage[][] audioImgs = new BufferedImage[2][3];
    private BufferedImage[][] easyImgs = new BufferedImage[1][3];
    private BufferedImage[][] mediumImgs = new BufferedImage[1][3];
    private BufferedImage[][] hardImgs = new BufferedImage[1][3];
    private BufferedImage[][] homeImgs = new BufferedImage[1][3];

    // Các object nút UI
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

        // Các nút độ khó và home
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

        // Tách riêng 2 loại kích thước nút
        int audioSize = (int) (42 * UI_SCALE);  // Cho nút âm thanh
        int normalSize = (int) (56 * UI_SCALE); // Cho nút độ khó và nút home

        // 1. Tọa độ 2 nút Audio (Music, SFX)
        int audioX = GAME_WIDTH / 2 + (int) (40 * UI_SCALE); 
        int audioStartY = boardY + (int) (110 * UI_SCALE);
        int audioGap = (int) (5 * UI_SCALE);

        // Dùng audioSize (42x42)
        audioButtons[0] = new AudioButton(audioX, audioStartY, audioSize, audioSize, audioImgs);
        audioButtons[1] = new AudioButton(audioX, audioStartY + audioSize + audioGap, audioSize, audioSize, audioImgs);

        // 2. Tọa độ 3 nút Độ Khó (Nằm ngang ở giữa bảng)
        int diffY = boardY + (int) (235 * UI_SCALE); 
        int diffGap = (int) (15 * UI_SCALE);
        // Tính tổng chiều rộng dựa trên normalSize
        int totalDiffWidth = 3 * normalSize + 2 * diffGap; 
        int diffStartX = GAME_WIDTH / 2 - totalDiffWidth / 2; 

        // Dùng normalSize (56x56)
        diffButtons[0] = new MenuButton(diffStartX, diffY, normalSize, normalSize, 0, easyImgs);
        diffButtons[1] = new MenuButton(diffStartX + normalSize + diffGap, diffY, normalSize, normalSize, 0, mediumImgs);
        diffButtons[2] = new MenuButton(diffStartX + 2 * (normalSize + diffGap), diffY, normalSize, normalSize, 0, hardImgs);

        // 3. Tọa độ nút Home (Nằm căn giữa ở đáy)
        // Đẩy lên một chút xíu để vừa với nút 56x56
        int homeY = boardY + boardHeight - (int) (95 * UI_SCALE); 
        int homeX = GAME_WIDTH / 2 - normalSize / 2;
        
        // Dùng normalSize (56x56)
        homeBtn = new MenuButton(homeX, homeY, normalSize, normalSize, 0, homeImgs);
    }

    public void render(Graphics g, SettingsModel settingsModel) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(optionBoardImg, boardX, boardY, boardWidth, boardHeight, null);

        // Render Audio Buttons
        audioButtons[0].setMuted(settingsModel.isMusicMuted());
        audioButtons[1].setMuted(settingsModel.isSFXMuted());
        for (AudioButton btn : audioButtons) {
            btn.draw(g);
        }

        // Render Difficulty Buttons
        for (int i = 0; i < 3; i++) {
            if (settingsModel.getDifficult() == i) {
                diffButtons[i].setPressed(true);
            } else if (!diffButtons[i].isHovered()) {
                diffButtons[i].setPressed(false);
            }
            diffButtons[i].draw(g);
        }

        // Render Home Button
        homeBtn.draw(g);
    }

    // Getters cho Controller
    public AudioButton[] getAudioButtons() { return audioButtons; }
    public MenuButton[] getDiffButtons() { return diffButtons; }
    public MenuButton getHomeButton() { return homeBtn; }
}