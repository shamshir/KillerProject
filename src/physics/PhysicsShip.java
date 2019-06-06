/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import visibleObjects.*;

/**
 *
 * @author berna
 */
public class PhysicsShip {

    private KillerShip ship;

    private double x, y, a, vx, vy, dx, dy,
            maxspeed, radians, tX, tY, lX, lY, rX, rY, m;
    private int WIDTH, HEIGHT;
    private double finalRadians = 0;

    public PhysicsShip(KillerShip ship) {

        this.ship = ship;

        updateFields();

    }

    public void move() {

        updateFields();
        angleUpdate();

        double maxspeedX = maxspeed * dx;
        double maxspeedY = maxspeed * dy;

        if (vx <= maxspeedX) {
            if (vx + a <= maxspeedX) {
                vx += a;

            } else {
                vx += 0.001;
            }
        } else if (vx > maxspeedX) {
            if (vx - a >= maxspeedX) {
                vx -= a;
            } else {
                vx -= 0.001;
            }
        } else {
            //   vx = 2.01 * -Math.sin(radians);

        }

        if (vy <= maxspeedY) {
            if (vy + a <= maxspeedY) {
                vy += a;
            } else {
                vy += 0.001;
            }
        } else if (vy > maxspeedY) {
            if (vy - a > maxspeedY) {
                vy -= a;
            } else {
                vy -= 0.001;
            }
        } else {
            //  vy = 2.01 * -Math.cos(radians);

        }

        x += vx;
        y += vy;

        updateHitBox();
        setValues();

    }

    public double[] collisionXShip(KillerShip alive) {

        updateFields();

        double thisradius = Math.max(this.HEIGHT / 2, this.WIDTH / 2);
        double aliveradius = Math.max(alive.getImgHeight(), alive.getImgWidth());

        double alivex = alive.getX();
        double alivey = alive.getY();
        double alivevx = alive.getVx();
        double alivevy = alive.getVy();
        double alivem = alive.getM();

        //distancia entre los dos objetos basado en sus centros
        double distance = 0.00001 + (Math.sqrt(Math.pow(this.x - alivex, 2) + Math.pow(this.y - alivey, 2)));
        //distancia que habrán de separarse cada uno del otro en base al radio
        double overlap = (distance - thisradius - aliveradius) / 2;
        //desplazamiento del primer objeto en x e y utilizando el vector unitario direccional 
        //obtenido entre el centro de los objetos, multiplicado por la distancia a separarse
//        this.x -= overlap * (this.x - alive.x) / distance;
//        this.y -= overlap * (this.y - alive.y) / distance;

        alivex += overlap * (this.x - alivex) / distance;
        alivey += overlap * (this.y - alivey) / distance;

        alive.setX(alivex);
        alive.setY(alivey);

        //vector normal al vector tangente a los circulos
        double normalX = (alivex - this.x) / distance;
        double normalY = (alivey - this.y) / distance;

        //vector tangente (normal al vector entre radios)
        double tangentX = -normalY;
        double tangentY = normalX;

        //producto punto tangente (hacia donde va a cambiar la dirección con respuesta tangente)
        double pTan1 = this.vx * tangentX + this.vy * tangentY;
        double pTan2 = alivevx * tangentX + alivevy * tangentY;

        //producto punto normal (hacia donde va a cambiar la dirección con respuesta normal)
        double pNorm1 = this.vx * normalX + this.vy * normalY;
        double pNorm2 = alivevx * normalX + alivevy * normalY;

        //conservación del momento
        double mom1 = (pNorm1 * (this.m - alivem) + 2 * alivem * pNorm2) / (this.m + alivem);
        double mom2 = (pNorm2 * (alivem - this.m) + 2 * this.m * pNorm1) / (this.m + alivem);

        double vx1 = tangentX * pTan1 + normalX * mom1 * 0.2;
        double vy1 = tangentY * pTan1 + normalY * mom1 * 0.2;
        double vx2 = tangentX * pTan2 + normalX * mom2 * 0.2;
        double vy2 = tangentY * pTan2 + normalY * mom2 * 0.2;

        double result1 = Math.sqrt(vx1 * vx1 + vy1 * vy1 + alivem) / 4;
        double result2 = Math.sqrt(vx2 * vx2 + vy2 * vy2 + m) / 4;
        
        if (result1 > 20) {
            result1 = 20;
        }

        if (result2 > 20) {
            result2 = 20;
        }

        double max = 1.5;

        if (vx1 > max) {
            this.vx = max;
        } else if (vx1 < -max) {
            this.vx = -max;
        } else {
            this.vx = vx1;
        }

        if (vy1 > max) {
            this.vy = max;
        } else if (vy1 < -max) {
            this.vy = -max;
        } else {
            this.vy = vy1;
        }

        if (vx2 > max) {
            alivevx = max;
        } else if (vx2 < -max) {
            alivevx = -max;
        } else {

            alivevx = vx2;
        }

        if (vy2 > max) {
            alivevy = max;
        } else if (vy2 < -max) {
            alivevy = -max;
        } else {

            alivevy = vy2;
        }

        alive.setVx(alivevx);
        alive.setVy(alivevy);
        setValues();
//        this.vx = vx1;
//        this.vy = vy1;
//        alive.vx = vx2;
//        alive.vy = vy2;
        return new double[]{result1, result2};

    }

