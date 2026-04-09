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
}
