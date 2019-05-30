package visibleObjects;

import game.KillerGame;
import java.awt.Color;
import java.awt.Graphics2D;
import physics.PhysicsShoot;

public class Shoot extends Automata {

    private String id;
    private int damage;
    private PhysicsShoot physicsShoot;

    /**
     * 
     * @param game
     * @param ship 
     */
    public Shoot(KillerGame game, KillerShip ship) {
        super();
        this.game = game;
        this.id = ship.getId();
        this.state = State.ALIVE; 
        this.damage = ship.getDamage(); // Daño de su nave
        
        this.maxspeed = 7;
        this.health = 1;

        this.imgHeight = 15;
        this.imgWidth = 15;
        this.radius = this.imgHeight / 2;  // --> imgHeight
        
        // Posición según la posición del morro de la nave
        this.x = ship.getTx();
        this.y = ship.getTy();
        
        this.radians = ship.radians + Math.PI/2;
        this.m = 30;
        this.physicsShoot = new PhysicsShoot(this); // han de estar inicializadas todas las variables de fisicas

        
    }

    /**
     * Constructor para replicar objeto enviado desde otro pc
     * @param game
     * @param x
     * @param y
     * @param radians
     * @param vx
     * @param vy
     * @param id
     * @param damage
     * @param health
     * @param state 
     */
    public Shoot(KillerGame game, double x, double y, double radians, double vx, double vy, String id, int damage, int health, State state) {
        super(game, x, y);
        
        this.radians = radians; // Repetido en vo.
        this.vx = vx; // quitar? Bernat
        this.vy = vy; // quitar? Bernat
        this.m = 30;
        
        this.id = id;
        this.state = state;
        this.damage = damage;
        
        this.maxspeed = 7;
        this.health = health;

        // Modificar con imgSize, añadir img
        this.imgWidth = 15;
        this.imgHeight = 15;
        this.radius = this.imgHeight / 2;
        this.physicsShoot = new PhysicsShoot(this); // han de estar inicializadas todas las variables de fisicas

    }

    @Override
    protected void move() {
        physicsShoot.move();
    }

    @Override
    protected void setImage() {

    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    // INTERFAZ Renderizable
    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.CYAN);
        g2d.fillOval((int)(this.x  - this.radius), (int)(this.y  - this.radius), this.imgWidth, this.imgHeight);

    }

    // *********************
    // * Getters & Setters *
    // *********************

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public PhysicsShoot getPhysicsShoot() {
        return physicsShoot;
    }

    public void setPhysicsShoot(PhysicsShoot physicsShoot) {
        this.physicsShoot = physicsShoot;
    }

}
