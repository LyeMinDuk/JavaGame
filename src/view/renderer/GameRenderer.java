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
import view.renderer.state.PlayingRenderer;
import view.renderer.state.VictoryRenderer;
import model.state.GameState;

public class GameRenderer {
    private MapModel map;
    private PlayerModel player;
    private CameraModel camera;
    private GameStateModel gameState;
    private MenuRenderer menuRenderer;
    private PlayingRenderer playingRenderer;
    private VictoryRenderer victoryRenderer;
    private GameOverRenderer gameOverRenderer;

    private MapRenderer mapRenderer;
    private PlayerRenderer playerRenderer;
    private EnemyRenderer enemyRenderer;
    private HealthBarRenderer healthBarRenderer;
    private EnemyController enemyController;

    public GameRenderer(MapModel map, PlayerModel player, CameraModel camera, EnemyController enemyController,
            GameStateModel gameState) {
        this.map = map;
        this.player = player;
        this.camera = camera;
        this.enemyController = enemyController;
        this.gameState = gameState;
        healthBarRenderer = new HealthBarRenderer();
        loadMapTexture();

        loadPlayerAnimation();
        initEnemyRenderer();
        initGameStateRenderer();
    }

    private void initGameStateRenderer() {
        menuRenderer = new MenuRenderer();
        playingRenderer = new PlayingRenderer(camera, map, player, mapRenderer, playerRenderer, enemyRenderer,
                enemyController, healthBarRenderer);
        victoryRenderer = new VictoryRenderer();
        gameOverRenderer = new GameOverRenderer();
    }

    private void initEnemyRenderer() {
        enemyRenderer = new SharkRenderer();
    }

    private void loadPlayerAnimation() {
        playerRenderer = new PlayerRenderer();
    }

    public void loadMapTexture() {
        BufferedImage outside = ResourceManager.loadImg("/outside_sprites.png");

        BufferedImage[] tiles = new BufferedImage[48];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                tiles[i * 12 + j] = outside.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
        mapRenderer = new MapRenderer(tiles);
    }

    public void update() {
        playerRenderer.update(player);
        enemyRenderer.updateAll(enemyController.getListEnemy());
    }

    public void render(Graphics g) {
        switch (gameState.getGameState()) {
            case GameState.MENU -> menuRenderer.render(g);
            case GameState.PLAYING -> playingRenderer.render(g);
            // case GameState.PAUSED ->
            case GameState.GAME_OVER -> gameOverRenderer.render(g);
            // case GameState.OPTIONS ->
            case GameState.VICTORY -> victoryRenderer.render(g);
            default -> throw new IllegalArgumentException("Unexpected value: " + gameState.getGameState());
        }
    }

    public MenuRenderer getMenuRenderer() {
        return menuRenderer;
    }

}
