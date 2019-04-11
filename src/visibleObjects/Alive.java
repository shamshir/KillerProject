/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visibleObjects;

import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author berna
 */
public abstract class Alive extends VisibleObject implements Runnable {

    public double dx;
    public double dy;
    public double speed;

    public Rectangle hitbox;
    public Color color;
    public String colorhex;

    public boolean alive;
    
    public long time;

    public abstract void move();

    public abstract void collision();
    
    public abstract Rectangle nextMove();

    public abstract void death();

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    
}
