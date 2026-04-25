package view.renderer.entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import model.entity.PlayerModel;
import view.assets.Animation;
import view.assets.ResourceManager;

import static core.GameConfig.*;
import static util.PlayerStateIndex.*;
import static util.AssetsPath.*;
import static view.renderer.entity.EntityRenderer.*;

public class PlayerRenderer {
    private Animation[] aniState = new Animation[MAX_STATE];
    private Animation slashAnimation;
    private int lastState = -1;
    private boolean debug = false;

    public PlayerRenderer() {
        loadAnimation();
    }

    private void loadAnimation() {
        aniState[IDLE] = new Animation(ResourceManager.loadSprite(playerIdle, PLAYER_FRAME.get(IDLE), 64, 42),
                25, true);
        aniState[RUN] = new Animation(ResourceManager.loadSprite(playerRun, PLAYER_FRAME.get(RUN), 64, 42), 15, true);
        aniState[JUMP] = new Animation(ResourceManager.loadSprite(playerJump, PLAYER_FRAME.get(JUMP), 64, 42),
                24, false);
        aniState[HURT] = new Animation(ResourceManager.loadSprite(playerHurt, PLAYER_FRAME.get(HURT), 64, 42),
                30, false);
        aniState[ATTACK] = new Animation(
                ResourceManager.loadSprite(playerAttack, PLAYER_FRAME.get(ATTACK), 64, 42), 15, false);
        aniState[ULTIMATE] = new Animation(
                ResourceManager.loadSprite(playerUltimate, PLAYER_FRAME.get(ULTIMATE), 64, 64), 7, false);

        slashAnimation = new Animation(
                ResourceManager.loadSprite(ultimateSlash, PLAYER_FRAME.get(ULTIMATE), 64, 64), 7, false);
    }

    public void update(PlayerModel player) {
        int state = player.getState();
        
        // NẾU CHUYỂN TRẠNG THÁI MỚI -> RESET ANIMATION
        if (state != lastState) {
            aniState[state].reset();
            // Nếu chuyển sang dùng Ulti, reset luôn cả Slash
            if (state == ULTIMATE) {
                slashAnimation.reset();
            }
            lastState = state;
        }
        
        // CHẠY ANIMATION CHÍNH
        Animation curAnimation = aniState[state];
        curAnimation.runAni();
        player.setAniIndex(curAnimation.getFrameIdx()); // Cập nhật Index cho Model check Hitbox
        
        // NẾU ĐANG ULTIMATE, CHẠY THÊM ANIMATION SLASH
        if (state == ULTIMATE) {
            slashAnimation.runAni();
        }

        // KIỂM TRA KẾT THÚC HOẠT ẢNH
        if (state == ATTACK && curAnimation.isLastFrame()) {
            player.setAtking(false);
        }
        if (state == ULTIMATE && curAnimation.isLastFrame()) {
            player.setUltimate(false);
        }
    }

    public void render(Graphics g, PlayerModel player, int xOffset, int yOffset) {
        if (debug) {
            drawHB(g, player, xOffset, yOffset);
            drawAttackBox(g, player, xOffset);
        }
        
        int state = player.getState();
        Animation curAnimation = aniState[state];

        // Lấy kích thước chuẩn (player.getWidth() đang là 64 * SCALE)
        int drawW = player.getWidth();
        // Nếu là Ultimate thì dùng chiều cao 64 * Scale, bình thường thì dùng chiều cao gốc 42 * Scale
        int drawH = (state == ULTIMATE) ? (int) (64 * SCALE) : player.getHeight(); 
        
        int drawX = (int) player.getX() - xOffset;
        // Đẩy tọa độ Y lên trên một đoạn bù trừ để gót chân luôn chạm đất
        int drawY = (int) player.getY() - (drawH - player.getHeight()) - yOffset; 

        if (player.isFacingRight()) {
            // Vẽ Player
            g.drawImage(curAnimation.getCurFrame(), drawX, drawY, drawW, drawH, null);
            
            // Vẽ Slash nằm bên phải Player
            if (state == ULTIMATE) {
                g.drawImage(slashAnimation.getCurFrame(), drawX + drawW, drawY, drawW, drawH, null);
            }

        } else {
            // Vẽ Player lật ngược (Bắt đầu từ drawX + drawW, vẽ lùi lại kích thước -drawW)
            g.drawImage(curAnimation.getCurFrame(), drawX + drawW, drawY, -drawW, drawH, null);
            
            // Vẽ Slash lật ngược nằm bên trái Player (Bắt đầu từ drawX, vẽ lùi lại kích thước -drawW)
            if (state == ULTIMATE) {
                g.drawImage(slashAnimation.getCurFrame(), drawX, drawY, -drawW, drawH, null);
            }
        }
    }

    private void drawAttackBox(Graphics g, PlayerModel player, int xOffset) {
        // Hitbox đánh thường
        Rectangle atkBox = player.getAttackBox();
        if (atkBox != null && player.isAtking()) {
            g.setColor(java.awt.Color.BLACK);
            g.drawRect(atkBox.x - xOffset, atkBox.y, atkBox.width, atkBox.height);
        }
        
        // Hitbox Ultimate (Hỗ trợ bạn dễ debug xem vùng đánh có bị lệch không)
        Rectangle ultBox = player.getUltimateBox();
        if (ultBox != null && player.isUltimate()) {
            g.setColor(java.awt.Color.RED);
            g.drawRect(ultBox.x - xOffset, ultBox.y, ultBox.width, ultBox.height);
        }
    }
}
