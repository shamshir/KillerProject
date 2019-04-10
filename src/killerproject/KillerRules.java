/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

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
        if (obj1 instanceof Controlled && obj2 instanceof Controlled) {

            if (!((Controlled) obj1).isDeath() && !((Controlled) obj2).isDeath()) {
//                ((Controlled) obj2).kill();
//                ((Controlled) obj1).kill();
//                System.out.println("muertos");
//            } else {
//                if (!((Controlled) obj1).isDeath() && ((Controlled) obj1).isDeath()) {
//                    System.out.println("muerto1");
//                    ((Controlled) obj2).kill();
//                }
//                if (!((Controlled) obj2).isDeath() && !((Controlled) obj1).isDeath()) {
//                    System.out.println("muerto2");
//                    ((Controlled) obj1).kill();
//                }
            }

        } else  {
            if (obj1 instanceof Controlled && obj2 instanceof Automata
                    && !((Controlled) obj1).isDeath()) {
                ((Controlled) obj1).kill();
                obj1.death();
            }

            if (obj2 instanceof Controlled && obj1 instanceof Automata
                    && !((Controlled) obj2).isDeath()) {
                ((Controlled) obj2).kill();
                obj1.death();
            }

        }

    }

    public static void collisionShoot(Shoot obj1, Alive obj2) {

        if (obj2 instanceof Controlled && !((Controlled) obj2).isDeath()) {
            System.out.println(obj1.getShip().getIp()
            +"/"+((Controlled) obj2).getIp());
            if (!obj1.getShip().getIp().equals(((Controlled) obj2).getIp())) {
                ((Controlled) obj2).kill();
                obj1.points(5);
            }
        } else if (obj2 instanceof Automata) {
            obj2.death();
            obj1.points(2);
        }

        obj1.death();
    }

}
