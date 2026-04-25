// VictoryController.java
package controller.state;

import controller.InputController;
import core.Game;
import model.state.GameState;
import model.state.GameStateModel;
import util.AssetsPath;
import view.renderer.state.VictoryRenderer;
import view.renderer.state.ui.MenuButton;

public class VictoryController {
    private InputController input;
    private GameStateModel gameState;
    private Game game;
    private VictoryRenderer renderer;

    public VictoryController(Game game, InputController input, GameStateModel gameState, VictoryRenderer renderer) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
    }

    public void update() {
        boolean isGameCompleted = game.getCurMapIdx() >= AssetsPath.levelMap.length - 1;
        
        MenuButton home = isGameCompleted ? renderer.getHomeBtnSingle() : renderer.getHomeBtn();
        MenuButton next = isGameCompleted ? null : renderer.getNextBtn();

        // 1. Reset trạng thái
        home.setHovered(false);
        home.setPressed(false);
        if (next != null) {
            next.setHovered(false);
            next.setPressed(false);
        }

        // 2. Xử lý Hover & Press bằng tọa độ chuột
        int mouseX = input.getMouseX(); // Đảm bảo InputController của bạn có hàm này
        int mouseY = input.getMouseY();

        if (home.isHit(mouseX, mouseY)) {
            home.setHovered(true);
            if (input.isMousePress()) home.setPressed(true);
        }

        if (next != null && next.isHit(mouseX, mouseY)) {
            next.setHovered(true);
            if (input.isMousePress()) next.setPressed(true);
        }

        // 3. Xử lý thả chuột (Click hoàn thành)
        if (input.isMouseRelease()) {
            if (home.isHovered()) {
                game.setCurMapIdx(0); // Trở về Menu thì reset lại game từ level 0
                gameState.setGameState(GameState.MENU);
            } 
            else if (next != null && next.isHovered()) {
                int index = game.getCurMapIdx();
                game.setCurMapIdx(index + 1);
                game.resetPlaying();
                gameState.setGameState(GameState.PLAYING);
            }
            input.resetKeys(); // Xóa trạng thái chuột/phím
        }
    }
}