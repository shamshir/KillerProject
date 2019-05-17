/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import visibleObjects.KillerShip;

/**
 *
 * @author berna
 */
public class PhysicsShip {

    private KillerShip ship;

    private double x, y, a, vx, vy, dx, dy,
            maxspeed, radians, tX, tY, lX, lY, rX, rY;
    private int WIDTH, HEIGHT;

    public PhysicsShip(KillerShip ship) {

        this.ship = ship;

        updateFields();

    }

    public void move() {

        //updateFields();
        angleUpdate();

        double maxspeedX = maxspeed * dx;
        double maxspeedY = maxspeed * dy;

        if (vx <= maxspeedX) {
            if (vx + a <= maxspeedX) {
                vx += a;

            } else {
                vx += 0.001;
            }
            System.out.println("menorX" + vx + "," + a);
        } else if (vx > maxspeedX) {
            if (vx - a >= maxspeedX) {
                vx -= a;
            } else {
                vx -= 0.001;
            }
            System.out.println("mayorX" + vx + "," + a);
        } else {
            //   vx = 2.01 * -Math.sin(radians);

            System.out.println("elseX" + vx);
        }

        if (vy <= maxspeedY) {
            if (vy + a <= maxspeedY) {
                vy += a;
            } else {
                vy += 0.001;
            }
            System.out.println("menorY" + vy + "," + a);
        } else if (vy > maxspeedY) {
            if (vy - a > maxspeedY) {
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

        updateHitBox();
        setValues();

    }

    public void angleUpdate() {

        // radian final
        double finalRadians = 0;

        if (dx <= 0 && dy * -1 > 0) {
            finalRadians = Math.atan(dx / dy);
            System.out.println(finalRadians);
        } else if (dx <= 0 && dy * -1 <= 0) {
            finalRadians = Math.atan(Math.abs(dy / dx)) + (Math.PI / 2);
            System.out.println(finalRadians);
        } else if (dx > 0 && dy * -1 <= 0) {
            finalRadians = Math.atan(Math.abs(dx / dy)) + Math.PI;;
        } else if (dx > 0 && dy * -1 > 0) {
            finalRadians = Math.atan(Math.abs(dy / dx)) + (Math.PI * 1.5);
        }

        if (finalRadians != radians) {
            if (dx != 0 || dy != 0) {
                if (radians < finalRadians) {
                    if (radians + 0.2 > finalRadians) {
                        radians += 0.015;
                    } else {
                        radians += 0.15;
                    }

                } else if (radians > finalRadians) {
                    if (radians - 0.2 < finalRadians) {
                        radians -= 0.015;
                    } else {
                        radians -= 0.15;
                    }
                }
            }
        }

    }

    public void setValues() {

        ship.setX(x);
        ship.setY(y);
        ship.setVx(vx);
        ship.setVy(vy);
        ship.setDx(dx);
        ship.setDy(dy);
        ship.setTx(tX);
        ship.setTy(tY);
        ship.setLx(lX);
        ship.setLy(lY);
        ship.setRx(rX);
        ship.setRy(rY);

        ship.setA(a);
        ship.setMaxspeed(maxspeed);
        ship.setRadians(radians);

        ship.setImgWidth(WIDTH);
        ship.setImgHeight(HEIGHT);
    }

    public void updateFields() {
        x = ship.getX();
        y = ship.getY();
        vx = ship.getVx();
        vy = ship.getVy();
        dx = ship.getDx();
        dy = ship.getDy();
        tX = ship.getTx();
        tY = ship.getTy();
        lX = ship.getLx();
        lY = ship.getLy();
        rX = ship.getRx();
        rY = ship.getRy();

        a = ship.getA();
        maxspeed = ship.getMaxspeed();
        radians = ship.getRadians();

        WIDTH = ship.getImgWidth();
        HEIGHT = ship.getImgHeight();

    }

    public void updateHitBox() {

        tX = (x + WIDTH / 2) - (Math.sin(radians) * (HEIGHT / 2));
        tY = (y + HEIGHT / 2) - (Math.cos(radians) * (HEIGHT / 2));

        lX = (x + WIDTH / 2) + (Math.sin(radians) * (HEIGHT / 2)) + (Math.sin(radians + Math.PI / 2) * (WIDTH / 2));
        lY = (y + HEIGHT / 2) + (Math.cos(radians) * (HEIGHT / 2)) + (Math.cos(radians + Math.PI / 2) * (WIDTH / 2));

        rX = (x + WIDTH / 2) + (Math.sin(radians) * (HEIGHT / 2)) - (Math.sin(radians + Math.PI / 2) * (WIDTH / 2));
        rY = (y + HEIGHT / 2) + (Math.cos(radians) * (HEIGHT / 2)) - (Math.cos(radians + Math.PI / 2) * (WIDTH / 2));

    }

}
