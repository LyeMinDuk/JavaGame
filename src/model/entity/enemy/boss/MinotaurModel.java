package model.entity.enemy.boss;

import java.awt.Rectangle;
import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import util.enemy.EnemyStateIndex.Minotaur;

import static core.GameConfig.SCALE;
import static core.GameConfig.TILE_SIZE;
import static util.enemy.EnemyAIState.*;

public class MinotaurModel extends EnemyModel {
    private final double detectRange = TILE_SIZE * 10;
    private final double atkRange = TILE_SIZE * 1.5;
    private final long atkCD = 2000;
    private long lastAtkTime = 0;
    private boolean invulnerable = false;
    private long invulnerableUntil = 0;
    private boolean hit1Done = false;
    private boolean hit2Done = false;

    private final int atkStartFrame = 3;
    private final int atkEndFrame = 9;

    private boolean phase2 = false;
    private boolean phase3 = false;

    public MinotaurModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
        this.aiState = IDLE;
        this.moveSpeed = 0.6 * SCALE;
        this.setHitBox((int) (91 * SCALE), (int) (36 * SCALE), (int) (90 * SCALE), (int) (90 * SCALE));
    }

    @Override
    public void updateAi(PlayerModel player) {
        long now = System.currentTimeMillis();
        if (aiState == DIE || (aiState == HURT && now < hurtUntil)) {
            dx = dy = 0;
            refreshState();
            return;
        }
        if (invulnerable && now >= invulnerableUntil) {
            invulnerable = false;
        }
        if (!phase2 && (1.0 * getCurHealth() / getMaxHealth() < 0.5)) {
            phase2 = true;
            damage *= 2;
            moveSpeed = 0.8 * SCALE;
            invulnerable = true;
            invulnerableUntil = now + 5000;
        }

        if (!phase3 && (1.0 * getCurHealth() / getMaxHealth() < 0.3)) {
            phase3 = true;
            damage *= 2;
            curHealth = maxHealth;
            invulnerable = true;
            invulnerableUntil = now + 15000;
        }
        Rectangle hitbox = getHitbox();
        Rectangle playerBox = player.getHitbox();
        double centerPlayer = (playerBox.x + playerBox.width) / 2.0;
        double centerEnemy = (hitbox.x + hitbox.width) / 2.0;
        double distX = centerPlayer - centerEnemy;
        double absX = Math.abs(distX);

        if (aiState == ATTACK) {
            dx = 0;
            if (aniIndex >= 14) {
                aiState = IDLE;
                lastAtkTime = now;
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
                    hit1Done = hit2Done = false;
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
    public void takeDamage(int amount) {
        long now = System.currentTimeMillis();
        if (invulnerable && now < invulnerableUntil) {
            return;
        }
        super.takeDamage(amount);
    }

    @Override
    public void refreshState() {
        switch (aiState) {
            case PATROL, CHASE -> aniState = Minotaur.RUN;
            case ATTACK -> aniState = Minotaur.ATTACK;
            case HURT -> aniState = Minotaur.HURT;
            case DIE -> aniState = Minotaur.DIE;
            default -> aniState = Minotaur.IDLE;
        }
    }

    private void tryAttack(PlayerModel player) {
        if (aiState != ATTACK)
            return;
        int frame = aniIndex;
        Rectangle atkBox = getAttackBox();
        if (!hit1Done && frame >= atkStartFrame) {
            if (atkBox.intersects(player.getHitbox())) {
                player.takeDamage(damage);
            }
            hit1Done = true;
        }
        if (!hit2Done && frame >= atkEndFrame) {
            if (atkBox.intersects(player.getHitbox())) {
                player.takeDamage(damage);
            }
            hit2Done = true;
        }
    }

    @Override
    public Rectangle getAttackBox() {
        Rectangle hb = getHitbox();
        int atkW = (int) (50 * SCALE);
        int atkH = (int) (50 * SCALE);
        int atkOffset = (int) (20 * SCALE);
        int x = facingRight ? hb.x + hb.width + atkOffset : hb.x - atkW - atkOffset;
        int y = hb.y + (hb.height - atkH);
        return new Rectangle(x, y, atkW, atkH);
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    @Override
    public boolean isBoss() {
        return true;
    }
}