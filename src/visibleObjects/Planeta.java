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
    public Planeta(KillerGame game, double x, double y, int height) {
        super(game, x, y);

        this.setImage();

        this.imgHeight = height;
        this.imgWidth = imgHeight;
        this.radius = this.imgHeight / 2;
        this.m = Math.PI * (this.radius * this.radius);
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    @Override
    protected void setImage() {
        if ((int)(Math.random()* 2 + 1) == 1) {
            this.loadImg("img/planeta1.png");
        } else {
            this.loadImg("img/planeta2.png");
        }
    }
    // *********************
    // * Getters & Setters *
    // *********************
}
