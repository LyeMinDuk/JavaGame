package view.renderer.state.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class MenuButton extends UIButton {
    private int rowIndex;
    private BufferedImage[][] buttonImgs;

    public MenuButton(int x, int y, int width, int height, int rowIndex, BufferedImage[][] buttonImgs) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        this.buttonImgs = buttonImgs;
    }

    @Override
    public void draw(Graphics g) {
        int state = 0;
        if (pressed)
            state = 2;
        else if (hovered)
            state = 1;

        g.drawImage(buttonImgs[rowIndex][state], x, y, width, height, null);
    }
    
}