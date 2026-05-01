package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.GolemModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static util.enemy.EnemyStateIndex.Golem.*;
import static util.AssetsPath.*;

public class GolemRenderer extends EnemyRenderer {
    private Animation[][] aniSet;

    public GolemRenderer() {
        aniSet = new Animation[2][MAX_STATE];
        loadAnimation();
    }

    private void loadAnimation() {
        aniSet[GolemModel.BLUE][IDLE] = new Animation(
                ResourceManager.loadSprite(blueGolemIdle, GOLEM_FRAME.get(IDLE), 90, 64),
                24, true);
        aniSet[GolemModel.BLUE][RUN] = new Animation(
                ResourceManager.loadSprite(blueGolemRun, GOLEM_FRAME.get(RUN), 90, 64),
                18, true);
        aniSet[GolemModel.BLUE][ATTACK] = new Animation(
                ResourceManager.loadSprite(blueGolemAttack, GOLEM_FRAME.get(ATTACK), 90, 64),
                15, false);
        aniSet[GolemModel.BLUE][HURT] = new Animation(
                ResourceManager.loadSprite(blueGolemHurt, GOLEM_FRAME.get(HURT), 90, 64),
                20, false);
        aniSet[GolemModel.BLUE][DIE] = new Animation(
                ResourceManager.loadSprite(blueGolemDie, GOLEM_FRAME.get(DIE), 90, 64),
                18, false);

        aniSet[GolemModel.ORANGE][IDLE] = new Animation(
                ResourceManager.loadSprite(orangeGolemIdle, GOLEM_FRAME.get(IDLE), 90, 64),
                24, true);
        aniSet[GolemModel.ORANGE][RUN] = new Animation(
                ResourceManager.loadSprite(orangeGolemRun, GOLEM_FRAME.get(RUN), 90, 64),
                18, true);
        aniSet[GolemModel.ORANGE][ATTACK] = new Animation(
                ResourceManager.loadSprite(orangeGolemAttack, GOLEM_FRAME.get(ATTACK), 90, 64),
                15, false);
        aniSet[GolemModel.ORANGE][HURT] = new Animation(
                ResourceManager.loadSprite(orangeGolemHurt, GOLEM_FRAME.get(HURT), 90, 64),
                20, false);
        aniSet[GolemModel.ORANGE][DIE] = new Animation(
                ResourceManager.loadSprite(orangeGolemDie, GOLEM_FRAME.get(DIE), 90, 64),
                18, false);
    }

    @Override
    protected void update(EnemyModel enemy) {
        if (!(enemy instanceof GolemModel golem))
            return;

        int type = golem.getType();
        int state = golem.getAniState();

        if (golem.getLastState() == -1 || state != golem.getLastState()) {
            aniSet[type][state].reset();
            golem.setLastState(state);
        }

        Animation cur = aniSet[type][state];
        cur.runAni();
        golem.setAniIndex(cur.getFrameIdx());
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof GolemModel golem))
            return;
        int type = golem.getType();
        Animation cur = aniSet[type][golem.getAniState()];

        if (golem.isFacingRight()) {
            g.drawImage(cur.getCurFrame(), x, y,
                    golem.getWidth(), golem.getHeight(), null);
        } else {
            g.drawImage(cur.getCurFrame(), x + golem.getWidth(), y,
                    -golem.getWidth(), golem.getHeight(), null);
        }
    }
}