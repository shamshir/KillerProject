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
import visibleObjects.PowerUp.Power;

/**
 * @author Alvaro & Christian
 */
public class KillerGame extends JFrame {

    // ***************************************************************************************************** //
    // *************************** [         KillerGame Attributes       ] ********************************* //
    // ***************************************************************************************************** //

    // Game status
    public enum Status { ROOM, GAME };
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
    private int windowNumber = 0;
    private boolean synchro = false;

    // Gamepad List | <ip, pads>
    private Hashtable<String, KillerPad> pads = new Hashtable();

    // Number of pads in all screens
    private int padsNum;

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

        // Init radio
        this.newRadio();

        // Init sounds
        this.newSound();

        // Show room
        this.newRoom();

    }

    // ***************************************************************************************************** //
    // *************************** [          KillerGame Methods         ] ********************************* //
    // ***************************************************************************************************** //

    /**
     * @author Christian & Alvaro
     * @param alive
     */
    public synchronized void checkColision(Alive alive) {

        if (alive instanceof KillerShip) {
            for (int inc = 0; inc < this.objects.size(); inc++) {
                VisibleObject object = this.objects.get(inc);
                this.checkColisionShip((KillerShip) (alive), object);
            }
        }

        if (alive instanceof Shoot) {
            for (int inc = 0; inc < this.objects.size(); inc++) {
                VisibleObject object = this.objects.get(inc);
                this.checkCollisionShoot((Shoot) (alive), object);
            }
        }

        if (alive instanceof Asteroid) {
            for (int inc = 0; inc < this.objects.size(); inc++) {
                VisibleObject object = this.objects.get(inc);
                this.checkCollisionAsteroid((Asteroid) (alive), object);
            }
        }

        if (alive instanceof Pacman) {
            for (int inc = 0; inc < this.objects.size(); inc++) {
                VisibleObject object = this.objects.get(inc);
                this.checkCollisionPacman((Pacman) (alive), object);
            }
        }

    }

    /**
     * @author Alvaro
     * @param shoot
     * @param object
     */
    private void checkColisionShip(KillerShip ship, VisibleObject object) {

        // Collision with Asteroid
        if (object instanceof Asteroid && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Asteroid) object )) {
                KillerRules.collisionShipWithAsteroid(this, ship, (Asteroid) (object));
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (BlackHole) object )) {
                KillerRules.collisionShipWithBlackHole(this, ship);
            }
        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Nebulosa) object )) {
                KillerRules.collisionShipWithNebulosa(ship);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Pacman) object )) {
                KillerRules.collisionShipWithPacman(this, ship, (Pacman) (object));
            }
        }

        // Collision with Planeta
        if (object instanceof Planeta && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Pacman) object )) {
                KillerRules.collisionShipWithPlaneta(this, ship, (Planeta) (object));
            }
        }

        // Collision with PowerUp
        if (object instanceof PowerUp) {
            if (CollidePhysics.collisionTxC(ship, (PowerUp) object )) {
                KillerRules.collisionShipWithPowerUp(this, ship, (PowerUp) (object));
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip && !ship.equals((KillerShip) object)) {
            if (CollidePhysics.collisionTxT(ship, (KillerShip) object)) {
                KillerRules.collisionShipWithShip(ship, (KillerShip) (object));
            }
        }

        // Collision with Shot
        if (object instanceof Shoot) {
            if (CollidePhysics.collisionTxC(ship, (Shoot) object )) {
                KillerRules.collisionShipWithShoot(ship, (Shoot) (object));
            }
        }

        // Collision with Wall
        if (object instanceof Wall) {
            if (CollidePhysics.collisionObjxWall(ship, (Wall) object)) {
                KillerRules.collisionAliveWithWall(this, ship, (Wall) (object));
            }
        }

    }

    /**
     * @author Alvaro
     * @param shoot
     * @param object
     */
    private void checkCollisionShoot(Shoot shoot, VisibleObject object) {

        // Collision with Asteroid
        if (object instanceof Asteroid) {
            if (CollidePhysics.collisionCxC(shoot, (Asteroid) object )) {
                KillerRules.collisionShootWithAsteroid(this, shoot, (Asteroid) (object));
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {
            if (CollidePhysics.collisionCxC(shoot, (BlackHole) object )) {
                KillerRules.collisionShootWithBlackHole(this, shoot, (BlackHole) (object));
            }
        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa) {
            if (CollidePhysics.collisionCxC(shoot, (Nebulosa) object )) {
                KillerRules.collisionShootWithNebulosa(shoot);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman) {
            if (CollidePhysics.collisionCxC(shoot, (Pacman) object )) {
                KillerRules.collisionShootWithPacman(this, shoot, (Pacman) (object));
            }
        }

        // Collision with Planeta
        if (object instanceof Planeta) {
            if (CollidePhysics.collisionCxC(shoot, (Planeta) object )) {
                KillerRules.collisionShootWithPlaneta(this, shoot, (Planeta) (object));
            }
        }

        // Collision with PowerUp
        if (object instanceof PowerUp) {
            if (CollidePhysics.collisionCxC(shoot, (PowerUp) object )) {
                KillerRules.collisionShootWithPowerUp(this, shoot, (PowerUp) (object));
            }
        }

        // Collision with Shot
        if (object instanceof Shoot && !shoot.equals((Shoot) object)) {
            if (CollidePhysics.collisionCxC(shoot, (Shoot) object)) {
                KillerRules.collisionShootWithShoot(this, shoot, (Shoot) (object));
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip) {
            if (CollidePhysics.collisionTxC((KillerShip) object, shoot)) {
                KillerRules.collisionShipWithShoot((KillerShip) (object), shoot);
            }
        }

        // Collision with Wall
        if (object instanceof Wall) {
            if (CollidePhysics.collisionObjxWall(shoot, (Wall) object)) {
                KillerRules.collisionShootWithWall(this, shoot, (Wall) (object));
            }
        }

    }

    /**
     * @author Alvaro
     * @param asteroid
     * @param object
     */
    private void checkCollisionAsteroid(Asteroid asteroid, VisibleObject object) {

        // Collision with Asteroid
        if (object instanceof Asteroid && !asteroid.equals(object)) {
            if (CollidePhysics.collisionCxC(asteroid , (Asteroid) object)) {
                KillerRules.collisionAsteroidWithAsteroid(this, asteroid, (Asteroid) (object));
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {
            if (CollidePhysics.collisionCxC(asteroid, (BlackHole) object)) {
                KillerRules.collisionAsteroidWithBlackHole(this, asteroid, (BlackHole) (object));
            }
        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa) {
            if (CollidePhysics.collisionCxC(asteroid, (Nebulosa) object)) {
                KillerRules.collisionAsteroidWithNebulosa(asteroid);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman) {
            if (CollidePhysics.collisionCxC(asteroid, (Pacman) object)) {
                KillerRules.collisionAsteroidWithPacman(this, asteroid, (Pacman) (object));
            }
        }

        // Collision with Planeta
        if (object instanceof Planeta) {
            if (CollidePhysics.collisionCxC(asteroid, (Planeta) object)) {
                KillerRules.collisionAsteroidWithPlaneta(asteroid);
            }
        }

        // Collision with PowerUp
        if (object instanceof PowerUp) {
            if (CollidePhysics.collisionCxC(asteroid, (PowerUp) object)) {
                KillerRules.collisionAsteroidWithPowerUp(this, asteroid, (PowerUp) (object));
            }
        }

        // Collision with Shot
        if (object instanceof Shoot) {
            if (CollidePhysics.collisionCxC(asteroid, (Shoot) object)) {
                KillerRules.collisionShootWithAsteroid(this, (Shoot) (object), asteroid);
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip) {
            if (CollidePhysics.collisionTxC((KillerShip) object, asteroid)) {
                KillerRules.collisionShipWithAsteroid(this, (KillerShip) (object), asteroid);
            }
        }

        // Collision with Wall
        if (object instanceof Wall) {
            if (CollidePhysics.collisionObjxWall(asteroid, (Wall) object)) {
                KillerRules.collisionAliveWithWall(this, asteroid, (Wall) (object));
            }
        }

    }

    /**
     * @author Alvaro
     * @param pacman
     * @param object
     */
    private void checkCollisionPacman(Pacman pacman, VisibleObject object) {

        // Collision with Asteroid
        if (object instanceof Asteroid) {
            if (CollidePhysics.collisionCxC(pacman, (Asteroid) object)) {
                KillerRules.collisionAsteroidWithPacman(this, (Asteroid) (object), pacman);
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {
            if (CollidePhysics.collisionCxC(pacman, (BlackHole) object)) {
                KillerRules.collisionPacmanWithBlackHole(pacman);
            }
        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa) {
            if (CollidePhysics.collisionCxC(pacman, (Nebulosa) object)) {
                KillerRules.collisionPacmanWithNebulosa(pacman);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman && !pacman.equals(object)) {
            if (CollidePhysics.collisionCxC(pacman, (Pacman) object)) {
                KillerRules.collisionPacmanWithPacman(this, pacman, (Pacman) (object));
            }
        }

        // Collision with Planeta
        if (object instanceof Planeta) {
            if (CollidePhysics.collisionCxC(pacman, (Planeta) object)) {
                KillerRules.collisionPacmanWithPlaneta(pacman);
            }
        }

        // Collision with PowerUp
        if (object instanceof PowerUp) {
            if (CollidePhysics.collisionCxC(pacman, (PowerUp) object)) {
                KillerRules.collisionPacmanWithPowerUp(this, pacman, (PowerUp) (object));
            }
        }

        // Collision with Shot
        if (object instanceof Shoot) {
            if (CollidePhysics.collisionCxC(pacman, (Shoot) object)) {
                KillerRules.collisionShootWithPacman(this, (Shoot) (object), pacman);
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip) {
            if (CollidePhysics.collisionCxC((KillerShip) object, pacman)) {
                KillerRules.collisionShipWithPacman(this, (KillerShip) (object), pacman);
            }
        }

        // Collision with Wall
        if (object instanceof Wall) {
            if (CollidePhysics.collisionObjxWall(pacman, (Wall) object)) {
                KillerRules.collisionAliveWithWall(this, pacman, (Wall) (object));
            }
        }

    }

    /**
     * @author Alvaro
     */
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
        this.addWalls();

        // Start threads
        this.startThreads();

        // Start music
        this.changeMusic(KillerRadio.ClipType.BATTLE);

    }

    /**
     * @author Alvaro
     */
    public void startThreads() {
        for (VisibleObject object : this.objects) {
            if (object instanceof Alive) {
                new Thread((Alive) object).start();
            }
        }
    }

    /**
     * @author Alvaro
     */
    public void addObjects() {

        // Añadir Objetos de Prueba
        this.objects.add(new Planeta(this, 300, 400, 100, 100));
        this.objects.add(new Nebulosa(this, 400, 150, 120, 90));
        this.objects.add(new BlackHole(this, 350, 500, 80, 80));
        this.objects.add(new PowerUp(this, 100, 500, 70, 70, Power.HEALTH));
        Asteroid a = new Asteroid(this, 75, 100, 40, 40, 6, 2);
        this.objects.add(a);
        Pacman p = new Pacman(this, 100, 450);
        this.objects.add(p);

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
        this.newWall(Wall.Limit.RIGHT);
        this.newWall(Wall.Limit.UP);
        this.newWall(Wall.Limit.DOWN);
        this.newWall(Wall.Limit.LEFT);
    }

    /**
     * @author Alvaro
     */
    private void showWindow() {
        // this.setSize(1120, 630);
        this.setSize(1500, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout());
        this.setResizable(false);
        //this.setUndecorated(true);
        this.setVisible(true);
    }

    // ***************************************************************************************************** //
    // *************************** [            Sound Methods           ] ********************************** //
    // ***************************************************************************************************** //

    /**
     * @author Alvaro
     * @param clip
     */
    public void changeMusic(KillerRadio.ClipType clip) {
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
     * @param x
     * @param y
     * @param imgHeight
     * @param m
     * @param health
     * @param maxspeed
     */
    public void newAsteroid(double x, double y, int imgHeight, double m, int health, double maxspeed) {
        Asteroid asteroid = new Asteroid(this, x, y, imgHeight, m, health, maxspeed);
        this.objects.add(asteroid);
    }

    /**
     * @author Alvaro
     * @param x
     * @param y
     */
    public void newPacman(double x, double y) {
        Pacman pacman = new Pacman(this, x, y);
        this.objects.add(pacman);
    }

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
        new Thread(this.radio).start();
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
        KillerShip ship = new KillerShip(this, 150, 150, ip, user, type, color);
        this.ships.put(ip, ship);
        this.objects.add(ship);
    }

    /**
     * @author Christian
     */
    public void newSound() {
        this.sound = new KillerSound();
        new Thread(this.sound).start();
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
    public void reciveShip(double x, double y, double radians, double dx, double dy, double vx, double vy, double tx, double ty, double lx, double ly, double rx, double ry, String ip, String user, KillerShip.ShipType type, int health, int damage, Color color) {
        KillerShip ship = new KillerShip(this, x, y, radians, dx, dy, vx, vy, tx, ty, lx, ly, rx, ry, ip, user, type, health, damage, color);
        int correctX = 1;
        if (dx < 0) {
            correctX = this.viewer.getWidth() - ship.getImgWidth() - 1;
        }
        ship.setX(correctX);
        this.ships.put(ip, ship);
        this.objects.add(ship);
        new Thread(ship).start();
    }

    /**
     * Este metodo sirve para crear una bala cuando venga desde otra pantalla. // Este metodo no es utilizado
     * @author Alvaro
     */
    public void reciveShoot(double x, double y, double radians, double vx, double vy, String ip, int damage) {
        Shoot shoot = new Shoot(this, x, y, radians, vx, vy, ip, damage, 1, Alive.State.ALIVE);
        this.objects.add(shoot);
        new Thread(shoot).start();
    }

    /**
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
     * @param id
     * @param user
     * @param type
     * @param health
     * @param damage
     * @param color
     */
    public void reciveAsteroid(double x, double y, int imgHeight, double m, int health, double radians, double vx, double vy, double a) {
        Asteroid asteroid = new Asteroid(this, x, y, imgHeight, m, health, radians, vx, vy, a);
        int correctX = 0;
        if (vx < 0) {
            correctX = this.viewer.getWidth() - asteroid.getImgWidth() - 1;
        }
        this.objects.add(asteroid);
        new Thread(asteroid).start();
    }

    /**
     * @author Christian
     * @param x
     * @param y
     * @param m
     * @param health
     * @param radians
     * @param vx
     * @param vy
     * @param a 
     */
    public void recivePacman(double x, double y, double m, int health, double radians, double vx, double vy, double a) {
        Pacman pacman = new Pacman(this, x, y, m, health, radians, vx, vy, a);
        int correctX = 0;
        if (vx < 0) {
            correctX = this.viewer.getWidth() - pacman.getImgWidth() - 1;
        }
        this.objects.add(pacman);
        new Thread(pacman).start();
    }

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

    public int getPadsNum(){
        return this.padsNum;
    }

    public int getPadsSize(){
       return this.pads.size();
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

    public int getWindowNumber() {
        return windowNumber;
    }

    public boolean isSynchronized() {
        return synchro;
    }
    
    public KillerPad getLastPad() {
       if (this.pads.size() == 1) {
            return this.pads.entrySet().iterator().next().getValue();
        }
       return null;
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

    public void setPadsNum(int padsSize){
        this.padsNum = padsSize;
    }

    public void setPortPrev(int port) {
        this.prevModule.setDestinationPort(port);
    }

    public void setPortNext(int port) {
        this.nextModule.setDestinationPort(port);
    }

    public void decrementPadsNum(){
        this.padsNum--;
        if (this.padsNum == 1) {
            this.nextModule.sendMessage(Message.Builder.builder("win", KillerServer.getId()).build());
        }
    }

    public void setServersQuantity(int serversQuantity) {
        this.serversQuantity = serversQuantity;
    }

    public void setSyncronized(boolean synchro) {
        this.synchro = synchro;
        this.room.getKPP().setButtonPlay(synchro);
    }

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

    // ***************************************************************************************************** //
    // *************************** [            Methods Remove           ] ********************************* //
    // ***************************************************************************************************** //

    /**
     * @author Alvaro
     * @param pad
     */
    public void removePad(KillerPad pad) {
        this.pads.remove(pad.getId());
        this.objects.remove(this.getShipByIP(pad.getId()));
        this.ships.remove(pad.getId());
    }

    /**
     * @author Christian
     * @param object
     */
    public void removeObject(VisibleObject object) {
        this.objects.remove(object);
        if (object instanceof KillerShip) {
            this.ships.remove(((KillerShip) object).getId());
        }
    }

    // ***************************************************************************************************** //
    // *************************** [             Main Activity           ] ********************************* //
    // ***************************************************************************************************** //
    public static void main(String[] args) {

        // New KillerGame
        KillerGame game = new KillerGame();

    }

}
