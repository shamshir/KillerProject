package game;

import communications.KillerServer;
import sound.KillerSound;
import visibleObjects.*;

/**
 * @author Alvaro & Christian
 */
public class KillerRules {

    // ***************************************************************************************************** //
    // *************************** [          Statistics Values          ] ********************************* //
    // ***************************************************************************************************** //
    
    // Octane
    public static final int OCTANE_HEALTH = 90;
    public static final int OCTANE_DAMAGE = 20;
    public static final int OCTANE_MAX_SPEED = 4;
    public static final int OCTANE_MAX_SPEED_NEBULOSA = 3;
    public static final int OCTANE_WIDTH = 100;
    public static final int OCTANE_HEIGHT = 100;
    
    // Batmobile
    public static final int BATMOBILE_HEALTH = 40;
    public static final int BATMOBILE_DAMAGE = 30;
    public static final int BATMOBILE_MAX_SPEED = 5;
    public static final int BATMOBILE_MAX_SPEED_NEBULOSA = 3;
    public static final int BATMOBILE_WIDTH = 100;
    public static final int BATMOBILE_HEIGHT = 100;
    
    // Marauder
    public static final int MARAUDER_HEALTH = 150;
    public static final int MARAUDER_DAMAGE = 10;
    public static final int MARAUDER_MAX_SPEED = 3;
    public static final int MARAUDER_MAX_SPEED_NEBULOSA = 3;
    public static final int MARAUDER_WIDTH = 100;
    public static final int MARAUDER_HEIGHT = 100;
    
    // Ship Modiffiers
    public static final int MAX_SPEED_INCREMENT = 3;
    public static final int POWER_UP_HEALTH_INCREMENT = 30;
    public static final int POWER_UP_DAMAGE_INCREMENT = 15;
    public static final int SAFE_TIME = 2500;
    
    // Alive Modiffiers
    public static final double COLLISION_MODIFFIER = 0.5;
    
    // Pacman
    public static final int PACMAN_INITIAL_HEALTH = 60;
    public static final int PACMAN_INCREMENT = 10;
    public static final int PACMAN_DECREMENT = 1;
    public static final double MIN_PACMANS = 0;
    public static final double MAX_PACMANS = 1;
    
    // Asteroids
    public static final double MIN_ASTEROIDS = 3;
    public static final double MAX_ASTEROIDS = 6;
    public static final double MIN_VX_ASTEROIDS = 1;
    public static final double MAX_VX_ASTEROIDS = 3;
    public static final double MIN_VY_ASTEROIDS = 1;
    public static final double MAX_VY_ASTEROIDS = 3;
    public static final int ASTEROID_HEATLTH = 40;
    
    // Nebulas
    public static final double MIN_NEBULOSAS = 0.8;
    public static final double MAX_NEBULOSAS = 2.3;
    
    // Planets
    public static final double MIN_PLANETS = 0.7;
    public static final double MAX_PLANETS = 2.3;
    
    // BlackHoles
    public static final double MIN_BLACKHOLES= 0;
    public static final double MAX_BLACKHOLES = 1;

    // Power Ups
    public static final double MIN_POWERUPS = 0.8;
    public static final double MAX_POWERUPS = 2.1;
    
    // World generation constants
    public static final int OBJECT_GRID_WIDTH = 8;
    public static final int OBJECT_GRID_HEIGHT = 4;

    // ***************************************************************************************************** //
    // *************************** [            Collision Alive          ] ********************************* //
    // ***************************************************************************************************** //
    
    /**
     * @author Alvaro
     * @param game
     * @param alive
     */
    static void collisionAliveWithBlackHole(KillerGame game, Alive alive) {
        KillerRules.correctXandYOnCollideBlackHole(alive);
        alive.getGame().startSound(KillerSound.ClipType.TELEPORT);
        if (!KillerServer.getId().equals(game.getNextModule().getDestinationId())) {
            if (Math.random() < 0.5) {
            KillerRules.sendAliveToPrev(game, alive);
            } else {
                KillerRules.sendAliveToNext(game, alive);
            }
        }
    }

    /**
     * @author Christian
     * @param game
     * @param alive
     * @param wall
     */
    public static void collisionAliveWithWall(KillerGame game, Alive alive, Wall wall) {
        
        // Detect wall type
        if (wall.getType() == Wall.Limit.UP) {
            // Teleport to bottom
            alive.setY( KillerGame.VIEWER_HEIGHT - (alive.getImgHeight() / 2) - 2);
        }
        if (wall.getType() == Wall.Limit.DOWN) {
            // Teleport to top
            alive.setY(1);
        }
        if (wall.getType() == Wall.Limit.RIGHT) {
            // Send to next
            alive.setX(1);
            if (!KillerServer.getId().equals(game.getNextModule().getDestinationId())) {
                KillerRules.sendAliveToNext(game, alive);
            }
        }
        if (wall.getType() == Wall.Limit.LEFT) {
            // Send to prev
            alive.setX(KillerGame.VIEWER_WIDTH - alive.getImgWidth() / 2);
            if (!KillerServer.getId().equals(game.getNextModule().getDestinationId())) {
                KillerRules.sendAliveToPrev(game, alive);
            }
        }
        
    }

