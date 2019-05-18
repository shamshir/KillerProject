/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import visibleObjects.Shoot;

/**
 *
 * @author berna
 */
public class PhysicsShoot {

    private Shoot shoot;

    private double x, y, speed, radians;

    public PhysicsShoot(Shoot shoot) {

        this.shoot = shoot;
        x = shoot.getX();
        y = shoot.getY();
        radians = shoot.getRadians();
        speed = shoot.getMaxspeed();

    }

    public void move() {

        x += speed * Math.cos(radians);
        y += speed * Math.sin(radians) * -1;

        shoot.setX(x);
        shoot.setY(y);

    }

}
