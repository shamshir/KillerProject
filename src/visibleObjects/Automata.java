package visibleObjects;

import game.KillerGame;

public abstract class Automata extends Alive {
    
    public Automata() {
    }
    
    public Automata(KillerGame game, double x, double y) {
        super(game, x, y);
        this.state = State.ALIVE;
    }
    
    // TO DO: constructor común para instanciar objetos Autonomous recibidos de otro pc

    @Override
    public void run() {

        while (state != State.DEAD) {

            this.move();
            game.checkColision(this);

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
