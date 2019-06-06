package visibleObjects;

import game.KillerGame;

public abstract class Alive extends VisibleObject implements Runnable, Destructible {

    public enum State {
        SAFE, ALIVE, DYING, DEAD
    }
    
    protected double a;
    protected double dx;
    protected double dy;
    protected double vx;
    protected double vy;
    protected double maxspeed;
    protected int health;
    protected State state;

    public Alive() {
    }

    public Alive(KillerGame game, double x, double y) {
        super(game, x, y);
        this.a = 0.01;
    }

    protected abstract void move();

    public void changeState(State state) {
        this.state = state;
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    /**
     * Método para restar vida
     * @param damage vida que quita
     */       
    @Override
    public void quitarVida(int damage) {
        this.health -= damage;
    }
    
    // *********************
    // * Getters & Setters *
    // *********************
    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

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

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(double maxspeed) {
        this.maxspeed = maxspeed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
