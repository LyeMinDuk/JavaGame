package model.entity.enemy.boss;

import java.awt.Rectangle;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import util.enemy.EnemyStateIndex.FrostGuardian;

import static core.GameConfig.SCALE;
import static core.GameConfig.TILE_SIZE;
import static util.enemy.EnemyAIState.*;

public class FrostGuardianModel extends EnemyModel {
    private final double detectRange = TILE_SIZE * 10;
    private final double atkRange = TILE_SIZE * 3;
    private long atkCD = 1500;
    private long lastAtkTime = 0;

    private final int atkStartFrame = 6;
    private final int atkEndFrame = 8;

    private boolean phase2 = false;
    private boolean phase3 = false;

    public FrostGuardianModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
        this.aiState = IDLE;
        this.moveSpeed = 0.7 * SCALE;
        this.setHitBox((int) (56 * SCALE), (int) (32 * SCALE), (int) (78 * SCALE), (int) (78 * SCALE));
    }

    @Override
    public void updateAi(PlayerModel player) {
        long now = System.currentTimeMillis();
        if (aiState == DIE || (aiState == HURT && now < hurtUntil)) {
            dx = dy = 0;
            refreshState();
            return;
        }
        if (!phase2 && ((1.0 * getCurHealth() / getMaxHealth()) <= 0.5)) {
            phase2 = true;
            damage *= 2;
        }

        if (!phase3 && ((1.0 * getCurHealth() / getMaxHealth()) <= 0.3)) {
            phase3 = true;
            curHealth = maxHealth;
        }
        Rectangle hitbox = getHitbox();
        Rectangle playerBox = player.getHitbox();
        double centerPlayer = playerBox.x + playerBox.width / 2.0;
        double centerEnemy = hitbox.x + hitbox.width / 2.0;
        double distX = centerPlayer - centerEnemy;
        double absX = Math.abs(distX);
        if (aiState == ATTACK) {
            dx = 0;
            if (aniIndex >= 13) {
                aiState = IDLE;
            } else {
                tryAttack(player);
                refreshState();
                return;
            }
        } else {
            facingRight = distX > 0;
            if (absX <= atkRange) {
                if (now - lastAtkTime >= atkCD) {
                    aiState = ATTACK;
                    dx = 0;
                    facingRight = distX > 0;
                } else {
                    dx = 0;
                    aiState = IDLE;
                }
            } else if (absX <= detectRange) {
                dx = distX > 0 ? moveSpeed : -moveSpeed;
                aiState = CHASE;
            } else {
                aiState = IDLE;
                dx = 0;
            }
        }
        refreshState();
    }

    @Override
    public void refreshState() {
        switch (aiState) {
            case PATROL, CHASE -> aniState = FrostGuardian.RUN;
            case ATTACK -> aniState = FrostGuardian.ATTACK;
            case HURT -> aniState = FrostGuardian.HURT;
            case DIE -> aniState = FrostGuardian.DIE;
            default -> aniState = FrostGuardian.IDLE;
        }
    }

    private void tryAttack(PlayerModel player) {
        if (aiState != ATTACK)
            return;
        long now = System.currentTimeMillis();
        int frame = aniIndex;
        if (now - lastAtkTime >= atkCD) {
            Rectangle atkBox = getAttackBox();
            if (frame >= atkStartFrame && frame <= atkEndFrame) {
                if (atkBox.intersects(player.getHitbox())) {
                    if (phase2) {
                        double chance = phase3 ? 0.9 : 0.7;
                        if (Math.random() < chance && !player.isFrozen()) {
                            player.applyFrozen();
                        }
                    }
                    player.takeDamage(damage);
                }
                lastAtkTime = now;
            }
        }
    }

    @Override
    public Rectangle getAttackBox() {
        Rectangle hb = getHitbox();
        int atkW = (int) (68 * SCALE);
        int atkH = (int) (22 * SCALE);
        int atkOffset = (int) (-17 * SCALE);
        int x = facingRight ? hb.x + hb.width + atkOffset : hb.x - atkW - atkOffset;
        int y = hb.y + (int) (26 * SCALE);
        return new Rectangle(x, y, atkW, atkH);
    }

    @Override
    public boolean isBoss() {
        return true;
    }
    
}