package view.assets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ResourceManager {
    public static BufferedImage loadImg(String path) {
        BufferedImage img = null;
        InputStream is = ResourceManager.class.getResourceAsStream(path);
        if (is == null) {
            System.out.println("File not Found: " + path);
        } else {
            try {
                img = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] loadSprite(String path, int frames, int width, int height) {
        BufferedImage img = loadImg(path);
        BufferedImage[] sprite = new BufferedImage[frames];

        for (int i = 0; i < frames; ++i) {
            sprite[i] = img.getSubimage(i * width, 0, width, height);
        }

        return sprite;
    }
}
