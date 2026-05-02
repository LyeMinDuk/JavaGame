package controller.entity;

import java.util.Arrays;
import java.util.List;
import java.awt.Rectangle;

import controller.AudioController;
import controller.InputController;
import model.entity.MageModel;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;

import static util.enemy.EnemyAIState.*;

public class PlayerController {
    private final InputController input;
    private final int atkHitFrame = 3;
    private final int[] knightUltHitFrames = { 4, 9, 13 };
    private final int[] knightSpecialHitFrames = { 3, 6, 8, 14 };
    private final int[] mageUltHitFrames = { 7, 3, 7, 12, 12, 9, 2, 7 };
    private final int[] mageSpecialHitFrames = { 7, 4, 6 };

    private boolean[] ultFrameChecked = new boolean[20];
    private boolean[] specialFrameChecked = new boolean[20];

    public PlayerController(InputController input) {
        this.input = input;
    }

    public void update(PlayerModel player, List<EnemyModel> enemies, AudioController audio) {
        if (player.isFrozen()) {
            player.setDx(0);
            player.setDy(0);
            player.setMoving(false);
            return;
        }

        player.regenMana();

        MageModel mage = (player instanceof MageModel m) ? m : null;
        boolean mageSkillActive = mage != null && mage.getSkillBox() != null;
        boolean mageLocked = mage != null
                && (player.isUltimate() || player.isSpecial() || player.isAtking() || mageSkillActive);

        if (mageLocked) {
            player.setDx(0);
            player.setMoving(false);

            if (mageSkillActive) {
                if (mage.getCurSkillType() == MageModel.SKILL_ULT) {
                    handleUltimateDamage(player, enemies, audio);
                } else if (mage.getCurSkillType() == MageModel.SKILL_SPECIAL) {
                    handleSpecialDamage(player, enemies, audio);
                } else {
                    handleAttack(player, enemies, audio);
                }
                return;
            }

            if (player.isUltimate())
                handleUltimateDamage(player, enemies, audio);
            if (player.isSpecial())
                handleSpecialDamage(player, enemies, audio);
            if (player.isAtking())
                handleAttack(player, enemies, audio);
            return;
        }

        double dx = 0;
        if (input.isLeft() && !input.isRight()) {
            dx = -player.getSpeed();
            player.setFacingRight(false);
        }
        if (input.isRight() && !input.isLeft()) {
            dx = player.getSpeed();
            player.setFacingRight(true);
        }
        player.setDx(dx);
        player.setMoving(dx != 0);

        boolean mageAttacking = mage != null && (player.isAtking() || player.isUltimate() || player.isSpecial());
        if (input.isJump() && player.isOnGround() && !mageAttacking) {
            audio.playSFX(AudioController.SFX_JUMP);
            player.requestJump(player.getJumpPow());
        }

        handleUltimateInput(player, audio);
        handleSpecialInput(player, audio);
        handleAttackInput(player, audio);

        if (player.isAtking() && !(player instanceof MageModel)) {
            handleAttack(player, enemies, audio);
        }
        if (player.isUltimate() && !(player instanceof MageModel)) {
            handleUltimateDamage(player, enemies, audio);
        }
        if (player.isSpecial() && !(player instanceof MageModel)) {
            handleSpecialDamage(player, enemies, audio);
        }
    }

    private void handleUltimateInput(PlayerModel player, AudioController audio) {
        if (input.isUltimate() && !player.isUltimate() && !player.isAtking() && !player.isSpecial()) {
            if (player.isUltReady()) {
                if (player.useMana(player.getUltimateCost())) {
                    if (player instanceof MageModel) {
                        player.setDx(0);
                        player.setMoving(false);
                        player.setJumping(false);
                    }
                    player.setUltimate(true);
                    player.setAniIndex(0);
                    player.setLastUltCastTime(System.currentTimeMillis());
                    Arrays.fill(ultFrameChecked, false);

                    if (player instanceof MageModel mage) {
                        mage.startSkill(MageModel.SKILL_ULT);
                        audio.playSFX(AudioController.SFX_MAGE_ULT);
                    } else {
                        audio.playSFX(AudioController.SFX_KNIGHT_ULT);
                    }
                }
            }
        }
    }

    private void handleSpecialInput(PlayerModel player, AudioController audio) {
        if (input.isSpecial() && !player.isSpecial() && !player.isAtking() && !player.isUltimate()) {
            if (player.isSpecialReady()) {
                if (player.useMana(player.getSpecialCost())) {
                    if (player instanceof MageModel) {
                        player.setDx(0);
                        player.setMoving(false);
                        player.setJumping(false);
                    }
                    player.setSpecial(true);
                    player.setAniIndex(0);
                    player.setLastSpecialCastTime(System.currentTimeMillis());
                    Arrays.fill(specialFrameChecked, false);

                    if (player instanceof MageModel mage) {
                        mage.startSkill(MageModel.SKILL_SPECIAL);
                        audio.playSFX(AudioController.SFX_MAGE_SPECIAL);
                    } else {
                        audio.playSFX(AudioController.SFX_KNIGHT_SPECIAL);
                    }
                }
            }
        }
    }

