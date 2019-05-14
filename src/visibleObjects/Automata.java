package visibleObjects;

import game.KillerGame;

public abstract class Automata extends Alive {
    
    public enum AutonomousState{
        ALIVE, DIE
    }
    
    protected AutonomousState state;
    
    public Automata() {
    }
    
    public Automata(KillerGame game, double x, double y) {
        super(game, x, y);
        this.state = AutonomousState.ALIVE;
    }
    
    // TO DO: constructor común para instanciar objetos Autonomous recibidos de otro pc

    @Override
    public void run() {

        while (state != AutonomousState.DIE) {

            this.move();
            game.checkColision(this);

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    
    /**
     * Método para restar vida, no cambia ningún estado ni comprueba nada...
     * @param damage vida que quita
     */    
    @Override
    public void quitarVida(int damage) {
//        if (state != AutonomousState.DYING) {
//            this.health -= damage;
//        }
        this.health -= damage;
    }
    
    @Override
    public void onDying() {
        
    }

    @Override
    public void die() {
        
    } 
    
    
    // *********************
    // * Getters & Setters *
    // *********************

    public AutonomousState getState() {
        return state;
    }

    public void setState(AutonomousState state) {
        this.state = state;
    }    
        
}
