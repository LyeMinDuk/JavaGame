package view.renderer.state.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class AudioButton extends UIButton {
    private boolean muted;
    private BufferedImage[][] buttonImgs;

    public AudioButton(int x, int y, int width, int height, BufferedImage[][] buttonImgs) {
        super(x, y, width, height);
        this.buttonImgs = buttonImgs;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
    }

    @Override
    public void draw(Graphics g) {
        int state = 0;
        if (pressed)
            state = 2;
        else if (hovered)
            state = 1;

        int imgIdx = muted ? 1 : 0;
        g.drawImage(buttonImgs[imgIdx][state], x, y, width, height, null);
    }
}