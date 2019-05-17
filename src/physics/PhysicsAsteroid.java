/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import visibleObjects.Asteroid;

/**
 *
 * @author berna
 */
public class PhysicsAsteroid {

    private Asteroid ast;

    private double x, y, a, vx, vy, dx, dy,
            maxspeed, radians, tX, tY, lX, lY, rX, rY;
    private int WIDTH, HEIGHT;

    public PhysicsAsteroid(Asteroid ast) {

        this.ast = ast;
        updateFields();

    }

    public void move() {

        if (vx <= maxspeed) {
            if (vx + a <= maxspeed) {
                vx += a;

            } else {
                vx += 0.001;
            }
            System.out.println("menorX" + vx + "," + a);
        } else if (vx > maxspeed) {
            if (vx - a >= maxspeed) {
                vx -= a;
            } else {
                vx -= 0.001;
            }
            System.out.println("mayorX" + vx + "," + a);
        } else {
            //   vx = 2.01 * -Math.sin(radians);

            System.out.println("elseX" + vx);
        }

        if (vy <= maxspeed) {
            if (vy + a <= maxspeed) {
                vy += a;
            } else {
                vy += 0.001;
            }
            System.out.println("menorY" + vy + "," + a);
        } else if (vy > maxspeed) {
            if (vy - a > maxspeed) {
                vy -= a;
                System.out.println("mayorY" + vy + "," + a);
            } else {
                vy -= 0.001;
            }
        } else {
            //  vy = 2.01 * -Math.cos(radians);

            System.out.println("elseY" + vy);
        }

        x += vx;
        y += vy;
        
        setValues();
    }

    public void setValues() {

        ast.setX(x);
        ast.setY(y);
        ast.setVx(vx);
        ast.setVy(vy);
        ast.setDx(dx);
        ast.setDy(dy);

        ast.setA(a);
        ast.setMaxspeed(maxspeed);
        ast.setRadians(radians);

        ast.setImgWidth(WIDTH);
        ast.setImgHeight(HEIGHT);
    }

    public void updateFields() {
        x = ast.getX();
        y = ast.getY();
        vx = ast.getVx();
        vy = ast.getVy();
        dx = ast.getDx();
        dy = ast.getDy();
        a = ast.getA();
        maxspeed = ast.getMaxspeed();
        radians = ast.getRadians();

        WIDTH = ast.getImgWidth();
        HEIGHT = ast.getImgHeight();

    }

}
