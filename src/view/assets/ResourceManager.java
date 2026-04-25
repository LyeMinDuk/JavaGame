package view.assets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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

    public static Clip loadClip(String path) {
        Clip clip = null;
        // Với âm thanh, ta dùng getResource (trả về URL) sẽ an toàn hơn
        // getResourceAsStream
        URL url = ResourceManager.class.getResource(path);

        if (url == null) {
            System.out.println("Audio file not Found: " + path);
        } else {
            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
            } catch (Exception e) {
                System.out.println("Error loading audio: " + path);
                e.printStackTrace();
            }
        }
        return clip;
    }
}
