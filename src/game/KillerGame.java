package game;

// Import Killer Game pakages
import visualEffects.*;
import visibleObjects.*;
import communications.*;
// Other imports
import java.awt.Color;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Hashtable;

/**
 * @author Alvaro
 */
public class KillerGame extends JFrame {
    
    // Attributes
    
    // Game
    public enum Status {
        room, starting, game
    };

    private Status status;
    
    // Lista de objetos
    private ArrayList<VisibleObject> objects = new ArrayList<>();
    
    // Lista de naves de naves | IP del mando al que pertenece, Nave
    private Hashtable<String, Controlled> ships = new Hashtable();
    
    // Server
    private KillerServer server;
    
    // Visual Handlers
    private VisualHandler nextModule;
    private VisualHandler prevModule;

    // Gamepad List | IP del mando, Mando
    private Hashtable<String, KillerPad> pads = new Hashtable();
    
    // Viewer
    private Viewer viewer;

    // Constructors 
    public KillerGame() {
        
        // Update game status
        this.status = Status.room;
        
        // Open left and right modules
        this.newPrevModule();
        this.newPrevModule();
        
        // Open server
        this.newServer();
        
        // Show window
        
        // Start room
        
    }

    // Methods
    public void checkColision(Alive obj) {
        
        // TODO

        // Bernat Code //  Este codigo sera eliminado //
        for (int i = 0; i < objects.size(); i++) {

            VisibleObject objCol = null;

            if (objects.get(i) != obj) {
                objCol = objects.get(i);
            }

            if (objCol instanceof Alive) {
                if (obj.nextMove().intersects(((Alive) objCol).hitbox)) {
                    KillerRules.collision(obj, objCol);
                }

            }

        }
        // Bernat Code // Este codigo sera eliminado //
        
    }

    // Bernat Code // Este metodo sera eliminado //
    public void checkColision(Shoot obj) {
        //colision con limites
        if (obj.getY() >= viewer.getHeight() || obj.getY() <= 0
                || obj.getX() >= viewer.getWidth() || obj.getX() <= 0) {
            obj.death();
        }
        for (int i = 0; i < objects.size(); i++) {
            VisibleObject objCol = null;
            if (objects.get(i) != obj.getControlled()) {
                objCol = objects.get(i);
            }
            if (objCol instanceof Alive) {
                if (obj.hitbox.intersects(((Alive) objCol).hitbox)) {
                    KillerRules.collisionShoot(obj, (Alive) objCol);
                }
            }
        }
    }
    // Bernat Code // Este metodo sera eliminado //
    
    public void sendMessageToPrev(Message message) {
        // TODO
    }
    
    public void sendMessageToNext(Message message) {
        // TODO
    }
    
    public void sendObjectToPrev(Alive object) {
        // TODO
    }
    
    public void sendObjectToNext(Alive object) {
        // TODO
    }
    
    public void StartGame() {
        if (this.status == Status.room) {
            this.status = Status.starting;
            // TODO
            this.status = Status.game;
        }
    }
    
    // Methods new
    private void newServer() {
        try {
            this.server = new KillerServer(this, 8000);
            new Thread(this.server).start();
        } catch (Exception exception) {
            System.out.println("No ha sido posible encender el servidor debido a la siguiente excepcion : " + exception);
        }
    }

    private void newPrevModule() {
        this.prevModule = new VisualHandler(this, false);
        new Thread(this.prevModule).start();
    }

    private void newNextModule() {
        this.nextModule = new VisualHandler(this, true);
        new Thread(this.nextModule).start();
    }

    public boolean newKillerPad(String ip, Socket socket, String user, String color) {
        
        boolean result = false;
        
        if (this.status == Status.room) {
            KillerPad pad = new KillerPad(this, socket, user, color);
            this.pads.put(ip, pad);
            new Thread(pad).start();
            result = true;
        }
        
        return result;
        
    }

    public void newKillerShip(String ip, Color color, String user) {
        Controlled ship = new Controlled(this, color, ip, user);
        this.ships.put(ip, ship);
        this.objects.add(ship);
        new Thread(ship).start();
    }
    
    public void newShoot(Color color, Controlled ship) {
        Shoot shoot = new Shoot(this, color, ship);
        this.objects.add(shoot);
        new Thread(shoot).start();
    }

    // Este metodo sera eliminado //
    public void createControlled(Controlled contr) {
        objects.add(contr);
        new Thread(contr).start();
    }
    // Este metodo sera eliminado //

    // Methods get    
    public KillerPad getPadByIP(String ip) {
        return this.pads.get(ip);
    }

    public Hashtable<String, KillerPad> getPads() {
        return pads;
    }

    public Controlled getShipByIP(String ip) {
        return this.ships.get(ip);
    }
    
    public KillerServer getServer() {
        return this.server;
    }
    
    /**
     * El nombre de este metodo sera cambiado por getPrevModule()
     * @return Modulo previo.
     */
    public VisualHandler getPk() {
        return this.prevModule;
    }
    
    /**
     * El nombre de este metodo sera cambiado por getNextModule()
     * @return Modulo siguiente.
     */
    public VisualHandler getNk() {
        return this.nextModule;
    }
    
    public Viewer getViewer() {
        return this.viewer;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    // Methods set
    
    // Main activity
    public static void main(String[] args) {
        KillerGame game = new KillerGame();
    }

}