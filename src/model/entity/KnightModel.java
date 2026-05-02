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
        int slashX = facingRight ? (int) x + SPRITE_W : (int) x - SLASH_W;
        int slashY = (int) y - (ULT_SPRITE_H - height);
        return new Rectangle(slashX, slashY, SLASH_W, SLASH_H);
    }

    @Override
    public Rectangle getSpecialBox() {
        int skillX = facingRight ? (int) x + SPRITE_W : (int) x - SPECIAL_W;
        int skillY = (int) y - (SPECIAL_H - height);
        return new Rectangle(skillX, skillY, SPECIAL_W, SPECIAL_H);
    }

}