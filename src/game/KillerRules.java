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
        if (wall.getType() == Wall.Limit.UP) {
            // Teleport to bottom
            alive.setY((game.getHeight()) - (alive.getImgHeight() + 1));
        }

        if (wall.getType() == Wall.Limit.DOWN) {
            // Teleport to top
            alive.setY(0 + alive.getImgHeight() + 1);
        }

        if (wall.getType() == Wall.Limit.RIGHT) {
            // Send alive to the prev module
            game.sendObjectToNext(alive);
            // Delete from the array
            alive.setState(Alive.State.DEAD);
        }

        if (wall.getType() == Wall.Limit.LEFT) {
            // Send alive to the next module
            game.sendObjectToPrev(alive);
            // Delete from the array
            alive.setState(Alive.State.DEAD);
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

        KillerRules.substractHealthShip(ship, 0); // Remplazar 0 por metodo de fisica que calcula el daño

    }

    /**
     * @author Alvaro
     * @param game
     * @param ship
     */
    public static void collisionShipWithBlackHole(KillerGame game, KillerShip ship) {

        // Math random de -1 a 1
        // Si da < 0 hacer un game.sendobjecttoprev con la nave
        // Si da >= 0 hacer un game.sendobjecttonext con la nave

        if (Math.random() < 0.5) {
            game.sendObjectToPrev(ship);
            ship.setState(Alive.State.DEAD);
        } else {
            game.sendObjectToNext(ship);
            ship.setState(Alive.State.DEAD);
        }

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
        pacman.setSize(KillerRules.PACMAN_INCREMENT);

    }

    public static void collisionShipWithPlaneta(KillerGame game, KillerShip ship, Planeta planeta) {

        // Se calcula el rebote de la nave con Physics
        // Se calcula el daño que recibe la nave con Physiscs
        // Se llama al metodo restar vida de nave de Killerrules
    }

    public static void collisionShipWithPowerUp(KillerGame game, KillerShip ship, PowerUp powerUp) {

        // Se comprueba si el powerup tiene envoltorio o no
        // True
        // Se calcula el daño que recibe la nave con Physiscs
        // Se llama al metodo restar vida de nave de Killerrules
        // False
        // Se elimina el powerup de la array de killargame
        // Se entrega el bufo a la nave
    }

    public static void collisionShipWithShip(KillerShip ship, KillerShip ship2) {

        // Se calcula el daño que reciben las naves con Physiscs
        // Se llama al metodo restar vida de nave de Killerrules para cada nave

        double[] damages = ship.getPhysicsShip().collisionXShip(ship2);

        KillerRules.substractHealthShip(ship, ((int) damages[0] * KillerRules.DAMAGE_BY_COLLISION ));
        KillerRules.substractHealthShip(ship2, ((int) damages[1] * KillerRules.DAMAGE_BY_COLLISION ));

    }

    /**
     * @author Christian
     * @param shoot
     * @param ship
     */
    public static void collisionShipWithShoot(KillerShip ship, Shoot shoot) {

        if (KillerRules.substractHealthShip(ship, shoot.getDamage())) {

            // Se pide el mando a la nave que disparo ese disparo
            // Se envia al mando un mensaje de score
        }

        // Remove shot from the array
        shoot.setState(Alive.State.DEAD);

    }

    // ***************************************************************************************************** //
    // *************************** [            Collision Shoot          ] ********************************* //
    // ***************************************************************************************************** //

    public static void collisionShootWithAsteroid(KillerGame game, Shoot shot, Asteroid asteroid) {

        // Quitar vida al asteroide / Posible metodo substract health to alive de killer rules
        KillerRules.substractHealthAlive(asteroid, shot.getDamage());

        // Remove shot from the array
        shot.setState(Alive.State.DEAD);

    }

    public static void collisionShootWithBlackHole(KillerGame game, Shoot shot, BlackHole blackhole) {

        // Remove shot from the array
        shot.setState(Alive.State.DEAD);

    }

    public static void collisionShootWithNebulosa(Shoot shot) {

        // Por ahora no pasa nada, aunque se podria implementar que los disparos fuesen mas lento igual que las naves
    }

    public static void collisionShootWithPacman(KillerGame game, Shoot shot, Pacman pacman) {

        // Restar vida al pacman en PACMAN_DECREMENT
        // Hacer pacman mas pequeño / Si maria implementa que la vida y el tamaño son el mismo atributo solo restar vida
        // Hacer que el pacman cambie de direccion
        // Remove shot from the array
        shot.setState(Alive.State.DEAD);

    }

    public static void collisionShootWithPlaneta(KillerGame game, Shoot shot, Planeta planeta) {

        // Remove shot from the array
        shot.setState(Alive.State.DEAD);

    }

    public static void collisionShootWithPowerUp(KillerGame game, Shoot shot, PowerUp powerUp) {

        // Se comprueba si el powerup tiene envoltorio
        // True
        // Restar vida al envoltorio del powerup
        // Remove shot from the array
        shot.setState(Alive.State.DEAD);

        // False
    }

    public static void collisionShootWithWall(KillerGame game, Shoot shot, Wall wall) {
        // Remove shot from the array
        shot.setState(Alive.State.DEAD);
    }

    // crear aqui un shot with wall y pegar lo mismo que en alive with wall pero sin enviar, solo matando

    /**
     * @author Christian
     * @param shoot
     * @param shooot Easter egg :)
     */
    public static void collisionShootWithShoot(KillerGame game, Shoot shoot, Shoot shooot) {
        // Remove shots from the array
        shoot.setState(Alive.State.DEAD);
        shooot.setState(Alive.State.DEAD);
    }

    // ***************************************************************************************************** //
    // *************************** [          Collision Asteroid         ] ********************************* //
    // ***************************************************************************************************** //
    static void collisionAsteroidWithAsteroid(KillerGame aThis, Asteroid asteroid, Asteroid asteroid0) {

        // Quitar vida a los asteroides / Posible metodo substract health to alive de killer rules
        // Aplicar metodo de Physics para el rebote
    }

    static void collisionAsteroidWithBlackHole(KillerGame aThis, Asteroid asteroid, BlackHole blackHole) {

        // Por ahora nada
    }

    static void collisionAsteroidWithNebulosa(Asteroid asteroid) {

        // Por ahora nada
    }

    static void collisionAsteroidWithPacman(KillerGame aThis, Asteroid asteroid, Pacman pacman) {

        // Quitar vida al asteroide / Posible metodo substract health to alive de killer rules
        // Aplicar metodo de Physics para el rebote
    }

    static void collisionAsteroidWithPlaneta(Asteroid asteroid) {

        // Quitar vida al asteroide / Posible metodo substract health to alive de killer rules
        // Aplicar metodo de Physics para el rebote
    }

    static void collisionAsteroidWithPowerUp(KillerGame aThis, Asteroid asteroid, PowerUp powerUp) {

        // Por ahora nada
    }

    // ***************************************************************************************************** //
    // *************************** [          Collision Pacman           ] ********************************* //
    // ***************************************************************************************************** //
    static void collisionPacmanWithBlackHole(Pacman pacman) {

        // Math random de -1 a 1
        // Si da < 0 hacer un game.sendobjecttoprev con el pacman
        // Si da >= 0 hacer un game.sendobjecttonext con el pacman
    }

    static void collisionPacmanWithNebulosa(Pacman pacman) {

        // Por ahora nada
    }

    static void collisionPacmanWithPacman(KillerGame aThis, Pacman pacman, Pacman pacwoman) {

        // Si pacman.health > pacwomen.health
        // Elimnar a pacwoman
        // Sumar a pacman la vida de pacwoman
        // Si pacwoman.health > pacman.health
        // Elimnar a pacman
        // Sumar a pacwoman la vida de pacman
        // Si pacman.health == pacwoman.health
        // Eliminar ambos
    }

    static void collisionPacmanWithPlaneta(Pacman pacman) {

        // Aplicar fisicas de rebote
    }

    static void collisionPacmanWithPowerUp(KillerGame aThis, Pacman pacman, PowerUp powerUp) {

        // Eliminar powerup de la array
        // Aumentar la vida de pacman en PACMAN_INCREMENT x 3
    }

    // ***************************************************************************************************** //
    // *************************** [           Auxiliar Methods          ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Alvaro
     * @param game
     * @param ship
     * @param damage
     * @return True if Ship state becomes dead and False if it still alive.
     */
    private static boolean substractHealthShip(KillerShip ship, int damage) {

        // Dead
        boolean dead = false;

        // Substract heatlh to ship
        ship.quitarVida(damage);

        // Set die status to KillerShip
        if (ship.getHealth() <= 0) {
            ship.changeState(Alive.State.DEAD);
            // Sacar nave de la array
            // Enviar al mando que la nave ha muerto
            dead = true;
        }

        // Return live status
        return dead;

    }

    /**
     * @author Alvaro
     * @param game
     * @param ship
     * @param damage
     * @return True if Alive state becomes dead and False if it still alive.
     */
    private static boolean substractHealthAlive(Alive alive, int damage) {

        // Dead
        boolean dead = false;

        // Substract heatlh to ship
        alive.quitarVida(damage);

        // Set die status to KillerShip
        if (alive.getHealth() <= 0) {
            alive.changeState(Alive.State.DEAD);
            // Sacar alive de la array
            // Enviar al mando que la nave ha muerto
            dead = true;
        }

        // Return live status
        return dead;

    }

}
