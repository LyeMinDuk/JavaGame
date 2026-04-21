package controller.state;

import controller.InputController;
import core.Game;
import model.state.GameState;
import model.state.GameStateModel;
import view.renderer.state.MenuRenderer;
import view.renderer.state.ui.MenuButton;


public class MenuController {
    private InputController input;
    private GameStateModel gameState;
    private MenuRenderer menuRenderer;
    private Game game;

    public MenuController(Game game, InputController input, GameStateModel gameState, MenuRenderer menuRenderer) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.menuRenderer = menuRenderer;
    }

    public void update() {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        boolean isPress = input.isMousePress();
        boolean isRelease = input.isMouseRelease();

        // Lấy danh sách các nút từ View
        MenuButton[] buttons = menuRenderer.getButtons();

        // 1. Reset trạng thái hover trước khi kiểm tra (để tránh lỗi kẹt hover)
        for (MenuButton btn : buttons) {
            btn.setHovered(false);
        }

        // 2. Kiểm tra va chạm chuột với từng nút
        for (int i = 0; i < buttons.length; i++) {
            MenuButton btn = buttons[i];
            
            if (btn.isHit(mouseX, mouseY)) {
                btn.setHovered(true);

                if (isPress) {
                    btn.setPressed(true);
                }

                // Nếu nhả chuột và trước đó có click đúng vào nút này
                if (isRelease && btn.isPressed()) {
                    switch (i) {
                        case 0 -> {
                            game.resetPlaying();
                            gameState.setGameState(GameState.PLAYING);
                        } 
                        case 1 -> gameState.setGameState(GameState.OPTIONS);
                        case 2 -> System.exit(0);
                    }
                }
            }
        }

        // 3. Xử lý khi nhả chuột (ở bất kỳ đâu trên màn hình) -> Reset toàn bộ nút
        if (isRelease) {
            for (MenuButton btn : buttons) {
                btn.resetState();
            }
            input.resetMouse();
        }

        // 4. Phím tắt Enter
        if (input.isEnter()) {
            game.resetPlaying(); // (Nên có dòng này giống case 0 cho đồng bộ)
            gameState.setGameState(GameState.PLAYING);
            input.resetKeys();
        }
    }
}