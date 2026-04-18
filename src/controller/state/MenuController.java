package controller.state;

import controller.InputController;
import core.Game;
import model.state.GameState;
import model.state.GameStateModel;
import view.renderer.state.MenuRenderer;

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
        int hoveredIdx = menuRenderer.getButtonIndexAt(mouseX, mouseY);
        menuRenderer.setHoveredButton(hoveredIdx);
        if (input.isMousePress()) {
            menuRenderer.setPressedButton(hoveredIdx);
        }
        if (input.isMouseRelease()) {
            int releasedIdx = menuRenderer.getButtonIndexAt(mouseX, mouseY);

            if (releasedIdx != -1) {
                switch (releasedIdx) {
                    case 0 -> gameState.setGameState(GameState.PLAYING);
                    case 1 -> gameState.setGameState(GameState.OPTIONS);
                    case 2 -> System.exit(0);
                }
            }
            menuRenderer.resetButtonStates();
            input.resetMouse();
        }
        if (input.isEnter()) {
            gameState.setGameState(GameState.PLAYING);
            input.resetKeys();
        }
    }
}
