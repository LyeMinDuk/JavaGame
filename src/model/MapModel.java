package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;
import static util.AssetsPath.*;

public class MapModel {
    private int[][] map;
    private BufferedImage levelImg;
    private int tileWide;

    public MapModel() {
        initLevel();
        loadMapData();
    }

    private void initLevel() {
        levelImg = ResourceManager.loadImg(levelMap[1]);
        map = new int[levelImg.getHeight()][levelImg.getWidth()];
    }

    private void loadMapData() {
        tileWide = map[0].length;
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < tileWide; ++j) {
                Color c = new Color(levelImg.getRGB(j, i));
                map[i][j] = c.getRed();
            }
        }
    }

    public int getTileWide() {
        return tileWide;
    }

    public int getTileHeight() {
        return map.length;
    }

    public int getTile(int x, int y) {
        return map[y][x];
    }
}
