package controller.state;

import controller.InputController;
import controller.WorldController;
import model.entity.PlayerModel;
import model.state.GameState;
import model.state.GameStateModel;
import view.renderer.GameRenderer;

public class PlayingController {
    private InputController input;
    private GameStateModel gameState;
    private WorldController worldController;
    private PlayerModel player;
    private GameRenderer renderer;


    public PlayingController(InputController input, GameStateModel gameState, WorldController worldController,
            PlayerModel player, GameRenderer renderer) {
        this.input = input;
        this.gameState = gameState;
        this.worldController = worldController;
        this.player = player;
        this.renderer = renderer;
    }

    public void update() {
        if (input.isEsc()) {
            gameState.setGameState(GameState.MENU);
        }
        if (worldController.getEnemyController().getListEnemy().size() == 0) {
            gameState.setGameState(GameState.VICTORY);
        }
        if (!player.isAlive()) {
            gameState.setGameState(GameState.GAME_OVER);
        }
        if (gameState.getGameState() == GameState.PLAYING) {
            worldController.update(player);
            renderer.getPlayingRenderer().update();;
        }
    }
}
