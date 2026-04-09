package view.renderer.entity;

import model.entity.PlayerModel;
import view.assets.Animation;
import static util.PlayerStateIndex.*;

import java.awt.Graphics;

public class PlayerRenderer {
    private Animation idle, run, jump;
    private int lastState = -1;

    public PlayerRenderer(Animation idle, Animation run, Animation jump) {
        this.idle = idle; this.run = run; this.jump = jump;
    }

    public void update(PlayerModel player) {
        int state = player.getState();
        if (state != lastState) {
            idle.reset(); run.reset(); jump.reset();
            lastState = state;
        }

        switch (state) {
            case IDLE -> idle.runAni();
            case RUN -> run.runAni();
            case JUMP -> jump.runAni();
        }
    }
    public void render(Graphics g, PlayerModel player, int xOffset){
        Animation a = switch(player.getState()){
            case RUN -> run;
            case JUMP -> jump;
            default -> idle;
        };
        g.drawImage(a.getCurFrame(), (int) player.getX() - xOffset, (int) player.getY(), player.getWidth(), player.getHeight(), null);
    }
    
}
