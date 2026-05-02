package model.entity.enemy.boss;

import java.awt.Rectangle;
import java.util.Random;

import model.entity.PlayerModel;
import model.entity.enemy.EnemyModel;
import util.enemy.EnemyStateIndex.Cthulu;

import static core.GameConfig.SCALE;
import static core.GameConfig.TILE_SIZE;
import static util.enemy.EnemyAIState.*;

public class CthuluModel extends EnemyModel {
    private final double detectRange = TILE_SIZE * 10;
    private double atkRange = TILE_SIZE * 1.5;
    private long atkCD = 2000;
    private long lastAtkTime = 0;
    private boolean hit1Done = false;
    private boolean hit2Done = false;
    private boolean magicDone = false;
    private Random rand = new Random();

    private final int atkStartFrame = 3;
    private final int atkStartFrame2 = 11;
    private final int atkEndFrame = 14;

    private boolean phase2 = false;
    private boolean phase3 = false;

    private int curSkill = -1;
    private final int[] hitFrames = { 8, 6, 8 };
    private final int[] endFrames = { 13, 11, 14 };
    private Rectangle magicBox;
    private int magicAniIndex = -1;

    public CthuluModel(double x, double y, int width, int height, int maxHealth, int damage) {
        super(x, y, width, height, maxHealth, damage);
        this.aiState = IDLE;
        this.moveSpeed = 1 * SCALE;
        this.setHitBox((int) (79 * SCALE), (int) (47 * SCALE), (int) (24 * SCALE), (int) (50 * SCALE));
    }

    @Override
    public void updateAi(PlayerModel player) {
        long now = System.currentTimeMillis();
        if (aiState == DIE || (aiState == HURT && now < hurtUntil)) {
            dx = dy = 0;
            refreshState();
            return;
        }
        if (!phase2 && (1.0 * getCurHealth() / getMaxHealth() <= 0.5)) {
            phase2 = true;
            damage *= 2;
            atkRange *= 3;
        }

        if (!phase3 && (1.0 * getCurHealth() / getMaxHealth() <= 0.3)) {
            phase3 = true;
            curHealth = maxHealth;
            damage *= 2;
            atkCD /= 2;
        }
        Rectangle hitbox = getHitbox();
        Rectangle playerBox = player.getHitbox();
        double centerPlayer = playerBox.x + playerBox.width / 2.0;
        double centerEnemy = hitbox.x + hitbox.width / 2.0;
        double distX = centerPlayer - centerEnemy;
        double absX = Math.abs(distX);

        if (phase2) {
            if (aiState == ATTACK) {
                dx = 0;
                tryAttack(player);
                if (magicBox == null) {
                    aiState = IDLE;
                }
                refreshState();
                return;
            } else {
                if (absX <= atkRange && now - lastAtkTime >= atkCD) {
                    aiState = ATTACK;
                    dx = 0;
                    magicDone = false;
                    curSkill = rand.nextInt(3);

                    int size = (int) (128 * SCALE);
                    int mx = playerBox.x + playerBox.width / 2 - size / 2;
                    int my = playerBox.y + playerBox.height / 2 - size / 2;
                    magicBox = new Rectangle(mx, my, size, size);

                    aniIndex = 0;
                    magicAniIndex = -1;
                    lastAtkTime = now;
                    facingRight = distX > 0;
                } else if (absX <= detectRange) {
                    dx = distX > 0 ? moveSpeed : -moveSpeed;
                    aiState = CHASE;
                } else {
                    dx = 0;
                    aiState = IDLE;
                }
            }
            refreshState();
            return;
        }
        if (aiState == ATTACK) {
            dx = 0;
            if (aniIndex >= 15) {
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
    public void refreshState() {
        if (phase2) {
            switch (aiState) {
                case ATTACK -> aniState = Cthulu.ATTACK1;
                case CHASE -> aniState = Cthulu.FLY;
                case HURT -> aniState = Cthulu.HURT;
                case DIE -> aniState = Cthulu.DIE;
                default -> aniState = Cthulu.FLY;
            }
        } else {
            switch (aiState) {
                case PATROL, CHASE -> aniState = Cthulu.RUN;
                case ATTACK -> aniState = Cthulu.ATTACK;
                case HURT -> aniState = Cthulu.HURT;
                case DIE -> aniState = Cthulu.DIE;
                default -> aniState = Cthulu.IDLE;
            }
        }
    }

    private void tryAttack(PlayerModel player) {
        if (aiState != ATTACK)
            return;
        if (phase2) {
            tryMagicAttack(player);
            return;
        }
        int frame = aniIndex;
        Rectangle atkBox = getAttackBox();
        if (!hit1Done && frame >= atkStartFrame) {
            if (atkBox.intersects(player.getHitbox())) {
                player.takeDamage(damage);
            }
            hit1Done = true;
        }
        if (!hit2Done && frame >= atkStartFrame2 && frame <= atkEndFrame) {
            if (atkBox.intersects(player.getHitbox())) {
                player.takeDamage(damage);
            }
            hit2Done = true;
        }
    }

    private void tryMagicAttack(PlayerModel player) {
        int frame = magicAniIndex;
        if (frame == -1)
            return;
        int hitFrame = hitFrames[curSkill];
        int endFrame = endFrames[curSkill];

        handleMagic(player, frame, hitFrame);
        if (frame >= endFrame) {
            aiState = IDLE;
            magicBox = null;
        }
    }

    private void handleMagic(PlayerModel player, int frame, int hitFrame) {
        if (!magicDone && frame == hitFrame) {
            if (magicBox != null && magicBox.intersects(player.getHitbox())) {
                player.takeDamage(damage);
            }
            magicDone = true;
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

    public int getCurSkill() {
        return curSkill;
    }

    public Rectangle getMagicBox() {
        return magicBox;
    }

    public void setMagicAniIndex(int magicAniIndex) {
        this.magicAniIndex = magicAniIndex;
    }

    public int getMagicAniIndex() {
        return magicAniIndex;
    }

    @Override
    public boolean isBoss() {
        return true;
    }
}