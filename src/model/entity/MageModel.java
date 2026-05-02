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

    }

    @Override
    public void applyDifficult(int difficult) {
        switch (difficult) {
            case 0 -> {
                this.maxHealth = 260;
                this.damage = 40;
                this.normalAtkCd = 800;
                this.maxMana = 200;
                this.manaRegeneraion = 12;
                this.ultimateDamage = 160;
                this.ultimateCost = 50;
                this.specialDamage = 220;
                this.specialCost = 60;
                this.ultCooldown = 1500;
                this.specialCooldown = 2000;
            }
            case 1 -> {
                this.maxHealth = 180;
                this.damage = 32;
                this.maxMana = 160;
                this.manaRegeneraion = 10;
                this.normalAtkCd = 1000;
                this.ultimateDamage = 130;
                this.ultCooldown = 2500;
                this.ultimateCost = 70;
                this.specialDamage = 180;
                this.specialCost = 80;
                this.specialCooldown = 3500;
            }
            case 2 -> {
                this.maxHealth = 120;
                this.damage = 26;
                this.maxMana = 130;
                this.manaRegeneraion = 8;
                this.normalAtkCd = 1500;
                this.ultimateDamage = 110;
                this.ultimateCost = 90;
                this.ultCooldown = 5000;
                this.specialDamage = 140;
                this.specialCost = 100;
                this.specialCooldown = 6500;
            }
        }
        this.curHealth = this.maxHealth;
        this.curMana = this.maxMana;
    }

    public void startSkill(int type) {
        curSkillType = type;
        switch (type) {
            case SKILL_NORMAL -> curSkillIndex = 0;
            case SKILL_ULT -> curSkillIndex = rand.nextInt(9);
            case SKILL_SPECIAL -> curSkillIndex = rand.nextInt(5);
        }
        skillAniIndex = -1;
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
        int x = isFacingRight() ? hitbox.x + hitbox.width + offset : hitbox.x - w - offset;
        int y = hitbox.y + hitbox.height / 2 - h / 2;
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