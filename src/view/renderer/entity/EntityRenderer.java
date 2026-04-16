package view.renderer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import model.entity.EntityModel;

public class EntityRenderer {
    public static void drawHB(Graphics g, EntityModel entity, int xOffset){
        Rectangle hitbox = entity.getHitbox();
        g.setColor(Color.red);
        g.drawRect(hitbox.x - xOffset, hitbox.y, hitbox.width, hitbox.height);
    }
}
