package controller.state;

import controller.InputController;
import model.state.GameState;
import model.state.GameStateModel;
import view.renderer.state.OptionRenderer;

public class OptionController {
    private InputController input;
    private GameStateModel gameState;
    private OptionRenderer optionRenderer;

    public OptionController(InputController input, GameStateModel gameState, OptionRenderer optionRenderer) {
        this.input = input;
        this.gameState = gameState;
        this.optionRenderer = optionRenderer;
    }

    public void update() {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        int hoveredIdx = optionRenderer.getButtonIndexAt(mouseX, mouseY);
        optionRenderer.setHoveredButton(hoveredIdx);
        if (input.isMousePress()) {
            optionRenderer.setPressedButton(hoveredIdx);
        }
        if (input.isMouseRelease()) {
            int releasedIdx = optionRenderer.getButtonIndexAt(mouseX, mouseY);

            if (releasedIdx != -1) {
                if (releasedIdx == 0) {
                    optionRenderer.setMusicMuted(!optionRenderer.isMusicMuted());
                    // TODO: audioController.musicMuteToggle();
                } else if (releasedIdx == 1) {
                    optionRenderer.setSFXMuted(!optionRenderer.isSFXMuted());
                    // TODO: audioController.sfxMuteToggle();
                }
            }
            optionRenderer.resetButtonStates();
            input.resetMouse();
        }
        if (input.isEnter()) {
            gameState.setGameState(GameState.PLAYING);
            input.resetKeys();
        }
        if (input.isEsc()) {
            gameState.setGameState(GameState.MENU);
        }
    }
}
