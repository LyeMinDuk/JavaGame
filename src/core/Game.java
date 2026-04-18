package core;

import java.awt.Graphics;

import controller.InputController;
import controller.WorldController;
import controller.entity.PlayerController;
import controller.state.GameOverController;
import controller.state.MenuController;
import controller.state.PlayingController;
import controller.state.VictoryController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import model.state.GameState;
import model.state.GameStateModel;
import view.GameWindow;
import view.renderer.GameRenderer;
import view.GamePanel;
import static core.GameConfig.*;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private boolean running;
    private Thread gameThread;

    private GameStateModel gameState;
    private MenuController menuController;
    private PlayingController playingController;
    private VictoryController victoryController;
    private GameOverController gameOverController;

    private InputController input;
    private PlayerModel player;
    private MapModel map;
    private CameraModel camera;
    private PlayerController playerController;
    private WorldController worldController;
    private GameRenderer renderer;

    public Game() {
        map = new MapModel();
        camera = new CameraModel();
        player = new PlayerModel(170, 150, (int) (64 * SCALE), (int) (40 * SCALE), 100);

        gameState = new GameStateModel();
        input = new InputController();
        playerController = new PlayerController(input);
        worldController = new WorldController(map, camera, playerController, gameState);

        renderer = new GameRenderer(map, player, camera, worldController.getEnemyController(), gameState);
        gamePanel = new GamePanel(this, input);
        gameWindow = new GameWindow(gamePanel);

        initGameStateController();

        startGame();
    }

    private void initGameStateController() {
        menuController = new MenuController(input, gameState, renderer.getMenuRenderer());
        playingController = new PlayingController(input, gameState, worldController, player, renderer);
        victoryController = new VictoryController(input, gameState);
        gameOverController = new GameOverController(input, gameState);
    }

    private void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (gameState.getGameState()) {
            case GameState.MENU -> menuController.update();
            case GameState.PLAYING -> playingController.update();
            // case GameState.PAUSED -> ;
            case GameState.GAME_OVER -> gameOverController.update();
            // case GameState.OPTIONS -> ;
            case GameState.VICTORY -> victoryController.update();
            default -> throw new IllegalArgumentException("Unexpected value: " + gameState.getGameState());
        }
    }

    public void render(Graphics g) {
        renderer.render(g);
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000 / FPS;

        long prevTime = System.nanoTime();
        double deltaFrame = 0;

        while (running) {
            long curTime = System.nanoTime();
            deltaFrame += (curTime - prevTime) / timePerFrame;
            prevTime = curTime;

            if (deltaFrame >= 1) {
                update();
                gamePanel.repaint();
                deltaFrame--;
            }
        }
    }

}
