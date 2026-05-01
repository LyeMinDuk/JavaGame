package controller.state;

import controller.AudioController;
import controller.InputController;
import core.Game;
import model.state.GameState;
import model.state.GameStateModel;
import view.renderer.state.ClassSelectRenderer;

public class ClassSelectController {
    private InputController input;
    private GameStateModel gameState;
    private Game game;
    private ClassSelectRenderer renderer;

    public ClassSelectController(Game game, InputController input, GameStateModel gameState,
            ClassSelectRenderer renderer) {
        this.game = game;
        this.input = input;
        this.gameState = gameState;
        this.renderer = renderer;
    }

    public void update(AudioController audio) {
        if (!input.isMouseRelease())
            return;

        int mx = input.getMouseX();
        int my = input.getMouseY();

        int x = renderer.getBoardX();
        int y = renderer.getBoardY();
        int w = renderer.getBoardW();
        int h = renderer.getBoardH();

        if (mx >= x && mx <= x + w && my >= y && my <= y + h) {
            int half = x + w / 2;
            if (mx < half) {
                game.setSelectedClassKnight();
            } else {
                game.setSelectedClassMage();
            }
            audio.playSFX(AudioController.SFX_CLICK);
            game.resetPlaying();
            gameState.setGameState(GameState.PLAYING);
        }
        input.resetMouse();
        input.resetKeys();
    }
}