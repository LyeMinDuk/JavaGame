package view.renderer.state;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;
import view.renderer.state.ui.MenuButton;

import static util.AssetsPath.*;
import static core.GameConfig.*;

public class MenuRenderer {
    private BufferedImage backgroundImg;
    private BufferedImage menuBoardImg;
    private BufferedImage[][] buttonImgs = new BufferedImage[3][3];

    // Quản lý mảng object Nút thay vì các biến tọa độ rời rạc
    private MenuButton[] buttons = new MenuButton[3];

    private int boardWidth, boardHeight;
    private int boardX, boardY;

    public MenuRenderer() {
        loadResource();
        initCoordinate();
    }

    private void loadResource() {
        backgroundImg = ResourceManager.loadImg(menuBG);
        menuBoardImg = ResourceManager.loadImg(menuBoard);
        buttonImgs[0] = ResourceManager.loadSprite(menuPlayButton, 3, 140, 56);
        buttonImgs[1] = ResourceManager.loadSprite(menuOptionButton, 3, 140, 56);
        buttonImgs[2] = ResourceManager.loadSprite(menuQuitButton, 3, 140, 56);
    }

    private void initCoordinate() {
        // 1. Khởi tạo kích thước bảng
        boardWidth = (int) (282 * UI_SCALE);
        boardHeight = (int) (350 * UI_SCALE);

        // 2. Căn giữa bảng
        boardX = GAME_WIDTH / 2 - boardWidth / 2;
        boardY = GAME_HEIGHT / 2 - boardHeight / 2;

        // 3. Khởi tạo kích thước nút
        int butWidth = (int) (140 * UI_SCALE);
        int butHeight = (int) (56 * UI_SCALE);
        int butX = GAME_WIDTH / 2 - butWidth / 2;

        // 4. Chia tỷ lệ và khoảng cách động (tránh lỗi lệch nút khi scale = 1)
        int headerHeight = (int) (90 * UI_SCALE);
        int paddingBottom = (int) (20 * UI_SCALE);

        int availableHeight = boardHeight - headerHeight - paddingBottom;
        int totalButtonHeight = 3 * butHeight;
        int gap = (availableHeight - totalButtonHeight) / 4;

        int startY = boardY + headerHeight + gap;

        // 5. Sinh ra các object Button
        for (int i = 0; i < buttons.length; ++i) {
            int yPos = startY + i * (butHeight + gap);
            // Truyền mảng ảnh buttonImgs vào cho nút tự quản lý việc vẽ
            buttons[i] = new MenuButton(butX, yPos, butWidth, butHeight, i, buttonImgs);
        }
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(menuBoardImg, boardX, boardY, boardWidth, boardHeight, null);

        // Chỉ cần bảo các nút tự vẽ chính nó
        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    // Controller sẽ dùng hàm này để lấy danh sách nút và xét va chạm chuột
    public MenuButton[] getButtons() {
        return buttons;
    }
}