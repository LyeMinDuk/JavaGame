package model;

import static core.GameConfig.*;

public class CameraModel {
    private int xOffset, yOffset;
    private int minOffsetX = 0;
    private final int leftBorder = (int) (0.4 * GAME_WIDTH);
    private final int rightBorder = (int) (0.6 * GAME_WIDTH);
    private final int topBorder = (int) (0.2 * GAME_HEIGHT);
    private final int botBorder = (int) (0.8 * GAME_HEIGHT);

    public void update(int playerX, int playerY, int tileWide, int tileHeight) {
        int diffX = playerX - xOffset;
        int diffY = playerY - yOffset;
        if (diffX > rightBorder) {
            xOffset += diffX - rightBorder;
        } else if (diffX < leftBorder) {
            xOffset += diffX - leftBorder;
        }
        if (diffY < topBorder) {
            yOffset += diffY - topBorder;
        } else if (diffY > botBorder) {
            yOffset += diffY - botBorder;
        }
        int maxOffsetX = tileWide * TILE_SIZE - GAME_WIDTH;
        int maxOffsetY = tileHeight * TILE_SIZE - GAME_HEIGHT;
        xOffset = Math.max(minOffsetX, Math.min(xOffset, maxOffsetX));
        yOffset = Math.max(0, Math.min(yOffset, maxOffsetY));
    }

    public int getOffsetX() {
        return xOffset;
    }

    public int getOffsetY() {
        return yOffset;
    }

    public void setMinOffsetX(int minOffsetX) {
        this.minOffsetX = minOffsetX;
    }

}