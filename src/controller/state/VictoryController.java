package controller.state;

import controller.InputController;
import core.Game;
import model.state.GameState;
import model.state.GameStateModel;
import util.AssetsPath;

public class VictoryController {
    private InputController input;
    private GameStateModel gameState;
    private Game game;

    public VictoryController(Game game, InputController input, GameStateModel gameState) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
    }

    public void update() {
        int index = game.getCurMapIdx();
        game.setCurMapIdx(++index);
        if (index >= AssetsPath.levelMap.length) {

            
        } else {
            game.resetPlaying();
            gameState.setGameState(GameState.PLAYING);
        }
        if (input.isEsc()) {
            gameState.setGameState(GameState.MENU);
        }
        if (input.isEnter()) {
            game.resetPlaying();
            gameState.setGameState(GameState.PLAYING);
            input.resetKeys();
        }
    }
}
