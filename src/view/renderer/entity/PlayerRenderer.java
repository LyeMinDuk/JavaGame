package view.renderer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import model.entity.KnightModel;
import model.entity.MageModel;
import model.entity.PlayerModel;
import util.PlayerStateIndex.KnightFrames;
import util.PlayerStateIndex.MageFrames;
import view.assets.Animation;
import view.assets.ResourceManager;

import static util.PlayerStateIndex.*;
import static view.renderer.entity.EntityRenderer.drawHB;
import static core.GameConfig.SCALE;
import static util.AssetsPath.*;

public class PlayerRenderer {
    private Animation[] knightAni = new Animation[MAX_STATE];
    private Animation[] mageAni = new Animation[MAX_STATE];
    private Animation knightSlash;
    private Animation knightSpecial;
    private Animation mageNormal;
    private Animation[] mageUlt = new Animation[8];
    private Animation[] mageSpecial = new Animation[3];
    private static final int[] SKILL_FRAMES_NORMAL = { 6 };
    private static final int[] SKILL_FRAMES_ULT = { 15, 6, 15, 20, 20, 15, 7, 12 };
    private static final int[] SKILL_FRAMES_SPECIAL = { 11, 9, 11 };
    private int lastState = -1;
    private static final boolean debug = true;

    public PlayerRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        knightAni[IDLE] = new Animation(ResourceManager.loadSprite(playerIdle, KnightFrames.FRAMES.get(IDLE), 64, 42),
                25, true);
        knightAni[RUN] = new Animation(ResourceManager.loadSprite(playerRun, KnightFrames.FRAMES.get(RUN), 64, 42), 15,
                true);
        knightAni[JUMP] = new Animation(ResourceManager.loadSprite(playerJump, KnightFrames.FRAMES.get(JUMP), 64, 42),
                15, false);
        knightAni[HURT] = new Animation(ResourceManager.loadSprite(playerHurt, KnightFrames.FRAMES.get(HURT), 64, 42),
                15, false);
        knightAni[ATTACK] = new Animation(
                ResourceManager.loadSprite(playerAttack, KnightFrames.FRAMES.get(ATTACK), 64, 42), 15, false);
        knightAni[ULTIMATE] = new Animation(
                ResourceManager.loadSprite(playerUltimate, KnightFrames.FRAMES.get(ULTIMATE), 64, 64), 10, false);
        knightAni[FROZEN] = new Animation(
                ResourceManager.loadSprite(playerFrozen, KnightFrames.FRAMES.get(FROZEN), 64, 42), 20, false);
        knightAni[SPECIAL] = new Animation(
                ResourceManager.loadSprite(playerSpecial, KnightFrames.FRAMES.get(SPECIAL), 64, 42), 15, false);
        knightSlash = new Animation(
                ResourceManager.loadSprite(ultimateSlash, KnightFrames.FRAMES.get(ULTIMATE), 64, 64), 10, false);
        knightSpecial = new Animation(
                ResourceManager.loadSprite(specialSkill, KnightFrames.FRAMES.get(SPECIAL), 128, 128), 15, false);

        mageAni[IDLE] = new Animation(ResourceManager.loadSprite(mageIdle, MageFrames.FRAMES.get(IDLE), 128, 128), 25,
                true);
        mageAni[RUN] = new Animation(ResourceManager.loadSprite(mageRun, MageFrames.FRAMES.get(RUN), 128, 128), 15,
                true);
        mageAni[JUMP] = new Animation(ResourceManager.loadSprite(mageJump, MageFrames.FRAMES.get(JUMP), 128, 128), 16,
                false);
        mageAni[HURT] = new Animation(ResourceManager.loadSprite(mageHurt, MageFrames.FRAMES.get(HURT), 128, 128), 15,
                false);
        mageAni[ATTACK] = new Animation(ResourceManager.loadSprite(mageAttack, MageFrames.FRAMES.get(ATTACK), 128, 128),
                13, false);
        mageAni[FROZEN] = new Animation(
                ResourceManager.loadSprite(mageFrozen, MageFrames.FRAMES.get(FROZEN), 128, 128), 20, false);
        mageAni[ULTIMATE] = new Animation(
                ResourceManager.loadSprite(mageUltimate, MageFrames.FRAMES.get(ULTIMATE), 128, 128), 11, false);
        mageAni[SPECIAL] = new Animation(
                ResourceManager.loadSprite(mageSpecialAni, MageFrames.FRAMES.get(SPECIAL), 128, 128), 26, false);
        mageNormal = new Animation(ResourceManager.loadSprite(mageSkill, SKILL_FRAMES_NORMAL[0], 80, 80), 15, false);

