package visibleObjects;

import game.KillerGame;

/**
 *
 * @author miaad
 */
public class PowerUp extends Static implements Destructible {

    public enum Power {
        HEALTH, DAMAGE
    }

    private boolean available;
    private boolean wrappered;
    private int health;
    private Power type;

    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param height
     * @param m
     * @param power 
     */
    public PowerUp(KillerGame game, double x, double y, int height, int m, Power power) {
        super(game, x, y);
        
        this.type = power;
        this.wrappered = true;
        this.available = false;
        this.setImage();
        this.imgHeight = height;
        this.imgWidth = imgHeight;
        this.radius = this.imgHeight / 2;
        this.m = m;
        this.health = 400;
    }

    /**
     * Método para cambiar la imagen del powerUp, llamado al quedarse sin vida
     */
    public void unwrapper() {
        this.setImage();
        this.setImgSize();
        this.radius = this.imgHeight / 2;
    }

    /**
     * Método que cargala imagen del Pwerup
     */
    @Override
    protected void setImage() {
        if (wrappered) {
            this.loadImg("src/visibleObjects/img/wrapper.png");
        } else {
            switch (type.name()) {
                case "HEALTH":
                   this.loadImg("src/visibleObjects/img/health.png");
                   break;
                case "DAMAGE":
                   this.loadImg("src/visibleObjects/img/power.png");
                   break;
            }
        }
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    

    // Interfaz Destructible
    @Override
    public void quitarVida(int damage) {
        this.health -= damage;
    }

    @Override
    public void onDying() {
        
    }

    @Override
    public void die() {
        
    }

    // *********************
    // * Getters & Setters *
    // *********************
    public boolean isWrappered() {
        return wrappered;
    }

    public void setWrappered(boolean wrappered) {
        this.wrappered = wrappered;
    }

    public Power getPowerType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Power getType() {
        return type;
    }

    public void setType(Power type) {
        this.type = type;
    }
       
}
