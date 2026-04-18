package controller.state;

import controller.InputController;
import model.state.GameState;
import model.state.GameStateModel;

public class GameOverController {
    private InputController input;
    private GameStateModel gameState;

    public GameOverController(InputController input, GameStateModel gameState) {
        this.input = input;
        this.gameState = gameState;
    }

    public void update() {
        if (input.isEsc()) {
            gameState.setGameState(GameState.MENU);
        }
        if (input.isEnter()) {
            gameState.setGameState(GameState.PLAYING);
            input.resetKeys();
        }
    }
}
