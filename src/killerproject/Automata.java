/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class Automata extends Alive {

    public Automata(KillerGame kg, Color color) {
        this.kg = kg;
        alive = true;

        HEIGHT = 30;
        WIDTH = 30;

        speed = 5.2;
        dx = speed;
        dy = speed;
        x = (int) (kg.getViewer().getWidth() / 2 * Math.random());
        y = (int) (kg.getViewer().getHeight() / 2 * Math.random());

        hitbox = new Rectangle((int) x, (int) y, WIDTH, HEIGHT);

        this.color = color;
        colorhex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        time = System.nanoTime();

    }

    public void run() {

        while (alive) {
            move();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {

            }
        }
    }

    public void collision() {
        dx *= -1;
        dy *= -1;
    }

    public void death() {
        alive = false;
        kg.getObjects().remove(this);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) x, (int) y, HEIGHT, WIDTH);
        // g.drawImage(,x, y,null);
    }

    @Override
    public void move() {

        double timedif = (System.nanoTime() - time) / 10000000d;
        kg.checkColision(this);

        x += dx * timedif;
        y += dy * timedif;

        updateHitBox();

        time = System.nanoTime();

    }

    public Rectangle nextMove() {
        return new Rectangle((int) x + (int) dx, (int) y + (int) dy, WIDTH, HEIGHT);
    }

    public void updateHitBox() {
        hitbox.setBounds((int) x, (int) y, WIDTH, HEIGHT);
    }

}
