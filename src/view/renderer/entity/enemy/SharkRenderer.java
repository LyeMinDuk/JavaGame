package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SharkModel;
import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.EnemyRenderer;

import static util.enemy.EnemyStateIndex.Shark.*;
import static util.AssetsPath.*;

public class SharkRenderer extends EnemyRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];

    public SharkRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(ResourceManager.loadSprite(sharkIdle, SHARK_FRAME.get(IDLE), 34, 30),
                24, true);
        aniState[HURT] = new Animation(ResourceManager.loadSprite(sharkHurt, SHARK_FRAME.get(HURT), 34, 30),
                30, false);
        aniState[RUN] = new Animation(ResourceManager.loadSprite(sharkRun, SHARK_FRAME.get(RUN), 34, 30),
                24, true);
        aniState[DIE] = new Animation(ResourceManager.loadSprite(sharkDie, SHARK_FRAME.get(DIE), 34, 30),
                24, false);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(sharkAttack, SHARK_FRAME.get(ATTACK), 34, 30),
                15, false);
    }
    
    @Override
    protected void update(EnemyModel enemy) {
        int state = enemy.getAniState();
        SharkModel shark = (SharkModel) enemy;
        if (shark.getLastState() == -1 || state != shark.getLastState()) {
            aniState[state].reset();
            shark.setLastState(state);
        }
        Animation curAnimation = aniState[state];
        curAnimation.runAni();
        enemy.setAniIndex(curAnimation.getFrameIdx());
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof SharkModel shark))
            return;
        Animation curAnimation = aniState[shark.getAniState()];
        if (!shark.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), x, y, +shark.getWidth(), shark.getHeight(), null);
        } else {
            g.drawImage(curAnimation.getCurFrame(), x + shark.getWidth(), y,
                    -shark.getWidth(), shark.getHeight(), null);
        }
    }

    
}
