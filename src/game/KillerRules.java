package game;

import visibleObjects.*;

/**
 *
 * @author Alvaro
 */
public class KillerRules {

    public static void collision(Alive alive, VisibleObject object) {
        
        if (alive instanceof KillerShip) {
            KillerRules.collisionShip((KillerShip) (alive), object);
        }
        
        if (alive instanceof Shoot) {
            KillerRules.collisionShoot((Shoot) (alive), object);
        }
        
    }

    private static void collisionShip(KillerShip killerShip, VisibleObject object) {
        
        // Collision with Wall
        if (object instanceof Wall) {
            
        }
        
        // Collision with Ship
        if (object instanceof KillerShip) {
            
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
            
        }
        
    }

    private static void collisionShoot(Shoot shoot, VisibleObject object) {
        
        // Collision with Wall
        if (object instanceof Wall) {
            
        }
        
        // Collision with Ship
        if (object instanceof KillerShip) {
            
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
            
        }
        
    }

}
