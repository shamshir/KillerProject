package visibleObjects;

import game.KillerGame;

/**
 *
 * @author miaad
 */
public class PowerUp extends Static implements Destructible {

    public enum Power {
        HEALTH, SAFE
    }

    private boolean wrappered;
    private int health;
    private Power type;

    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param height
     * @param weight
     * @param power 
     */
    public PowerUp(KillerGame game, double x, double y, int height, int weight, Power power) {
        super(game, x, y);
        
        this.type = power;
        this.setImage();

        this.imgHeight = height;
        this.setImgSize();
        this.m = weight;

        this.wrappered = true;
        this.health = 400;
    }

    /**
     * 
     */
    private void unwrapper() {
        this.setImage();
        this.setImgSize();
    }

    @Override
    protected void setImage() {
        if (wrappered) {
            this.loadImg("img/wrapper.png");
        } else {
            switch (type.name()) {
                case "HEALTH":
                   this.loadImg("img/health.png");
                   break;
                case "SAFE":
                   this.loadImg("img/safe.png");
                   break;
            }
        }
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    

    // Interfaz Destructible
    /**
     * El KG tendrá que hacer lo que está comentado en onDying: cambiar estado y cambiar img
     * @param damage 
     */
    @Override
    public void quitarVida(int damage) {
        this.health -= damage;
        
//        if (health <= 0) {
//            this.onDying();
//        }
    }

    @Override
    public void onDying() {
//        this.wrappered = false;
//        this.unwrapper();
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
       
}
