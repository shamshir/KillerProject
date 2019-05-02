package visibleObjects;

import game.KillerGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author miaad
 */
public class Pacman extends Autonomous {
    
    public Pacman(KillerGame game) {
        super(game);
        type = "asteroid";
        this.color = Color.YELLOW;
        //alive = true;

        height = 50;
        width = 50;

        //speed = 5.2;
        speed = 2.2;
        dx = speed;
        dy = speed;
        x = (int) (game.getViewer().getWidth() / 2 * Math.random());
        y = (int) (game.getViewer().getHeight() / 2 * Math.random());

        hitbox = new Rectangle((int) x, (int) y, width, height);
        colorhex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        time = System.nanoTime();

    }

    @Override
    public void move() {

        double timedif = (System.nanoTime() - time) / 10000000d;
        //game.checkColision(this);

        x += dx * timedif;
        y += dy * timedif;

        updateHitBox();

        time = System.nanoTime();

    }

    public void die() {
        alive = false;
        game.getObjects().remove(this);
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) x, (int) y, height, width);

        if (dx > 0) {
            g2d.fillArc((int) x, (int) y, width, height, 35, 290);
        } else {
            g2d.fillArc((int) x, (int) y, width, height, 215, 290);
        }
    }
    
}
