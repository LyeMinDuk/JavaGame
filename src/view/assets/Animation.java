package view.assets;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int speed;
    private int tick;
    private int index;
    private boolean loop;

    public Animation(BufferedImage[] frames, int speed, boolean loop) {
        this.frames = frames;
        this.speed = speed;
        this.loop = loop;
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
                if (loop) {
                    index = 0;
                } else {
                    index = frames.length - 1;
                }
            }
        }
    }

    public BufferedImage getCurFrame() {
        return frames[index];
    }

    public int getFrameIdx() {
        return index;
    }

    public boolean isLastFrame() {
        return index == frames.length - 1;
    }

    public boolean isFinished() {
        return !loop && index == frames.length - 1;
    }
}
