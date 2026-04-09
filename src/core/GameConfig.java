package core;

public class GameConfig {
    public static final int FPS = 120;

    public static final int TILE_DEFAULT_SIZE = 32;
    public static final double SCALE = 1.5;
    public static final int TILE_SIZE = (int) (TILE_DEFAULT_SIZE * SCALE);
    public static final int TILE_IN_ROW = 14;
    public static final int TILE_IN_COL = 26;
    public static final int GAME_WIDTH = TILE_SIZE * TILE_IN_COL;
    public static final int GAME_HEIGHT = TILE_SIZE * TILE_IN_ROW;
}
