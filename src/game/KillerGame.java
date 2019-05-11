package game;

// Import Killer Game pakages
import visualEffects.*;
import visibleObjects.*;
import communications.*;
import gameRoom.KillerPanel;
// Other imports
import java.awt.Color;
import java.awt.GridLayout;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * @author Alvaro
 */
public class KillerGame extends JFrame {

    public void addKillerShoot(KillerShip aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
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
        
        // Set game status
        // this.status = Status.room;
        
        // Show window
        this.showWindow();
        
        // Open left and right modules
        this.newPrevModule();
        this.newNextModule();
        
        // Open server
        this.newServer();
        
        // Show room
        // this.newKillerRoom();
        
        // Start game
        this.startGame();
        
        // Add walls
        this.newWall(Wall.Limit.EAST);
        this.newWall(Wall.Limit.NORTH);
        this.newWall(Wall.Limit.SOUTH);
        this.newWall(Wall.Limit.WEST);
        
    }

    // Methods
    public void checkColision(Alive alive) {
        
        for (int inc = 0; inc < this.objects.size(); inc ++) {
            
            if ( false ) { // Aqui no se que condicion poner para detectar si se chocan, tengo que hablar con Maria
                
                KillerRules.collision(alive, this.objects.get(inc));
                
            }
            
        }
        
    }
    
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
    
    public void sendReady() {
        this.nextModule.startGame();
        /*/
        if (this.status == Status.room) {
            this.status = Status.starting;
            
        }
        /*/
        System.out.println("Ready");
    }
    
    public void startGame() {
        
        /*/
        if (this.status == Status.starting) {
            this.status = Status.starting;
            this.nextModule.startGame();
        }
        /*/
        
        // TODO Empezar Juego
        this.status = Status.starting;
        this.newViewer();
    }
    
    public void showWindow() {
        this.setSize(1120, 630);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout());
        this.setResizable(false);
        this.setVisible(true);
    }
    
    // Methods new
    
    // New communications
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
        
        // if (this.status == Status.room) {
            KillerPad pad = new KillerPad(this, socket, user, color, ip);
            this.pads.put(ip, pad);
            new Thread(pad).start();
            result = true;
        // }
        
        return result;
        
    }
    
    // New VisibleObjects
    public void newKillerShip(String ip, Color color, String user, int x, int y, KillerShip.ShipType type) {
        KillerShip ship = new KillerShip(this, x, y, ip, user, type);
        this.ships.put(ip, ship);
        this.objects.add(ship);
        new Thread(ship).start();
    }
    
    public void newShoot(KillerShip ship) {
        Shoot shoot = new Shoot(this, ship);
        this.objects.add(shoot);
        new Thread(shoot).start();
    }
    
    public void newWall(Wall.Limit limit) {
        Wall wall = new Wall(this, limit);
        this.objects.add(wall);
    }
    
    // New room
    public void newKillerRoom() {
        KillerPanel panel = new KillerPanel(this);
    }
    
    // New Viewer
    private void newViewer() {
        this.viewer = new Viewer(this);
        this.add(this.viewer, 0, 0);
        new Thread(this.viewer).start();
    }

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
    
    public VisualHandler getPrevModule() {
        return this.prevModule;
    }
    
    public VisualHandler getNextModule() {
        return this.nextModule;
    }
    
    public Viewer getViewer() {
        return this.viewer;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public ArrayList<VisibleObject> getObjects() {
        return this.objects;
    }
    
    // Methods set
    
    // Main activity
    public static void main(String[] args) {
        KillerGame game = new KillerGame();
        
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter IP");

        String userName = myObj.nextLine();  // Read user input
        System.out.println("IP to connect is: " + userName);  // Output user input

        System.out.println("Enter PORT");

        int port = Integer.parseInt(myObj.nextLine());  // Read user input
        System.out.println("IP to connect is: " + port);  // Output user input ¡
        
        game.getNextModule().setDestinationPort(port);
        game.getNextModule().setDestinationIp(userName);
        
    }

}