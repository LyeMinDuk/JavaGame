package controller;

import model.MapModel;
import model.entity.EntityModel;
import model.entity.PlayerModel;
import model.entity.enemy.SharkModel;

import static core.GameConfig.*;

import java.awt.Rectangle;

public class PhysicsController {
    private final MapModel map;

    public PhysicsController(MapModel map) {
        this.map = map;
    }

    public void apply(EntityModel entity) {
        applyGravity(entity);
        moveY(entity); // Y trước để rơi/nhảy ổn định kiểu platformer
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

        if (canMove(nextX, hb.y, hb.width, hb.height, map)) {
            e.move(dx, 0);
            return;
        }

        // snap sát tường gần nhất, không đẩy xa
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

        // snap sát trần/sàn gần nhất
        int newHitboxY = getEntityYAboveFloor(hb, dy);
        e.setPosition(e.getX(), newHitboxY - e.getHbOffsetY());

        if (dy > 0) { // rơi xuống sàn
            e.setDy(0);
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
        // System.out.println("onGround: " + onGround + ", currentY: " + entity.getY());
    }

    private boolean isOnGround(EntityModel e) {
        Rectangle hb = e.getHitbox();
        int inset = 2;
        return !canMove(hb.x + inset, hb.y + hb.height, hb.width, 1, map);
    }

    // ===== Snap helpers (mượt, sát mép tile) =====
    private int getEntityXNextToWall(Rectangle hb, double xSpeed) {
        int currentTile = hb.x / TILE_SIZE;

        if (xSpeed > 0) { // sang phải: đặt cạnh phải hb sát cạnh trái tile cản
            int tileXPos = (currentTile + 1) * TILE_SIZE;
            return tileXPos - hb.width;
        } else { // sang trái: đặt cạnh trái hb sát cạnh phải tile cản
            return currentTile * TILE_SIZE;
        }
    }

    private int getEntityYAboveFloor(Rectangle hb, double airSpeed) {
        int currentTile = hb.y / TILE_SIZE;

        if (airSpeed > 0) { // rơi: đặt đáy hb sát đỉnh tile sàn
            int tileYPos = (currentTile + 1) * TILE_SIZE;
            return tileYPos - hb.height;
        } else { // nhảy: đặt đỉnh hb sát đáy tile trần
            return currentTile * TILE_SIZE;
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
        int maxY = map.getTileHeight() * TILE_SIZE;

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