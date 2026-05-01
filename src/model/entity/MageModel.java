package model.entity;

import java.awt.Rectangle;

import static core.GameConfig.TILE_SIZE;
import static core.GameConfig.SCALE;

public class MageModel extends PlayerModel {
    public MageModel(double x, double y, int width, int height, int maxHealth) {
        super(x, y, width, height, maxHealth);
    }

    @Override
    public Rectangle getDefaultAttackBox() {
        Rectangle hb = getHitbox();
        int atkW = (int) (32 * SCALE);
        int atkH = (int) (20 * SCALE);
        int offset = TILE_SIZE * 3;
        int atkX = isFacingRight() ? hb.x + hb.width + offset : hb.x - atkW - offset;
        int atkY = hb.y + (hb.height - atkH) / 2;
        return new Rectangle(atkX, atkY, atkW, atkH);
    }
}