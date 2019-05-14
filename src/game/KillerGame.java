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
 * @author Alvaro & Christian
 */
public class KillerGame extends JFrame {

    public void addKillerShoot(KillerShip aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Game Attributes
    public enum Status {
        room, starting, game
    };

    private Status status;

    // Object list
    private ArrayList<VisibleObject> objects = new ArrayList<>();

    // KillerShip list | <ip, ship>
    private Hashtable<String, KillerShip> ships = new Hashtable();

    // Server
    private KillerServer server;

    // Visual Handlers
    private VisualHandler nextModule;
    private VisualHandler prevModule;

    /**
     * Communication Handlers Estas variables son usadas por el modulo de comunicaciones.
     */
    private int serversQuantity = 0;
    private boolean syncronized = false;

    // Gamepad List | <ip, pads>
    private Hashtable<String, KillerPad> pads = new Hashtable();

    // Viewer
    private Viewer viewer;

    // Constructors 
    public KillerGame() {

        // Set game status
        // this.status = Status.room;
        // Show window
        this.showWindow();

        // Open communications
        this.generateComunnications();

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
    /**
     * @author Christian & Alvaro
     * @param alive
     */
    public void checkColision(Alive alive) {
        for (int inc = 0; inc < this.objects.size(); inc++) {
            VisibleObject object = this.objects.get(inc);
            if (KillerPhysiscs.collision(alive, object)) {
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
    
    /**
     * @author Christian
    */
    private void generateUI() {
        this.showWindow();
        newKillerRoom();
    }

    /**
     * @author Christian
     */
    private void generateComunnications() {
        newPrevModule();
        newNextModule();
        newServer();
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
        KillerPad pad = new KillerPad(this, socket, user, color);
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

    /**
     *
     * Este metodo sirve para crear una nave cuando venga desde otra pantalla.
     *
     * @param x
     * @param y
     * @param radians
     * @param dx
     * @param dy
     * @param vx
     * @param vy
     * @param tx
     * @param ty
     * @param lx
     * @param ly
     * @param rx
     * @param ry
     * @param ip
     * @param user
     * @param type
     * @param health
     */
    public void reciveKillerShip(double x, double y, double radians, double dx, double dy, double vx, double vy, double tx, double ty, double lx, double ly, double rx, double ry, String ip, String user, KillerShip.ShipType type, int health) {
        KillerShip ship = new KillerShip(this, x, y, radians, dx, dy, vx, vy, tx, ty, lx, ly, rx, ry, ip, user, type, health);
        this.ships.put(ip, ship);
        this.objects.add(ship);
        new Thread(ship).start();
    }

    public void newShoot(KillerShip ship) {
        Shoot shoot = new Shoot(this, ship);
        this.objects.add(shoot);
        new Thread(shoot).start();
    }

    public void reciveShoot(double x, double y, double radians, double vx, double vy, String ip) {
        Shoot shoot = new Shoot(this, x, y, radians, vx, vy, ip);
        this.objects.add(shoot);
        new Thread(shoot).start();
    }

    public void newWall(Wall.Limit limit) {
        Wall wall = new Wall(this, limit);
        this.objects.add(wall);
    }

    // New room
    /**
     * @author Chirtsian
     */
    public void newKillerRoom() {
        KillerMenu menu = new KillerMenu(this);
        this.add(menu);
    }

    // New Viewer
    private void newViewer() {
        this.viewer = new Viewer(this);
        this.add(this.viewer, 0, 0);
        new Thread(this.viewer).start();
    }

    // Methods get
    
    // Methods get Objects
    public Controlled getShipByIP(String ip) {
        return this.ships.get(ip);
    }

    public ArrayList<VisibleObject> getObjects() {
        return this.objects;
    }

    // Methods get Communications
    public KillerServer getServer() {
        return this.server;
    }

    public VisualHandler getPrevModule() {
        return this.prevModule;
    }

    public VisualHandler getNextModule() {
        return this.nextModule;
    }

    public KillerPad getPadByIP(String ip) {
        return this.pads.get(ip);
    }

    public Hashtable<String, KillerPad> getPads() {
        return pads;
    }

    public int getServersQuantity() {
        return serversQuantity;
    }

    public boolean isSyncronized() {
        return syncronized;
    }

    // Get Viewer
    public Viewer getViewer() {
        return this.viewer;
    }

    public Status getStatus() {
        return this.status;
    }

    // Methods set Communications
    public void setPortPrev(int port) {
        this.prevModule.setDestinationPort(port);
    }

    public void setPortNext(int port) {
        this.nextModule.setDestinationPort(port);
    }

    public void setIpPrev(String ip) {
        this.prevModule.setDestinationIp(ip);
    }

    public void setIpNext(String ip) {
        this.prevModule.setDestinationIp(ip);
    }

    public void setServersQuantity(int serversQuantity) {
        this.serversQuantity = serversQuantity;
    }

    public void setSyncronized(boolean syncronized) {
        this.syncronized = syncronized;
    }

    // Main activity
    public static void main(String[] args) {

        // New KillerGame
        KillerGame game = new KillerGame();

        // Create a Scanner object
        Scanner scanner = new Scanner(System.in);

        // Read user input
        System.out.println("Enter IP. ");
        String ip = scanner.nextLine();
        System.out.println("IP to connect is: " + ip);

        // Read PORT
        System.out.println("Enter PORT. ");
        int port = Integer.parseInt(scanner.nextLine());
        System.out.println("IP to connect is: " + port);

        // Set to next module
        game.getNextModule().setDestinationPort(port);
        game.getNextModule().setDestinationIp(ip);

    }

}
