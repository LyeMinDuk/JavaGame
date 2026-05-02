package view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import controller.InputController;
import core.Game;
import static core.GameConfig.*;

public class GamePanel extends JPanel {
    private Game game;

    public GamePanel(Game game, InputController input) {
        this.game = game;
        this.addKeyListener(input);
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

}