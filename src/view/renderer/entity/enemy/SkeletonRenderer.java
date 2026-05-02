package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SkeletonModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static util.enemy.EnemyStateIndex.Skeleton.*;
import static util.AssetsPath.*;

public class SkeletonRenderer extends EnemyRenderer {
    private Animation[][] aniSet;

    public SkeletonRenderer() {
        aniSet = new Animation[2][MAX_STATE];
        loadAnimation();
    }

    private void loadAnimation() {
        aniSet[SkeletonModel.WHITE][IDLE] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonIdle, SKELETON_FRAME.get(IDLE), 96, 64),
                22, true);
        aniSet[SkeletonModel.WHITE][RUN] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonRun, SKELETON_FRAME.get(RUN), 96, 64),
                18, true);
        aniSet[SkeletonModel.WHITE][ATTACK] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonAttack, SKELETON_FRAME.get(ATTACK), 96, 64),
                13, false);
        aniSet[SkeletonModel.WHITE][HURT] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonHurt, SKELETON_FRAME.get(HURT), 96, 64),
                19, false);
        aniSet[SkeletonModel.WHITE][DIE] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonDie, SKELETON_FRAME.get(DIE), 96, 64),
                11, false);

        aniSet[SkeletonModel.YELLOW][IDLE] = new Animation(
                ResourceManager.loadSprite(yellowSkeletonIdle, SKELETON_FRAME.get(IDLE), 96, 64),
                22, true);
        aniSet[SkeletonModel.YELLOW][RUN] = new Animation(
                ResourceManager.loadSprite(yellowSkeletonRun, SKELETON_FRAME.get(RUN), 96, 64),
                18, true);
        aniSet[SkeletonModel.YELLOW][ATTACK] = new Animation(
                ResourceManager.loadSprite(yellowSkeletonAttack, SKELETON_FRAME.get(ATTACK), 96, 64),
                13, false);
        aniSet[SkeletonModel.YELLOW][HURT] = new Animation(
                ResourceManager.loadSprite(yellowSkeletonHurt, SKELETON_FRAME.get(HURT), 96, 64),
                19, false);
        aniSet[SkeletonModel.YELLOW][DIE] = new Animation(
                ResourceManager.loadSprite(yellowSkeletonDie, SKELETON_FRAME.get(DIE), 96, 64),
                11, false);
    }

    @Override
    protected void update(EnemyModel enemy) {
        if (!(enemy instanceof SkeletonModel skeleton))
            return;

        int type = skeleton.getType();
        int state = skeleton.getAniState();

        if (skeleton.getLastState() == -1 || state != skeleton.getLastState()) {
            aniSet[type][state].reset();
            skeleton.setLastState(state);
        }

        Animation cur = aniSet[type][state];
        cur.runAni();
        skeleton.setAniIndex(cur.getFrameIdx());
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof SkeletonModel skeleton))
            return;
        int type = skeleton.getType();
        Animation cur = aniSet[type][skeleton.getAniState()];

        if (skeleton.isFacingRight()) {
            g.drawImage(cur.getCurFrame(), x, y,
                    skeleton.getWidth(), skeleton.getHeight(), null);
        } else {
            g.drawImage(cur.getCurFrame(), x + skeleton.getWidth(), y,
                    -skeleton.getWidth(), skeleton.getHeight(), null);
        }
    }
}