    public double[] collisionXAsteroid(Asteroid alive) {

        updateFields();

        double thisradius = Math.max(this.HEIGHT, this.WIDTH) * 0.7;
        double aliveradius = alive.getRadius();

        double centerXs = x + WIDTH / 2;
        double centerYs = y + HEIGHT / 2;

        double alivex = alive.getX();
        double alivey = alive.getY();
        double alivevx = alive.getVx();
        double alivevy = alive.getVy();
        double alivem = alive.getM();

        //distancia entre los dos objetos basado en sus centros
        double distance = Math.sqrt(Math.pow((this.x + WIDTH / 2) - alivex, 2)
                + Math.pow((this.y + WIDTH / 2) - alivey, 2));
        //distancia que habrán de separarse cada uno del otro en base al radio
        double overlap = (distance - thisradius - aliveradius) / 2;
        //desplazamiento del primer objeto en x e y utilizando el vector unitario direccional 
        //obtenido entre el centro de los objetos, multiplicado por la distancia a separarse

        centerXs -= (overlap * ((this.x + WIDTH / 2) - alivex) / distance);
        centerYs -= (overlap * ((this.y + WIDTH / 2) - alivey) / distance);

        centerXs -= ((overlap * ((this.x + WIDTH / 2) - alivex) / distance));
        centerYs -= ((overlap * ((this.y + HEIGHT / 2) - alivey) / distance));

        this.x = centerXs - WIDTH / 2;
        this.y = centerYs - HEIGHT / 2;

        alivex += overlap * (this.x - alivex) / distance;
        alivey += overlap * (this.y - alivey) / distance;

        // alive.setX(alivex);
        // alive.setY(alivey);
        //vector normal al vector tangente a los circulos
        double normalX = (alivex - this.x) / distance;
        double normalY = (alivey - this.y) / distance;

        //vector tangente (normal al vector entre radios)
        double tangentX = -normalY;
        double tangentY = normalX;

        //producto punto tangente (hacia donde va a cambiar la dirección con respuesta tangente)
        double pTan1 = this.vx * tangentX + this.vy * tangentY;
        double pTan2 = alivevx * tangentX + alivevy * tangentY;

        //producto punto normal (hacia donde va a cambiar la dirección con respuesta normal)
        double pNorm1 = this.vx * normalX + this.vy * normalY;
        double pNorm2 = alivevx * normalX + alivevy * normalY;

        //conservación del momento
        double mom1 = (pNorm1 * (this.m - alivem) + 2 * alivem * pNorm2) / (this.m + alivem);
        double mom2 = (pNorm2 * (alivem - this.m) + 2 * this.m * pNorm1) / (this.m + alivem);

        double var1 = 0;
        double var2 = 0;
        double maxship = 1.5;

        if (aliveradius < HEIGHT / 2) {
            var1 = 0.6;
            var2 = 1;
        } else {
            maxship = 4;
            var1 = 1;
            var2 = 0.2;

        }

        double vx1 = tangentX * pTan1 + normalX * mom1 * var1;
        double vy1 = tangentY * pTan1 + normalY * mom1 * var1;
        double vx2 = tangentX * pTan2 + normalX * mom2 * var2;
        double vy2 = tangentY * pTan2 + normalY * mom2 * var2;

        double result1 = Math.sqrt(vx1 * vx1 + vy1 * vy1 + alivem) / 4;
        double result2 = Math.sqrt(vx2 * vx2 + vy2 * vy2 + m) / 4;
        
        if (result1 > 30) {
            result1 = 30;
        }

        double maxast = 0.6;

        if (vx1 > maxship) {
            this.vx = maxship;
        } else if (vx1 < -maxship) {
            this.vx = -maxship;
        } else {
            this.vx = vx1;
        }

        if (vy1 > maxship) {
            this.vy = maxship;
        } else if (vy1 < -maxship) {
            this.vy = -maxship;
        } else {
            this.vy = vy1;
        }

        if (vx2 > maxast) {
            alivevx = maxast;
        } else if (vx2 < -maxast) {
            alivevx = -maxast;
        } else {

            alivevx = vx2;
        }

        if (vy2 > maxast) {
            alivevy = maxast;
        } else if (vy2 < -maxast) {
            alivevy = -maxast;
        } else {

            alivevy = vy2;
        }

        alive.setVx(alivevx);
        alive.setVy(alivevy);
        setValues();
//        this.vx = vx1;
//        this.vy = vy1;
//        alive.vx = vx2;
//        alive.vy = vy2;

        return new double[]{result1, result2};
    }