    // ***************************************************************************************************** //
    // *************************** [            Collision Ship           ] ********************************* //
    // ***************************************************************************************************** //
    
    /**
     * @author Alvaro
     * @param game
     * @param ship
     * @param asteroid
     */
    public static void collisionShipWithAsteroid(KillerGame game, KillerShip ship, Asteroid asteroid) {
        double[] damages = ship.getPhysicsShip().collisionXAsteroid(asteroid);
        KillerRules.substractHealthShip(game, ship, (int) (damages[0] * KillerRules.COLLISION_MODIFFIER));
        KillerRules.substractHealthAlive(asteroid, (int) (damages[1] * KillerRules.COLLISION_MODIFFIER));
    }

    /**
     * Por ahora no es utilizado. Ya que se usa collisionWithAlive(). 
     * En caso de querer que esta colision haga algo diferente se debe tocar aqui.
     * @author Alvaro
     * @param game
     * @param ship
     */
    public static void collisionShipWithBlackHole(KillerGame game, KillerShip ship) {
        KillerRules.collisionAliveWithBlackHole(game, ship);
    }

    /**
     * @author Alvaro
     * @param ship
     */
    public static void collisionShipWithNebulosa(KillerShip ship) {
        if (ship.getType() == KillerShip.ShipType.OCTANE) {
            ship.setMaxspeed(KillerRules.OCTANE_MAX_SPEED_NEBULOSA);
            ship.setTiempoEnNebulosa(10);
        }
        if (ship.getType() == KillerShip.ShipType.BATMOBILE) {
            ship.setMaxspeed(KillerRules.BATMOBILE_MAX_SPEED_NEBULOSA);
            ship.setTiempoEnNebulosa(10);
        }
        if (ship.getType() == KillerShip.ShipType.MARAUDER) {
            ship.setMaxspeed(KillerRules.MARAUDER_MAX_SPEED_NEBULOSA);
            ship.setTiempoEnNebulosa(10);
        }
    }
    
    /**
     * @author Alvaro
     * @param game
     * @param ship
     * @param pacman 
     */
    public static void collisionShipWithPacman(KillerGame game, KillerShip ship, Pacman pacman) {
        ship.setState(Alive.State.DEAD);
        pacman.setSize(ship.getHealth());
        game.startSound(KillerSound.ClipType.PACMAN_EAT);
        game.getNextModule().sendInfoMessageToPad("pad_dead", ship.getId());
    }
    
    /**
     * @author Alvaro
     * @param game
     * @param ship
     * @param planeta 
     */
    public static void collisionShipWithPlaneta(KillerGame game, KillerShip ship, Planeta planeta) {
        double[] damages = ship.getPhysicsShip().collisionXPlanet(planeta);
        KillerRules.substractHealthShip(game, ship, (int) (damages[0] * KillerRules.COLLISION_MODIFFIER));
    }

    public static void collisionShipWithPowerUp(KillerGame game, KillerShip ship, PowerUp powerUp) {
        if (!powerUp.isWrappered()) {
            game.removeObject(powerUp);
            powerUp.setAvailable(false);
            game.getNextModule().sendInfoMessageToPad("pad_powerup", ship.getId());
            if (powerUp.getType() == PowerUp.Power.DAMAGE) {
                ship.powerUpDamage(KillerRules.POWER_UP_DAMAGE_INCREMENT);
            }
            if (powerUp.getType() == PowerUp.Power.HEALTH) {
                ship.powerUpHealth(KillerRules.POWER_UP_HEALTH_INCREMENT);
                game.getNextModule().sendInfoHealthMessageToPad(ship.getId(), ship.getHealth());
            }
        }
    }

    public static void collisionShipWithShip(KillerGame game, KillerShip ship, KillerShip ship2) {
        double[] damages = ship.getPhysicsShip().collisionXShip(ship2);
        KillerRules.substractHealthShip(game, ship, ((int)(damages[0] * KillerRules.COLLISION_MODIFFIER)));
        KillerRules.substractHealthShip(game, ship2, ((int)(damages[1] * KillerRules.COLLISION_MODIFFIER)));
    }

