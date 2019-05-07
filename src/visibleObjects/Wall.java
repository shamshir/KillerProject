package visibleObjects;

import game.KillerGame;
import java.awt.image.BufferedImage;

public class Wall extends Static {

    public enum Limit {
        NORTH, SOUTH, WEST, EAST
    }
    
    private Limit limit;
    
    /**
     * 
     * @param game
     * @param position: tipo enumerado --> posibles valores:
     * Limit.NORTH, Limit.SOUTH, Limit.WEST, Limit.EAST
     */    
    public Wall(KillerGame game, Limit position) {
        super();
        this.game = game;        
        this.limit = position;        
        this.setBorder(game, position);        
        this.setImage();
    }

    private void setBorder(KillerGame game, Limit position) {
        switch (position) {
            case NORTH:
                this.x = 0;
                this.y = 0;
                this.imgWidth = game.getViewer().getWidth();
                this.imgHeight = 1;
                break;
            case SOUTH:
                this.x = 0;
                this.y = this.game.getViewer().getHeight() - 1;
                this.imgWidth = this.game.getViewer().getWidth();
                this.imgHeight = 1;
                break;
            case WEST:
                this.x = 0;
                this.y = 0;
                this.imgWidth = 1;
                this.imgHeight = this.game.getViewer().getHeight();
                break;
            case EAST:
                this.x = this.game.getViewer().getWidth() -1;
                this.y = 0;
                this.imgWidth = 1;
                this.imgHeight = game.getViewer().getHeight();
                break;
        }
    }

    @Override
    protected void setImage() {
        this.img = new BufferedImage(this.imgWidth, this.imgHeight, BufferedImage.TYPE_4BYTE_ABGR);
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    

    // *********************
    // * Getters & Setters *
    // *********************
    
    public Limit getType( ) {
        return this.limit;
    }
}
