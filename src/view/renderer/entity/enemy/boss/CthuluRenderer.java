package view.renderer.entity.enemy.boss;

import java.awt.Graphics;
import java.awt.Rectangle;

import view.assets.Animation;
import view.assets.ResourceManager;
import view.renderer.entity.enemy.EnemyRenderer;
import model.entity.enemy.EnemyModel;
import model.entity.enemy.boss.CthuluModel;

import static util.enemy.EnemyStateIndex.Cthulu.*;
import static util.AssetsPath.*;

public class CthuluRenderer extends EnemyRenderer {
    private Animation[] magicAni = new Animation[3];

    public CthuluRenderer() {
        aniState = new Animation[MAX_STATE];
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(
                ResourceManager.loadSprite(cthuluIdle, CTHULU_FRAME.get(IDLE), 192, 112),
                20, true);
        aniState[RUN] = new Animation(
                ResourceManager.loadSprite(cthuluRun, CTHULU_FRAME.get(RUN), 192, 112),
                10, true);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(cthuluAttack, CTHULU_FRAME.get(ATTACK), 192, 112),
                10, false);
        aniState[ATTACK1] = new Animation(
                ResourceManager.loadSprite(cthuluAttack1, CTHULU_FRAME.get(ATTACK1), 192, 112),
                7, false);
        aniState[HURT] = new Animation(
                ResourceManager.loadSprite(cthuluHurt, CTHULU_FRAME.get(HURT), 192, 112),
                20, false);
        aniState[DIE] = new Animation(
                ResourceManager.loadSprite(cthuluDie, CTHULU_FRAME.get(DIE), 192, 112),
                19, false);
        aniState[FLY] = new Animation(
                ResourceManager.loadSprite(cthuluFly, CTHULU_FRAME.get(FLY), 192, 112),
                15, true);
        magicAni[0] = new Animation(
                ResourceManager.loadSprite(cthuluSkill1, 14, 128, 128), 20, false);
        magicAni[1] = new Animation(
                ResourceManager.loadSprite(cthuluSkill2, 12, 128, 128), 25, false);
        magicAni[2] = new Animation(
                ResourceManager.loadSprite(cthuluSkill3, 15, 128, 128), 20, false);
    }

    @Override
    protected void update(EnemyModel enemy) {
        super.update(enemy);
        if (!(enemy instanceof CthuluModel cthulu))
            return;
        if (cthulu.getMagicBox() != null && cthulu.getCurSkill() >= 0) {
            int skill = cthulu.getCurSkill();
            Animation magic = magicAni[skill];

            if (cthulu.getMagicAniIndex() == -1) {
                magic.reset();
            }

            magic.runAni();
            cthulu.setMagicAniIndex(magic.getFrameIdx());
        } else {
            cthulu.setMagicAniIndex(-1);
        }
    }

    @Override
    protected void render(Graphics g, EnemyModel enemy, int x, int y) {
        if (!(enemy instanceof CthuluModel cthulu))
            return;
        Animation cur = aniState[enemy.getAniState()];
        if (cthulu.getMagicBox() != null && cthulu.getCurSkill() >= 0) {
            Animation magic = magicAni[cthulu.getCurSkill()];
            Rectangle magicBox = cthulu.getMagicBox();

            int drawX = x + (magicBox.x - (int) cthulu.getX());
            int drawY = y + (magicBox.y - (int) cthulu.getY());

            g.drawImage(magic.getCurFrame(), drawX, drawY, magicBox.width, magicBox.height, null);
        }
        if (cthulu.isFacingRight()) {
            g.drawImage(cur.getCurFrame(), x, y, cthulu.getWidth(), cthulu.getHeight(), null);
        } else {
            g.drawImage(cur.getCurFrame(), x + cthulu.getWidth(), y, -cthulu.getWidth(), cthulu.getHeight(), null);
        }
    }
}