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
        this.radius = this.imgHeight / 2;
        this.radians = Math.random() * (Math.PI * 2); // angulo aleatorio
        this.m = Math.PI * (this.radius * this.radius);

        this.maxspeed = 1;

        mouthOpened = true;
        fpsControlTime = System.currentTimeMillis();

        this.physics = new PhysicsPacman(this);

        this.killerRadio = new KillerRadio();
    }

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
    }

    private void checkInitialSize(int health) {
        if (health >= 30) {
            this.imgHeight = health;
            this.imgWidth = health;
        } else {            
            this.imgHeight = 30;
            this.imgWidth = 30;
        }
    }

    /**
     * Suma a width, height y health la cantidad pasada por parámetro
     *
     * @param size cantidad a incrementar
     */
    public void setSize(int size) {
//        if (size >= 0) {
            if ((this.imgHeight + size) <= 300) {
                this.imgWidth += size;
                this.imgHeight += size;
                this.health += size;
                this.radius = this.imgHeight / 2;
                this.m = Math.PI * (this.radius * this.radius);
                System.out.println("Pacman: crezco");
            } else {
                this.imgWidth = 300;
                this.imgHeight = 300;
                this.health = 300;
                this.radius = this.imgHeight / 2;
                this.m = Math.PI * (this.radius * this.radius);
                System.out.println("Pacman: me quedo igual");
            }
//        } else {
//            if ((this.imgHeight + size) >= 30) {
//                this.imgWidth += size;
//                this.imgHeight += size;
//                this.health += size;
//                this.radius = this.imgHeight / 2;
//                this.m = Math.PI * (this.radius * this.radius);
//                System.out.println("Pacman: me hago pequeño");
//            } else if ((this.imgHeight + size) < 0){
//                this.imgWidth = 30;
//                this.imgHeight = 30;
//                this.health = size;
//                this.radius = this.imgHeight / 2;
//                this.m = Math.PI * (this.radius * this.radius);
//            }
//
//        }
    }

    @Override
    protected void move() {
        this.physics.move();
    }

    @Override
    protected void setImage() {

    }

    private void drawPacman(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);

        if (System.currentTimeMillis() - this.fpsControlTime >= 500) {
            mouthOpened = !mouthOpened;
            this.fpsControlTime = System.currentTimeMillis();
        }

        int degrees = (int) (this.radians * 180 / Math.PI);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (mouthOpened) {
//            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (this.radians + 50), 260);
            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (degrees + 30), 270);
        } else {
//            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (this.radians + 10), 340);
            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (degrees + 5), 330);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    @Override
    public void onDying() {
        this.kImg = new ExplosionEffect(this);
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