    public double[] collisionXPlanet(Planeta alive) {

        updateFields();

        double thisradius = Math.max(this.HEIGHT, this.WIDTH) * 0.7;
        double aliveradius = alive.getRadius();

        double centerXs = x + WIDTH / 2;
        double centerYs = y + HEIGHT / 2;

        double alivex = alive.getX();
        double alivey = alive.getY();
        double alivem = alive.getM();

        //distancia entre los dos objetos basado en sus centros
        double distance = Math.sqrt(Math.pow((this.x + WIDTH / 2) - alivex, 2)
                + Math.pow((this.y + WIDTH / 2) - alivey, 2));
        //distancia que habrán de separarse cada uno del otro en base al radio
        double overlap = (distance - thisradius - aliveradius) / 2;
        //desplazamiento del primer objeto en x e y utilizando el vector unitario direccional 
        //obtenido entre el centro de los objetos, multiplicado por la distancia a separarse

        centerXs -= (overlap * ((this.x + WIDTH / 2) - alivex) / distance);
        centerYs -= (overlap * ((this.y + WIDTH / 2) - alivey) / distance);

        centerXs -= ((overlap * ((this.x + WIDTH / 2) - alivex) / distance));
        centerYs -= ((overlap * ((this.y + HEIGHT / 2) - alivey) / distance));

        this.x = centerXs - WIDTH / 2;
        this.y = centerYs - HEIGHT / 2;

        // alive.setX(alivex);
        // alive.setY(alivey);
        //vector normal al vector tangente a los circulos
        double normalX = (alivex - this.x) / distance;
        double normalY = (alivey - this.y) / distance;

        //vector tangente (normal al vector entre radios)
        double tangentX = -normalY;
        double tangentY = normalX;

        //producto punto tangente (hacia donde va a cambiar la dirección con respuesta tangente)
        double pTan1 = this.vx * tangentX + this.vy * tangentY;

        //producto punto normal (hacia donde va a cambiar la dirección con respuesta normal)
        double pNorm1 = this.vx * normalX + this.vy * normalY;
        double pNorm2 = 0 * normalX + 0 * normalY;

        //conservación del momento
        double mom1 = (pNorm1 * (this.m - alivem) + 2 * alivem * pNorm2) / (this.m + alivem);
        double mom2 = (pNorm2 * (alivem - this.m) + 2 * this.m * pNorm1) / (this.m + alivem);

        double var1 = 1;
        double var2 = 0;
        double maxship = 1.5;

        //    maxship = 4;
        var1 = 1;

        double vx1 = tangentX * pTan1 + normalX * mom1 * var1;
        double vy1 = tangentY * pTan1 + normalY * mom1 * var1;

        double result1 = Math.sqrt(vx1 * vx1 + vy1 * vy1);
        
        if (result1 > 40) {
            result1 = 40;
        }

        if (vx1 > maxship) {
            this.vx = maxship;
        } else if (vx1 < -maxship) {
            this.vx = -maxship;
        } else {
            this.vx = vx1;
        }

        if (vy1 > maxship) {
            this.vy = maxship;
        } else if (vy1 < -maxship) {
            this.vy = -maxship;
        } else {
            this.vy = vy1;
        }

        setValues();
//        this.vx = vx1;
//        this.vy = vy1;
//        alive.vx = vx2;
//        alive.vy = vy2;

        return new double[]{result1, 0};
    }

