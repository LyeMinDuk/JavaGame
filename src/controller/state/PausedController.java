package controller.state;

import controller.AudioController;
import controller.InputController;
import controller.SaveLoadController;
import core.Game;
import model.state.GameState;
import model.state.GameStateModel;
import model.state.SettingsModel;
import view.renderer.state.PausedRenderer;
import view.renderer.state.ui.AudioButton;
import view.renderer.state.ui.MenuButton;
import view.renderer.state.ui.UIButton;

public class PausedController {
    private Game game;
    private InputController input;
    private GameStateModel gameState;
    private PausedRenderer renderer;
    private SettingsModel settingsModel;
    private SaveLoadController saveLoad;

    public PausedController(Game game, InputController input, GameStateModel gameState, PausedRenderer renderer,
            SettingsModel settingsModel, SaveLoadController saveLoad) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
        this.settingsModel = settingsModel;
        this.saveLoad = saveLoad;
    }

    public void update(AudioController audio) {
        if (input.isP() || input.isEsc()) {
            resumeGame();
            return;
        }

        AudioButton[] audioBtns = renderer.getAudioButtons();
        MenuButton home = renderer.getHomeBtn();
        MenuButton resume = renderer.getResumeBtn();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        checkButtonStatus(home, mouseX, mouseY);
        checkButtonStatus(resume, mouseX, mouseY);
        for (AudioButton btn : audioBtns) {
            checkButtonStatus(btn, mouseX, mouseY);
        }
        if (input.isMouseRelease()) {
            if (home.isHovered() && home.isPressed()) {
                audio.playSFX(AudioController.SFX_CLICK);
                game.setCurMapIdx(0);
                gameState.setGameState(GameState.MENU);
                input.resetKeys();
            } else if (resume.isHovered() && resume.isPressed()) {
                audio.playSFX(AudioController.SFX_CLICK);
                resumeGame();
            } else if (audioBtns[0].isHovered() && audioBtns[0].isPressed()) {
                boolean isMuted = !settingsModel.isMusicMuted();
                settingsModel.setMusicMuted(isMuted);
                audio.toggleMusic(isMuted);
                saveLoad.saveGame();
                audio.playSFX(AudioController.BGM_PLAYING);
            } else if (audioBtns[1].isHovered() && audioBtns[1].isPressed()) {
                boolean isMuted = !settingsModel.isSFXMuted();
                settingsModel.setSFXMuted(isMuted);
                audio.toggleSFX(isMuted);
                saveLoad.saveGame();
                audio.playSFX(AudioController.SFX_CLICK);
            }
            home.resetState();
            resume.resetState();
            for (AudioButton btn : audioBtns)
                btn.resetState();
            input.resetMouse();
        }
    }

    private void checkButtonStatus(UIButton btn, int mouseX, int mouseY) {
        if (btn.isHit(mouseX, mouseY)) {
            btn.setHovered(true);
            if (input.isMousePress())
                btn.setPressed(true);
        } else {
            btn.setHovered(false);
            btn.setPressed(false);
        }
    }

    private void resumeGame() {
        gameState.setGameState(GameState.PLAYING);
        input.resetKeys();
    }
}