package view.renderer.entity;

import model.entity.PlayerModel;
import view.assets.Animation;
import static util.PlayerStateIndex.*;

import java.awt.Graphics;

public class PlayerRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];
    private int lastState = -1;

    public PlayerRenderer(Animation[] aniState) {
        this.aniState = aniState;
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
