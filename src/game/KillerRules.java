package game;

import visibleObjects.*;

/**
 * @author Alvaro & Christian
 */
public class KillerRules {

    // ***************************************************************************************************** //
    // *************************** [          Statistics Ships           ] ********************************* //
    // ***************************************************************************************************** //
    public static final int OCTANE_HEALTH = 100;
    public static final int OCTANE_DAMAGE = 20;
    public static final int OCTANE_MAX_SPEED = 4;
    public static final int OCTANE_MAX_SPEED_NEBULOSA = 3;
    public static final int BATMOBILE_HEALTH = 75;
    public static final int BATMOBILE_DAMAGE = 30;
    public static final int BATMOBILE_MAX_SPEED = 5;
    public static final int BATMOBILE_MAX_SPEED_NEBULOSA = 3;
    public static final int MARAUDER_HEALTH = 150;
    public static final int MARAUDER_DAMAGE = 10;
    public static final int MARAUDER_MAX_SPEED = 3;
    public static final int MARAUDER_MAX_SPEED_NEBULOSA = 3;

    // ***************************************************************************************************** //
    // *************************** [            Collision Alive          ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Christian
     * @param game
     * @param alive
     * @param wall
     */
    public static void collisionAliveWithWall(KillerGame game, Alive alive, Wall wall) {

        // Detect wall type
        if (wall.getType() == Wall.Limit.NORTH) {
            // Teleport to bottom
            alive.setY((game.getHeight()) + (alive.getImgWidth()));
        }

        if (wall.getType() == Wall.Limit.SOUTH) {
            // Teleport to top
            alive.setY(0 + alive.getImgHeight());
        }

        if (wall.getType() == Wall.Limit.EAST) {
            // Delete from the array
            game.removeObject(alive);
            // Send alive to the prev module
            game.sendObjectToPrev(alive);
        }

        if (wall.getType() == Wall.Limit.WEST) {
            // Delete from the array
            game.removeObject(alive);
            // Send alive to the next module
            game.sendObjectToNext(alive);
        }

    }

    // ***************************************************************************************************** //
    // *************************** [            Collision Ship           ] ********************************* //
    // ***************************************************************************************************** //
    public static void collisionShipWithAsteroid(KillerGame game, KillerShip ship, Asteroid asteroid) {

        KillerRules.substractHealthShip(game, ship, 0); // Remplazar 0 por metodo de fisica que calcula el daño

    }

    public static void collisionShipWithBlackHole(KillerGame game, KillerShip ship) {

    }

    /**
     * @author Alvaro
     * @param ship
     */
    public static void collisionShipWithNebulosa(KillerShip ship) {

        if (ship.getType() == KillerShip.ShipType.OCTANE) {
            ship.setMaxSpeed(KillerRules.OCTANE_MAX_SPEED_NEBULOSA);
            ship.setTiempoEnNebulosa(10);
        }

        if (ship.getType() == KillerShip.ShipType.BATMOBILE) {
            ship.setMaxSpeed(KillerRules.BATMOBILE_MAX_SPEED_NEBULOSA);
            ship.setTiempoEnNebulosa(10);
        }

        if (ship.getType() == KillerShip.ShipType.MARAUDER) {
            ship.setMaxSpeed(KillerRules.MARAUDER_MAX_SPEED_NEBULOSA);
            ship.setTiempoEnNebulosa(10);
        }

    }

    public static void collisionShipWithPacman(KillerGame game, KillerShip ship, Pacman pacman) {

    }

    public static void collisionShipWithPlaneta(KillerGame game, KillerShip ship, Planeta planeta) {

    }

    public static void collisionShipWithPowerUp(KillerGame game, KillerShip ship, PowerUp powerUp) {

    }

    public static void collisionShipWithShip(KillerGame game, KillerShip ship, KillerShip ship2) {

    }

    /**
     * @author Christian
     * @param shoot
     * @param ship
     */
    public static void collisionShipWithShoot(KillerGame game, KillerShip ship, Shoot shoot) {

        // Substract heatlh to ship
        ship.quitarVida(shoot.getDamage());

        // Calls die method from KillerShip
        if (ship.getHealth() <= 0) {
            ship.die();
        }

        // Remove shot from the array
        game.removeObject(shoot);

    }

    // ***************************************************************************************************** //
    // *************************** [            Collision Shoot          ] ********************************* //
    // ***************************************************************************************************** //
    
    public static void collisionShootWithAsteroid(KillerGame game, Shoot shot, Asteroid asteroid) {

    }

    public static void collisionShootWithBlackHole(KillerGame game, Shoot shot, BlackHole blackhole) {

    }

    public static void collisionShootWithNebulosa(Shoot shot) {

    }

    public static void collisionShootWithPacman(KillerGame game, Shoot shot, Pacman pacman) {

    }

    public static void collisionShootWithPlaneta(KillerGame game, Shoot shot, Planeta planeta) {

    }

    public static void collisionShootWithPowerUp(KillerGame game, Shoot shot, PowerUp powerUp) {

    }

    /**
     * @author Christian
     * @param shoot
     * @param shoot2
     */
    public static void collisionShootWithShoot(KillerGame game, Shoot shoot, Shoot shoot2) {
        // reventar ambos shots
        game.removeObject(shoot);
        game.removeObject(shoot2);
    }

    // ***************************************************************************************************** //
    // *************************** [          Collision Asteroid         ] ********************************* //
    // ***************************************************************************************************** //
    static void collisionAsteroidWithAsteroid(KillerGame aThis, Asteroid asteroid, Asteroid asteroid0) {

    }
    
    static void collisionAsteroidWithBlackHole(KillerGame aThis, Asteroid asteroid, BlackHole blackHole) {
        
    }
    
    static void collisionAsteroidWithNebulosa(Asteroid asteroid) {
        
    }

    static void collisionAsteroidWithPacman(KillerGame aThis, Asteroid asteroid, Pacman pacman) {

    }

    static void collisionAsteroidWithPlaneta(Asteroid asteroid) {

    }

    static void collisionAsteroidWithPowerUp(KillerGame aThis, Asteroid asteroid, PowerUp powerUp) {
        
    }

    // ***************************************************************************************************** //
    // *************************** [          Collision Asteroid         ] ********************************* //
    // ***************************************************************************************************** //
    
    static void collisionPacmanWithBlackHole(Pacman pacman) {
        
    }

    static void collisionPacmanWithNebulosa(Pacman pacman) {
        
    }
    
    static void collisionPacmanWithPacman(KillerGame aThis, Pacman pacman, Pacman pacman0) {
        
    }

    static void collisionPacmanWithPlaneta(Pacman pacman) {
        
    }

    static void collisionPacmanWithPowerUp(KillerGame aThis, Pacman pacman, PowerUp powerUp) {
        
    }


    // ***************************************************************************************************** //
    // *************************** [           Auxiliar Methods          ] ********************************* //
    // ***************************************************************************************************** //
    
    /**
     * @author Alvaro
     * @param game
     * @param ship
     * @param damage
     * @return
     */
    private static boolean substractHealthShip(KillerGame game, KillerShip ship, int damage) {

        // Dead
        boolean dead = false;

        // Substract heatlh to ship
        ship.quitarVida(damage);

        // Set die status to KillerShip
        if (ship.getHealth() <= 0) {
            ship.changeState(KillerShip.ShipState.DEAD);
            dead = true;
        }

        // Return live status
        return dead;

    }
    
}
