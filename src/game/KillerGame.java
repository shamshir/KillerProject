package game;

// Import Killer Game pakages
import visualEffects.*;
import visibleObjects.*;
import communications.*;
import gameRoom.*;
import sound.*;
// Other imports
import java.awt.Color;
import java.awt.GridLayout;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Hashtable;
import javax.sound.sampled.Clip;
import physics.CollidePhysics;
import visibleObjects.Automata.AutonomousState;

/**
 * @author Alvaro & Christian
 */
public class KillerGame extends JFrame {

    // ***************************************************************************************************** //
    // *************************** [         KillerGame Attributes       ] ********************************* //
    // ***************************************************************************************************** //
    // Game Attributes
    public enum Status {
        ROOM,
        GAME
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
    private int serversQuantity = 0;
    private boolean synchro = false;

    // Gamepad List | <ip, pads>
    private Hashtable<String, KillerPad> pads = new Hashtable();

    // Viewer
    private Viewer viewer;

    // Room
    private KillerRoom room;

    // Radio
    private KillerRadio radio;

    // Sounds
    private KillerSound sound;

    // ***************************************************************************************************** //
    // *************************** [        KillerGame Constructors      ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Alvaro
     */
    public KillerGame() {

        // Open communications
        this.generateComunnications();

        // Set game status
        this.status = Status.ROOM;

        // Show room
        this.newRoom();

        this.addVisibleObjects();

    }

    public void addVisibleObjects() {

        Asteroid a = new Asteroid(this, 200, 40, 40, 20, 20, 20);
        new Thread(a).start();

        new Thread(a.getKillerImage()).start();

        this.objects.add(a);
    }

    // ***************************************************************************************************** //
    // *************************** [          KillerGame Methods         ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Christian & Alvaro
     * @param alive
     */
    public void checkColision(Alive alive) {
        for (int inc = 0; inc < this.objects.size(); inc++) {
            VisibleObject object = this.objects.get(inc);

            if (alive instanceof KillerShip) {

            }

            if (alive instanceof Shoot) {

            }

            if (alive instanceof Asteroid) {

            }

            if (alive instanceof Pacman) {

            }

        }
    }

    private void checkColisionShip(Alive alive, VisibleObject object) {

        // Collision with Asteroid
        if (object instanceof Asteroid) {

        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {

        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa) {

        }

        // Collision with Pacman
        if (object instanceof Pacman) {

        }

        // Collision with Planeta
        if (object instanceof Planeta) {

        }

        // Collision with PowerUp
        if (object instanceof PowerUp) {

        }

        // Collision with Ship
        if (object instanceof KillerShip) {

        }

        // Collision with Shot
        if (object instanceof Shoot) {

        }

        // Collision with Wall
        if (object instanceof Wall) {

        }

    }

    /**
     * @author Christian
     * @param object
     */
    public void removeObject(VisibleObject object) {
        try {
            this.objects.remove(this);
        } catch (Exception e) {
            System.out.println("Este objecto no se encuentra en la array");
        }
    }

    public void startGame() {

        // Hide room
        this.room.setVisible(false);

        // Change Status
        this.status = KillerGame.Status.GAME;

        // Mostrar ventana
        this.showWindow();

        // Add Viewer
        this.newViewer();

        // Add walls
        addWalls();

    }

    // ***************************************************************************************************** //
    // *************************** [       Communication Methods         ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Christian
     */
    private void generateComunnications() {
        newPrevModule();
        newNextModule();
        newServer();
    }

    /**
     * @author Alvaro
     */
    public void sendMessageToPrev(Message message) {
        this.prevModule.sendMessage(message);
    }

    /**
     * @author Alvaro
     */
    public void sendMessageToNext(Message message) {
        this.nextModule.sendMessage(message);
    }

    /**
     * @author Alvaro
     */
    public void sendObjectToPrev(Alive object) {
        this.prevModule.sendObject(object);
    }

    /**
     * @author Alvaro
     */
    public void sendObjectToNext(Alive object) {
        this.nextModule.sendObject(object);
    }

    /**
     * @author Alvaro
     */
    public void sendStart() {
        if (this.status == Status.ROOM) {
            this.nextModule.sendStart();
            this.status = Status.GAME;
        }
    }

    // ***************************************************************************************************** //
    // *************************** [            Window Methods           ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Christian
     */
    private void addWalls() {
        // Add walls
        this.newWall(Wall.Limit.EAST);
        this.newWall(Wall.Limit.NORTH);
        this.newWall(Wall.Limit.SOUTH);
        this.newWall(Wall.Limit.WEST);
    }

    /**
     * @author Alvaro
     */
    private void showWindow() {
        this.setSize(1120, 630);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout());
        this.setResizable(false);
        this.setUndecorated(true);
        this.setVisible(true);
    }

    // ***************************************************************************************************** //
    // *************************** [            Sound Methods           ] ********************************** //
    // ***************************************************************************************************** //
    /**
     * @author Alvaro
     * @param clip
     */
    public void startMusic(KillerRadio.ClipType clip) {
        this.newRadio();
        this.radio.setClip(clip);
    }

    /**
     * @author Alvaro
     */
    public void stopMusic() {
        this.radio.stopSound();
    }

    /**
     * @author Alvaro
     * @param clip
     */
    public void changeMusic(KillerRadio.ClipType clip) {
        this.radio.setClip(clip);
    }

    /**
     * @author Alvaro
     * @param clip
     */
    public void startSound(KillerSound.ClipType clip) {
        this.sound.addSound(sound.createSound(clip));
    }

