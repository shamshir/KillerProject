package visibleObjects;

import game.KillerGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;
import java.awt.geom.AffineTransform;
import visualEffects.Animacion;

public class Asteroid extends Autonomous {

    public Asteroid(KillerGame game) {
        super(game);
        this.type = "asteroid";
        this.angle = 25;
        this.color = Color.GREEN;
        //alive = true;

        this.setImage("img/killerShip.png", 300);

//        height = 30;
//        width = 30;
        this.height = this.killerImage.getHeight();
        this.width = this.killerImage.getWidth();
//        this.width = (this.height * this.image.getWidth()) / this.image.getHeight();

        //speed = 5.2;
        this.speed = 6;
        this.dx = this.speed;
        this.dy = this.speed;
        this.x = 200;
        this.y = 200;
//        this.x = (int) (game.getViewer().getWidth() / 2 * Math.random());
//        this.y = (int) (game.getViewer().getHeight() / 2 * Math.random());

        this.hitbox = new Rectangle((int) x, (int) y, 1, 1);
        this.colorhex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        this.time = System.nanoTime();

    }

    @Override
    public void move() {

        double timedif = (System.nanoTime() - time) / 10000000d;

        x += dx * timedif;
        y += dy * timedif;

        updateHitBox();

        time = System.nanoTime();

    }

    @Override
    public void die() {
        alive = false;
        game.getObjects().remove(this);
    }

    @Override
    public void render(Graphics2D g2d) {

        AffineTransform old = g2d.getTransform();

//        Animacion.addFire(this.image);
//        Animacion.blackHole(this);
//        Animacion.addRotation(g2d, this);
        g2d.drawImage(this.killerImage, (int) x, (int) y, this.width, this.height, null);

        g2d.setTransform(old);    
    }

    // *********************
    // * Getters & Setters *
    // *********************
}
