package visibleObjects;

import game.KillerGame;
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
     * Toda la clase es provisional, pa que se vea algo
     * @param game
     * @param x
     * @param y 
     */
    public Pacman(KillerGame game, double x, double y) {
        super(game, x, y);

        this.imgHeight = game.getViewer().getHeight() / 50;
        this.imgWidth = game.getViewer().getHeight() / 50;
        
        this.m = 200;
        this.health = 500; 
        
        this.maxspeed = 1;      
        this.dx = maxspeed;

        mouthOpened = true;
        fpsControlTime = System.currentTimeMillis();
    }

    /**
     * Método para aumentar de volumen, provisional, sólo aumenta el ancho y alto de la img sumando el parámetro, o sea que no pasarse, y en los bordes...!!?@@#3
     * @param food 
     */
    public void grow(int food) {
        // TO DO
        imgWidth += food;
        imgHeight += food;
    }
    
    public void move() {
//        KillerPhysics.move(this);        
        x += dx;
        y += dy;
    }

    @Override
    public void collision() {
        // TO DO
        dx *= -1;
        dy *= -1;
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
