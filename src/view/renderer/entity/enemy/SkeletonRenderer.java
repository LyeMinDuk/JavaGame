package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SkeletonModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static util.enemy.EnemyStateIndex.Skeleton.*;
import static util.AssetsPath.*;

public class SkeletonRenderer extends EnemyRenderer {

    public SkeletonRenderer() {
        aniState = new Animation[MAX_STATE];
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonIdle, SKELETON_FRAME.get(IDLE), 96, 64),
                24, true);
        aniState[RUN] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonRun, SKELETON_FRAME.get(RUN), 96, 64),
                18, true);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonAttack, SKELETON_FRAME.get(ATTACK), 96, 64),
                15, false);
        aniState[HURT] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonHurt, SKELETON_FRAME.get(HURT), 96, 64),
                20, false);
        aniState[DIE] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonDie, SKELETON_FRAME.get(DIE), 96, 64),
                18, false);
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof SkeletonModel skeleton))
            return;
        Animation curAnimation = aniState[skeleton.getAniState()];
        if (skeleton.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), x, y, skeleton.getWidth(), skeleton.getHeight(), null);
        } else {
            g.drawImage(curAnimation.getCurFrame(), x + skeleton.getWidth(), y, -skeleton.getWidth(),
                    skeleton.getHeight(), null);
        }
    }
}