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
    public static final int PACMAN_INITIAL_HEALTH = 10;
    public static final int PACMAN_INCREMENT = 10;
    public static final int PACMAN_DECREMENT = 1;
    public static final int DAMAGE_BY_COLLISION = 100;
    public static final int MAX_SPEED_INCREMENT = 3;
    public static final int POWER_UP_HEALTH_INCREMENT = 30;
    public static final int POWER_UP_DAMAGE_INCREMENT = 15;

    // ***************************************************************************************************** //
    // *************************** [            Collision Alive          ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Alvaro
     * @param game
     * @param alive
     * @param wall
     */
    static void collisionAliveWithBlackHole(KillerGame game, Alive alive) {
        if (Math.random() < 0.5) {
            KillerRules.sendAliveToPrev(game, alive);
        } else {
            KillerRules.sendAliveToNext(game, alive);
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
            alive.setY((game.getHeight()) - (alive.getImgHeight() + 1));
        }
        if (wall.getType() == Wall.Limit.DOWN) {
            // Teleport to top
            alive.setY(0 + alive.getImgHeight() + 1);
        }
        if (wall.getType() == Wall.Limit.RIGHT) {
            KillerRules.sendAliveToNext(game, alive);
        }
        if (wall.getType() == Wall.Limit.LEFT) {
            KillerRules.sendAliveToPrev(game, alive);
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
        KillerRules.substractHealthShip(game, ship, (int) damages[0] * DAMAGE_BY_COLLISION);
        KillerRules.substractHealthAlive(asteroid, (int) damages[1] * DAMAGE_BY_COLLISION);
    }

    /**
     * Por ahora no es utilizado. Ya que se usa collisionWithAlive(). En caso de
     * querer que esta colision haga algo diferente se debe tocar aqui.
     *
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

    public static void collisionShipWithPacman(KillerGame game, KillerShip ship, Pacman pacman) {
        ship.changeState(Alive.State.DEAD);
        ship.getGame().removeObject(ship);
        pacman.setSize(ship.getHealth());
    }

    public static void collisionShipWithPlaneta(KillerGame game, KillerShip ship, Planeta planeta) {
        double[] damages = ship.getPhysicsShip().collisionXPlanet(planeta);
        KillerRules.substractHealthShip(game, ship, (int) damages[0] * DAMAGE_BY_COLLISION);
    }

    public static void collisionShipWithPowerUp(KillerGame game, KillerShip ship, PowerUp powerUp) {
        if (!powerUp.isWrappered()) {
            game.removeObject(powerUp);
            powerUp.setAvailable(false);
            if (powerUp.getType() == PowerUp.Power.DAMAGE) {
                ship.powerUpDamage(KillerRules.POWER_UP_DAMAGE_INCREMENT);
            }
            if (powerUp.getType() == PowerUp.Power.HEALTH) {
                ship.powerUpHealth(KillerRules.POWER_UP_HEALTH_INCREMENT);
            }
        } else {
            // Se calcula el daño que recibe la nave con Physiscs
            // Se llama al metodo restar vida de nave de Killerrules
        }
    }

    public static void collisionShipWithShip(KillerGame game, KillerShip ship, KillerShip ship2) {
        double[] damages = ship.getPhysicsShip().collisionXShip(ship2);
        KillerRules.substractHealthShip(game, ship, ((int) damages[0] * KillerRules.DAMAGE_BY_COLLISION));
        KillerRules.substractHealthShip(game, ship2, ((int) damages[1] * KillerRules.DAMAGE_BY_COLLISION));
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
            // Remove shot from the array
            shoot.setState(Alive.State.DEAD);
            shoot.getGame().removeObject(shoot);
        }
    }

    // ***************************************************************************************************** //
    // *************************** [            Collision Shoot          ] ********************************* //
    // ***************************************************************************************************** //
    public static void collisionShootWithAsteroid(KillerGame game, Shoot shot, Asteroid asteroid) {
        // Quitar vida al asteroide / Posible metodo substract health to alive de killer rules
        KillerRules.substractHealthAlive(asteroid, shot.getDamage());
        // Remove shot from the array
        shot.setState(Alive.State.DEAD);
        shot.getGame().removeObject(shot);
    }

    /**
     * @author Christian
     * @param shot
     */
    public static void collisionShootWithBlackHole(Shoot shot) {
        // Remove shot from the array
        shot.setState(Alive.State.DEAD);
        shot.getGame().removeObject(shot);
    }

    public static void collisionShootWithNebulosa(Shoot shot) {
        // Por ahora no pasa nada, aunque se podria implementar que los disparos fuesen mas lento igual que las naves
    }

    public static void collisionShootWithPacman(KillerGame game, Shoot shot, Pacman pacman) {
        KillerRules.substractHealthAlive(pacman, shot.getDamage());
        pacman.setDx(shot.getDx());
        pacman.setDy(shot.getDy());
        // Remove shot from the array
        shot.setState(Alive.State.DEAD);
        shot.getGame().removeObject(shot);
    }

    public static void collisionShootWithPlaneta(KillerGame game, Shoot shot, Planeta planeta) {
        // Remove shot from the array
        shot.setState(Alive.State.DEAD);
        shot.getGame().removeObject(shot);
    }

    public static void collisionShootWithPowerUp(KillerGame game, Shoot shot, PowerUp powerUp) {
        if (powerUp.isWrappered()) {
            powerUp.quitarVida(shot.getDamage());
            if (powerUp.getHealth() < 0) {
                powerUp.unwrapper();
                powerUp.setWrappered(false);
                powerUp.setAvailable(true);
            }
            // Remove shot from the array
            shot.setState(Alive.State.DEAD);
            shot.getGame().removeObject(shot);
        }
    }

    /**
     * @author Christian
     * @param shoot
     * @param shooot Easter egg :)
     */
    public static void collisionShootWithShoot(KillerGame game, Shoot shoot, Shoot shooot) {
        // Remove shots from the array
        shoot.setState(Alive.State.DEAD);
        shoot.getGame().removeObject(shoot);
        shooot.setState(Alive.State.DEAD);
        shooot.getGame().removeObject(shooot);
    }

    /**
     * @author Christian
     * @param shoot
     */
    public static void collisionShootWithWall(Shoot shot) {
        // Remove shot from the array
        shot.setState(Alive.State.DEAD);
        shot.getGame().removeObject(shot);
    }

    // ***************************************************************************************************** //
    // *************************** [          Collision Asteroid         ] ********************************* //
    // ***************************************************************************************************** //
    static void collisionAsteroidWithAsteroid(KillerGame aThis, Asteroid asteroid, Asteroid geodude) {
        double[] damages = asteroid.getPhysicsAsteroid().collisionXAsteroid(geodude);
        KillerRules.substractHealthAlive(asteroid, (int) damages[0] * DAMAGE_BY_COLLISION);
        KillerRules.substractHealthAlive(geodude, (int) damages[1] * DAMAGE_BY_COLLISION);
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

    static void collisionAsteroidWithNebulosa(Asteroid asteroid) {
        // Por ahora nada
    }

    static void collisionAsteroidWithPacman(KillerGame aThis, Asteroid asteroid, Pacman pacman) {
        if (asteroid.getImgHeight() < pacman.getImgHeight()) {
            asteroid.changeState(Alive.State.DEAD);
            asteroid.getGame().removeObject(asteroid);
            pacman.setSize(asteroid.getHealth());
        } else {
            pacman.setDx(asteroid.getDx());
            pacman.setDy(asteroid.getDy());
        }
    }

    static void collisionAsteroidWithPlaneta(Asteroid asteroid, Planeta planeta) {
        double[] damages = asteroid.getPhysicsAsteroid().collisionXPlanet(planeta);
        KillerRules.substractHealthAlive(asteroid, (int) damages[0] * DAMAGE_BY_COLLISION);
    }

    static void collisionAsteroidWithPowerUp(KillerGame aThis, Asteroid asteroid, PowerUp powerUp) {
        // Por ahora nada
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

    static void collisionPacmanWithNebulosa(Pacman pacman) {
        // Por ahora nada
    }

    static void collisionPacmanWithPacman(KillerGame aThis, Pacman pacman, Pacman pacwoman) {
        if (pacman.getHealth() > pacwoman.getHealth()) {
            pacman.setSize(pacwoman.getHealth());
            pacwoman.changeState(Alive.State.DEAD);
            pacwoman.getGame().removeObject(pacwoman);
        } else if (pacman.getHealth() < pacwoman.getHealth()) {
            pacwoman.setSize(pacwoman.getHealth());
            pacman.changeState(Alive.State.DEAD);
            pacman.getGame().removeObject(pacman);
        } else {
            pacman.changeState(Alive.State.DEAD);
            pacman.getGame().removeObject(pacman);
            pacwoman.changeState(Alive.State.DEAD);
            pacwoman.getGame().removeObject(pacwoman);
        }
    }

    static void collisionPacmanWithPlaneta(Pacman pacman) {
        // Aplicar fisicas de rebote
    }

    static void collisionPacmanWithPowerUp(KillerGame game, Pacman pacman, PowerUp powerUp) {
        // Remove Power up from the array
        game.removeObject(powerUp);
        // Increment Pacman Health on PACMAN_INCREMENT
        pacman.setHealth(KillerRules.PACMAN_INCREMENT);
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
        game.removeObject(alive);
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
        game.removeObject(alive);
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
            // alive.changeState(Alive.State.DYING);
            // alive.onDying();
            alive.changeState(Alive.State.DEAD);
            alive.getGame().removeObject(alive);
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
        // Return live status
        return dead;
    }

}
