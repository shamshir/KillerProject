package visibleObjects;

import game.KillerGame;
import java.awt.Graphics2D;
import physics.PhysicsAsteroid;
import visualEffects.ExplosionEffect;

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

    /**
     * Constructor para instanciar el obj si viene de otro pc
     * @param game
     * @param x
     * @param y
     * @param imgHeight
     * @param m
     * @param health
     * @param radians
     * @param vx
     * @param vy
     * @param a 
     */
    public Asteroid(KillerGame game, double x, double y, int imgHeight, double m, int health, 
            double radians, double vx, double vy, double a) {
        super(game, x, y);
        
        this.setImage();
        this.imgHeight = imgHeight;
        this.imgWidth = imgHeight;
        this.radius = this.imgHeight / 2;
        this.m = m;
        this.health = health;
        this.radians = radians;
        this.vx = vx;
        this.vy = vy;
        this.a = a;
        this.physicsAsteroid = new PhysicsAsteroid(this); // han de estar inicializadas todas las variables de fisicas
        
    }
        
     
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
    public void onDying() {
        this.kImg = new ExplosionEffect(this);
        (new Thread(this.kImg)).start();
    }

    // Interfaz Renderizable
    @Override
    public void render(Graphics2D g2d) {

        switch (this.state) {
            case ALIVE:
                g2d.drawImage(this.img, (int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, null);
                break;
            case DYING:
                g2d.drawImage(this.kImg, (int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, null);
                break;
            default:
                //System.out.println("Asteroid render --> SAFE or DEAD, rendering DEFAULT");
                break;
        }

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
