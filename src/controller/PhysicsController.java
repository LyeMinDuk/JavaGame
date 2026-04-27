package controller;

import java.awt.Rectangle;

import model.MapModel;
import model.entity.EntityModel;
import model.entity.PlayerModel;
import controller.entity.enemy.EnemyController;

import static core.GameConfig.*;

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

    private void moveY(EntityModel entity) {
        double dy = entity.getDy();
        // if (dy == 0)
        // return;
        Rectangle hb = entity.getHitbox();
        if (canMove(hb.x, (int) (hb.y + dy), hb.width, hb.height, map)) {
            entity.move(0, dy);
            return;
        }
        int newY = getYPositionNextTile(hb, dy);
        entity.setPosition(entity.getX(), newY - entity.getHbOffsetY());
        if (dy > 0) {
            entity.setDy(0);
            entity.setOnGround(true);
        }
    }

    private void moveX(EntityModel entity) {
        double dx = entity.getDx();
        // if (dx == 0)
        // return;
        Rectangle hb = entity.getHitbox();
        int nextX = (int) (hb.x + dx);
        if (entity instanceof PlayerModel && map.getBossCheckpoint() != -1) {
            int barrierBossX = map.getBossCheckpoint() * TILE_SIZE;
            int enemyNum = enemyController.getListEnemy().size();
            if (enemyNum > 1) {
                if (hb.x + hb.width <= barrierBossX && nextX + hb.width > barrierBossX) {
                    entity.setPosition(barrierBossX - hb.width - entity.getHbOffsetX(), entity.getY());
                    entity.setDx(0);
                    return;
                }
            } else {
                if (hb.x >= barrierBossX && nextX < barrierBossX) {
                    entity.setPosition(barrierBossX - entity.getHbOffsetX(), entity.getY());
                    entity.setDx(0);
                    return;
                }
            }
        }
        if (canMove(nextX, hb.y, hb.width, hb.height, map)) {
            entity.move(dx, 0);
            return;
        }
        int newHitboxX = getXPositionNextTile(hb, dx);
        entity.setPosition(newHitboxX - entity.getHbOffsetX(), entity.getY());
        entity.setDx(0);

    }

    private void updateOnGround(EntityModel entity) {
        boolean onGround = isOnGround(entity);
        entity.setOnGround(onGround);
        // if (onGround && entity.getDy() > 0) {
        //     entity.setDy(0);
        // }
        if (entity instanceof PlayerModel p) {
            p.setJumping(!onGround);
        }
    }

    private boolean isOnGround(EntityModel entity) {
        Rectangle hb = entity.getHitbox();
        int inset = 2;
        return !canMove(hb.x + inset, hb.y + hb.height + 1, hb.width - inset * 2, 2, map);
    }

    private int getYPositionNextTile(Rectangle hb, double dy) {
        int nextY = (int) (hb.y + dy);
        if (dy > 0) {
            int bottomY = nextY + hb.height;
            int tileY = bottomY / TILE_SIZE;
            return tileY * TILE_SIZE - hb.height;
        } else {
            int tileY = nextY / TILE_SIZE;
            return (tileY + 1) * TILE_SIZE;
        }
    }

    private int getXPositionNextTile(Rectangle hb, double dx) {
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
                && !isSolid(x + width, y, map)
                && !isSolid(x + width, y + height, map)
                && !isSolid(x, y + height, map);
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