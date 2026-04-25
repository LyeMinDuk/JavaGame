package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SkeletonModel;
import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.EnemyRenderer;

import static util.enemy.EnemyStateIndex.Shark.*;
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
        aniState[IDLE] = new Animation(ResourceManager.loadSprite(whiteSkeletonIdle, SHARK_FRAME.get(IDLE), 34, 30),
                24, true);
        aniState[HURT] = new Animation(ResourceManager.loadSprite(whiteSkeletonHurt, SHARK_FRAME.get(HURT), 34, 30),
                24, false);
        aniState[RUN] = new Animation(ResourceManager.loadSprite(whiteSkeletonRun, SHARK_FRAME.get(RUN), 34, 30),
                24, true);
        aniState[DIE] = new Animation(ResourceManager.loadSprite(whiteSkeletonDie, SHARK_FRAME.get(DIE), 34, 30),
                24, false);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonAttack1, SHARK_FRAME.get(ATTACK), 34, 30),
                24, false);
    }

    @Override
    protected void update(EnemyModel enemy) {
        int state = enemy.getAniState();
        if (state != lastState) {
            aniState[state].reset();
            lastState = state;
        }

        Animation curAnimation = aniState[state];
        curAnimation.runAni();
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof SkeletonModel skeleton))
            return;
        y -= 2 * TILE_SIZE;
        drawHB(g, skeleton, x, y);
        Animation curAnimation = aniState[skeleton.getAniState()];
        if (skeleton.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), x, y, skeleton.getWidth(), skeleton.getHeight(), null);
        } else {
            g.drawImage(curAnimation.getCurFrame(), x + skeleton.getWidth(), y,
                    -skeleton.getWidth(), skeleton.getHeight(), null);
        }
    }

}
