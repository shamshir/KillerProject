package visibleObjects;

import game.KillerGame;
import java.awt.Color;
import java.awt.Graphics2D;
import physics.CollidePhysics;

public class Shoot extends Automata {

    private String id;
    private int damage;

    /**
     * 
     * @param game
     * @param ship 
     */
    public Shoot(KillerGame game, KillerShip ship) {
        super();
        this.id = ship.getId();
        this.state = AutonomousState.ALIVE;
        // Posición según la posición del morro de la nave
        this.x = ship.getTx();
        this.y = ship.getTy(); 
        this.damage = ship.getDamage();
        
        this.maxspeed = 7;
        this.health = 1;

        this.imgHeight = 15;
        this.imgWidth = 15;
        this.m = 30;
        
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
     */
    public Shoot(KillerGame game, double x, double y, double radians, double vx, double vy, String id, int damage) {
        super(game, x, y);        
        this.a = 0.01;
        
        this.radians = radians;
        this.vx = vx;
        this.vy = vy;
        this.m = 30;
        
        this.id = id;
        this.state = AutonomousState.ALIVE;
        this.damage = damage;
        
        this.maxspeed = 7;
        this.health = 1;

        // Modificar con imgSize, añadir img
        this.imgWidth = 10;
        this.imgHeight = 10;
    }

    @Override
    public void collision() {
        // TO DO
    }

    @Override
    protected void move() {
        CollidePhysics.move(this);
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
        g2d.fillOval((int) x, (int) y, imgWidth, imgHeight);

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

}
