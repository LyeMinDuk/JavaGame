package view.renderer.entity;

import model.entity.PlayerModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static util.PlayerStateIndex.*;
import static util.AssetsPath.*;

import java.awt.Graphics;

public class PlayerRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];
    private int lastState = -1;

    public PlayerRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(ResourceManager.loadSprite(playerIdle, PLAYER_FRAME.get(IDLE), 64, 40),
                24);
        aniState[RUN] = new Animation(ResourceManager.loadSprite(playerRun, PLAYER_FRAME.get(RUN), 64, 40), 20);
        aniState[JUMP] = new Animation(ResourceManager.loadSprite(playerJump, PLAYER_FRAME.get(JUMP), 64, 40),
                40);
        aniState[HURT] = new Animation(ResourceManager.loadSprite(playerHit, PLAYER_FRAME.get(HURT), 64, 40),
                30);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(playerAttack, PLAYER_FRAME.get(ATTACK), 64, 40), 18);
    }

    public void update(PlayerModel player) {
        int state = player.getState();
        if (state != lastState) {
            aniState[state].reset();
            lastState = state;
        }

        Animation curAnimation = aniState[state];
        curAnimation.runAni();
    }

    public void render(Graphics g, PlayerModel player, int xOffset) {
        Animation curAnimation = aniState[player.getState()];
        if (player.isFacingRight()) {
            g.drawImage(curAnimation.getCurFrame(), (int) player.getX() - xOffset, (int) player.getY(), player.getWidth(),
                    player.getHeight(), null);

        } else {
            g.drawImage(curAnimation.getCurFrame(), (int) player.getX() + player.getWidth() - xOffset, (int) player.getY(),
                    -player.getWidth(), player.getHeight(), null);
        }
    }

}
