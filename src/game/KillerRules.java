/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import visibleObjects.Asteroid;
import visibleObjects.VisibleObject;
import visibleObjects.Alive;
import visibleObjects.Shoot;
import visibleObjects.KillerShip;

/**
 *
 * @author berna
 */
public class KillerRules {

    public static void collision(VisibleObject obj1, VisibleObject obj2) {
        if (obj1 instanceof Alive && obj2 instanceof Alive) {
            collisionAlive((Alive) obj1, (Alive) obj2);
        }
    }

    public static void collisionAlive(Alive obj1, Alive obj2) {

//        obj1.collision();
//        obj2.collision();
        if (obj1 instanceof KillerShip && obj2 instanceof KillerShip) {

            if (!((KillerShip) obj1).isDeath() && !((KillerShip) obj2).isDeath()) {
//                ((KillerShip) obj2).kill();
//                ((KillerShip) obj1).kill();
//                System.out.println("muertos");
//            } else {
//                if (!((KillerShip) obj1).isDeath() && ((KillerShip) obj1).isDeath()) {
//                    System.out.println("muerto1");
//                    ((KillerShip) obj2).kill();
//                }
//                if (!((KillerShip) obj2).isDeath() && !((KillerShip) obj1).isDeath()) {
//                    System.out.println("muerto2");
//                    ((KillerShip) obj1).kill();
//                }
            }

        } else  {
            if (obj1 instanceof KillerShip && obj2 instanceof Asteroid
                    && !((KillerShip) obj1).isDeath()) {
                ((KillerShip) obj1).kill();
                obj1.die();
            }

            if (obj2 instanceof KillerShip && obj1 instanceof Asteroid
                    && !((KillerShip) obj2).isDeath()) {
                ((KillerShip) obj2).kill();
                obj1.die();
            }

        }

    }

    public static void collisionShoot(Shoot obj1, Alive obj2) {
//        System.out.println("RULES: colision shoot");

        if (obj2 instanceof KillerShip && !((KillerShip) obj2).isDeath()) {
//            System.out.println(obj1.getShip().getIp()
//            +"/"+((KillerShip) obj2).getIp());
            
            if (!obj1.getShip().getIp().equals(((KillerShip) obj2).getIp())) {
                ((KillerShip) obj2).kill();
                obj1.points(5);
            }
        } else if (obj2 instanceof Asteroid) {
            obj2.die();
            obj1.points(2);
        }

        // -----------------------> original
        //obj1.die();
        
        // ----------------> added
        // para que la bala no mate a su nave
        // pintar bien la bala
        if (obj2 instanceof KillerShip && !((KillerShip) obj2).isDeath()) {            
            if (!obj1.getShip().getIp().equals(((KillerShip) obj2).getIp())) {
                obj1.die();
            }
        }
    }

}
