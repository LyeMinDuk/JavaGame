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

    public void render(Graphics g, MapModel map, int xOffset){
        int tileWide = map.getTileWide();
        int firstCol = xOffset / TILE_SIZE;
        int lastCol = (xOffset + GAME_WIDTH) / TILE_SIZE + 1;

        if(firstCol < 0){
            firstCol = 0;
        }
        if(lastCol > tileWide){
            lastCol = tileWide;
        }
        
        for(int i = 0; i < TILE_IN_ROW; ++i){
            for(int j = firstCol; j < lastCol; ++j){
                int idx = map.getTile(j, i);
                if (idx < 0 || idx >= tiles.length || tiles[idx] == null){
                    idx = 11;
                }
                g.drawImage(tiles[idx], j * TILE_SIZE - xOffset, i * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
            }
        }
    }
}
