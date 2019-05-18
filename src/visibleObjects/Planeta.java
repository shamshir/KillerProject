package visibleObjects;

import game.KillerGame;

public class Planeta extends Static {
    
    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param height
     * @param m 
     */
    public Planeta(KillerGame game, double x, double y, int height, int m) {
        super(game, x, y);

        this.setImage();

        this.imgHeight = height;
        this.setImgSize();
        this.m = m;
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    

    // *********************
    // * Getters & Setters *
    // *********************

    @Override
    protected void setImage() {
        this.loadImg("src/visibleObjects/img/planet1.png");
    }
}
