package view.renderer.entity.enemy.boss;

import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.enemy.EnemyRenderer;

import static util.enemy.EnemyStateIndex.Minotaur.*;
import static util.AssetsPath.*;

public class MinotaurRenderer extends EnemyRenderer {

    public MinotaurRenderer() {
        aniState = new Animation[MAX_STATE];
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(
                ResourceManager.loadSprite(minotaurIdle, MINOTAUR_FRAME.get(IDLE), 256, 128),
                11, true);
        aniState[RUN] = new Animation(
                ResourceManager.loadSprite(minotaurRun, MINOTAUR_FRAME.get(RUN), 256, 128),
                15, true);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(minotaurAttack, MINOTAUR_FRAME.get(ATTACK), 256, 128),
                12, false);
        aniState[HURT] = new Animation(
                ResourceManager.loadSprite(minotaurHurt, MINOTAUR_FRAME.get(HURT), 256, 128),
                16, false);
        aniState[DIE] = new Animation(
                ResourceManager.loadSprite(minotaurDie, MINOTAUR_FRAME.get(DIE), 256, 128),
                14, false);
    }

}