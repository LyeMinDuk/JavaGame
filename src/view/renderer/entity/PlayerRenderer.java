package view.renderer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import model.entity.PlayerModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static core.GameConfig.*;
import static util.PlayerStateIndex.*;
import static util.AssetsPath.*;

public class PlayerRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];
    private Animation slashAnimation;
    private int lastState = -1;
    private static final boolean DEBUG = false;

    public PlayerRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(ResourceManager.loadSprite(playerIdle, PLAYER_FRAME.get(IDLE), 64, 42), 25,
                true);
        aniState[RUN] = new Animation(ResourceManager.loadSprite(playerRun, PLAYER_FRAME.get(RUN), 64, 42), 15, true);
        aniState[JUMP] = new Animation(ResourceManager.loadSprite(playerJump, PLAYER_FRAME.get(JUMP), 64, 42), 24,
                false);
        aniState[HURT] = new Animation(ResourceManager.loadSprite(playerHurt, PLAYER_FRAME.get(HURT), 64, 42), 15,
                false);
        aniState[ATTACK] = new Animation(ResourceManager.loadSprite(playerAttack, PLAYER_FRAME.get(ATTACK), 64, 42), 15,
                false);
        aniState[ULTIMATE] = new Animation(
                ResourceManager.loadSprite(playerUltimate, PLAYER_FRAME.get(ULTIMATE), 64, 64), 7, false);
        // DEATH: loop=false, dừng ở frame cuối
        aniState[DIE] = new Animation(ResourceManager.loadSprite(playerDie, PLAYER_FRAME.get(DIE), 64, 42), 15,
                false);

        slashAnimation = new Animation(ResourceManager.loadSprite(ultimateSlash, PLAYER_FRAME.get(ULTIMATE), 64, 64), 7,
                false);
    }

    public void update(PlayerModel player) {
        int state = player.getState();

        if (state != lastState) {
            aniState[state].reset();
            if (state == ULTIMATE)
                slashAnimation.reset();
            lastState = state;
        }

        Animation cur = aniState[state];
        cur.runAni();
        player.setAniIndex(cur.getFrameIdx());

        if (state == ULTIMATE)
            slashAnimation.runAni();

        if (state == ATTACK && cur.isLastFrame())
            player.setAtking(false);
        if (state == ULTIMATE && cur.isLastFrame())
            player.setUltimate(false);
        // DEATH và HURT tự dừng do loop=false — không cần reset thêm
    }

    public void render(Graphics g, PlayerModel player, int xOffset, int yOffset) {
        if (DEBUG)
            drawDebug(g, player, xOffset, yOffset);

        int state = player.getState();
        Animation cur = aniState[state];

        int drawW = player.getWidth();
        int drawH = (state == ULTIMATE) ? (int) (64 * SCALE) : player.getHeight();
        int drawX = (int) player.getX() - xOffset;
        int drawY = (int) player.getY() - (drawH - player.getHeight()) - yOffset;

        if (player.isFacingRight()) {
            g.drawImage(cur.getCurFrame(), drawX, drawY, drawW, drawH, null);
            if (state == ULTIMATE)
                g.drawImage(slashAnimation.getCurFrame(), drawX + drawW, drawY, drawW, drawH, null);
        } else {
            g.drawImage(cur.getCurFrame(), drawX + drawW, drawY, -drawW, drawH, null);
            if (state == ULTIMATE)
                g.drawImage(slashAnimation.getCurFrame(), drawX, drawY, -drawW, drawH, null);
        }
    }

    private void drawDebug(Graphics g, PlayerModel player, int xOffset, int yOffset) {
        Rectangle hb = player.getHitbox();
        g.setColor(Color.RED);
        g.drawRect(hb.x - xOffset, hb.y - yOffset, hb.width, hb.height);

        Rectangle atkBox = player.getAttackBox();
        if (atkBox != null && player.isAtking()) {
            g.setColor(Color.BLACK);
            g.drawRect(atkBox.x - xOffset, atkBox.y - yOffset, atkBox.width, atkBox.height);
        }
        Rectangle ultBox = player.getUltimateBox();
        if (ultBox != null && player.isUltimate()) {
            g.setColor(Color.ORANGE);
            g.drawRect(ultBox.x - xOffset, ultBox.y - yOffset, ultBox.width, ultBox.height);
        }
    }
}