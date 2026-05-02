package controller.state;

import controller.AudioController;
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
    private GameOverRenderer renderer;

    public GameOverController(Game game, InputController input, GameStateModel gameState, GameOverRenderer renderer) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
    }

    public void update(AudioController audio) {
        MenuButton home = renderer.getHomeBtn();
        MenuButton restart = renderer.getRestartBtn();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        if (home.isHit(mouseX, mouseY)) {
            home.setHovered(true);
            if (input.isMousePress())
                home.setPressed(true);
        } else {
            home.setHovered(false);
            home.setPressed(false);
        }
        if (restart.isHit(mouseX, mouseY)) {
            restart.setHovered(true);
            if (input.isMousePress())
                restart.setPressed(true);
        } else {
            restart.setHovered(false);
            restart.setPressed(false);
        }
        if (input.isMouseRelease()) {
            if (home.isPressed()) {
                audio.playSFX(AudioController.SFX_CLICK);
                game.resetPlaying();
                gameState.setGameState(GameState.MENU);
                input.resetKeys();
            } else if (restart.isPressed()) {
                audio.playSFX(AudioController.SFX_CLICK);
                game.resetPlaying();
                gameState.setGameState(GameState.PLAYING);
                input.resetKeys();
            }
            home.resetState();
            restart.resetState();
        }
    }
    
}