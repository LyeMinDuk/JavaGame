package view.renderer.state;

import static core.GameConfig.*;

import java.awt.Font;
import java.awt.Graphics;

public class VictoryRenderer {

    public void render(Graphics g) {
        g.setFont(new Font("QuickSand", Font.BOLD, 24));
        g.drawString("VICTORY", GAME_WIDTH / 2, GAME_HEIGHT /2);
    }
}
