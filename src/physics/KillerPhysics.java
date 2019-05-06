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
public static class KillerPhysics {

    public static Controlled move(Controlled ship) {

        double maxspeedX = ship.maxspeed * ship.dx;
        double maxspeedY = ship.maxspeed * ship.dy;

        if (ship.vx + ship.a <= maxspeedX) {
            ship.vx += ship.a;
        } else if (ship.vx - ship.a > maxspeedX) {
            ship.vx -= ship.a;
        } else {
        }

        if (ship.vy + ship.a < maxspeedY) {
            ship.vy += ship.a;
        } else if (ship.vy - ship.a > maxspeedY) {
            ship.vy -= ship.a;
        } else {
        }

        return updateHitBox(ship);
    }

    public static Controlled updateHitBox(Controlled ship) {
        ship.x += ship.vx;
        ship.y += ship.vy;

        ship.tX = (ship.x + ship.WIDTH / 2) - (Math.sin(ship.radians) * (ship.HEIGHT / 2));
        ship.tY = (ship.y + ship.HEIGHT / 2) - (Math.cos(ship.radians) * (ship.HEIGHT / 2));

        ship.lX = (ship.x + ship.WIDTH / 2) + (Math.sin(ship.radians) * (ship.HEIGHT / 2)) + (Math.sin(ship.radians + Math.PI / 2) * (ship.WIDTH / 2));
        ship.lX = (ship.y + ship.HEIGHT / 2) + (Math.cos(ship.radians) * (ship.HEIGHT / 2)) + (Math.cos(ship.radians + Math.PI / 2) * (ship.WIDTH / 2));

        ship.rX = (ship.x + ship.WIDTH / 2) + (Math.sin(ship.radians) * (ship.HEIGHT / 2)) - (Math.sin(ship.radians + Math.PI / 2) * (ship.WIDTH / 2));
        ship.rY = (ship.y + ship.HEIGHT / 2) + (Math.cos(ship.radians) * (ship.HEIGHT / 2)) - (Math.cos(ship.radians + Math.PI / 2) * (ship.WIDTH / 2));

        return ship;
    }

    public static Shoot move(Shoot bullet) {

        bullet.x += bullet.speed * Math.cos(bullet.radians);
        bullet.y += bullet.speed * Math.sin(bullet.radians) * -1;

        return bullet;
    }
}