    private void handleAttackInput(PlayerModel player, AudioController audio) {
        long now = System.currentTimeMillis();
        if (input.isAttack() && !player.isAtking() && !player.isUltimate() && !player.isSpecial()
                && player.isNormalAtkReady()) {
            if (player instanceof MageModel) {
                player.setDx(0);
                player.setMoving(false);
                player.setJumping(false);
            }
            player.setAtking(true);
            player.setAniIndex(0);
            player.setLastNormalAttack(now);

            if (player instanceof MageModel mage) {
                mage.startSkill(MageModel.SKILL_NORMAL);
                audio.playSFX(AudioController.SFX_MAGE_ATTACK);
            } else {
                audio.playSFX(AudioController.SFX_KNIGHT_ATTACK);
            }
        }
    }

    private void handleUltimateDamage(PlayerModel player, List<EnemyModel> enemies, AudioController audio) {
        int frame = player.getAniIndex();
        Rectangle ultBox = player.getUltimateBox();
        if (player instanceof MageModel mage && mage.getSkillBox() != null) {
            ultBox = mage.getSkillBox();
            frame = mage.getSkillAniIndex();

            int hitFrame = getUltHitFrame(player);
            if (frame == hitFrame && !ultFrameChecked[frame]) {
                for (EnemyModel enemy : enemies) {
                    if (enemy.isAlive() && ultBox.intersects(enemy.getHitbox())) {
                        enemy.takeDamage(player.getUltimateDamage());
                    }
                }
                ultFrameChecked[frame] = true;
            }
            return;
        }
        for (int hitFrame : knightUltHitFrames) {
            if (frame == hitFrame && !ultFrameChecked[frame]) {
                for (EnemyModel enemy : enemies) {
                    if (enemy.isAlive() && ultBox.intersects(enemy.getHitbox())) {
                        enemy.takeDamage(player.getUltimateDamage());
                    }
                }
                ultFrameChecked[frame] = true;
            }
        }
    }

    private void handleSpecialDamage(PlayerModel player, List<EnemyModel> enemies, AudioController audio) {
        int frame = player.getAniIndex();
        Rectangle skillBox = player.getSpecialBox();
        if (player instanceof MageModel mage && mage.getSkillBox() != null) {
            skillBox = mage.getSkillBox();
            frame = mage.getSkillAniIndex();

            int hitFrame = getSpecialHitFrame(player);
            if (frame == hitFrame && !specialFrameChecked[frame]) {
                for (EnemyModel enemy : enemies) {
                    if (enemy.isAlive() && skillBox.intersects(enemy.getHitbox())) {
                        enemy.takeDamage(player.getSpecialDamage());
                    }
                }
                specialFrameChecked[frame] = true;
            }
            return;
        }
        for (int hitFrame : knightSpecialHitFrames) {
            if (frame == hitFrame && !specialFrameChecked[frame]) {
                for (EnemyModel enemy : enemies) {
                    if (enemy.isAlive() && skillBox.intersects(enemy.getHitbox())) {
                        enemy.takeDamage(player.getSpecialDamage());
                    }
                }
                specialFrameChecked[frame] = true;
            }
        }
    }

    private void handleAttack(PlayerModel player, List<EnemyModel> enemies, AudioController audio) {
        int frame = player.getAniIndex();
        Rectangle atkBox = player.getDefaultAttackBox();

        if (player instanceof MageModel mage && mage.getSkillBox() != null) {
            atkBox = mage.getSkillBox();
        }

        player.setAttackBox(atkBox);

        if (frame == atkHitFrame) {
            for (EnemyModel enemy : enemies) {
                if (enemy.isAlive() && enemy.getAiState() != HURT && atkBox.intersects(enemy.getHitbox())) {
                    enemy.takeDamage(player.getDamage());
                    break;
                }
            }
        }
    }

    private int getUltHitFrame(PlayerModel player) {
        if (player instanceof MageModel mage) {
            int idx = mage.getCurSkillIndex();
            if (idx >= 0 && idx < mageUltHitFrames.length) {
                return mageUltHitFrames[idx];
            }
        }
        return knightUltHitFrames[1];
    }

    private int getSpecialHitFrame(PlayerModel player) {
        if (player instanceof MageModel mage) {
            int idx = mage.getCurSkillIndex();
            if (idx >= 0 && idx < mageSpecialHitFrames.length) {
                return mageSpecialHitFrames[idx];
            }
        }
        return knightSpecialHitFrames[1];
    }
}