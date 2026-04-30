package view.renderer.entity.enemy;

import java.awt.Graphics;

import model.entity.enemy.EnemyModel;
import model.entity.enemy.SharkModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static util.enemy.EnemyStateIndex.Shark.*;
import static util.AssetsPath.*;

public class SharkRenderer extends EnemyRenderer {

    public SharkRenderer() {
        aniState = new Animation[MAX_STATE];
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
}
