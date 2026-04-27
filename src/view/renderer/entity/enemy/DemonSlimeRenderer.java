package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.DemonSlimeModel;
import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.EnemyRenderer;

import static util.enemy.EnemyStateIndex.DemonSlime.*;
import static util.AssetsPath.*;

public class DemonSlimeRenderer extends EnemyRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];

    public DemonSlimeRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(
                ResourceManager.loadSprite(demonSlimeIdle, DEMON_SLIME_FRAME.get(IDLE), 256, 128),
                20, true);
        aniState[RUN] = new Animation(
                ResourceManager.loadSprite(demonSlimeRun, DEMON_SLIME_FRAME.get(RUN), 256, 128),
                15, true);
        // FIX: loop=false để animation ATTACK chạy 1 lần rồi dừng
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(demonSlimeAttack, DEMON_SLIME_FRAME.get(ATTACK), 256, 128),
                7, false);
        aniState[HURT] = new Animation(
                ResourceManager.loadSprite(demonSlimeHurt, DEMON_SLIME_FRAME.get(HURT), 256, 128),
                20, false);
        aniState[DIE] = new Animation(
                ResourceManager.loadSprite(demonSlimeDie, DEMON_SLIME_FRAME.get(DIE), 256, 128),
                15, false);
    }

    @Override
    protected void update(EnemyModel enemy) {
        if (!(enemy instanceof DemonSlimeModel boss))
            return;

        int state = boss.getAniState();

        if (boss.getLastState() == -1 || state != boss.getLastState()) {
            aniState[state].reset();
            boss.setLastState(state);
        }

        Animation curAnimation = aniState[state];
        curAnimation.runAni();
        boss.setAniIndex(curAnimation.getFrameIdx());
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof DemonSlimeModel boss))
            return;

        Animation curAnimation = aniState[boss.getAniState()];
        int w = boss.getWidth();
        int h = boss.getHeight();

        // Slime assets quay mặt trái → facingLeft vẽ thẳng, facingRight lật
        if (!boss.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), x, y, w, h, null);
        } else {
            g.drawImage(curAnimation.getCurFrame(), x + w, y, -w, h, null);
        }
    }
}