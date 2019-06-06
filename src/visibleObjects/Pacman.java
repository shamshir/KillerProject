package visibleObjects;

import game.KillerGame;
import game.KillerRules;
import java.awt.Color;
import java.awt.Graphics2D;
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
        this.imgHeight = health;
        this.imgWidth = health;
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

    @Override
    public void run() {
        // Iniciar sonido del Pacman
        this.killerRadio.setClip(KillerRadio.ClipType.PACMAN_MOVE);

        while (state != State.DEAD) {

            if (this.state != State.DYING) {
                this.move();
                game.checkColision(this);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        // Parar sonido del Pacman
        this.killerRadio.stopSound();
        this.game.removeObject(this);
    }

    /**
     * Suma a width, height y health la cantidad pasada por parámetro.
     * En principio tendrá el mismo ancho y alto que su salud, pero se
     * limita el tamaño mínimo (50px) y máximo (500px) que podrá tener,
     * independientemente de su salud.
     * Se recalcula el radio y la masa.
     *
     * @param size cantidad a incrementar (positiva o negativa)
     */
    public void setSize(int size) {
        this.health += size;

        if (this.health > 500) {
            this.imgWidth = 500;
            this.imgHeight = 500;
        } else if (this.health < 50) {
            this.imgWidth = 50;
            this.imgHeight = 50;
        } else {
            this.imgWidth += size;
            this.imgHeight += size;
        }
        this.radius = this.imgHeight / 2;
        this.m = Math.PI * (this.radius * this.radius);
    }

    /**
     * Método para quitar vida, modificando el alto, ancho, radio y m del pacman.
     * Cambia el valor de la variable hit para que durante unos fotogramas se pinte de diferente color.
     * @param damage daño recibido
     */
    @Override
    public void quitarVida(int damage) {
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

    /**
     * Método para pintar el Pacman.
     * Tiene una pequeña animación para que abra y cierre la boca
     * y para que cambie de color durante unos segundos cuando le disparan
     * @param g2d 
     */
    private void drawPacman(Graphics2D g2d) {
        // Color en función de si le han disparado o no
        if (this.hit > 0) {
            g2d.setColor(Color.decode("#fcf5bf"));
            this.hit--;
        } else {
            g2d.setColor(Color.decode("#f4db1d"));
        }

        // Control para que tenga la boca abierta o cerrada
        if (System.currentTimeMillis() - this.fpsControlTime >= 500) {
            mouthOpened = !mouthOpened;
            this.fpsControlTime = System.currentTimeMillis();
        }

        // Ángulo con el que se pintará, según su dirección
        int degrees = (int) (this.radians * 180 / Math.PI);

        // Se pinta un arco de longitud adecuada según si ha de tener la boca abierta o cerrada, y según el ángulo que lleva
        if (mouthOpened) {
            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (degrees + 50), 260);
        } else {
            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (degrees + 10), 340);
        }

    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    /**
     * Método para iniciar el ExplosionEffect
     */
    @Override
    public void onDying() {
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
