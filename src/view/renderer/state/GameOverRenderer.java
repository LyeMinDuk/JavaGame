package view.renderer.state;

import static core.GameConfig.GAME_HEIGHT;
import static core.GameConfig.GAME_WIDTH;
import static core.GameConfig.UI_SCALE;
import static util.AssetsPath.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;
import view.renderer.state.ui.MenuButton;

public class GameOverRenderer {

    private BufferedImage gameOverBoardImg;

    // Mảng ảnh cho các nút
    private BufferedImage[][] homeImgs = new BufferedImage[1][3];
    private BufferedImage[][] restartImgs = new BufferedImage[1][3]; // Dùng chung nút "Play/Next" cho nút Try Again

    // Các nút bấm UI
    private MenuButton homeBtn;
    private MenuButton restartBtn;

    private int boardWidth, boardHeight;
    private int boardX, boardY;

    public GameOverRenderer() {
        loadResource();
        initCoordinate();
    }

    private void loadResource() {
        // Điền biến chứa đường dẫn ảnh bảng "YOU DEAD" của bạn vào đây
        gameOverBoardImg = ResourceManager.loadImg(dieBoard);

        homeImgs[0] = ResourceManager.loadSprite(homeButton, 3, 56, 56);
        restartImgs[0] = ResourceManager.loadSprite(resumeButton, 3, 56, 56);
    }

    private void initCoordinate() {
        // Chỉnh kích thước theo ảnh gốc của bạn (Giả sử là 258x258 giống bảng Game
        // Completed)
        boardWidth = (int) (235 * UI_SCALE);
        boardHeight = (int) (225 * UI_SCALE);
        boardX = GAME_WIDTH / 2 - boardWidth / 2;
        boardY = GAME_HEIGHT / 2 - boardHeight / 2;

        int normalSize = (int) (56 * UI_SCALE);
        int gap = (int) (20 * UI_SCALE);

        // Nút nằm ở khoảng giữa bảng tính từ trên xuống
        int startY = boardY + (int) (130 * UI_SCALE);

        // Tính toán tọa độ X để 2 nút nằm cân đối ở giữa
        int startX = GAME_WIDTH / 2 - normalSize - gap / 2;

        homeBtn = new MenuButton(startX, startY, normalSize, normalSize, 0, homeImgs);
        restartBtn = new MenuButton(startX + normalSize + gap, startY, normalSize, normalSize, 0, restartImgs);
    }

    public void render(Graphics g) {
        // Phủ lớp màu đen mờ lên toàn bộ màn hình game
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Vẽ bảng và nút
        g.drawImage(gameOverBoardImg, boardX, boardY, boardWidth, boardHeight, null);
        homeBtn.draw(g);
        restartBtn.draw(g);
    }

    // Getters cho Controller
    public MenuButton getHomeBtn() {
        return homeBtn;
    }

    public MenuButton getRestartBtn() {
        return restartBtn;
    }
}