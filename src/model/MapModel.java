package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import view.assets.ResourceManager;
import static util.AssetsPath.*;

public class MapModel {
    private int[][] map;
    private BufferedImage levelImg;
    private int tileWide;
    private Point playerLocation;
    private Point bossLocation;
    private int bossCheckpoint = -1;
    private List<Point> enemyLocation = new ArrayList<>();

    public MapModel(String path) {
        initLevel(path);
        loadMapData();
    }

    private void initLevel(String path) {
        levelImg = ResourceManager.loadImg(path);
        map = new int[levelImg.getHeight()][levelImg.getWidth()];
    }

    private void loadMapData() {
        tileWide = map[0].length;
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < tileWide; ++j) {
                Color c = new Color(levelImg.getRGB(j, i));
                map[i][j] = c.getRed();
                if (c.getGreen() == 150)
                    playerLocation = new Point(j, i);
                if (c.getGreen() == 50) {
                    enemyLocation.add(new Point(j, i));
                }
                if (c.getGreen() == 200) {
                    bossCheckpoint = j;
                }
                if (c.getGreen() == 250) {
                    bossLocation = new Point(j, i);
                }
            }
        }
    }

    public int getTileWide() {
        return tileWide;
    }

    public int getTileHigh() {
        return map.length;
    }

    public int getTile(int x, int y) {
        return map[y][x];
    }

    public Point getPlayerLocation() {
        return playerLocation;
    }

    public List<Point> getEnemyLocation() {
        return enemyLocation;
    }

    public Point getBossLocation() {
        return bossLocation;
    }

    public int getBossCheckpoint() {
        return bossCheckpoint;
    }

}
