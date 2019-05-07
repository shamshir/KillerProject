package visibleObjects;

import game.KillerGame;

public class BlackHole extends Static {
    
    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param height
     * @param weight 
     */
    public BlackHole(KillerGame game, double x, double y, int height, int weight) {
        super(game, x, y);

        this.setImage();

        this.imgHeight = height;
        this.setImgSize();
        this.m = weight;

    }

    @Override
    protected void setImage() {
        this.loadImg("img/blackhole.png");
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    

    // *********************
    // * Getters & Setters *
    // *********************
}

