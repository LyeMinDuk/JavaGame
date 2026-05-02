package model.entity;

import java.awt.Rectangle;
import java.util.Random;

import static core.GameConfig.*;

public class MageModel extends PlayerModel {
    public static final int SKILL_NORMAL = 0;
    public static final int SKILL_ULT = 1;
    public static final int SKILL_SPECIAL = 2;

    private Rectangle skillBox;
    private int curSkillType = -1;
    private int curSkillIndex = -1;
    private int skillAniIndex = -1;
    private final Random rand = new Random();

    private static final int NORMAL_SIZE = 80;
    private static final int ULT_SIZE = 128;
    private static final int SPECIAL_W = 192;
    private static final int SPECIAL_H = 128;

    public MageModel(double x, double y, int maxHealth) {
        super(x, y, 128, 128, maxHealth);
        this.setHitBox(46, 62, 35, 66);
        this.normalAtkCd = 1500;
    }

    @Override
    public void applyDifficult(int difficult) {
        switch (difficult) {
            case 0 -> {
                this.maxHealth = 10000;
                this.damage = 10000;
                this.maxMana = 10000;
                this.ultimateDamage = this.specialDamage = 50000;
                this.ultimateCost = this.specialCost = 1;
                this.ultCooldown = this.specialCooldown = 1000;
            }
            case 1 -> {
                this.maxHealth = 150;
                this.damage = 60;
                this.maxMana = 500;
                this.ultimateDamage = 180;
                this.ultCooldown = 6000;
                this.ultimateCost = 120;
                this.specialDamage = 150;
                this.specialCost = 80;
                this.specialCooldown = 8000;
            }
            case 2 -> {
                this.maxHealth = 50;
                this.damage = 30;
                this.maxMana = 300;
                this.ultimateDamage = 50;
                this.ultimateCost = 100;
                this.ultCooldown = 12000;
                this.specialDamage = 80;
                this.specialCost = 150;
                this.specialCooldown = 15000;
            }
        }
        this.curHealth = this.maxHealth;
        this.curMana = this.maxMana;
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

    @Override
    public Rectangle getSpecialBox() {
        return getDefaultAttackBox();
    }

    public void startSkill(int type) {
        curSkillType = type;
        switch (type) {
            case SKILL_NORMAL -> curSkillIndex = 0; // SKILL.png
            case SKILL_ULT -> curSkillIndex = rand.nextInt(8); // SKILL1..8
            case SKILL_SPECIAL -> curSkillIndex = rand.nextInt(3); // SKILL9..11
        }
        skillAniIndex = -1;

        Rectangle hb = getHitbox();
        int offset = TILE_SIZE * 3;

        int w, h;
        if (type == SKILL_NORMAL) {
            w = h = (int) (NORMAL_SIZE * SCALE);
        } else if (type == SKILL_ULT) {
            w = h = (int) (ULT_SIZE * SCALE);
        } else {
            w = (int) (SPECIAL_W * SCALE);
            h = (int) (SPECIAL_H * SCALE);
        }

        int x = isFacingRight() ? hb.x + hb.width + offset : hb.x - w - offset;
        int y = hb.y + hb.height / 2 - h / 2;
        skillBox = new Rectangle(x, y, w, h);
    }

    public void clearSkill() {
        skillBox = null;
        curSkillType = -1;
        curSkillIndex = -1;
        skillAniIndex = -1;
    }

    public Rectangle getSkillBox() {
        return skillBox;
    }

    public int getCurSkillType() {
        return curSkillType;
    }

    public int getCurSkillIndex() {
        return curSkillIndex;
    }

    public int getSkillAniIndex() {
        return skillAniIndex;
    }

    public void setSkillAniIndex(int idx) {
        skillAniIndex = idx;
    }
}