// DemonSlimeRenderer.java
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
                ResourceManager.loadSprite(demonSlimeIdle, DEMON_SLIME_FRAME.get(IDLE), 256, 128), 24, true);
        aniState[RUN] = new Animation(ResourceManager.loadSprite(demonSlimeRun, DEMON_SLIME_FRAME.get(RUN), 256, 128),
                24, true);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(demonSlimeAttack, DEMON_SLIME_FRAME.get(ATTACK), 256, 128), 24, true);
        aniState[HURT] = new Animation(
                ResourceManager.loadSprite(demonSlimeHurt, DEMON_SLIME_FRAME.get(HURT), 256, 128), 24, true);
        aniState[DIE] = new Animation(ResourceManager.loadSprite(demonSlimeDie, DEMON_SLIME_FRAME.get(DIE), 256, 128),
                24, false); // Lưu ý: Animation DIE thường để lặp là false để quái chết hẳn
    }

    @Override
    protected void update(EnemyModel enemy) {
        // KIỂM TRA BẢO VỆ VÀ ÉP KIỂU ĐÚNG LOẠI BOSS
        if (!(enemy instanceof DemonSlimeModel boss)) return;

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
        if (!(enemy instanceof DemonSlimeModel boss)) return;

        Animation curAnimation = aniState[boss.getAniState()];
        if (!boss.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), x, y, boss.getWidth(), boss.getHeight(), null);
        } else {
            g.drawImage(curAnimation.getCurFrame(), x + boss.getWidth(), y, -boss.getWidth(), boss.getHeight(), null);
        }
    }
}