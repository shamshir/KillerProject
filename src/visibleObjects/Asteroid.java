package visibleObjects;

import game.KillerGame;
import java.awt.Graphics2D;
import physics.PhysicsAsteroid;
import visualEffects.FireEffect;

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
        this.physicsAsteroid = new PhysicsAsteroid(this); // han de estar inicializadas todas las variables de fisicas

        this.kImg = new FireEffect(this, this.img);

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(this.kImg,
                (int) x, (int) y,
                imgWidth, this.kImg.getRenderHeight(),
                null);
    }

    /**
     * Cambiar en función de las físicas
     */
    @Override
    protected void move() {
        physicsAsteroid.move();
    }

    private void divide() {
        // Si el Asteroid se ha de dividir al morir
        // TO DO
    }

    @Override
    protected void setImage() {
        this.loadImg("src/visibleObjects/img/asteroid.png");
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
    public PhysicsAsteroid getPhysicsAsteroid() {
        return physicsAsteroid;
    }

    public void setPhysicsAsteroid(PhysicsAsteroid physicsAsteroid) {
        this.physicsAsteroid = physicsAsteroid;
    }
}
