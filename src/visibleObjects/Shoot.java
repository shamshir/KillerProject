package visibleObjects;

import game.KillerGame;
import java.awt.Color;
import java.awt.Graphics2D;
import physics.PhysicsShoot;
import visualEffects.KillerImage;

public class Shoot extends Automata {

    private String id;
    private int damage;
    private PhysicsShoot physicsShoot;
    private Color color;

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
        this.color = ship.getColor();

        this.maxspeed = 7;
        this.health = 1;

        this.setImage();
        this.imgHeight = 16;
        this.imgWidth = 16;
        this.radius = this.imgHeight / 2;

        // Posición según la posición del morro de la nave
        this.x = ship.getTx();
        this.y = ship.getTy();

        this.radians = ship.radians + Math.PI / 2;
        this.m = 30;
        this.physicsShoot = new PhysicsShoot(this); // han de estar inicializadas todas las variables de fisicas

        this.kImg = new KillerImage(this);
    }

    @Override
    protected void move() {
        physicsShoot.move();
    }

    @Override
    protected void setImage() {
        this.loadImg("src/visibleObjects/img/shot.png");
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    // INTERFAZ Renderizable
    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(this.kImg, (int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, null);
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
