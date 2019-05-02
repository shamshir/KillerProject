package visibleObjects;

import game.KillerGame;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Autonomous extends Alive {
    
    public Autonomous(KillerGame game) {
        super(game);
    }

    public void collision() {
        dx *= -1;
        dy *= -1;
    }

    public Rectangle nextMove() {
        return new Rectangle((int) x + (int) dx, (int) y + (int) dy, width, height);
    }
    
    
    
    // *********************
    // * Getters & Setters *
    // *********************
    
}
