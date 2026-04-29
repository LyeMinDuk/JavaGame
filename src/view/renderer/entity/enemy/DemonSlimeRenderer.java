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
        int state = enemy.getAniState();

        if (enemy.getLastState() == -1 || state != enemy.getLastState()) {
            aniState[state].reset();
            enemy.setLastState(state);
        }

        Animation curAnimation = aniState[state];
        curAnimation.runAni();
        enemy.setAniIndex(curAnimation.getFrameIdx());
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof DemonSlimeModel slime))
            return;

        Animation curAnimation = aniState[slime.getAniState()];
        if (!slime.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), x, y, slime.getWidth(), slime.getHeight(), null);
        } else {
            g.drawImage(curAnimation.getCurFrame(), x + slime.getWidth(), y, -slime.getWidth(), slime.getHeight(), null);
        }
    }
}