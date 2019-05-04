package game;

// Import Killer Game pakages
import visualEffects.*;
import visibleObjects.*;
import communications.*;
// Other imports
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Hashtable;

/**
 *
 * @author Alvaro
 */
public class KillerGame extends JFrame {
    
    // Attributes
    
    // Game
    private String status;
    
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

    // Constructors 
    public KillerGame() {
        
        // Update game status
        this.status = "starting";
        
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
        
    }
    
    // Methods new
    private void newServer() {
        /*/
        this.server = new Server();
        Thread thread = new Thread(this.server).start();
        /*/
    }

    private void newPrevModule() {
        /*/
        this.prevModule = new VisualHandler();
        Thread thread = new Thread(this.prevModule).start();
        /*/
    }

    private void newNextModule() {
        /*/
        this.nextModule = new VisualHandler();
        Thread thread = new Thread(this.nextModule).start();
        /*/
    }

    public void newKillerPad(String ip) {
        /*/
        KillerPad pad = new KillerPad();
        this.pads.put(ip, pad)
        Thread thread = new Thread(pad).start();
        /*/
    }

    public void newKillerShip(String ip) {
        /*/
        KillerShip ship = new Controlled();
        this.ships.put(ip, ship);
        this.objects.add(ship);
        Thread thread = new Thread(ship).start();
        /*/
    }
    
    public void newShoot(Color color, Controlled ship) {
        /*/
        Shoot shoot = new Shoot(this, color, ship);
        this.objects.add(shoot);
        Thread thread = new Thread(shoot).start();
        /*/
    }

    // Methods get    
    public KillerPad getPadByIP(String ip) {
        return this.pads.get(ip);
    }

    public Controlled getShipByIP(String ip) {
        return this.ships.get(ip);
    }
    
    public VisualHandler getPrevModule() {
        return this.prevModule;
    }
    
    public VisualHandler getNextModule() {
        return this.nextModule;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    // Methods set
    
    // Main activity
    public static void main(String[] args) {
        KillerGame game = new KillerGame();
    }

}