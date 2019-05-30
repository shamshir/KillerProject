/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import visibleObjects.Asteroid;
import visibleObjects.Planeta;

/**
 *
 * @author berna
 */
public class PhysicsAsteroid {

    private Asteroid ast;

    private double x, y, a, vx, vy, dx, dy,
            maxspeed, radians, tX, tY, lX, lY, rX, rY, radius, m;
    private int WIDTH, HEIGHT;

    public PhysicsAsteroid(Asteroid ast) {

        this.ast = ast;
        updateFields();

    }

    public void move() {

        updateFields();

        a = -Math.max(vx, vy) * 0.0000001f;
        vx += a;
        vy += a;
        x += vx;
        y += vy;

        setValues();
    }

    public double[] collisionXAsteroid(Asteroid alive) {

        double aliveradius = alive.getRadius();
        double alivex = alive.getX();
        double alivey = alive.getY();
        double alivevx = alive.getVx();
        double alivevy = alive.getVy();
        double alivem = alive.getM();

        //distancia entre los dos objetos basado en sus centros
        double distance = Math.sqrt(Math.pow(this.x - alivex, 2) + Math.pow(this.y - alivey, 2));
        //distancia que habrán de separarse cada uno del otro en base al radio
        double overlap = (distance - this.radius - aliveradius) / 2;
        //desplazamiento del primer objeto en x e y utilizando el vector unitario direccional 
        //obtenido entre el centro de los objetos, multiplicado por la distancia a separarse
        this.x -= overlap * (this.x - alivex) / distance;
        this.x -= overlap * (this.y - alivey) / distance;

        this.ast.setX(x);
        this.ast.setY(y);

        alivex += overlap * (this.x - alivex) / distance;
        alivey += overlap * (this.y - alivey) / distance;

        alive.setX(alivex);
        alive.setY(alivey);

        //vector normal al vector tangente a los circulos
        double normalX = (alivex - this.x) / distance;
        double normalY = (alivey - this.y) / distance;

        //vector tangente
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

        double vx1 = tangentX * pTan1 + normalX * mom1 * 0.6;
        double vy1 = tangentY * pTan1 + normalY * mom1 * 0.6;
        double vx2 = tangentX * pTan2 + normalX * mom2 * 0.6;
        double vy2 = tangentY * pTan2 + normalY * mom2 * 0.6;

        double result1 = Math.sqrt(vx1 * vx1 + vy1 * vy1) / 100;
        double result2 = Math.sqrt(vx2 * vx2 + vy2 * vy2) / 100;

        double max = 0.6;

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
        } else if (vx1 < -max) {
            alivevx = -max;
        } else {

            alivevx = vx2;
        }

        if (vy2 > max) {
            alivevy = max;
        } else if (vy1 < -max) {
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

    public double[] collisionXPlanet(Planeta alive) {

        double aliveradius = alive.getRadius();
        double alivex = alive.getX();
        double alivey = alive.getY();
        double alivem = alive.getM();

        //distancia entre los dos objetos basado en sus centros
        double distance = Math.sqrt(Math.pow(this.x - alivex, 2) + Math.pow(this.y - alivey, 2));
        //distancia que habrán de separarse cada uno del otro en base al radio
        double overlap = (distance - this.radius - aliveradius) / 2;
        //desplazamiento del primer objeto en x e y utilizando el vector unitario direccional 
        //obtenido entre el centro de los objetos, multiplicado por la distancia a separarse
        this.x -= overlap * (this.x - alivex) / distance;
        this.x -= overlap * (this.y - alivey) / distance;

        this.ast.setX(x);
        this.ast.setY(y);

        alivex += overlap * (this.x - alivex) / distance;
        alivey += overlap * (this.y - alivey) / distance;


        //vector normal al vector tangente a los circulos
        double normalX = (alivex - this.x) / distance;
        double normalY = (alivey - this.y) / distance;

        //vector tangente
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

        double vx1 = tangentX * pTan1 + normalX * mom1 * 0.6;
        double vy1 = tangentY * pTan1 + normalY * mom1 * 0.6;

        double result1 = Math.sqrt(vx1 * vx1 + vy1 * vy1) / 100;

        double max = 0.6;

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

        setValues();

        return new double[]{result1, 0};

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
        radius = ast.getRadius();
        m = ast.getM();
        WIDTH = ast.getImgWidth();
        HEIGHT = ast.getImgHeight();

    }

}
