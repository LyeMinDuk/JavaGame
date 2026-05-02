package view.renderer.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import model.object.SpikeModel;
import view.assets.ResourceManager;

import static core.GameConfig.SCALE;
import static util.AssetsPath.spikePath;;

public class SpikeRenderer {
    private BufferedImage[] spikeImg;

    public SpikeRenderer() {
        spikeImg = new BufferedImage[4];
        spikeImg = ResourceManager.loadSprite(spikePath, 4, 32, 32);
    }

    public void render(Graphics g, List<SpikeModel> spikeList, int xOffset, int yOffset) {
        for (SpikeModel spike : spikeList) {
            int x = (int) Math.round(spike.getX() - xOffset);
            int y = (int) Math.round(spike.getY() - yOffset);
            g.drawImage(spikeImg[spike.getDirection()], x, y, (int) (32 * SCALE), (int) (32 * SCALE), null);
        }
    }
}
