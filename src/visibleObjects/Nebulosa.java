package visibleObjects;

import game.KillerGame;

public class Nebulosa extends Static {

    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param height
     * @param m 
     */
    public Nebulosa(KillerGame game, double x, double y, int height, int m) {
        super(game, x, y);

        this.setImage();

        this.imgHeight = height;
        this.setImgSize();
        this.m = m;
    }

    @Override
    protected void setImage() {
        this.loadImg("./img/nebulosa.png");
    }
    
    // TO DO --> frenar(Alive obj) ??

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
        

    // *********************
    // * Getters & Setters *
    // *********************
}