    /**
     * @author Alvaro
     * @param clip
     */
    public void stopSound(Clip clip) {
        this.sound.stopSound(clip);
    }

    // ***************************************************************************************************** //
    // *************************** [             Methods New             ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Alvaro
     * @param ip
     * @param socket
     * @param user
     * @param color
     * @return
     */
    public boolean newPad(String ip, Socket socket, String user, String color) {
        boolean result = false;
        if (this.status == Status.ROOM) {
            KillerPad pad = new KillerPad(this, socket, user, color);
            this.pads.put(ip, pad);
            new Thread(pad).start();
            result = true;
        }
        return result;
    }

    /**
     * @author Chirtsian
     */
    private void newRadio() {
        this.radio = new KillerRadio();
    }

    /**
     * @author Chirtsian
     */
    private void newRoom() {
        this.room = new KillerRoom(this);
        this.room.setVisible(true);
    }

    /**
     * @author Alvaro
     */
    public void newShip(String ip, Color color, String user, KillerShip.ShipType type) {
        KillerShip ship = new KillerShip(this, 150, 150, ip, user, type);
        this.ships.put(ip, ship);
        this.objects.add(ship);
        new Thread(ship).start();
    }

    /**
     * @author Christian
     */
    public void newSound() {
        this.sound = new KillerSound();
    }

    /**
     * @author Alvaro
     */
    private void newPrevModule() {
        this.prevModule = new VisualHandler(this, false);
        new Thread(this.prevModule).start();
    }

    /**
     * @author Alvaro
     */
    private void newNextModule() {
        this.nextModule = new VisualHandler(this, true);
        new Thread(this.nextModule).start();
    }

    /**
     * @author Alvaro
     */
    private void newServer() {
        try {
            this.server = new KillerServer(this, 8000);
            new Thread(this.server).start();
        } catch (Exception exception) {
            System.out.println("No ha sido posible encender el servidor debido a la siguiente excepcion : " + exception);
        }
    }

    /**
     * @author Alvaro
     * @param ship
     */
    public void newShoot(KillerShip ship) {
        Shoot shoot = new Shoot(this, ship);
        this.objects.add(shoot);
        new Thread(shoot).start();
    }

    /**
     * Este metodo sirve para crear una nave cuando venga desde otra pantalla.
     *
     * @author Alvaro
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
    public void reciveShip(double x, double y, double radians, double dx, double dy, double vx, double vy, double tx, double ty, double lx, double ly, double rx, double ry, String ip, String user, KillerShip.ShipType type, int health, int damage) {
        KillerShip ship = new KillerShip(this, x, y, radians, dx, dy, vx, vy, tx, ty, lx, ly, rx, ry, ip, user, type, health, damage);
        this.ships.put(ip, ship);
        this.objects.add(ship);
        new Thread(ship).start();
    }

    /**
     * @author Alvaro
     */
    public void reciveShoot(double x, double y, double radians, double vx, double vy, String ip, int damage) {
        Shoot shoot = new Shoot(this, x, y, radians, vx, vy, ip, damage, 1, AutonomousState.ALIVE);
        this.objects.add(shoot);
        new Thread(shoot).start();
    }

    // New Viewer
    /**
     * @author Alvaro
     */
    private void newViewer() {
        this.viewer = new Viewer(this);
        this.add(this.viewer, 0, 0);
        new Thread(this.viewer).start();
    }

    /**
     * @author Alvaro
     */
    private void newWall(Wall.Limit limit) {
        Wall wall = new Wall(this, limit);
        this.objects.add(wall);
    }

    // ***************************************************************************************************** //
    // *************************** [             Methods Get             ] ********************************* //
    // ***************************************************************************************************** //
    public VisualHandler getNextModule() {
        return this.nextModule;
    }

    public ArrayList<VisibleObject> getObjects() {
        return this.objects;
    }

    public VisualHandler getPrevModule() {
        return this.prevModule;
    }

    public Hashtable<String, KillerPad> getPads() {
        return pads;
    }

    public KillerPad getPadByIP(String ip) {
        return this.pads.get(ip);
    }

    public KillerRoom getRoom() {
        return room;
    }

    public KillerServer getServer() {
        return this.server;
    }

    public int getServersQuantity() {
        return serversQuantity;
    }

    public Hashtable<String, KillerShip> getShips() {
        return this.ships;
    }

    public KillerShip getShipByIP(String ip) {
        return this.ships.get(ip);
    }

    public Status getStatus() {
        return this.status;
    }

    public Viewer getViewer() {
        return this.viewer;
    }

    public boolean isSynchronized() {
        return synchro;
    }

    // ***************************************************************************************************** //
    // *************************** [              Methods Set            ] ********************************* //
    // ***************************************************************************************************** //
    public void setIpPrev(String ip) {
        this.prevModule.setDestinationIp(ip);
    }

    public void setIpNext(String ip) {
        this.nextModule.setDestinationIp(ip);
    }

    public void setPortPrev(int port) {
        this.prevModule.setDestinationPort(port);
    }

    public void setPortNext(int port) {
        this.nextModule.setDestinationPort(port);
    }

    public void setServersQuantity(int serversQuantity) {
        this.serversQuantity = serversQuantity;
    }

    public void setSyncronized(boolean synchro) {
        this.synchro = synchro;
        this.room.getKPP().setButtonPlay(synchro);
    }

    // ***************************************************************************************************** //
    // *************************** [             Main Activity           ] ********************************* //
    // ***************************************************************************************************** //
    public static void main(String[] args) {

        // New KillerGame
        KillerGame game = new KillerGame();

    }

}
