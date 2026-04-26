package controller.state;

import controller.InputController;
import core.Game;
import model.state.GameState;
import model.state.GameStateModel;
import view.renderer.state.GameOverRenderer;
import view.renderer.state.ui.MenuButton;

public class GameOverController {
    private InputController input;
    private GameStateModel gameState;
    private Game game;
    private GameOverRenderer renderer; // Khai báo thêm renderer

    private int waitTimer = 0;

    public GameOverController(Game game, InputController input, GameStateModel gameState, GameOverRenderer renderer) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
    }

    public void update() {
        // 1. CHỐNG ĐÈ PHÍM: Ép người chơi phải chờ khoảng 0.5 giây (30 frames)
        if (waitTimer < 30) {
            waitTimer++;
            input.resetKeys();
            return;
        }

        MenuButton home = renderer.getHomeBtn();
        MenuButton restart = renderer.getRestartBtn();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        // 2. Xử lý trạng thái Hover & Press cho nút Home
        if (home.isHit(mouseX, mouseY)) {
            home.setHovered(true);
            if (input.isMousePress())
                home.setPressed(true);
        } else {
            home.setHovered(false);
            home.setPressed(false);
        }

        // 3. Xử lý trạng thái Hover & Press cho nút Restart
        if (restart.isHit(mouseX, mouseY)) {
            restart.setHovered(true);
            if (input.isMousePress())
                restart.setPressed(true);
        } else {
            restart.setHovered(false);
            restart.setPressed(false);
        }

        // 4. BẮT SỰ KIỆN CLICK (Thả chuột)
        if (input.isMouseRelease()) {
            if (home.isHovered() && home.isPressed()) {
                game.setCurMapIdx(0); // Chết, chọn Rage Quit về Menu -> Reset game từ đầu
                gameState.setGameState(GameState.MENU);
                input.resetKeys();
                waitTimer = 0;
            } else if (restart.isHovered() && restart.isPressed()) {
                game.resetPlaying(); // Chọn Try Again -> Chơi lại level hiện tại
                gameState.setGameState(GameState.PLAYING);
                input.resetKeys();
                waitTimer = 0;
            }

            home.resetState();
            restart.resetState();
        }
    }
}