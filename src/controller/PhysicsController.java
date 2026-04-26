package controller;

import model.MapModel;
import model.entity.EntityModel;
import model.entity.PlayerModel;

import static core.GameConfig.*;

import java.awt.Rectangle;

import controller.entity.enemy.EnemyController;

public class PhysicsController {
    private MapModel map;
    private EnemyController enemyController;

    public PhysicsController(MapModel map, EnemyController enemyController) {
        this.map = map;
        this.enemyController = enemyController;
    }

    public void apply(EntityModel entity) {
        applyGravity(entity);
        moveY(entity);
        moveX(entity);
        updateOnGround(entity);
    }

    private void applyGravity(EntityModel entity) {
        if (!entity.isOnGround()) {
            double dy = entity.getDy() + GRAVITY;
            if (dy > MAX_FALL_SPEED)
                dy = MAX_FALL_SPEED;
            entity.setDy(dy);
        }
    }

    private void moveX(EntityModel e) {
        double dx = e.getDx();
        if (dx == 0)
            return;
        Rectangle hb = e.getHitbox();
        int nextX = (int) (hb.x + dx);
        if (e instanceof PlayerModel && map.getBossCheckpoint() != -1) {
            int barrierPixX = map.getBossCheckpoint() * TILE_SIZE;
            int enemyNum = enemyController.getListEnemy().size();

            if (enemyNum > 1) {
                if (hb.x + hb.width <= barrierPixX && nextX + hb.width > barrierPixX) {
                    e.setPosition(barrierPixX - hb.width - e.getHbOffsetX(), e.getY());
                    e.setDx(0);
                    return;
                }
            } else {
                if (hb.x >= barrierPixX && nextX < barrierPixX) {
                    
                    e.setPosition(barrierPixX - e.getHbOffsetX(), e.getY());
                    e.setDx(0);
                    return;
                }
            }
        }
        if (canMove(nextX, hb.y, hb.width, hb.height, map)) {
            e.move(dx, 0);
            return;
        }

        int newHitboxX = getEntityXNextToWall(hb, dx);
        e.setPosition(newHitboxX - e.getHbOffsetX(), e.getY());
        e.setDx(0);

    }

    private void moveY(EntityModel e) {
        double dy = e.getDy();
        if (dy == 0)
            return;
        Rectangle hb = e.getHitbox();
        int nextY = (int) (hb.y + dy);
        if (canMove(hb.x, nextY, hb.width, hb.height, map)) {
            e.move(0, dy);
            return;
        }
        int newHitboxY = getEntityYAboveFloor(hb, dy);
        e.setPosition(e.getX(), newHitboxY - e.getHbOffsetY());
        if (dy > 0) {
            e.setDy(0);
            e.setOnGround(true);
        }
    }

    private void updateOnGround(EntityModel entity) {
        boolean onGround = isOnGround(entity);
        entity.setOnGround(onGround);

        if (onGround && entity.getDy() > 0) {
            entity.setDy(0);

        }
        if (entity instanceof PlayerModel p) {
            p.setJumping(!onGround);
        }
    }

    private boolean isOnGround(EntityModel e) {
        Rectangle hb = e.getHitbox();
        int inset = 2;
        return !canMove(hb.x + inset, hb.y + hb.height + 1, hb.width - inset * 2, 2, map);
    }

    private int getEntityYAboveFloor(Rectangle hb, double dy) {
        int nextY = (int) (hb.y + dy);
        if (dy > 0) {
            int bottomY = nextY + hb.height;
            int tileY = bottomY / TILE_SIZE;
            return tileY * TILE_SIZE - hb.height;
        } else {
            int topY = nextY;
            int tileY = topY / TILE_SIZE;
            return (tileY + 1) * TILE_SIZE;
        }
    }

    private int getEntityXNextToWall(Rectangle hb, double dx) {
        int nextX = (int) (hb.x + dx);
        if (dx > 0) {
            int rightX = nextX + hb.width;
            int tileX = rightX / TILE_SIZE;
            return tileX * TILE_SIZE - hb.width;
        } else {
            int tileX = nextX / TILE_SIZE;
            return (tileX + 1) * TILE_SIZE;
        }
    }

    public static boolean canMove(int x, int y, int width, int height, MapModel map) {
        return !isSolid(x, y, map)
                && !isSolid(x + width - 1, y, map)
                && !isSolid(x + width - 1, y + height - 1, map)
                && !isSolid(x, y + height - 1, map);
    }

    private static boolean isSolid(int x, int y, MapModel map) {
        int maxX = map.getTileWide() * TILE_SIZE;
        int maxY = map.getTileHigh() * TILE_SIZE;
        if (x < 0 || x >= maxX)
            return true;
        if (y < 0 || y >= maxY)
            return true;
        int tileX = x / TILE_SIZE;
        int tileY = y / TILE_SIZE;
        return isTileSolid(tileX, tileY, map);
    }

    public static boolean isTileSolid(int x, int y, MapModel map) {
        int idx = map.getTile(x, y);
        return idx != 11;
    }
}