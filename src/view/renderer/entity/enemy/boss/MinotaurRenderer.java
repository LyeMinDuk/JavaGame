package view.renderer.entity.enemy.boss;

import java.awt.Graphics;

import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.enemy.EnemyRenderer;
import model.entity.enemy.EnemyModel;
import model.entity.enemy.boss.MinotaurModel;

import static util.AssetsPath.*;
import static core.GameConfig.SCALE;
import static util.enemy.EnemyStateIndex.Minotaur.*;

public class MinotaurRenderer extends EnemyRenderer {
    private static final int SHIELD_FRAMES = 20;
    private static final int SHIELD_SIZE = 128;
    private Animation shieldAni;

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
        shieldAni = new Animation(
                ResourceManager.loadSprite(minotaurShield, SHIELD_FRAMES, 128, 128),
                10, true);
    }

    @Override
    protected void update(EnemyModel enemy) {
        super.update(enemy);
        if (!(enemy instanceof MinotaurModel minotaur)) {
            return;
        }
        if (minotaur.isInvulnerable()) {
            shieldAni.runAni();
        } else {
            shieldAni.reset();
        }
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof MinotaurModel minotaur)) {
            return;
        }
        super.render(g, enemy, x, y);
        if (minotaur.isInvulnerable()) {
            int shieldW= (int) (SHIELD_SIZE * SCALE);;
            int shieldH = shieldW;

            if (minotaur.isFacingRight()) {
                g.drawImage(shieldAni.getCurFrame(), x + 2 * shieldW, y, -shieldW, shieldH, null);
            } else {
                g.drawImage(shieldAni.getCurFrame(), x, y, shieldW, shieldH, null);
            }
        }
    }

}