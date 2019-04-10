/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author berna
 */
public class Shoot extends Automata implements Runnable {

    private int radius;
    private double speed;
    private boolean frightdir;
    private Controlled ship;
    Rectangle hitbox;

    private boolean alive;

    public Shoot(KillerGame kg, Color color, Controlled ship) {
        super(kg, color);
        this.ship = ship;
        this.radius = (int) (Math.min(ship.HEIGHT, ship.WIDTH) / 2);
        this.x = ship.getX();
        this.y = ship.getY() + (this.radius / 2);
        hitbox = new Rectangle((int) this.x, (int) this.y, this.radius, this.radius);
        this.frightdir = ship.fright;
        this.speed = 15;
        alive = true;

    }

    @Override
    public void run() {

        while (alive) {
            move();
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {

            }
        }

    }

    public void death() {
        ship.getShoots().remove(this);
        alive = false;
    }

    public void move() {
        kg.checkColision(this);
        if (this.frightdir) {
            x += (int) speed;
        } else {
            x -= (int) speed;
        }
        updateHitBox();
    }

    public void points(int points) {
        KillerPad.sendMessageToPad("pnt" + points, kg, ship.getIp(), kg.getIplocal());
    }

    public void updateHitBox() {
        hitbox.setBounds((int) x, (int) y, radius, radius);
    }

    public Controlled getControlled() {
        return ship;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isFrightdir() {
        return frightdir;
    }

    public void setFrightdir(boolean frightdir) {
        this.frightdir = frightdir;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public Controlled getShip() {
        return ship;
    }

    public void setShip(Controlled ship) {
        this.ship = ship;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
