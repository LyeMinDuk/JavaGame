package view.renderer;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

import model.MapModel;

import static core.GameConfig.*;

public class MapRenderer {
    private BufferedImage[] tiles;

    public MapRenderer(BufferedImage[] tiles) {
        this.tiles = tiles;
    }

    public void render(Graphics g, MapModel map, int xOffset, int yOffset) {
        int tileWide = map.getTileWide();
        int tileHigh = map.getTileHigh();
        int firstCol = xOffset / TILE_SIZE;
        int lastCol = (xOffset + GAME_WIDTH) / TILE_SIZE + 1;
        int firstRow = yOffset / TILE_SIZE;
        int lastRow = (yOffset + GAME_HEIGHT) / TILE_SIZE + 1;

        if (firstCol < 0) {
            firstCol = 0;
        }
        if (lastCol > tileWide) {
            lastCol = tileWide;
        }

        if (firstRow < 0) {
            firstRow = 0;
        }
        if (lastRow > tileHigh) {
            lastRow = tileHigh;
        }

        for (int i = firstRow; i < lastRow; ++i) {
            for (int j = firstCol; j < lastCol; ++j) {
                int idx = map.getTile(j, i);
                g.drawImage(tiles[idx], j * TILE_SIZE - xOffset, i * TILE_SIZE - yOffset, TILE_SIZE, TILE_SIZE, null);
            }
        }
    }
}