    public void angleUpdate() {

        // radian final
        if (dx <= 0 && dy * -1 > 0) {
            finalRadians = Math.atan(dx / dy);
        } else if (dx <= 0 && dy * -1 <= 0) {
            finalRadians = Math.atan(Math.abs(dy / dx)) + (Math.PI / 2);
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
                    } else if (radians < Math.PI / 2 && finalRadians >= Math.PI) {
                        radians -= 0.2;
                        if (radians < 0) {
                            radians += 2 * Math.PI;
                        }
                    } else {
                        radians += 0.15;
                    }

                } else if (radians > finalRadians) {
                    if (radians - 0.2 < finalRadians) {
                        radians -= 0.015;
                    } else if (radians >= Math.PI * 1.5 && finalRadians < Math.PI) {
                        radians += 0.2;
                        if (radians > 2 * Math.PI) {
                            radians -= 2 * Math.PI;
                        }
                    } else {
                        radians -= 0.15;
                    }
                }
            }
        }

    }

    public void dash() {

        if (Math.abs(vx) <= maxspeed && Math.abs(vy) <= maxspeed) {

            vy = (Math.cos(radians) * -1) * Math.abs((maxspeed + 4));
            vx = (Math.sin(radians) * -1) * Math.abs((maxspeed + 4));

            setValues();
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
        m = ship.getM();

        WIDTH = ship.getImgWidth();
        HEIGHT = ship.getImgHeight();

    }

    public void updateHitBox() {

        tX = (x + WIDTH / 2) - (Math.sin(radians) * (HEIGHT / 2));
        tY = (y + HEIGHT / 2) - (Math.cos(radians) * (HEIGHT / 2));

        rX = (x + WIDTH / 2) + (Math.sin(radians) * (HEIGHT / 2)) + (Math.sin(radians + Math.PI / 2) * (WIDTH / 2));
        rY = (y + HEIGHT / 2) + (Math.cos(radians) * (HEIGHT / 2)) + (Math.cos(radians + Math.PI / 2) * (WIDTH / 2));

        lX = (x + WIDTH / 2) + (Math.sin(radians) * (HEIGHT / 2)) - (Math.sin(radians + Math.PI / 2) * (WIDTH / 2));
        lY = (y + HEIGHT / 2) + (Math.cos(radians) * (HEIGHT / 2)) - (Math.cos(radians + Math.PI / 2) * (WIDTH / 2));

    }

}
