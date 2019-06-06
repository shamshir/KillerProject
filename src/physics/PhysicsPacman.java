/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import visibleObjects.Planeta;
import visibleObjects.Pacman;
import visibleObjects.Shoot;

/**
 *
 * @author berna
 */
public class PhysicsPacman {

    private Pacman pac;

    private double x, y, a, vx, vy, dx, dy,
            maxspeed, radians, tX, tY, lX, lY, rX, rY, radius, m;
    private int WIDTH, HEIGHT;

    public PhysicsPacman(Pacman pac) {

        this.pac = pac;
        updateFields();

    }

    public void move() {

        updateFields();

        vx = maxspeed * Math.cos(radians);
        vy = maxspeed * Math.sin(radians) * -1;

//        vx = maxspeed;
//        vy = maxspeed;
        x += vx;
        y += vy;

        setValues();
    }

    public void collisionXShoot(Shoot shoot) {
        radians = shoot.getRadians();
        setValues();

    }

    public void setValues() {

        pac.setX(x);
        pac.setY(y);
        pac.setVx(vx);
        pac.setVy(vy);
        pac.setDx(dx);
        pac.setDy(dy);

        pac.setA(a);
        pac.setMaxspeed(maxspeed);
        pac.setRadians(radians);

    //    pac.setImgWidth(WIDTH);
    //    pac.setImgHeight(HEIGHT);
    }

    public void updateFields() {
        x = pac.getX();
        y = pac.getY();
        vx = pac.getVx();
        vy = pac.getVy();
        dx = pac.getDx();
        dy = pac.getDy();
        a = pac.getA();
        maxspeed = pac.getMaxspeed();
        radians = pac.getRadians();
        radius = pac.getRadius();
        m = pac.getM();
        WIDTH = pac.getImgWidth();
        HEIGHT = pac.getImgHeight();

    }

}
