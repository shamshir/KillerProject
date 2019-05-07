package visibleObjects;

import communications.KillerPad;
import game.KillerGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Shoot extends Autonomous {

    private KillerShip ship;

    public Shoot(KillerGame game, Color color, KillerShip ship) {
        super(game);
        type = "shoot";
        this.color = color;
        this.ship = ship;

        // Modificar
//        this.width = (int) (Math.min(ship.height, ship.width) / 2);
//        this.height = (int) (Math.min(ship.height, ship.width) / 2);
        this.width = 10;
        this.height = 10;

        //this.radius = (int) (Math.min(ship.height, ship.width) / 2);
        // Segun como se calculen las colisiones hay que pintar la bala fuera de la nave
//        this.x = ship.getX();
        //this.y = ship.getY() + (this.radius / 2);
//        this.y = ship.getY() + (this.height / 2);
        //pintar la bala
        // x = sin(angulo) * radio  !!angulo en radianes!!
        // y = cos(angulo) * radio  !!angulo en radianes!!
        double radio = this.ship.height / 2;

        double sin = Math.sin(Math.toRadians(this.ship.getAngle()));
        double cos = Math.cos(Math.toRadians(this.ship.getAngle()));

        this.x = sin * radio;
        this.y = cos * radio;

        System.out.println("sin * radio: " + x);
        System.out.println("cos * radio: " + y);

        this.x += this.ship.getX() + this.ship.getWidth() / 2;
        this.y = (this.ship.getY() + this.ship.getHeight() / 2) - this.y;

        System.out.println("this.x: " + x + " -- " + "this.y: " + y + "\n");

//        this.x = (Math.sin(Math.toRadians(this.ship.getAngle())) * this.ship.height / 2) + (this.ship.getX() + this.ship.getWidth() / 2);
//        this.y = (Math.cos(Math.toRadians(this.ship.getAngle())) * this.ship.height / 2) + (this.ship.getY() + this.ship.getHeight() / 2);
//        this.x = (Math.sin(this.ship.getAngle()) * this.ship.height / 2);
//        this.y = (Math.cos(this.ship.getAngle()) * this.ship.height / 2);

//        System.out.println("shoot pos x: " + this.x);
//        System.out.println("shoot pos y: " + this.y);
//
//        System.out.println("shoot sin: " + Math.sin(this.ship.getAngle()) * this.ship.height / 2);
//        System.out.println("shoot cos: " + Math.cos(this.ship.getAngle()) * this.ship.height / 2);

        this.hitbox = new Rectangle((int) this.x, (int) this.y, this.width, this.height);
        //this.frightdir = ship.fright;
        //this.speed = 15;

        if (ship.fright) {
            this.speed = 15;
        } else {
            this.speed = -15;
        }

        time = System.nanoTime();
    }

    @Override
    public void move() {

//        double timedif = (System.nanoTime() - time) / 10000000d;
//        x += (int) speed * timedif;
//
//        updateHitBox();
//
//        time = System.nanoTime();
    }

    @Override
    public void updateHitBox() {
        hitbox.setBounds((int) x, (int) y, width, height);
    }

    @Override
    public void die() {
        ship.getShoots().remove(this);
        alive = false;
    }

    @Override
    public void render(Graphics2D g2d) {
        if (alive) {
//            System.out.println("------------->PINTAR SHOOT");
            g2d.setColor(color);
            g2d.fillOval((int) x, (int) y, width, height);
            // g.drawImage(,x, y,null);
        }
    }

    public void points(int points) {
        KillerPad.sendMessageToPad("pnt" + points, game, ship.getIp(), game.getIplocal());
    }

    // *********************
    // * Getters & Setters *
    // *********************
    public KillerShip getControlled() {
        return ship;
    }

//    public int getRadius() {
//        return radius;
//    }
//
//    public void setRadius(int radius) {
//        this.radius = radius;
//    }
//    public boolean isFrightdir() {
//        return frightdir;
//    }
//
//    public void setFrightdir(boolean frightdir) {
//        this.frightdir = frightdir;
//    }
    public KillerShip getShip() {
        return ship;
    }

    public void setShip(KillerShip ship) {
        this.ship = ship;
    }

}
