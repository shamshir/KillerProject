package visibleObjects;

import game.KillerGame;
import physics.CollidePhysics;

public class Asteroid extends Automata {

    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param imgHeight
     * @param m
     * @param health 
     * @param maxspeed 
     */
    public Asteroid(KillerGame game, double x, double y, int imgHeight, double m, int health, double maxspeed) {
        super(game, x, y);

        this.setImage();
        this.imgHeight = imgHeight;
        this.setImgSize();
        this.m = m;        

        this.health = health;
        this.maxspeed = maxspeed;
    }

    // Constructor para instanciar el obj si viene de otro pc
    // TO DO: qué parámetros hay que mandar de físicas?     
//    public Asteroid(KillerGame game, double x, double y, double dx, double dy, double speed, double health, int imgHeight, int m, String state) {
//        super(game, x, y, dx, speed, dy, health);
//      //radians?
//        this.setImage();
//
//        this.imgHeight = imgHeight;
//        this.setImgSize();
//        this.m = m;
//    }
        
     
    /**
     *  Cambiar en función de las físicas
     */
    @Override
    protected void move() {
        CollidePhysics.move(this);
    }

    private void divide() {
        // Si el Asteroid se ha de dividir al morir
        // TO DO
    }

    @Override
    protected void setImage() {
        this.loadImg("./img/asteroid2.png");
    }

    @Override
    public void collision() {
        // TO DO
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    // *********************
    // * Getters & Setters *
    // *********************
}
