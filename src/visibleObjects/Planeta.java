package visibleObjects;

import game.KillerGame;

public class Planeta extends Static {
    
    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param height
     * @param weight 
     */
    public Planeta(KillerGame game, double x, double y, int height, int weight) {
        super(game, x, y);

        this.setImage();

        this.imgHeight = height;
        this.setImgSize();
        this.m = weight;
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    

    // *********************
    // * Getters & Setters *
    // *********************

    @Override
    protected void setImage() {
        this.loadImg("img/planet1.png");
    }
}
