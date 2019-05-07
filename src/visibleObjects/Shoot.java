package visibleObjects;

import game.KillerGame;
import java.awt.Color;
import java.awt.Graphics2D;
import physics.KillerPhysics;

public class Shoot extends Automata {

    private KillerShip ship;

    /**
     * 
     * @param game
     * @param ship 
     */
    public Shoot(KillerGame game, KillerShip ship) {
        super();
        this.ship = ship;
        this.state = AutonomousState.ALIVE;
        // Posición según la posición del morro de la nave
        this.x = this.ship.tx;
        this.y = this.ship.ty;  
        
        this.maxspeed = 7;
        this.health = 1;

        this.imgHeight = 15;
        this.imgWidth = 15;
        this.m = 30;
        
    }

    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param angle
     * @param dx
     * @param dy
     * @param vx
     * @param vy
     * @param ship
     * @param state 
     */
    public Shoot(KillerGame game, double x, double y, double angle, double dx, double dy, double vx, double vy, KillerShip ship, AutonomousState state) {
        super(game, x, y);        
        this.a = 0.01;
        
        this.angle = angle;
        this.dx = dx;
        this.dy = dy;
        this.vx = vx;
        this.vy = vy;
        this.m = 30;
        
        this.ship = ship;
        this.state = state;
        
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
        KillerPhysics.move(this);
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
    public KillerShip getControlled() {
        return ship;
    }

    public KillerShip getShip() {
        return ship;
    }

    public void setShip(KillerShip ship) {
        this.ship = ship;
    }

}
