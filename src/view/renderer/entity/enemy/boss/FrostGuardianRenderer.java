package view.renderer.entity.enemy.boss;

import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.enemy.EnemyRenderer;

import static util.enemy.EnemyStateIndex.FrostGuardian.*;
import static util.AssetsPath.*;

public class FrostGuardianRenderer extends EnemyRenderer {

    public FrostGuardianRenderer() {
        aniState = new Animation[MAX_STATE];
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(
                ResourceManager.loadSprite(frostGuardianIdle, FROST_GUARDIAN_FRAME.get(IDLE), 192, 128),
                20, true);
        aniState[RUN] = new Animation(
                ResourceManager.loadSprite(frostGuardianRun, FROST_GUARDIAN_FRAME.get(RUN), 192, 128),
                10, true);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(frostGuardianAttack, FROST_GUARDIAN_FRAME.get(ATTACK), 192, 128),
                7, false);
        aniState[HURT] = new Animation(
                ResourceManager.loadSprite(frostGuardianHurt, FROST_GUARDIAN_FRAME.get(HURT), 192, 128),
                20, false);
        aniState[DIE] = new Animation(
                ResourceManager.loadSprite(frostGuardianDie, FROST_GUARDIAN_FRAME.get(DIE), 192, 128),
                13, false);
    }

}