    /**
     * @author Christian
     * @param shoot
     * @param ship
     */
    public static void collisionShipWithShoot(KillerGame game, KillerShip ship, Shoot shoot) {
        if (!ship.getId().equalsIgnoreCase(shoot.getId())) {
            if (KillerRules.substractHealthShip(game, ship, shoot.getDamage())) {
                game.getNextModule().sendInfoMessageToPad("pad_kill", shoot.getId());
            }
            shoot.setState(Alive.State.DEAD);
        }
    }

    // ***************************************************************************************************** //
    // *************************** [            Collision Shoot          ] ********************************* //
    // ***************************************************************************************************** //
    
    /**
     * @author Alvaro
     * @param game
     * @param shot
     * @param asteroid 
     */
    public static void collisionShootWithAsteroid(KillerGame game, Shoot shot, Asteroid asteroid) {
        KillerRules.substractHealthAlive(asteroid, shot.getDamage());
        shot.setState(Alive.State.DEAD);
    }

    /**
     * @author Christian
     * @param shot
     */
    public static void collisionShootWithBlackHole(Shoot shot) {
        shot.setState(Alive.State.DEAD);
    }

    public static void collisionShootWithPacman(KillerGame game, Shoot shot, Pacman pacman) {
        if (KillerRules.substractHealthAlive(pacman, shot.getDamage())) {
            pacman.getKillerRadio().stopSound();
            game.startSound(KillerSound.ClipType.PACMAN_DIE);
        }
        pacman.getPhysics().collisionXShoot(shot);
        shot.setState(Alive.State.DEAD);
    }

    public static void collisionShootWithPlaneta(KillerGame game, Shoot shot, Planeta planeta) {
        shot.setState(Alive.State.DEAD);
    }

    public static void collisionShootWithPowerUp(KillerGame game, Shoot shot, PowerUp powerUp) {
        if (powerUp.isWrappered()) {
            powerUp.quitarVida(shot.getDamage());
            if (powerUp.getHealth() < 0) {
                powerUp.setWrappered(false);
                powerUp.unwrapper();
                powerUp.setAvailable(true);
            }
            shot.setState(Alive.State.DEAD);
        }
    }

    /**
     * @author Christian
     * @param shoot
     * @param shooot Easter egg :)
     */
    public static void collisionShootWithShoot(KillerGame game, Shoot shoot, Shoot shooot) {
        if (shoot.getId() != shooot.getId()) {
            shoot.setState(Alive.State.DEAD);
            shooot.setState(Alive.State.DEAD);
        }
    }

    /**
     * @author Christian
     * @param shoot
     */
    public static void collisionShootWithWall(Shoot shot) {
        shot.setState(Alive.State.DEAD);
    }

    // ***************************************************************************************************** //
    // *************************** [          Collision Asteroid         ] ********************************* //
    // ***************************************************************************************************** //
    static void collisionAsteroidWithAsteroid(Asteroid asteroid, Asteroid geodude) {
        double[] damages = asteroid.getPhysicsAsteroid().collisionXAsteroid(geodude);
        KillerRules.substractHealthAlive(asteroid, (int) (damages[0] * KillerRules.COLLISION_MODIFFIER));
        KillerRules.substractHealthAlive(geodude, (int) (damages[1] * KillerRules.COLLISION_MODIFFIER));
    }

    /**
     * Por ahora no es utilizado. Ya que se usa collisionWithAlive(). En caso de
     * querer que esta colision haga algo diferente se debe tocar aqui.
     *
     * @author Alvaro
     * @param asteroid
     * @param ship
     */
    static void collisionAsteroidWithBlackHole(KillerGame game, Asteroid asteroid) {
        KillerRules.collisionAliveWithBlackHole(game, (Alive) asteroid);
    }
    
    /**
     * @author Alvaro
     * @param game
     * @param asteroid
     * @param pacman 
     */
    static void collisionAsteroidWithPacman(KillerGame game, Asteroid asteroid, Pacman pacman) {
        if (game.getUltraPacman() || asteroid.getImgHeight() < pacman.getImgHeight()) {
            asteroid.changeState(Alive.State.DEAD);
            pacman.setSize(asteroid.getHealth());
            game.startSound(KillerSound.ClipType.PACMAN_EAT);
        }
    }
    
    /**
     * @author Alvaro
     * @param asteroid
     * @param planeta 
     */
    static void collisionAsteroidWithPlaneta(Asteroid asteroid, Planeta planeta) {
        double[] damages = asteroid.getPhysicsAsteroid().collisionXPlanet(planeta);
        KillerRules.substractHealthAlive(asteroid, (int) (damages[0] * KillerRules.COLLISION_MODIFFIER));
    }

    // ***************************************************************************************************** //
    // *************************** [          Collision Pacman           ] ********************************* //
    // ***************************************************************************************************** //
    
