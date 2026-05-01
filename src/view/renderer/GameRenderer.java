package view.renderer;

import java.awt.Graphics;
import java.util.List;

import core.Game;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import model.object.SpikeModel;
import model.state.GameStateModel;
import view.renderer.state.GameOverRenderer;
import view.renderer.state.MenuRenderer;
import view.renderer.state.OptionRenderer;
import view.renderer.state.PausedRenderer;
import view.renderer.state.PlayingRenderer;
import view.renderer.state.VictoryRenderer;
import model.state.SettingsModel;
import model.state.GameState;

import static util.AssetsPath.*;

public class GameRenderer {
    private MapModel map;
    private PlayerModel player;
    private CameraModel camera;
    private GameStateModel gameState;
    private SettingsModel settingsModel;
    private Game game;

    private MenuRenderer menuRenderer;
    private PlayingRenderer playingRenderer;
    private VictoryRenderer victoryRenderer;
    private GameOverRenderer gameOverRenderer;
    private OptionRenderer optionRenderer;
    private PausedRenderer pausedRenderer;

    private List<EnemyModel> enemies;
    private List<SpikeModel> spikes;

    public GameRenderer(MapModel map, PlayerModel player, Game game, CameraModel camera, List<EnemyModel> enemies,
            List<SpikeModel> spikes, GameStateModel gameState, SettingsModel settingsModel) {
        this.map = map;
        this.player = player;
        this.game = game;
        this.camera = camera;
        this.gameState = gameState;
        this.settingsModel = settingsModel;
        this.enemies = enemies;
        this.spikes = spikes;
        initGameStateRenderer();
    }

    private void initGameStateRenderer() {
        menuRenderer = new MenuRenderer();
        playingRenderer = new PlayingRenderer(camera, map, player, game.getCurMapIdx(), enemies, spikes,
                game.getWorldController().getQuest());
        victoryRenderer = new VictoryRenderer();
        gameOverRenderer = new GameOverRenderer();
        optionRenderer = new OptionRenderer();
        pausedRenderer = new PausedRenderer();
    }

    public void render(Graphics g, int curMapIdx) {
        switch (gameState.getGameState()) {
            case GameState.MENU -> menuRenderer.render(g);
            case GameState.PLAYING -> playingRenderer.render(g);
            case GameState.PAUSED -> {
                playingRenderer.render(g);
                pausedRenderer.render(g, settingsModel);
            }
            case GameState.GAME_OVER -> {
                playingRenderer.render(g);
                gameOverRenderer.render(g);
            }
            case GameState.OPTIONS -> optionRenderer.render(g, settingsModel);
            case GameState.VICTORY -> {
                playingRenderer.render(g);
                boolean isGameCompleted = curMapIdx >= levelMap.length - 1;
                victoryRenderer.render(g, isGameCompleted);
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + gameState.getGameState());
        }
    }

    public void reloadForNewGame(MapModel newMap, PlayerModel newPlayer, CameraModel newCamera,
            List<EnemyModel> enemies, List<SpikeModel> spikes) {
        this.map = newMap;
        this.player = newPlayer;
        this.camera = newCamera;
        playingRenderer = new PlayingRenderer(camera, map, player, game.getCurMapIdx(), enemies, spikes,
                game.getWorldController().getQuest());
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

    public VictoryRenderer getVictoryRenderer() {
        return victoryRenderer;
    }

    public PausedRenderer getPausedRenderer() {
        return pausedRenderer;
    }

    public GameOverRenderer getGameOverRenderer() {
        return gameOverRenderer;
    }
}
