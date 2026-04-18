package model.state;

public class GameStateModel {
    private GameState state = GameState.MENU;

    public GameState getGameState() {
        return state;
    }

    public void setGameState(GameState state) {
        this.state = state;
    }

}
