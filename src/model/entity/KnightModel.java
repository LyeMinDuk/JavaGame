package model.entity;

import java.awt.Rectangle;

import static core.GameConfig.*;

public class KnightModel extends PlayerModel {
    private static final int SPRITE_W = (int) (64 * SCALE);
    private static final int ULT_SPRITE_H = (int) (64 * SCALE);
    private static final int SLASH_W = (int) (64 * SCALE);
    private static final int SLASH_H = (int) (64 * SCALE);
    private static final int SPECIAL_W = (int) (128 * SCALE);
    private static final int SPECIAL_H = (int) (128 * SCALE);

    public KnightModel(double x, double y, int maxHealth) {
        super(x, y, SPRITE_W, (int) (42 * SCALE), maxHealth);
        this.setHitBox((int) (18 * SCALE), (int) (6 * SCALE), (int) (27 * SCALE), (int) (35 * SCALE));
    }

    @Override
    public Rectangle getUltimateBox() {
        int spriteX = (int) x;
        int spriteY = (int) y;
        int slashX = isFacingRight() ? spriteX + SPRITE_W : spriteX - SLASH_W;
        int slashY = spriteY - (ULT_SPRITE_H - height);
        return new Rectangle(slashX, slashY, SLASH_W, SLASH_H);
    }

    @Override
    public Rectangle getSpecialBox() {
        int spriteX = (int) x;
        int drawY = (int) y;
        int skillX = isFacingRight() ? spriteX + SPRITE_W : spriteX - SPECIAL_W;
        int skillY = drawY - (SPECIAL_H - height);
        return new Rectangle(skillX, skillY, SPECIAL_W, SPECIAL_H);
    }
}