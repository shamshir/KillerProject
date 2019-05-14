package game;

import visibleObjects.*;

/**
 * @author Alvaro & Christian
 */
public class KillerRules {
    
    // Static Constants Normal Ship
    public static final int NORMAL_SHIP_HEALTH = 100;
    public static final int NORMAL_SHIP_DAMAGE = 10;
    
    // Static Methods
    
    /**
     * @author Alvaro
     * @param alive
     * @param object 
     */
    public static void collision(Alive alive, VisibleObject object) {
        
        if (alive instanceof KillerShip) {
            KillerRules.collisionShip((KillerShip) (alive), object);
        }
        
        if (alive instanceof Shoot) {
            KillerRules.collisionShoot((Shoot) (alive), object);
        }
        
    }
    
    /**
     * @author Alvaro
     * @param ship
     * @param object 
     */
    private static void collisionShip(KillerShip ship, VisibleObject object) {
        
        // Collision with Wall
        if (object instanceof Wall) {
            KillerRules.collisionShipWithWall(ship, (Wall) (object));
        }
        
        // Collision with Ship
        if (object instanceof KillerShip) {
            KillerRules.collisionShipWithShip(ship, (KillerShip) (ship));
        }
        
        // Collision with Asteroid
        if (object instanceof Asteroid) {
            
        }
        
        // Collision with BlackHole
        if (object instanceof BlackHole) {
            
        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa) {
            
        }
        
        // Collision with Pacman
        if (object instanceof Pacman) {
            
        }
        
        // Collision with Planeta
        if (object instanceof Planeta) {
            
        }
        
        // Collision with PowerUp
        if (object instanceof PowerUp) {
            
        }
        
        // Collision with PowerUp
        if (object instanceof Shoot) {
            KillerRules.collisionShipWithShoot(ship, (Shoot) (object));
        }
        
    }
    
    /**
     * @author Alvaro
     * @param shoot
     * @param object 
     */
    private static void collisionShoot(Shoot shoot, VisibleObject object) {
        
        // Collision with Wall
        if (object instanceof Wall) {
            KillerRules.collisionShootWithWall(shoot, (Wall) (object));
        }
        
        // Collision with Ship
        if (object instanceof KillerShip) {
            KillerRules.collisionShootWithShip(shoot, (KillerShip) (object));
        }
        
        // Collision with Asteroid
        if (object instanceof Asteroid) {
            
        }
        
        // Collision with BlackHole
        if (object instanceof BlackHole) {
            
        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa) {
            
        }
        
        // Collision with Pacman
        if (object instanceof Pacman) {
            
        }
        
        // Collision with Planeta
        if (object instanceof Planeta) {
            
        }
        
        // Collision with PowerUp
        if (object instanceof PowerUp) {
            
        }
        
        // Collision with PowerUp
        if (object instanceof Shoot) {
            KillerRules.collisionShootWithShoot(shoot, (Shoot) (object));
        }
        
    }
    
    // Collisions Ship
    private static void collisionShipWithWall(KillerShip ship, Wall wall) {}
    
    private static void collisionShipWithShip(KillerShip ship, KillerShip ship2) {}
    
    private static void collisionShipWithShoot(KillerShip ship, Shoot shoot) {}
    
    // Collision Shoot
    private static void collisionShootWithWall(Shoot shoot, Wall wall) {}
    
    private static void collisionShootWithShip(Shoot shoot, KillerShip ship) {}
    
    private static void collisionShootWithShoot(Shoot shoot, Shoot shoot2) {}

}
