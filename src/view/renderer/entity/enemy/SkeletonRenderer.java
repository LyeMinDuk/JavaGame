package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SkeletonModel;
import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.EnemyRenderer;

import static util.enemy.EnemyStateIndex.Skeleton.*; // FIX: đổi từ Shark.* sang Skeleton.*
import static view.renderer.entity.EntityRenderer.drawHB;
import static util.AssetsPath.*;
import static core.GameConfig.*;

public class SkeletonRenderer extends EnemyRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];
    private int lastState = -1;

    public SkeletonRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        // FIX: frame size đổi từ 34x30 (của Shark) sang 96x64 (của Skeleton)
        aniState[IDLE] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonIdle, SKELETON_FRAME.get(IDLE), 96, 64),
                24, true);
        aniState[RUN] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonRun, SKELETON_FRAME.get(RUN), 96, 64),
                18, true);
        aniState[ATTACK1] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonAttack1, SKELETON_FRAME.get(ATTACK1), 96, 64),
                15, false);
        aniState[ATTACK2] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonAttack2, SKELETON_FRAME.get(ATTACK2), 96, 64),
                15, false);
        aniState[HURT] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonHurt, SKELETON_FRAME.get(HURT), 96, 64),
                20, false);
        aniState[DIE] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonDie, SKELETON_FRAME.get(DIE), 96, 64),
                18, false);
    }

    @Override
    protected void update(EnemyModel enemy) {
        int state = enemy.getAniState();
        if (state != lastState) {
            aniState[state].reset();
            lastState = state;
        }
        aniState[state].runAni();
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof SkeletonModel skeleton))
            return;

        Animation curAnimation = aniState[skeleton.getAniState()];
        int w = skeleton.getWidth();
        int h = skeleton.getHeight();

        // Skeleton assets quay mặt phải → facingRight vẽ thẳng, facingLeft lật
        if (skeleton.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), x, y, w, h, null);
        } else {
            g.drawImage(curAnimation.getCurFrame(), x + w, y, -w, h, null);
        }
    }
}