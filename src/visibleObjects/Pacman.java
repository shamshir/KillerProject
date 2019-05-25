package visibleObjects;

import game.KillerGame;
import game.KillerRules;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author miaad
 */
public class Pacman extends Automata {

    private long fpsControlTime;
    private boolean mouthOpened;

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

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    // INTERFAZ Renderizable    
    @Override
    public void render(Graphics2D g2d) {
        // TO DO: cambiar por img
        g2d.setColor(Color.YELLOW);

        if (System.currentTimeMillis() - this.fpsControlTime >= 500) {
            mouthOpened = !mouthOpened;
            this.fpsControlTime = System.currentTimeMillis();
        }

        if (mouthOpened) {
            if (dx >= 0) {
                g2d.fillArc((int) x, (int) y, imgWidth, imgHeight, 50, 260);
            } else {
                g2d.fillArc((int) x, (int) y, imgWidth, imgHeight, 230, 260);
            }
        } else {
            if (dx >= 0) {
                g2d.fillArc((int) x, (int) y, imgWidth, imgHeight, 20, 320);
            } else {
                g2d.fillArc((int) x, (int) y, imgWidth, imgHeight, 200, 320);
            }
        }
        
    }
    

    // *********************
    // * Getters & Setters *
    // *********************
}
