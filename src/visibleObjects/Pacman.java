package visibleObjects;

import game.KillerGame;
import game.KillerRules;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import sound.KillerRadio;
import visualEffects.ExplosionEffect;
import physics.PhysicsPacman;

/**
 *
 * @author miaad
 */
public class Pacman extends Automata {

    private long fpsControlTime;
    private boolean mouthOpened;
    private KillerRadio killerRadio;
    private PhysicsPacman physics;
    private int hit;

    /**
     *
     * @param game
     * @param x
     * @param y
     */
    public Pacman(KillerGame game, double x, double y) {
        super(game, x, y);

        this.health = KillerRules.PACMAN_INITIAL_HEALTH;
        this.imgHeight = KillerRules.PACMAN_INITIAL_HEALTH;
        this.imgWidth = KillerRules.PACMAN_INITIAL_HEALTH;
        System.out.println("Pacman width: " + this.imgWidth);
        System.out.println("Pacman height: " + this.imgHeight);
        this.radius = this.imgHeight / 2;
        this.radians = Math.random() * (Math.PI * 2); // angulo aleatorio
        this.m = Math.PI * (this.radius * this.radius);
        this.hit = 0;

        this.maxspeed = 1;

        mouthOpened = true;
        fpsControlTime = System.currentTimeMillis();

        this.physics = new PhysicsPacman(this);

        this.killerRadio = new KillerRadio();
        this.kImg = new ExplosionEffect(this);
    }
    
    // 2o CONSTRUCTOR DEFINITIVO
    
    /**
     * Constructor para instanciar un pacman cuando se recibe de otro PC
     * 
     * @param game
     * @param x
     * @param y
     * @param m
     * @param health
     * @param radians
     * @param vx
     * @param vy
     * @param a
     * @param imgHeight 
     */
    public Pacman(KillerGame game, double x, double y, double m, int health,
            double radians, double vx, double vy, double a, int imgHeight) {
        super(game, x, y);

        this.m = m;
        this.hit = 0;
        this.health = health;
        this.imgHeight = imgHeight;
        this.imgWidth = imgHeight;
        this.radius = this.imgHeight / 2;
        this.radians = radians;
        this.vx = vx;
        this.vy = vy;
        this.a = a;

        this.maxspeed = 1;

        mouthOpened = true;
        fpsControlTime = System.currentTimeMillis();

        this.physics = new PhysicsPacman(this);

        this.killerRadio = new KillerRadio();
        this.kImg = new ExplosionEffect(this);
    }

    // ELIMINAR CONSTRUCTOR
    /**
     *
     * @param game
     * @param x
     * @param y
     * @param m
     * @param health
     * @param radians
     * @param vx
     * @param vy
     * @param a
     */
    public Pacman(KillerGame game, double x, double y, double m, int health,
            double radians, double vx, double vy, double a) {
        super(game, x, y);

        this.m = m;
        this.hit = 0;
        this.health = health;
        this.imgHeight = health; // cambiar por checkInitialSize
        this.imgWidth = health; // cambiar por checkInitialSize
        this.radius = this.imgHeight / 2;
        this.radians = radians;
        this.vx = vx;
        this.vy = vy;
        this.a = a;

        this.maxspeed = 1;

        mouthOpened = true;
        fpsControlTime = System.currentTimeMillis();

        this.physics = new PhysicsPacman(this);

        this.killerRadio = new KillerRadio();
        this.kImg = new ExplosionEffect(this);
    }

    /**
     * Suma a width, height y health la cantidad pasada por parámetro
     *
     * @param size cantidad a incrementar
     */
    public void setSize(int size) {
        this.health += size;
//        System.out.println("Pacman EAT " + size);

        if (this.health > 300) {
            this.imgWidth = 300;
            this.imgHeight = 300;
        } else if (this.health < 20) {
            this.imgWidth = 20;
            this.imgHeight = 20;
        } else {
            this.imgWidth += size;
            this.imgHeight += size;
        }
        this.radius = this.imgHeight / 2;
        this.m = Math.PI * (this.radius * this.radius);

//        System.out.println("Pacman HEALTH = " + this.health);
//        System.out.println("Pacman WIDTH = " + this.imgWidth);
//        System.out.println("Pacman HEIGHT = " + this.imgHeight);
//        System.out.println("--------------");
    }

    @Override
    public void quitarVida(int damage) {
//        System.out.println("Pacman HITTED: damage --> " + damage);
//        System.out.println("--------------");
        this.setSize(-damage);
        this.hit = 8;
    }

    @Override
    protected void move() {
        this.physics.move();
    }

    @Override
    protected void setImage() {

    }

    private void drawPacman(Graphics2D g2d) {
        if (this.hit > 0) {
            g2d.setColor(Color.decode("#f4ee7a"));
            this.hit--;
        } else {
            g2d.setColor(Color.decode("#f4db1d"));
        }

        if (System.currentTimeMillis() - this.fpsControlTime >= 500) {
            mouthOpened = !mouthOpened;
            this.fpsControlTime = System.currentTimeMillis();
        }

        int degrees = (int) (this.radians * 180 / Math.PI);
        
        if (mouthOpened) {
            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (degrees + 50), 260);
        } else {
            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (degrees + 10), 340);
        }

    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    @Override
    public void onDying() {
//        this.kImg = new ExplosionEffect(this);
        (new Thread(this.kImg)).start();

    }

    // INTERFAZ Renderizable    
    @Override
    public void render(Graphics2D g2d) {

        switch (this.state) {
            case ALIVE:
                this.drawPacman(g2d);
                break;
            case DYING:
                g2d.drawImage(this.kImg, (int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, null);
                break;
            default:
                //System.out.println("Pacman render --> SAFE or DEAD, rendering DEFAULT");
                break;
        }

    }

    // *********************
    // * Getters & Setters *
    // *********************
    public KillerRadio getKillerRadio() {
        return killerRadio;
    }

    public void setKillerRadio(KillerRadio killerRadio) {
        this.killerRadio = killerRadio;
    }

    public PhysicsPacman getPhysics() {
        return physics;
    }

    public void setPhysics(PhysicsPacman physics) {
        this.physics = physics;
    }
}
