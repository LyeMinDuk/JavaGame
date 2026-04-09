package model;

import static core.GameConfig.*;

public class CameraModel {
    private int xOffset;
    private final int leftBorder = (int) (0.4 * GAME_WIDTH);
    private final int rightBorder = (int) (0.6 * GAME_WIDTH);

    public void update(int playerX, int tileWide){
        int diff = playerX - xOffset;
        if(diff > rightBorder){
            xOffset += diff - rightBorder;
        }
        else if(diff < leftBorder){
            xOffset += diff - leftBorder;
        }

        int maxOffset = tileWide * TILE_SIZE - GAME_WIDTH;
        if(xOffset < 0){
            xOffset = 0;
        }
        if(xOffset > maxOffset){
            xOffset = maxOffset;
        }
    }

    public int getXOffset(){
        return xOffset;
    }
}
