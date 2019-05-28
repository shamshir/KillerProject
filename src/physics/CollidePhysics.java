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

    public static boolean collisionTxC(KillerShip obj1, VisibleObject obj2) {

        double radius = obj2.getRadius();

        // si el círculo toca uno de los vertices del triangulo.
//        double v1x = obj2.getX() - obj1.getTx();
//        double v1y = obj2.getY() - obj1.getTy();
//
//        double v2x = obj2.getX() - obj1.getLx();
//        double v2y = obj2.getY() - obj1.getLy();
//
//        double v3x = obj2.getX() - obj1.getRx();
//        double v3y = obj2.getY() - obj1.getRy();
//
//        if (Math.sqrt(v1x * v1x + v1y * v1y) <= Math.sqrt(radius * radius)
//                || Math.sqrt(v2x * v2x + v2y * v2y) <= Math.sqrt(radius * radius)
//                || Math.sqrt(v3x * v3x + v3y * v3y) <= Math.sqrt(radius * radius)) {
//            //  return false;
//        }


        //si colisiona con la recta AB (T-L)
        double c1x = obj2.getX() - obj1.getTx();
        double c1y = obj2.getY() - obj1.getTy();

        double e1x = obj1.getLx() - obj1.getTx();
        double e1y = obj1.getLy() - obj1.getTy();

        double c1sqr = c1x * c1x + c1y * c1y - radius * radius;

        double producto = c1x * e1x + c1y * e1y;

        if (producto > 0) {
            double len = e1x * e1x + e1y * e1y;
            if (producto < len) {
                if (c1sqr * len <= producto * producto) {
                    return true;

                }
            }

        }
        //si colisiona con la recta BC (L-R)
        double c2x = obj2.getX() - obj1.getLx();
        double c2y = obj2.getY() - obj1.getLy();

        double e2x = obj1.getRx() - obj1.getLx();
        double e2y = obj1.getRy() - obj1.getLy();

        double c2sqr = c2x * c2x + c2y * c2y - radius * radius;

        producto = c2x * e2x + c2y * e2y;

        if (producto > 0) {
            double len = e2x * e2x + e2y * e2y;
            if (producto < len) {
                if (c2sqr * len <= producto * producto) {
                    return true;

                }
            }

        }

        //si colisiona con la recta CA (R-T)
        double c3x = obj2.getX() - obj1.getRx();
        double c3y = obj2.getY() - obj1.getRy();

        double e3x = obj1.getTx() - obj1.getRx();
        double e3y = obj1.getTy() - obj1.getRy();

        double c3sqr = c3x * c3x + c3y * c3y - radius * radius;

        producto = c3x * e3x + c3y * e3y;

        if (producto > 0) {
            double len = e3x * e3x + e3y * e3y;
            if (producto < len) {
                if (c3sqr * len <= producto * producto) {                    
                    return true;

                }
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
