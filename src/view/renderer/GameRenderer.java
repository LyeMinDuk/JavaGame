package view.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.entity.enemy.EnemyController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import model.state.GameStateModel;
import view.assets.ResourceManager;
import view.renderer.entity.EnemyRenderer;
import view.renderer.entity.PlayerRenderer;
import view.renderer.entity.enemy.SharkRenderer;
import view.renderer.entity.enemy.SkeletonRenderer;
import view.renderer.hud.HealthBarRenderer;
import view.renderer.state.GameOverRenderer;
import view.renderer.state.MenuRenderer;
import view.renderer.state.OptionRenderer;
import view.renderer.state.PlayingRenderer;
import view.renderer.state.VictoryRenderer;
import model.state.SettingsModel;
import model.state.GameState;

public class GameRenderer {
    private MapModel map;
    private PlayerModel player;
    private CameraModel camera;
    private GameStateModel gameState;
    private SettingsModel settingsModel;

    private MenuRenderer menuRenderer;
    private PlayingRenderer playingRenderer;
    private VictoryRenderer victoryRenderer;
    private GameOverRenderer gameOverRenderer;
    private OptionRenderer optionRenderer;

    private EnemyController enemyController;

    public GameRenderer(MapModel map, PlayerModel player, CameraModel camera, EnemyController enemyController,
            GameStateModel gameState, SettingsModel settingsModel) {
        this.map = map;
        this.player = player;
        this.camera = camera;
        this.enemyController = enemyController;
        this.gameState = gameState;
        this.settingsModel = settingsModel;

        initGameStateRenderer();
    }

    private void initGameStateRenderer() {
        menuRenderer = new MenuRenderer();
        playingRenderer = new PlayingRenderer(camera, map, player, enemyController);
        victoryRenderer = new VictoryRenderer();
        gameOverRenderer = new GameOverRenderer();
        optionRenderer = new OptionRenderer();
    }

    public void render(Graphics g) {
        switch (gameState.getGameState()) {
            case GameState.MENU -> menuRenderer.render(g);
            case GameState.PLAYING -> playingRenderer.render(g);
            // case GameState.PAUSED ->
            case GameState.GAME_OVER -> gameOverRenderer.render(g);
            case GameState.OPTIONS -> optionRenderer.render(g, settingsModel);
            case GameState.VICTORY -> victoryRenderer.render(g);
            default -> throw new IllegalArgumentException("Unexpected value: " + gameState.getGameState());
        }
    }

    public void reloadForNewGame(MapModel newMap, PlayerModel newPlayer, CameraModel newCamera,
            EnemyController newEnemyController) {
        this.map = newMap;
        this.player = newPlayer;
        this.camera = newCamera;
        this.enemyController = newEnemyController;

        playingRenderer = new PlayingRenderer(camera, map, player, enemyController);
    }

    public MenuRenderer getMenuRenderer() {
        return menuRenderer;
    }

    public OptionRenderer getOptionRenderer() {
        return optionRenderer;
    }

    public PlayingRenderer getPlayingRenderer() {
        return playingRenderer;
    }

}
