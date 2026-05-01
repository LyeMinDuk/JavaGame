package controller.state;

import controller.AudioController;
import controller.InputController;
import controller.SaveLoadController;
import core.Game;
import model.state.GameState;
import model.state.GameStateModel;
import view.renderer.state.MenuRenderer;
import view.renderer.state.ui.MenuButton;

public class MenuController {
    private InputController input;
    private GameStateModel gameState;
    private MenuRenderer menuRenderer;
    private SaveLoadController saveLoad;
    private Game game;

    public MenuController(Game game, InputController input, GameStateModel gameState, MenuRenderer menuRenderer,
            SaveLoadController saveLoad) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.menuRenderer = menuRenderer;
        this.saveLoad = saveLoad;
    }

    public void update(AudioController audio) {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        boolean isPress = input.isMousePress();
        boolean isRelease = input.isMouseRelease();

        MenuButton[] buttons = menuRenderer.getButtons();
        for (MenuButton btn : buttons) {
            btn.setHovered(false);
        }
        for (int i = 0; i < buttons.length; i++) {
            MenuButton btn = buttons[i];
            if (btn.isHit(mouseX, mouseY)) {
                btn.setHovered(true);
                if (isPress) {
                    btn.setPressed(true);
                }
                if (isRelease && btn.isPressed()) {
                    switch (i) {
                        case 0 -> {
                            audio.playSFX(AudioController.SFX_CLICK);
                            gameState.setGameState(GameState.CHOOSE_CLASS);
                        }
                        case 1 -> {
                            audio.playSFX(AudioController.SFX_CLICK);
                            gameState.setGameState(GameState.OPTIONS);
                        }
                        case 2 -> {
                            audio.playSFX(AudioController.SFX_CLICK);
                            saveLoad.saveGame();
                            System.exit(0);
                        }
                    }
                }
            }
        }
        if (isRelease) {
            for (MenuButton btn : buttons) {
                btn.resetState();
            }
            input.resetMouse();
        }

        if (input.isEnter()) {
            game.resetPlaying();
            gameState.setGameState(GameState.PLAYING);
            input.resetKeys();
        }
    }
}