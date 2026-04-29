package controller.state;

import controller.AudioController;
import controller.InputController;
import controller.SaveLoadController;
import model.state.GameState;
import model.state.GameStateModel;
import model.state.SettingsModel;
import view.renderer.state.OptionRenderer;
import view.renderer.state.ui.AudioButton;
import view.renderer.state.ui.MenuButton;

public class OptionController {
    private InputController input;
    private GameStateModel gameState;
    private OptionRenderer renderer;
    private SettingsModel settingsModel;
    private AudioController audioController;
    private SaveLoadController saveLoad;

    public OptionController(InputController input, GameStateModel gameState, OptionRenderer renderer,
            SettingsModel settingsModel, AudioController audioController, SaveLoadController saveLoad) {
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
        this.settingsModel = settingsModel;
        this.audioController = audioController;
        this.saveLoad = saveLoad;
    }

    public void update() {
        AudioButton[] audioBtns = renderer.getAudioButtons();
        MenuButton[] diffBtns = renderer.getDiffButtons();
        MenuButton homeBtn = renderer.getHomeButton();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        for (AudioButton btn : audioBtns) {
            checkAudioButtonStatus(btn, mouseX, mouseY);
        }
        for (MenuButton btn : diffBtns) {
            checkMenuButtonStatus(btn, mouseX, mouseY);
        }
        checkMenuButtonStatus(homeBtn, mouseX, mouseY);
        if (input.isMouseRelease()) {
            if (audioBtns[0].isHovered() && audioBtns[0].isPressed()) {
                boolean isMuted = !settingsModel.isMusicMuted();
                settingsModel.setMusicMuted(isMuted);
                audioController.toggleMusic(isMuted);
                saveLoad.saveGame();
                audioController.playMusic(AudioController.BGM_PLAYING);
            } else if (audioBtns[1].isHovered() && audioBtns[1].isPressed()) {
                boolean isMuted = !settingsModel.isSFXMuted();
                settingsModel.setSFXMuted(isMuted);
                audioController.toggleSFX(isMuted);
                saveLoad.saveGame();
                audioController.playSFX(AudioController.SFX_CLICK);
            } else {
                for (int i = 0; i < diffBtns.length; i++) {
                    if (diffBtns[i].isHovered() && diffBtns[i].isPressed()) {
                        settingsModel.setDifficult(i);
                        saveLoad.saveGame();
                        audioController.playSFX(AudioController.SFX_CLICK);
                        break;
                    }
                }
            }
            if (homeBtn.isHovered() && homeBtn.isPressed()) {
                gameState.setGameState(GameState.MENU);
                audioController.playSFX(AudioController.SFX_CLICK);
            }
            for (AudioButton btn : audioBtns)
                btn.resetState();
            for (MenuButton btn : diffBtns)
                btn.resetState();
            homeBtn.resetState();
            input.resetMouse();
        }
    }
    private void checkAudioButtonStatus(AudioButton btn, int mouseX, int mouseY) {
        if (btn.isHit(mouseX, mouseY)) {
            btn.setHovered(true);
            if (input.isMousePress())
                btn.setPressed(true);
        } else {
            btn.setHovered(false);
            btn.setPressed(false);
        }
    }

    private void checkMenuButtonStatus(MenuButton btn, int mouseX, int mouseY) {
        if (btn.isHit(mouseX, mouseY)) {
            btn.setHovered(true);
            if (input.isMousePress())
                btn.setPressed(true);
        } else {
            btn.setHovered(false);
            btn.setPressed(false);
        }
    }
}