package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.DemonSlimeModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static util.enemy.EnemyStateIndex.DemonSlime.*;
import static util.AssetsPath.*;

public class DemonSlimeRenderer extends EnemyRenderer {

    public DemonSlimeRenderer() {
        aniState = new Animation[MAX_STATE];
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(
                ResourceManager.loadSprite(demonSlimeIdle, DEMON_SLIME_FRAME.get(IDLE), 256, 128),
                20, true);
        aniState[RUN] = new Animation(
                ResourceManager.loadSprite(demonSlimeRun, DEMON_SLIME_FRAME.get(RUN), 256, 128),
                10, true);
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

}