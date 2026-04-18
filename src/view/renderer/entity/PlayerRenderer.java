package view.renderer.entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import model.entity.PlayerModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static util.PlayerStateIndex.*;
import static util.AssetsPath.*;
import static view.renderer.entity.EntityRenderer.*;

public class PlayerRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];
    private int lastState = -1;

    public PlayerRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(ResourceManager.loadSprite(playerIdle, PLAYER_FRAME.get(IDLE), 64, 40),
                24, true);
        aniState[RUN] = new Animation(ResourceManager.loadSprite(playerRun, PLAYER_FRAME.get(RUN), 64, 40), 20, true);
        aniState[JUMP] = new Animation(ResourceManager.loadSprite(playerJump, PLAYER_FRAME.get(JUMP), 64, 40),
                40, false);
        aniState[HURT] = new Animation(ResourceManager.loadSprite(playerHurt, PLAYER_FRAME.get(HURT), 64, 40),
                30, false);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(playerAttack, PLAYER_FRAME.get(ATTACK), 64, 40), 18, false);
    }

    public void update(PlayerModel player) {
        int state = player.getState();
        if (state != lastState) {
            aniState[state].reset();
            lastState = state;
        }
        Animation curAnimation = aniState[state];
        curAnimation.runAni();
        player.setAniIndex(curAnimation.getFrameIdx());
        if (state == ATTACK && curAnimation.isLastFrame()) {
            player.setAtking(false);
            player.setHitted(false);

        }
    }

    public void render(Graphics g, PlayerModel player, int xOffset) {
        // drawHB(g, player, xOffset);
        // drawAttackBox(g, player, xOffset);
        Animation curAnimation = aniState[player.getState()];
        if (player.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), (int) player.getX() - xOffset, (int) player.getY(),
                    player.getWidth(),
                    player.getHeight(), null);

        } else {
            g.drawImage(curAnimation.getCurFrame(), (int) player.getX() + player.getWidth() - xOffset,
                    (int) player.getY(),
                    -player.getWidth(), player.getHeight(), null);
        }
    }

    private void drawAttackBox(Graphics g, PlayerModel player, int xOffset) {
        Rectangle atkBox = player.getAttackBox();
        if (atkBox != null && player.isAtking()) {
            g.setColor(java.awt.Color.BLACK);
            g.drawRect(atkBox.x - xOffset, atkBox.y, atkBox.width, atkBox.height);
        }
    }
}
