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
    private AudioController audioController;
    private SaveLoadController saveLoad;

    public PausedController(Game game, InputController input, GameStateModel gameState, PausedRenderer renderer,
            SettingsModel settingsModel, AudioController audioController, SaveLoadController saveLoad) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
        this.settingsModel = settingsModel;
        this.audioController = audioController;
        this.saveLoad = saveLoad;
    }

    public void update() {
        // Nhấn P hoặc Esc lần nữa để tiếp tục chơi
        if (input.isP() || input.isEsc()) {
            resumeGame();
            return;
        }

        AudioButton[] audioBtns = renderer.getAudioButtons();
        MenuButton home = renderer.getHomeBtn();
        MenuButton resume = renderer.getResumeBtn();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        // 1. Kiểm tra va chạm chuột cho tất cả các nút
        checkButtonStatus(home, mouseX, mouseY);
        checkButtonStatus(resume, mouseX, mouseY);
        for (AudioButton btn : audioBtns) {
            checkButtonStatus(btn, mouseX, mouseY);
        }

        // 2. Xử lý khi click (thả chuột)
        if (input.isMouseRelease()) {
            
            // Xử lý nút Home (Về Menu)
            if (home.isHovered() && home.isPressed()) {
                game.setCurMapIdx(0); 
                gameState.setGameState(GameState.MENU);
                input.resetKeys();
            } 
            // Xử lý nút Resume (Tiếp tục chơi)
            else if (resume.isHovered() && resume.isPressed()) {
                resumeGame();
            }
            // Xử lý bật tắt Music
            else if (audioBtns[0].isHovered() && audioBtns[0].isPressed()) {
                boolean isMuted = !settingsModel.isMusicMuted();
                settingsModel.setMusicMuted(isMuted);
                audioController.toggleMusic(isMuted); // Tùy chỉnh nhạc map
                saveLoad.saveGame();
                audioController.playSFX(AudioController.BGM_MENU);
            }
            // Xử lý bật tắt SFX
            else if (audioBtns[1].isHovered() && audioBtns[1].isPressed()) {
                boolean isMuted = !settingsModel.isSFXMuted();
                settingsModel.setSFXMuted(isMuted);
                audioController.toggleSFX(isMuted);
                saveLoad.saveGame();
                audioController.playSFX(AudioController.SFX_CLICK);
            }

            // Reset UI
            home.resetState();
            resume.resetState();
            for (AudioButton btn : audioBtns) btn.resetState();
            input.resetMouse();
        }
    }

    private void checkButtonStatus(UIButton btn, int mouseX, int mouseY) {
        if (btn.isHit(mouseX, mouseY)) {
            btn.setHovered(true);
            if (input.isMousePress()) btn.setPressed(true);
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