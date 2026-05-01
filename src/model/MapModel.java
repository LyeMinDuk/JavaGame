package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import view.assets.ResourceManager;

public class MapModel {
    public static final int ENEMY_TYPE_SHARK = 1;
    public static final int ENEMY_TYPE_SKELETON = 2;
    public static final int ENEMY_TYPE_DEMON_SLIME = 3;
    public static final int ENEMY_TYPE_MINOTAUR = 4;
    public static final int ENEMY_TYPE_FROST_GUARDIAN = 5;
    public static final int ENEMY_TYPE_CTHULU = 6;
    public static final int ENEMY_TYPE_GOLEM = 7;

    private int[][] map;
    private BufferedImage levelImg;

    private int tileWide;
    private Point playerLocation;
    private int bossCheckpoint = -1;
    private List<int[]> enemySpawns = new ArrayList<>();
    private List<Point> spikeSpawns = new ArrayList<>();

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
                if (c.getRed() == 100) {
                    map[i][j] = 11;
                    spikeSpawns.add(new Point(j, i));
                } else {
                    map[i][j] = c.getRed();
                }
                if (c.getGreen() == 150) {
                    playerLocation = new Point(j, i);
                } else if (c.getGreen() == 50) {
                    int type = c.getBlue();
                    enemySpawns.add(new int[] { j, i, type });
                } else if (c.getGreen() == 200) {
                    bossCheckpoint = j;
                } else if (c.getGreen() == 250) {
                    int type = c.getBlue();
                    enemySpawns.add(new int[] { j, i, type });
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

    public int getBossCheckpoint() {
        return bossCheckpoint;
    }

    public List<int[]> getEnemySpawns() {
        return enemySpawns;
    }

    public List<Point> getSpikeSpawns() {
        return spikeSpawns;
    }

}