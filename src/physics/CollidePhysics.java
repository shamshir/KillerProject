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
public class CollidePhysics {

    /**
     * Colisión entre dos objetos circulares.
     * @param obj1 Primer objeto circular
     * @param obj2 Segundo objeto circular
     * @return true si han colisionado, false en caso contrario.
     */
    public static boolean colideCxC(Alive obj1, Alive obj2) {

        double xDif = obj1.getX() - obj2.getX();
        double yDif = obj1.getY() - obj2.getY();

        double module = Math.abs(Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2)));

        if (module <= obj1.getRadius() + obj2.getRadius()) {
            return true;
        }
        return false;
    }

    /*Ignorar*/
    //Este método está en construcción
    public static void collision(Alive obj1, Alive obj2) {

//        double vxº = (this.m * vx + alive.m * alive.vx - alive.m * this.vx - alive.m * alive.vx - this.m * vx)
//                / alive.m;
//        double vyº = (this.m * vy + alive.m * alive.vy - alive.m * this.vy - alive.m * alive.vy - this.m * vy)
//                / alive.m;
        double vx1 = (this.vx * (this.m - alive.m) + 2 * (alive.m * alive.vx)) / (this.m + alive.m);
        double vy1 = (this.vy * (this.m - alive.m) + 2 * (alive.m * alive.vy)) / (this.m + alive.m);

        double vx2 = (alive.vx * (alive.m - this.m) + 2 * (this.m * this.vx)) / (this.m + alive.m);
        double vy2 = (alive.vy * (alive.m - this.m) + 2 * (this.m * this.vy)) / (this.m + alive.m);

        double xDif = this.x - alive.x;
        double yDif = this.y - alive.y;

        double angle = Math.atan(yDif / xDif);

//
        this.vx = vx1;
        this.vy = vy1 - 1;

        alive.vx = vx2;
        alive.vy = vy2;

//        this.maxspeedX = vxº;
//        this.maxspeedY = vyº - 1;
    }
}
