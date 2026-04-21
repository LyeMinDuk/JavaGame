package controller.state;

import controller.InputController;
import model.state.GameState;
import model.state.GameStateModel;
import model.state.SettingsModel;
import view.renderer.state.OptionRenderer;
import view.renderer.state.ui.AudioButton;
import view.renderer.state.ui.MenuButton;

public class OptionController {
    private InputController input;
    private GameStateModel gameState;
    private OptionRenderer view;
    private SettingsModel settingsModel;

    public OptionController(InputController input, GameStateModel gameState, OptionRenderer view,
            SettingsModel settingsModel) {
        this.input = input;
        this.gameState = gameState;
        this.view = view;
        this.settingsModel = settingsModel;
    }

    public void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();
        boolean isPress = input.isMousePress();
        boolean isRelease = input.isMouseRelease();

        // 1. Lấy các nút từ View
        AudioButton[] audioBtns = view.getAudioButtons();
        MenuButton[] diffBtns = view.getDiffButtons();
        MenuButton homeBtn = view.getHomeButton();

        // 2. Reset Hover
        for (AudioButton btn : audioBtns)
            btn.setHovered(false);
        for (MenuButton btn : diffBtns)
            btn.setHovered(false);
        homeBtn.setHovered(false);

        // 3. Xử lý va chạm Audio
        for (int i = 0; i < audioBtns.length; i++) {
            if (audioBtns[i].isHit(mx, my)) {
                audioBtns[i].setHovered(true);
                if (isPress)
                    audioBtns[i].setPressed(true);
                if (isRelease && audioBtns[i].isPressed()) {
                    if (i == 0)
                        settingsModel.setMusicMuted(!settingsModel.isMusicMuted());
                    if (i == 1)
                        settingsModel.setSFXMuted(!settingsModel.isSFXMuted());
                }
            }
        }

        // 4. Xử lý va chạm Độ khó
        for (int i = 0; i < diffBtns.length; i++) {
            if (diffBtns[i].isHit(mx, my)) {
                diffBtns[i].setHovered(true);
                // Cập nhật model ngay khi press (như radio button)
                if (isPress) {
                    settingsModel.setDifficulty(i);
                }
            }
        }

        // 5. Xử lý va chạm nút Home
        if (homeBtn.isHit(mx, my)) {
            homeBtn.setHovered(true);
            if (isPress)
                homeBtn.setPressed(true);
            if (isRelease && homeBtn.isPressed()) {
                gameState.setGameState(GameState.MENU); // Quay lại Menu
            }
        }

        // 6. Reset khi thả chuột
        if (isRelease) {
            for (AudioButton btn : audioBtns)
                btn.resetState();
            homeBtn.resetState();
            input.resetMouse();
        }
    }
}