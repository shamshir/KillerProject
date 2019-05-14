/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import visibleObjects.*;

/**
 *
 * @author Javi
 */
public class KillerPhysics {

    public static void move(KillerShip ship) {

        double x = ship.getX();
        double y = ship.getY();
        double maxspeedX = ship.getMaxspeed() * ship.getDx();
        double maxspeedY = ship.getMaxspeed() * ship.getDy();
        double vx = ship.getVx();
        double vy = ship.getVy();
        double a = ship.getA();

        if (vx + a <= maxspeedX) {
            vx += a;
        } else if (vx - a > maxspeedX) {
            vx -= a;
        } else {
        }

        if (vy + a < maxspeedY) {
            vy += a;
        } else if (vy - a > maxspeedY) {
            vy -= a;
        } else {
        }

        x += vx;
        y += vy;
        
        ship.setX(x);
        ship.setY(y);

        updateHitBox(ship);
    }

    public static void updateHitBox(KillerShip ship) {
        
        double x = ship.getX();
        double y = ship.getY();
        
        double tX = ship.getTx();
        double tY = ship.getTy();
        
        double lX = ship.getLx();
        double lY = ship.getLy();
        
        double rX = ship.getRx();
        double rY = ship.getRy();
        
        double radians = ship.getRadians();
        
        int WIDTH = ship.getImgWidth();
        int HEIGHT = ship.getImgHeight();

        tX = (x + WIDTH / 2) - (Math.sin(radians) * (HEIGHT / 2));
        tY = (y + HEIGHT / 2) - (Math.cos(radians) * (HEIGHT / 2));

        lX = (x + WIDTH / 2) + (Math.sin(radians) * (HEIGHT / 2)) + (Math.sin(radians + Math.PI / 2) * (WIDTH / 2));
        lX = (y + HEIGHT / 2) + (Math.cos(radians) * (HEIGHT / 2)) + (Math.cos(radians + Math.PI / 2) * (WIDTH / 2));

        rX = (x + WIDTH / 2) + (Math.sin(radians) * (HEIGHT / 2)) - (Math.sin(radians + Math.PI / 2) * (WIDTH / 2));
        rY = (y + HEIGHT / 2) + (Math.cos(radians) * (HEIGHT / 2)) - (Math.cos(radians + Math.PI / 2) * (WIDTH / 2));
        
        ship.setTx(tX);
        ship.setTy(tY);
        
        ship.setRx(rX);
        ship.setRy(rY);
        
        ship.setLx(lX);
        ship.setLy(lY);
    }

    public static void move(Shoot bullet) {
        
        double x = bullet.getX();
        double y = bullet.getY();
        double radians = bullet.getRadians();
        double speed = bullet.getMaxspeed();

        x += speed * Math.cos(radians);
        y += speed * Math.sin(radians) * -1;
        
        bullet.setX(x);
        bullet.setY(y);

    }
}
