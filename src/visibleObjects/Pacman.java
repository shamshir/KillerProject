package visibleObjects;

import game.KillerGame;
import game.KillerRules;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import sound.KillerRadio;
import visualEffects.ExplosionEffect;

/**
 *
 * @author miaad
 */
public class Pacman extends Automata {

    private long fpsControlTime;
    private boolean mouthOpened;
    private KillerRadio killerRadio;

    /**
     * 
     * @param game
     * @param x
     * @param y 
     */
    public Pacman(KillerGame game, double x, double y) {
        super(game, x, y);
        
        this.m = 200;
        this.health = KillerRules.PACMAN_INITIAL_HEALTH;
        this.imgHeight = KillerRules.PACMAN_INITIAL_HEALTH;
        this.imgWidth = KillerRules.PACMAN_INITIAL_HEALTH;
        this.radius = this.imgHeight / 2;
        
        this.maxspeed = 8;

        mouthOpened = true;
        fpsControlTime = System.currentTimeMillis();
        
        // Instanciar physicsPacman
        
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
        this.imgHeight = health;
        this.imgWidth = health;
        this.radius = this.imgHeight / 2;
        this.radians = radians;
        this.vx = vx;
        this.vy = vy;
        this.a = a;
        
        this.maxspeed = 8;

        mouthOpened = true;
        fpsControlTime = System.currentTimeMillis();
        
        // Instanciar physicsPacman
        
        this.killerRadio = new KillerRadio();
    }
    
    /**
     * Suma a width, height y health la cantidad pasada por parámetro
     * @param size cantidad a incrementar
     */
    public void setSize(int size) {
        this.imgWidth += size;
        this.imgHeight += size;
        this.health += size;
    }
    
    @Override
    protected void move() {
//        KillerPhysics.move(this);
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

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (mouthOpened) {
            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (this.radians + 50), 260);
        } else {
            g2d.fillArc((int) (x - radius), (int) (y - radius), this.imgWidth, this.imgHeight, (int) (this.radians + 10), 340);
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
}