        mageUlt[0] = new Animation(ResourceManager.loadSprite(mageSkill1, SKILL_FRAMES_ULT[0], 128, 128), 11, false);
        mageUlt[1] = new Animation(ResourceManager.loadSprite(mageSkill2, SKILL_FRAMES_ULT[1], 128, 128), 29, false);
        mageUlt[2] = new Animation(ResourceManager.loadSprite(mageSkill3, SKILL_FRAMES_ULT[2], 128, 128), 11, false);
        mageUlt[3] = new Animation(ResourceManager.loadSprite(mageSkill4, SKILL_FRAMES_ULT[3], 128, 128), 8, false);
        mageUlt[4] = new Animation(ResourceManager.loadSprite(mageSkill5, SKILL_FRAMES_ULT[4], 128, 128), 8, false);
        mageUlt[5] = new Animation(ResourceManager.loadSprite(mageSkill6, SKILL_FRAMES_ULT[5], 128, 128), 11, false);
        mageUlt[6] = new Animation(ResourceManager.loadSprite(mageSkill7, SKILL_FRAMES_ULT[6], 128, 128), 25, false);
        mageUlt[7] = new Animation(ResourceManager.loadSprite(mageSkill8, SKILL_FRAMES_ULT[7], 128, 128), 7, false);

