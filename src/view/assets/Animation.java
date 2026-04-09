package view.assets;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int speed;
    private int tick;
    private int index;

    public Animation(BufferedImage[] frames, int speed) {
        this.frames = frames;
        this.speed = speed;
    }

    public void reset() {
        tick = index = 0;
    }

    public void runAni() {
        tick++;
        if (tick >= speed) {
            tick = 0;
            index++;
            if (index >= frames.length) {
                index = 0;
            }
        }
    }

    public BufferedImage getCurFrame() {
        return frames[index];
    }
}
