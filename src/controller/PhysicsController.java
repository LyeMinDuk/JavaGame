package controller;

import model.MapModel;
import model.entity.PlayerModel;

import static core.GameConfig.TILE_SIZE;

public class PhysicsController {
    private static final double GRAVITY = 0.05;
    private static final double TERMINAL_VELOCITY = 3.5;

    public void update(PlayerModel player, MapModel map) {
        double startX = player.getX();
        double startY = player.getY();

        double intendedDx = player.getDx();
        double velocityY = player.getDy() + GRAVITY;
        if (velocityY > TERMINAL_VELOCITY) {
            velocityY = TERMINAL_VELOCITY;
        }

        double resolvedDx = resolveX(player, map, intendedDx);
        double resolvedDy = resolveY(player, map, startX + resolvedDx, velocityY);

        double x = startX + resolvedDx;
        double y = startY + resolvedDy;
        boolean onGround = isSolidBelow(player, map, x, y);

        player.move(resolvedDx, resolvedDy);
        player.setDx(intendedDx);
        player.setDy(resolvedDy);

        if (onGround && player.getDy() >= 0) {
            player.setDy(0);
            player.setJumping(false);
            player.setFalling(false);
        } else if (player.getDy() < 0) {
            player.setJumping(true);
            player.setFalling(false);
        } else if (player.getDy() > 0) {
            player.setJumping(false);
            player.setFalling(true);
        }
    }

    private double resolveX(PlayerModel player, MapModel map, double dx) {
        double x = player.getX();
        double y = player.getY();

        if (dx > 0) {
            double nextRight = x + player.getWidth() + dx;
            double top = y + 1;
            double bottom = y + player.getHeight() - 1;
            if (map.isSolidAtWorld(nextRight, top) || map.isSolidAtWorld(nextRight, bottom)) {
                int tileX = (int) Math.floor(nextRight / TILE_SIZE);
                dx = tileX * TILE_SIZE - (x + player.getWidth());
            }
        } else if (dx < 0) {
            double nextLeft = x + dx;
            double top = y + 1;
            double bottom = y + player.getHeight() - 1;
            if (map.isSolidAtWorld(nextLeft, top) || map.isSolidAtWorld(nextLeft, bottom)) {
                int tileX = (int) Math.floor(nextLeft / TILE_SIZE);
                dx = (tileX + 1) * TILE_SIZE - x;
            }
        }

        return dx;
    }

    private double resolveY(PlayerModel player, MapModel map, double x, double dy) {
        double y = player.getY();

        if (dy > 0) {
            double nextBottom = y + player.getHeight() + dy;
            double left = x + 1;
            double right = x + player.getWidth() - 1;
            if (map.isSolidAtWorld(left, nextBottom) || map.isSolidAtWorld(right, nextBottom)) {
                int tileY = (int) Math.floor(nextBottom / TILE_SIZE);
                dy = tileY * TILE_SIZE - (y + player.getHeight());
            }
        } else if (dy < 0) {
            double nextTop = y + dy;
            double left = x + 1;
            double right = x + player.getWidth() - 1;
            if (map.isSolidAtWorld(left, nextTop) || map.isSolidAtWorld(right, nextTop)) {
                int tileY = (int) Math.floor(nextTop / TILE_SIZE);
                dy = (tileY + 1) * TILE_SIZE - y;
            }
        }

        return dy;
    }

    private boolean isSolidBelow(PlayerModel player, MapModel map, double x, double y) {
        double probeY = y + player.getHeight() + 1;
        double left = x + 1;
        double right = x + player.getWidth() - 1;
        return map.isSolidAtWorld(left, probeY) || map.isSolidAtWorld(right, probeY);
    }

}
