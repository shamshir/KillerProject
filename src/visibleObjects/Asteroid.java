package visibleObjects;

import game.KillerGame;
import java.awt.Graphics2D;
import physics.PhysicsAsteroid;

public class Asteroid extends Automata {
    private PhysicsAsteroid physicsAsteroid;

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
        this.imgWidth = imgHeight;
        this.radius = this.imgHeight / 2;
        this.m = m;

        this.health = health;
        this.maxspeed = maxspeed;
        this.vx = maxspeed;
        this.vy = maxspeed;
        this.physicsAsteroid = new PhysicsAsteroid(this); // han de estar inicializadas todas las variables de fisicas

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
        physicsAsteroid.move();
    }

    @Override
    protected void setImage() {
        this.loadImg("src/visibleObjects/img/asteroid.png");
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(this.img, (int) (x - radius), (int) (y - radius), imgHeight, imgHeight, null);
    }
    
    // *********************
    // * Getters & Setters *
    // *********************

    public PhysicsAsteroid getPhysicsAsteroid() {
        return physicsAsteroid;
    }

    public void setPhysicsAsteroid(PhysicsAsteroid physicsAsteroid) {
        this.physicsAsteroid = physicsAsteroid;
    }
}
