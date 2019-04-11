/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visibleObjects;

import game.KillerGame;
import interfaces.Renderizable;

/**
 *
 * @author berna
 */
public abstract class VisibleObject implements Renderizable {

    public KillerGame kg;

    public double x;
    public double y;

    public int HEIGHT;
    public int WIDTH;

    public KillerGame getKg() {
        return kg;
    }

    public void setKg(KillerGame kg) {
        this.kg = kg;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

}
