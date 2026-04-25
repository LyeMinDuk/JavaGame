package controller.state;

import controller.AudioController;
import controller.InputController;
// import controller.SaveLoadController;
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
    // private SaveLoadController saveLoad;

    public OptionController(InputController input, GameStateModel gameState, OptionRenderer renderer, 
                            SettingsModel settingsModel, AudioController audioController) {
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
        this.settingsModel = settingsModel;
        this.audioController = audioController;
        // this.saveLoad = saveLoad;
    }

    public void update() {
        AudioButton[] audioBtns = renderer.getAudioButtons();
        MenuButton[] diffBtns = renderer.getDiffButtons();
        MenuButton homeBtn = renderer.getHomeButton();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        // 1. Kiểm tra trạng thái Hover & Press cho TẤT CẢ các nút
        for (AudioButton btn : audioBtns) {
            checkAudioButtonStatus(btn, mouseX, mouseY);
        }
        for (MenuButton btn : diffBtns) {
            checkMenuButtonStatus(btn, mouseX, mouseY);
        }
        checkMenuButtonStatus(homeBtn, mouseX, mouseY);

        // 2. BẮT SỰ KIỆN CLICK (Thả chuột)
        if (input.isMouseRelease()) {
            
            // --- Nút MUSIC (Vị trí số 0) ---
            if (audioBtns[0].isHovered() && audioBtns[0].isPressed()) {
                boolean isMuted = !settingsModel.isMusicMuted(); 
                settingsModel.setMusicMuted(isMuted);            
                audioController.toggleMusic(isMuted, "/audio/t.wav"); 
                // saveLoad.saveGame();                             
                // audioController.playSFX("/audio/click.wav");         
            }
            
            // --- Nút SFX (Vị trí số 1) ---
            else if (audioBtns[1].isHovered() && audioBtns[1].isPressed()) {
                boolean isMuted = !settingsModel.isSFXMuted();
                settingsModel.setSFXMuted(isMuted);
                audioController.toggleSFX(isMuted);
                // saveLoad.saveGame();
                // audioController.playSFX("/audio/click.wav"); 
            }

            // --- Các nút ĐỘ KHÓ (0: Easy, 1: Medium, 2: Hard) ---
            else {
                for (int i = 0; i < diffBtns.length; i++) {
                    if (diffBtns[i].isHovered() && diffBtns[i].isPressed()) {
                        settingsModel.setDifficult(i); // Cập nhật độ khó vào model
                        // saveLoad.saveGame();
                        // audioController.playSFX("/audio/click.wav");
                        break; // Click 1 nút rồi thì thoát vòng lặp
                    }
                }
            }

            // --- Nút HOME ---
            if (homeBtn.isHovered() && homeBtn.isPressed()) {
                gameState.setGameState(GameState.MENU); // Trở về màn hình chính
                // audioController.playSFX("/audio/click.wav");
            }
            
            // --- Reset lại toàn bộ trạng thái nút sau khi click ---
            for (AudioButton btn : audioBtns) btn.resetState();
            for (MenuButton btn : diffBtns) btn.resetState();
            homeBtn.resetState();
            
            input.resetMouse(); // Cập nhật lại trạng thái chuột (nếu InputController của bạn yêu cầu)
        }
    }

    // --- CÁC HÀM HỖ TRỢ ĐỂ CODE GỌN HƠN ---

    private void checkAudioButtonStatus(AudioButton btn, int mouseX, int mouseY) {
        if (btn.isHit(mouseX, mouseY)) {
            btn.setHovered(true);
            if (input.isMousePress()) btn.setPressed(true);
        } else {
            btn.setHovered(false);
            btn.setPressed(false);
        }
    }

    private void checkMenuButtonStatus(MenuButton btn, int mouseX, int mouseY) {
        if (btn.isHit(mouseX, mouseY)) {
            btn.setHovered(true);
            if (input.isMousePress()) btn.setPressed(true);
        } else {
            btn.setHovered(false);
            btn.setPressed(false);
        }
    }
}