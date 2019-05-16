package visibleObjects;

import game.KillerGame;

public class BlackHole extends Static {
    
    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param height
     * @param m 
     */
    public BlackHole(KillerGame game, double x, double y, int height, int m) {
        super(game, x, y);

        this.setImage();

        this.imgHeight = height;
        this.setImgSize();
        this.m = m;

    }

    @Override
    protected void setImage() {
        this.loadImg("./img/blackhole.png");
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    

    // *********************
    // * Getters & Setters *
    // *********************
}

