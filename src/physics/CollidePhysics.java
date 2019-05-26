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
     *
     * @param obj1 Primer objeto circular
     * @param obj2 Segundo objeto circular
     * @return true si han colisionado, false en caso contrario.
     */
    public static boolean collisionCxC(VisibleObject obj1, VisibleObject obj2) {

        double xDif = obj1.getX() - obj2.getX();
        double yDif = obj1.getY() - obj2.getY();

        double module = Math.abs(Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2)));

        if (module <= obj1.getRadius() + obj2.getRadius()) {
            return true;
        }
        return false;
    }

    //----- Colisión entre triangulos -----
    //Por ahora es solo entre naves, mas adelante lo hare para todos los visible objects en caso de 
    // que hayan mas objetos triangulares
    public static boolean collisionTxT(KillerShip obj1, KillerShip obj2) {

        double[][] points = new double[][]{{obj1.getTx(), obj1.getTy()},
        {obj1.getLx(), obj1.getLy()}, {obj1.getRx(), obj1.getRy()}};

        double obj2Tx = obj2.getTx();
        double obj2Ty = obj2.getTy();
        double obj2Lx = obj2.getLx();
        double obj2Ly = obj2.getLy();
        double obj2Rx = obj2.getRx();
        double obj2Ry = obj2.getRy();

        double contTriangleArea = (obj2.getImgWidth() * obj2.getImgHeight() / 2) * (obj2.getImgWidth() * obj2.getImgHeight() / 2);

        //Fórmula de Heron.
        double areaOrig = Math.abs((obj2Lx - obj2Tx) * (obj2Ry - obj2Ty) - (obj2Rx - obj2Tx) * (obj2Ly - obj2Ty));

        for (int i = 0; i < points.length; i++) {

            double currX = points[i][0];
            double currY = points[i][1];

            double area1 = Math.abs((obj2Tx - currX) * (obj2Ly - currY) - (obj2Lx - currX) * (obj2Ty - currY));
            double area2 = Math.abs((obj2Lx - currX) * (obj2Ry - currY) - (obj2Rx - currX) * (obj2Ly - currY));
            double area3 = Math.abs((obj2Rx - currX) * (obj2Ty - currY) - (obj2Tx - currX) * (obj2Ry - currY));

            if (area1 + area2 + area3 == areaOrig) {
                return true;
            }

        }
        return false;

    }

    public static boolean collisionObjxWall(VisibleObject obj, Wall wall) {

        if (wall.getType() == Wall.Limit.NORTH) {

            return (obj.getY() + obj.getImgHeight() / 2 < wall.getY());

        } else if (wall.getType() == Wall.Limit.SOUTH) {

            return (obj.getY() + obj.getImgHeight() / 2 > wall.getY());

        } else if (wall.getType() == Wall.Limit.EAST) {

            return (obj.getX() + obj.getImgWidth() / 2 > wall.getX());

        } else if (wall.getType() == Wall.Limit.WEST) {

            return (obj.getX() + obj.getImgHeight() / 2 < wall.getX());
        }
        return false;
    }

}
