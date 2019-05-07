package visibleObjects;

import game.KillerGame;
import java.awt.Color;
import java.awt.Rectangle;

public abstract class Alive extends VisibleObject implements Runnable {

    protected double dx;
    protected double dy;
    protected double speed;
    protected long time;
    
    public Alive(KillerGame game) {
        super(game);
    }

    @Override
    public void run() {
        
        while (this.alive) {

            game.checkColision(this);
            move();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {

            }
        }
    }

    protected void updateHitBox() {
        hitbox.setBounds((int) x, (int) y, width, height);
    }

    public abstract void move();
    
    // TODAS las clases que heredan han de implementarlo
    public abstract void collision();

    public abstract Rectangle nextMove();

    public abstract void die();
    
    
    // *********************
    // * Getters & Setters *
    // *********************

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

}
