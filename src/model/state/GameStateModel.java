package model.state;

public class GameStateModel {
    private GameState state = GameState.MENU;
    private boolean showHelp = false;

    public GameState getGameState() {
        return state;
    }

    public void setGameState(GameState state) {
        this.state = state;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }

}