        mageSpecial[0] = new Animation(ResourceManager.loadSprite(mageSkill9, SKILL_FRAMES_SPECIAL[0], 192, 128), 21,
                false);
        mageSpecial[1] = new Animation(ResourceManager.loadSprite(mageSkill10, SKILL_FRAMES_SPECIAL[1], 192, 128), 25,
                false);
        mageSpecial[2] = new Animation(ResourceManager.loadSprite(mageSkill11, SKILL_FRAMES_SPECIAL[2], 192, 128), 21,
                false);
    }

    public void update(PlayerModel player) {
        int state = player.getState();
        if (state != lastState) {
            getAniSet(player)[state].reset();
            if (player instanceof KnightModel && state == ULTIMATE) {
                knightSlash.reset();
            }
            if (player instanceof KnightModel && state == SPECIAL) {
                knightSpecial.reset();
            }
            lastState = state;
        }
        Animation cur = getAniSet(player)[state];
        cur.runAni();
        player.setAniIndex(cur.getFrameIdx());
        if (player instanceof KnightModel && state == ULTIMATE) {
            knightSlash.runAni();
        }
        if (player instanceof KnightModel && state == SPECIAL) {
            knightSpecial.runAni();
        }
        if (state == ATTACK && cur.isLastFrame())
            player.setAtking(false);
        if ((state == ULTIMATE || state == SPECIAL) && cur.isLastFrame()) {
            player.setUltimate(false);
            player.setSpecial(false);
        }

        if (player instanceof MageModel mage && mage.getSkillBox() != null) {
            Animation skillAni = getMageSkillAni(mage);
            if (skillAni != null && mage.getSkillAniIndex() == -1) {
                skillAni.reset();
            }
            if (skillAni != null) {
                skillAni.runAni();
                mage.setSkillAniIndex(skillAni.getFrameIdx());
                if (skillAni.isFinished()) {
                    mage.clearSkill();
                }
            }
        }
    }

    public void render(Graphics g, PlayerModel player, int xOffset, int yOffset) {
        if (debug)
            drawBox(g, player, xOffset, yOffset);

        if (player instanceof MageModel mage && mage.getSkillBox() != null) {
            Rectangle box = mage.getSkillBox();
            Animation skillAni = getMageSkillAni(mage);
            if (skillAni != null) {
                g.drawImage(skillAni.getCurFrame(), box.x - xOffset, box.y - yOffset, box.width, box.height, null);
            }
        }

        int state = player.getState();
        Animation cur = getAniSet(player)[state];

        int drawW = player.getWidth();
        int drawH = player.getHeight();
        if (player instanceof KnightModel && state == ULTIMATE) {
            drawH = (int) (64 * SCALE);
        }
        int drawX = (int) player.getX() - xOffset;
        int drawY = (int) player.getY() - (drawH - player.getHeight()) - yOffset;

        if (player.isFacingRight()) {
            g.drawImage(cur.getCurFrame(), drawX, drawY, drawW, drawH, null);

            if (player instanceof KnightModel && state == ULTIMATE) {
                g.drawImage(knightSlash.getCurFrame(), drawX + drawW, drawY, drawW, drawH, null);
            }

            if (player instanceof KnightModel && state == SPECIAL) {
                int skillW = (int) (128 * SCALE);
                int skillH = (int) (128 * SCALE);
                int skillY = drawY - (skillH - drawH);

                int skillX = player.isFacingRight() ? drawX + drawW : drawX - skillW;
                int drawSkillX = player.isFacingRight() ? skillX : skillX + skillW;
                int drawSkillW = player.isFacingRight() ? skillW : -skillW;
                g.drawImage(knightSpecial.getCurFrame(), drawSkillX, skillY, drawSkillW, skillH, null);
            }
        } else {
            g.drawImage(cur.getCurFrame(), drawX + drawW, drawY, -drawW, drawH, null);

            if (player instanceof KnightModel && state == ULTIMATE) {
                g.drawImage(knightSlash.getCurFrame(), drawX, drawY, -drawW, drawH, null);
            }

            if (player instanceof KnightModel && state == SPECIAL) {
                int skillW = (int) (128 * SCALE);
                int skillH = (int) (128 * SCALE);
                int skillX = drawX - skillW;
                int skillY = drawY - (skillH - drawH);
                g.drawImage(knightSpecial.getCurFrame(), skillX + skillW, skillY, -skillW, skillH, null);
            }
        }
    }

    private Animation[] getAniSet(PlayerModel player) {
        return (player instanceof MageModel) ? mageAni : knightAni;
    }

    private Animation getMageSkillAni(MageModel mage) {
        return switch (mage.getCurSkillType()) {
            case MageModel.SKILL_NORMAL -> mageNormal;
            case MageModel.SKILL_ULT -> mageUlt[mage.getCurSkillIndex()];
            case MageModel.SKILL_SPECIAL -> mageSpecial[mage.getCurSkillIndex()];
            default -> null;
        };
    }

    private void drawBox(Graphics g, PlayerModel player, int xOffset, int yOffset) {
        drawHB(g, player, xOffset, yOffset);

        Rectangle atkBox = player.getAttackBox();
        if (atkBox != null && player.isAtking()) {
            g.setColor(Color.BLACK);
            g.drawRect(atkBox.x - xOffset, atkBox.y - yOffset, atkBox.width, atkBox.height);
        }
        Rectangle ultBox = player.getUltimateBox();
        if (player instanceof MageModel mage)
            ultBox = mage.getSkillBox();
        if (ultBox != null && player.isUltimate()) {
            g.setColor(Color.BLACK);
            g.drawRect(ultBox.x - xOffset, ultBox.y - yOffset, ultBox.width, ultBox.height);
        }
        Rectangle skillBox = player.getSpecialBox();
        if (player instanceof MageModel mage)
            skillBox = mage.getSkillBox();
        if (skillBox != null && player.isSpecial()) {
            g.setColor(Color.BLUE);
            g.drawRect(skillBox.x - xOffset, skillBox.y - yOffset, skillBox.width, skillBox.height);
        }
    }
}