    /**
     * Por ahora no es utilizado. Ya que se usa collisionWithAlive(). En caso de
     * querer que esta colision haga algo diferente se debe tocar aqui.
     *
     * @author Alvaro
     * @param asteroid
     * @param ship
     */
    static void collisionPacmanWithBlackHole(KillerGame game, Pacman pacman) {
        KillerRules.collisionAliveWithBlackHole(game, pacman);
    }
    
    /**
     * @author Alvaro
     * @param game
     * @param pacman
     * @param pacwoman 
     */
    static void collisionPacmanWithPacman(KillerGame game, Pacman pacman, Pacman pacwoman) {
        if (pacman.getHealth() > pacwoman.getHealth()) {
            pacman.setSize(pacwoman.getHealth());
            pacwoman.changeState(Alive.State.DEAD);
            game.startSound(KillerSound.ClipType.PACMAN_EAT);
            game.startSound(KillerSound.ClipType.PACMAN_DIE);
        } else if (pacman.getHealth() < pacwoman.getHealth()) {
            pacwoman.setSize(pacman.getHealth());
            pacman.changeState(Alive.State.DEAD);
            game.startSound(KillerSound.ClipType.PACMAN_EAT);
            game.startSound(KillerSound.ClipType.PACMAN_DIE);
        } else {
            pacman.changeState(Alive.State.DEAD);
            pacwoman.changeState(Alive.State.DEAD);
            game.startSound(KillerSound.ClipType.PACMAN_DIE);
        }
    }

    static void collisionPacmanWithPlaneta(KillerGame game, Pacman pacman, Planeta planeta) {
        if (game.getUltraPacman()) {
            game.removeObject(planeta);
            pacman.setSize((int) planeta.getM());
            game.startSound(KillerSound.ClipType.PACMAN_EAT);
        }
    }

    static void collisionPacmanWithPowerUp(KillerGame game, Pacman pacman, PowerUp powerUp) {
        // Remove Power up from the array
        game.removeObject(powerUp);
        // Increment Pacman Health on PACMAN_INCREMENT
        pacman.setSize(KillerRules.PACMAN_INCREMENT);
    }
    
    public static void collisionPacmanWithWall(KillerGame game, Pacman pacman, Wall wall) {
        KillerRules.collisionAliveWithWall(game, pacman, wall);
    }

    // ***************************************************************************************************** //
    // *************************** [           Auxiliar Methods          ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Alvaro
     * @param game
     * @param alive
     */
    public static void sendAliveToPrev(KillerGame game, Alive alive) {
        // Delete from the array
        alive.setState(Alive.State.DEAD);
        // Send alive to the prev module
        game.sendObjectToPrev(alive);
    }

    /**
     * @author Alvaro
     * @param game
     * @param alive
     */
    public static void sendAliveToNext(KillerGame game, Alive alive) {
        // Delete from the array
        alive.setState(Alive.State.DEAD);
        // Send alive to the next module
        game.sendObjectToNext(alive);
    }

    /**
     * @author Alvaro
     * @param ship
     * @param damage
     * @return True if Alive state becomes dead and False if it still alive.
     */
    private static boolean substractHealthAlive(Alive alive, int damage) {
        // Dead status
        boolean dead = false;
        // Substract heatlh to ship
        alive.quitarVida(damage);
        // Set die status to KillerShip
        if (alive.getHealth() <= 0) {
            alive.changeState(Alive.State.DYING);
            alive.onDying();
            alive.getGame().startSound(KillerSound.ClipType.EXPLOSION);
            //alive.changeState(Alive.State.DEAD);
            //alive.getGame().removeObject(alive);
            dead = true;
        }
        // Return live status
        return dead;
    }

    /**
     * @author Alvaro
     * @param ship
     * @param damage
     * @return True if Ship state becomes dead and False if it still alive.
     */
    private static boolean substractHealthShip(KillerGame game, KillerShip ship, int damage) {
        // Dead status
        boolean dead = false;
        if (KillerRules.substractHealthAlive(ship, damage)) {
            game.getNextModule().sendInfoMessageToPad("pad_dead", ship.getId());
            dead = true;
        }
        // Bling
        if (!dead) {
            ship.getKillerImage().blink();
        }
        // Send health info to pad
        game.getNextModule().sendInfoHealthMessageToPad(ship.getId(), ship.getHealth());
        // Return live status
        return dead;
    }
        
    /**
     * @author Alvaro
     * @param alive
     */
    private static void correctXandYOnCollideBlackHole(Alive alive) {
        double correctX = Math.random() * (KillerGame.VIEWER_WIDTH - alive.getImgWidth())  + alive.getImgWidth();
        double correctY = Math.random() * (KillerGame.VIEWER_HEIGHT - alive.getImgHeight())  + alive.getImgHeight();
        alive.setX(correctX);
        alive.setY(correctY);
    }

}
