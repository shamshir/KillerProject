package visibleObjects;

import game.KillerGame;
import sound.KillerRadio;

public abstract class Automata extends Alive {
    
    public Automata() {
    }
    
    public Automata(KillerGame game, double x, double y) {
        super(game, x, y);
        this.state = State.ALIVE;
    }

    @Override
    public void run() {  

        while (state != State.DEAD) {

            if (this.state != State.DYING) {
                this.move();
                game.checkColision(this);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        this.game.removeObject(this);
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    @Override
    public void onDying() {
        
    }

    @Override
    public void die() {
        
    } 
    
    
    // *********************
    // * Getters & Setters *
    // *********************
        
}
