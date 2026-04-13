package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SkeletonModel;
import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.EnemyRenderer;

import static util.enemy.EnemyStateIndex.Skeleton.*;
import static util.AssetsPath.*;
import static core.GameConfig.*;

public class SkeletonRenderer extends EnemyRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];
    private int lastState = -1;

    public SkeletonRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(ResourceManager.loadSprite(whiteSkeletonIdle, SKELETON_FRAME.get(IDLE), 96, 64),
                24);
        aniState[HURT] = new Animation(ResourceManager.loadSprite(whiteSkeletonHurt, SKELETON_FRAME.get(HURT), 96, 64),
                24);
        aniState[WALK] = new Animation(ResourceManager.loadSprite(whiteSkeletonWalk, SKELETON_FRAME.get(WALK), 96, 64),
                24);
        aniState[DIE] = new Animation(ResourceManager.loadSprite(whiteSkeletonDie, SKELETON_FRAME.get(DIE), 96, 64),
                24);
        aniState[ATTACK1] = new Animation(
                ResourceManager.loadSprite(whiteSkeletonAttack1, SKELETON_FRAME.get(ATTACK1), 96, 64),
                24);
    }

    public void update(SkeletonModel skeleton) {
        int state = skeleton.getState();
        if (state != lastState) {
            aniState[state].reset();
            lastState = state;
        }

        Animation curAnimation = aniState[state];
        curAnimation.runAni();
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        y -= 2 * TILE_SIZE;
        Animation curAnimation = aniState[enemy.getState()];
        if (enemy.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), x, y, enemy.getWidth(),
                    enemy.getHeight(), null);

        } else {
            g.drawImage(curAnimation.getCurFrame(), (int) x + enemy.getWidth(), y,
                    -enemy.getWidth(), enemy.getHeight(), null);
        }
    }
}
