package controller.state;

import controller.AudioController;
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

    public void update(AudioController audio) {
        audio.playMusic(AudioController.BGM_PLAYING);
        if(!player.isAlive()){
            gameState.setGameState(GameState.GAME_OVER);
            input.resetKeys();
            return;
        }
        if (player.isAlive() && (input.isP() || input.isEsc())) {
            gameState.setGameState(GameState.PAUSED);
            input.resetKeys();
            return;
        }
        if (player.isAlive() && worldController.getEnemyController().getListEnemy().isEmpty()) {
            gameState.setGameState(GameState.VICTORY);
            return;
        }

        worldController.update(player, audio);
        renderer.getPlayingRenderer().update();
    }
}