package core;

import java.awt.Graphics;

import controller.InputController;
import controller.WorldController;
import controller.entity.PlayerController;
import model.CameraModel;
import model.MapModel;
import model.entity.PlayerModel;
import view.GameWindow;
import view.renderer.GameRenderer;
import view.GamePanel;
import static core.GameConfig.*;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private boolean running;
    private Thread gameThread;

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
        player = new PlayerModel(150, 150, (int) (64 * GameConfig.SCALE), (int) (40 * GameConfig.SCALE), 100);

        input = new InputController();
        playerController = new PlayerController(input, 2.0, -1.0);
        worldController = new WorldController(map, camera);

        renderer = new GameRenderer(map, player, camera);
        gamePanel = new GamePanel(this, input);
        gameWindow = new GameWindow(gamePanel);

        running = true;
        startGame();
    }

    private void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        playerController.update(player);
        worldController.update(player);
        player.refreshState();
        renderer.update();
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
