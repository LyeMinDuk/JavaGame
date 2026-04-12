package controller;

import java.awt.Rectangle;

import model.MapModel;
import model.entity.PlayerModel;
import static core.GameConfig.*;

public class PhysicsController {
    private MapModel map;
    private double gravity = 0.2 * SCALE;
    private double maxFallSpeed = 3.0 * SCALE;

    public PhysicsController(MapModel map) {
        this.map = map;
    }

    public void updatePlayer(PlayerModel player) {
        double dy = player.getDy() + gravity;
        if (dy > maxFallSpeed) {
            dy = maxFallSpeed;
        }

        player.setDy(dy);
        moveX(player, player.getDx());
        boolean onGroundBefore = isOnGround(player);
        moveY(player, player.getDy());
        boolean onGroundAfter = isOnGround(player);

        player.setFalling(!onGroundAfter && player.getDy() > 0);
        if (onGroundAfter) {
            player.setJumping(false);
            player.setDy(0);
        } else if (!onGroundBefore && player.getDy() < 0) {
            player.setJumping(true);
        }
    }

    private void moveX(PlayerModel player, double dx) {
        if (dx == 0)
            return;
        Rectangle hitBox = player.getHitbox();

        Rectangle next = new Rectangle((int) (player.getX() + dx), hitBox.y, hitBox.width, hitBox.height);
        if (!collide(next)) {
            player.move(dx, 0);
            return;
        }
        player.setDx(0);
    }

    private void moveY(PlayerModel player, double dy) {
        if (dy == 0)
            return;
        Rectangle hitBox = player.getHitbox();

        Rectangle next = new Rectangle(hitBox.x, (int) (player.getY() + dy), hitBox.width, hitBox.height);
        if (!collide(next)) {
            player.move(0, dy);
            return;
        }
        player.setDy(0);
    }

    private boolean isOnGround(PlayerModel player) {
        Rectangle hitBox = player.getHitbox();
        Rectangle temp = new Rectangle(hitBox.x, hitBox.y + hitBox.height, hitBox.width, 1);
        return collide(temp);
    }

    private boolean collide(Rectangle hitBox) {
        int leftTile = hitBox.x / TILE_SIZE;
        int rightTile = (hitBox.x + hitBox.width - 1) / TILE_SIZE;
        int topTile = hitBox.y / TILE_SIZE;
        int bottomTile = (hitBox.y + hitBox.height - 1) / TILE_SIZE;

        for (int i = topTile; i <= bottomTile; i++) {
            for (int j = leftTile; j <= rightTile; j++) {
                if (isSolid(j, i))
                    return true;
            }
        }
        return false;
    }

    private boolean isSolid(int x, int y) {
        if (x < 0 || y < 0 || x >= map.getTileWide() || y >= map.getTileHeight()) {
            return true;
        }

        int tileIndex = map.getTile(x, y);
        return tileIndex != 11;
    }
}