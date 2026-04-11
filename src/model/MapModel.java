package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import view.assets.ResourceManager;
import static core.GameConfig.*;
import static util.AssetsPath.*;

public class MapModel {
    private int[][] map;
    private BufferedImage levelImg;
    private int tileWide;
    private int tileHigh;

    public MapModel() {
        initLevel();
        loadMapData();
    }

    private void initLevel() {
        levelImg = ResourceManager.loadImg(levelMap[0]);
        map = new int[levelImg.getHeight()][levelImg.getWidth()];
    }

    private void loadMapData() {
        tileWide = map[0].length;
        tileHigh = map.length;
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

    public int getTileHigh() {
        return tileHigh;
    }

    public int getTile(int x, int y) {
        return map[y][x];
    }

    public boolean isSolidTile(int tileIdx) {
        return tileIdx != AIR_TILE_INDEX;
    }

    public boolean isSolidAtWorld(double worldX, double worldY) {
        int tileX = (int) Math.floor(worldX / TILE_SIZE);
        int tileY = (int) Math.floor(worldY / TILE_SIZE);

        if (tileX < 0 || tileX >= tileWide || tileY < 0 || tileY >= tileHigh) {
            return true;
        }

        return isSolidTile(map[tileY][tileX]);
    }
}
