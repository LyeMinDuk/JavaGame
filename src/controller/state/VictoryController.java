package controller.state;

import controller.AudioController;
import controller.InputController;
import controller.SaveLoadController;
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
    private SaveLoadController saveLoad;

    public VictoryController(Game game, InputController input, GameStateModel gameState, VictoryRenderer renderer,
            SaveLoadController saveLoad) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
        this.saveLoad = saveLoad;
    }

    public void update(AudioController audio) {
        boolean isGameCompleted = game.getCurMapIdx() >= AssetsPath.levelMap.length - 1;
        MenuButton home = isGameCompleted ? renderer.getHomeBtnSingle() : renderer.getHomeBtn();
        MenuButton next = isGameCompleted ? null : renderer.getNextBtn();
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
        if (next != null && next.isHit(mouseX, mouseY)) {
            next.setHovered(true);
            if (input.isMousePress())
                next.setPressed(true);
        } else if (next != null) {
            next.setHovered(false);
            next.setPressed(false);
        }
        if (input.isMouseRelease()) {
            if (home.isHovered() && home.isPressed()) {
                audio.playSFX(AudioController.SFX_CLICK);
                saveLoad.saveGame();
                game.resetPlaying();
                gameState.setGameState(GameState.MENU);
            } else if (next != null && next.isHovered() && next.isPressed()) {
                audio.playSFX(AudioController.SFX_CLICK);
                int index = game.getCurMapIdx();
                game.setCurMapIdx(index + 1);
                game.resetPlaying();
                saveLoad.saveGame();
                gameState.setGameState(GameState.PLAYING);
            }
            home.resetState();
            if (next != null)
                next.resetState();
            input.resetMouse();
            input.resetKeys();
        }
    }

}