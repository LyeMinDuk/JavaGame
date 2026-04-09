package core;

import java.awt.Graphics;

import controller.InputController;
import view.GameWindow;
import view.GamePanel;
import static core.GameConfig.*;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private boolean running;
    private Thread gameThread;

    private InputController input;

    public Game() {

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

    }

    public void render(Graphics g) {

    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000 / FPS;

        long prevTime = System.nanoTime();
        double deltaFrame = 0;

        while(running){
            long curTime = System.nanoTime();
            deltaFrame += (curTime - prevTime) / timePerFrame;
            prevTime = curTime;

            if(deltaFrame >= 1){
                update();
                gamePanel.repaint();
                deltaFrame--;
            }
        }
    }

}
