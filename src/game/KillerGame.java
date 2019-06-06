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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
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
    public enum Status {
        ROOM, GAME
    };
    private Status status;

    // Game configurations
    private boolean soundMusic = true;
    private boolean soundEffects = true;
    private boolean pacmanActive = true;
    private boolean ultraPacman = false;
    public static boolean exit = false;
    public static int worldType = 0;
    public static final int VIEWER_WIDTH = 1920;
    public static final int VIEWER_HEIGHT = 1080;

    // Gen objects
    private int objectMatrixWidth[][] = new int[KillerRules.OBJECT_GRID_WIDTH][KillerRules.OBJECT_GRID_HEIGHT];
    private int objectMatrixHeight[][] = new int[KillerRules.OBJECT_GRID_WIDTH][KillerRules.OBJECT_GRID_HEIGHT];
    private int nebulaMatrixWidth[][] = new int[KillerRules.OBJECT_GRID_WIDTH][KillerRules.OBJECT_GRID_HEIGHT];
    private int nebulaMatrixHeight[][] = new int[KillerRules.OBJECT_GRID_WIDTH][KillerRules.OBJECT_GRID_HEIGHT];

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

        // Add key listener
        addKeyEventListener();

        // Set game status
        this.status = Status.ROOM;

        // Init radio
        this.newRadio();

        // Init sounds
        this.newSound();

        // Show room
        this.newRoom();

        // Open communications
        this.generateComunnications();

    }

    // ***************************************************************************************************** //
    // *************************** [          KillerGame Methods         ] ********************************* //
    // ***************************************************************************************************** //
    /**
     * @author Christian & Alvaro
     */
    private void addKeyEventListener() {

        // Key listener
        KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {

            // Attributes
            private int exitCounter = 0;
            KillerGame game;

            @Override
            public boolean dispatchKeyEvent(final KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    exitCounter++;
                } else {
                    exitCounter = 0;
                }
                if (exitCounter > 1) {
                    KillerGame.exit();
                }
                return false;
            }

        };

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);

    }

    /**
     * @author Christian & Alvaro
     * @param alive
     */
    public synchronized void checkColision(Alive alive) {

        if (alive instanceof KillerShip && alive.getState() == Alive.State.ALIVE) {
            for (int inc = 0; inc < this.objects.size(); inc++) {
                VisibleObject object = this.objects.get(inc);
                this.checkColisionShip((KillerShip) (alive), object);
            }
        }

        if (alive instanceof Shoot && alive.getState() == Alive.State.ALIVE) {
            for (int inc = 0; inc < this.objects.size(); inc++) {
                VisibleObject object = this.objects.get(inc);
                this.checkCollisionShoot((Shoot) (alive), object);
            }
        }

        if (alive instanceof Asteroid && alive.getState() == Alive.State.ALIVE) {
            for (int inc = 0; inc < this.objects.size(); inc++) {
                VisibleObject object = this.objects.get(inc);
                this.checkCollisionAsteroid((Asteroid) (alive), object);
            }
        }

        if (alive instanceof Pacman && alive.getState() == Alive.State.ALIVE) {
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
        if (object instanceof Asteroid && ((Asteroid) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Asteroid) object)) {
                KillerRules.collisionShipWithAsteroid(this, ship, (Asteroid) (object));
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {
            if (CollidePhysics.collisionTxC(ship, (BlackHole) object)) {
                KillerRules.collisionShipWithBlackHole(this, ship);
            }
        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa) {
            if (CollidePhysics.collisionTxC(ship, (Nebulosa) object)) {
                KillerRules.collisionShipWithNebulosa(ship);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman && ((Pacman) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Pacman) object)) {
                KillerRules.collisionShipWithPacman(this, ship, (Pacman) (object));
            }
        }

        // Collision with Planeta
        if (object instanceof Planeta) {
            if (CollidePhysics.collisionTxC(ship, (Planeta) object)) {
                KillerRules.collisionShipWithPlaneta(this, ship, (Planeta) (object));
            }
        }

        // Collision with PowerUp
        if (object instanceof PowerUp) {
            if (CollidePhysics.collisionTxC(ship, (PowerUp) object)) {
                KillerRules.collisionShipWithPowerUp(this, ship, (PowerUp) (object));
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip && !ship.equals((KillerShip) object) && ((KillerShip) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxT(ship, (KillerShip) object)) {
                KillerRules.collisionShipWithShip(this, ship, (KillerShip) (object));
            }
        }

        // Collision with Shot
        if (object instanceof Shoot && ((Shoot) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Shoot) object)) {
                KillerRules.collisionShipWithShoot(this, ship, (Shoot) (object));
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
        if (object instanceof Asteroid && ((Asteroid) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC(shoot, (Asteroid) object)) {
                KillerRules.collisionShootWithAsteroid(this, shoot, (Asteroid) (object));
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {
            if (CollidePhysics.collisionCxC(shoot, (BlackHole) object)) {
                KillerRules.collisionShootWithBlackHole(shoot);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman && ((Pacman) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC(shoot, (Pacman) object)) {
                KillerRules.collisionShootWithPacman(this, shoot, (Pacman) (object));
            }
        }

        // Collision with Planeta
        if (object instanceof Planeta) {
            if (CollidePhysics.collisionCxC(shoot, (Planeta) object)) {
                KillerRules.collisionShootWithPlaneta(this, shoot, (Planeta) (object));
            }
        }

        // Collision with PowerUp
        if (object instanceof PowerUp) {
            if (CollidePhysics.collisionCxC(shoot, (PowerUp) object)) {
                KillerRules.collisionShootWithPowerUp(this, shoot, (PowerUp) (object));
            }
        }

        // Collision with Shot
        if (object instanceof Shoot && !shoot.equals((Shoot) object) && ((Shoot) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC(shoot, (Shoot) object)) {
                KillerRules.collisionShootWithShoot(this, shoot, (Shoot) (object));
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip && ((KillerShip) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC((KillerShip) object, shoot)) {
                KillerRules.collisionShipWithShoot(this, (KillerShip) (object), shoot);
            }
        }

        // Collision with Wall
        if (object instanceof Wall) {
            if (CollidePhysics.collisionObjxWall(shoot, (Wall) object)) {
                KillerRules.collisionShootWithWall(shoot);
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
        if (object instanceof Asteroid && !asteroid.equals(object) && ((Asteroid) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC(asteroid, (Asteroid) object)) {
                KillerRules.collisionAsteroidWithAsteroid(asteroid, (Asteroid) (object));
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {
            if (CollidePhysics.collisionCxC(asteroid, (BlackHole) object)) {
                KillerRules.collisionAsteroidWithBlackHole(this, asteroid);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman && ((Pacman) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC(asteroid, (Pacman) object)) {
                KillerRules.collisionAsteroidWithPacman(this, asteroid, (Pacman) (object));
            }
        }

        // Collision with Planeta
        if (object instanceof Planeta) {
            if (CollidePhysics.collisionCxC(asteroid, (Planeta) object)) {
                KillerRules.collisionAsteroidWithPlaneta(asteroid, (Planeta) object);
            }
        }

        // Collision with Shot
        if (object instanceof Shoot && ((Shoot) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC(asteroid, (Shoot) object)) {
                KillerRules.collisionShootWithAsteroid(this, (Shoot) (object), asteroid);
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip && ((KillerShip) object).getState() == Alive.State.ALIVE) {
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
        if (object instanceof Asteroid && ((Asteroid) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC(pacman, (Asteroid) object)) {
                KillerRules.collisionAsteroidWithPacman(this, (Asteroid) (object), pacman);
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {
            if (CollidePhysics.collisionCxC(pacman, (BlackHole) object)) {
                KillerRules.collisionPacmanWithBlackHole(this, pacman);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman && !pacman.equals(object) && ((Pacman) object).getState() == Alive.State.ALIVE) {
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
        if (object instanceof Shoot && ((Shoot) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC(pacman, (Shoot) object)) {
                KillerRules.collisionShootWithPacman(this, (Shoot) (object), pacman);
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip && ((KillerShip) object).getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionCxC((KillerShip) object, pacman)) {
                KillerRules.collisionShipWithPacman(this, (KillerShip) (object), pacman);
            }
        }

        // Collision with Wall
        if (object instanceof Wall) {
            if (CollidePhysics.collisionObjxWall(pacman, (Wall) object)) {
                KillerRules.collisionPacmanWithWall(this, pacman, (Wall) (object));
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

        this.addObjects(this.worldType);

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

    public void restartGame() {
        this.viewer.stop();
        this.remove(this.viewer);
        this.viewer = null;
        this.objects = new ArrayList<>();
        this.ships = new Hashtable();
        this.pads = new Hashtable();
        this.status = KillerGame.Status.ROOM;
        this.stopMusic();
        this.changeMusic(KillerRadio.ClipType.MENU);
        this.room.setVisible(true);
    }

    public void setWinner(String name) {

        this.room.setKillerPanelWinner(name);
        this.setVisible(false);
        this.room.setVisible(true);

        Enumeration<KillerPad> enumeration = this.pads.elements();
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement().closeSocket();
        }

        for (VisibleObject object : this.objects) {
            if (object instanceof Alive) {
                ((Alive) object).setState(Alive.State.DEAD);
            }
            if (object instanceof BlackHole) {
                ((BlackHole) object).setAlive(false);
            }
        }

    }

    // ***************************************************************************************************** //
    // *************************** [             Add Methods             ] ********************************* //
    // ***************************************************************************************************** //
    
    /**
     * @author Christian
     * @param number 
     */
    public void addObjects(int number) {
        switch (number) {
            case 1:
                this.BasicWorld();
                break;
            case 2:
                this.AsteroidWorld();
                break;
            case 3:
                this.PacmanWorld();
                break;
            case 4:
                this.ClearedWorld();
                break;
            case 5:
                this.TpWorld();
                break;
            case 6:
                this.PowerUpsWorld();
                break;
            case 7:
                this.PlanetWorld();
                break;
            case 8:
                this.addObjects((int) (Math.random() * (7 - 0) + 1));
                break;
            default:
                System.out.println("�No has seleccionado un mundo valido! Se pondr� por defecto el mundo b�sico");
                this.BasicWorld();
                break;
        }
    }
    
    /**
     * @author Christian
     */
    public void BasicWorld() {
        this.newGirdPositions();
        this.newGirdNebulaPositions();
        this.addNebulas(genNebulosas());
        this.addPlanets(genPlanets());
        this.addAsteroids(genAsteroids());
        this.addPacmans(genPacmans());
        this.addBlackHoles(genBlackHoles());
        this.addPowerUps(genPowerUps());
    }
    
    /**
     * @author Christian
     */
    public void AsteroidWorld() {
        this.newGirdPositions();
        this.addPlanets(3);
        this.addAsteroids(15);
        this.addPowerUps(3);
    }
    
    /**
     * @author Christian
     */
    public void PacmanWorld() {
        this.newGirdPositions();
        this.addPlanets(3);
        this.addPacmans(15);
        this.addPowerUps(3);
    }
    
    /**
     * @author Christian
     */
    public void TpWorld() {
        this.newGirdPositions();
        this.addPlanets(3);
        this.addBlackHoles(10);
    }
    
    /**
     * @author Christian
     */
    public void PowerUpsWorld() {
        this.newGirdPositions();
        this.addPowerUps(15);
    }
    
    /**
     * @author Christian
     */
    public void PlanetWorld() {
        this.newGirdPositions();
        this.addPlanets(20);
    }
    
    /**
     * @author Christian
     */
    public void ClearedWorld() {
        this.newGirdNebulaPositions();
        this.addNebulas(4);
    }
    
    /**
     * @author Christian
     * @param quantity 
     */
    private void addPlanets(int quantity) {
        for (int i = 0; i < quantity; i++) {
            // Get Planet values
            int[] values = selectPosition();
            int x = values[0];
            int y = values[1];
            int height = values[4];
            int randomX = values[6] / 4;
            int randomY = values[7] / 4;
            // New asteroid and set values
            this.objects.add(new Planeta(this, x + randomX, y + randomY, height));
        }
    }
    
    /**
     * @author Christian
     * @param quantity 
     */
    private void addAsteroids(int quantity) {
        for (int i = 0; i < quantity; i++) {
            // Get Asteroid values
            int[] values = selectPosition();
            int x = values[0];
            int y = values[1];
            int vx = values[2];
            int vy = values[3];
            int randomX = values[6];
            int randomY = values[7];
            int randomHeight = values[8];
            // New asteroid and set values                                  random height 40  range (30-(60-70))
            Asteroid asteroid = new Asteroid(this, x + randomX, y + randomY, randomHeight, KillerRules.ASTEROID_HEATLTH, 2);
            asteroid.setVx(vx);
            asteroid.setVy(vy);
            this.objects.add(asteroid);
        }
    }
    
    /**
     * @author Christian
     * @param quantity 
     */
    private void addBlackHoles(int quantity) {
        for (int i = 0; i < quantity; i++) {
            // Get Black Hole values
            int[] values = selectPosition();
            int x = values[0];
            int y = values[1];
            int randomX = values[6];
            int randomY = values[7];
            // New Black Hole and set values
            this.newBlackHole(x, y);
        }
    }
    
    /**
     * @author Christian
     * @param quantity 
     */
    private void addNebulas(int quantity) {
        for (int i = 0; i < quantity; i++) {
            // Get Nebulosa values
            int[] values = selectPositionNebulosas();
            int x = values[0];
            int y = values[1];
            int randomX = values[2];
            int randomY = values[3];
            // New Nebulosa
            this.objects.add(new Nebulosa(this, x + (randomX), y + (randomY), 720, 440));
        }
    }
    
    /**
     * @author Christian
     * @param quantity 
     */
    private void addPacmans(int quantity) {
        if (this.pacmanActive) {
            for (int i = 0; i < quantity; i++) {
                // Get Pacman values
                int[] values = selectPosition();
                int x = values[0];
                int y = values[1];
                int randomX = values[6];
                int randomY = values[7];
                // New Pacman
                this.objects.add(new Pacman(this, x + randomX, y + randomY));
            }
        }
    }

    /**
     * @author Christian
     * @param number
     */
    private void addPowerUps(int number) {

        for (int i = 0; i < number; i++) {

            int[] values = selectPosition();
            int x = values[0];
            int y = values[1];
            int powerUpType = values[5];
            int randomX = values[6];
            int randomY = values[7];
            if (powerUpType < 0.5) {
                this.objects.add(new PowerUp(this, x + randomX, y + randomY, 70, 70, Power.HEALTH));
            } else {
                this.objects.add(new PowerUp(this, x + randomX, y + randomY, 70, 70, Power.DAMAGE));
            }
        }
    }

    /**
     * @author Christian
     * @return
     */
    public int genAsteroids() {
        return genObjects(KillerRules.MIN_ASTEROIDS, KillerRules.MAX_ASTEROIDS);
    }

    /**
     * @author Christian
     * @return 
     */
    public int genNebulosas() {
        return genObjects(KillerRules.MIN_NEBULOSAS, KillerRules.MAX_NEBULOSAS);
    }

    public int genPlanets() {
        return genObjects(KillerRules.MIN_PLANETS, KillerRules.MAX_PLANETS);
    }

    public int genBlackHoles() {
        return genObjects(KillerRules.MIN_BLACKHOLES, KillerRules.MAX_BLACKHOLES);
    }

    public int genPacmans() {
        return genObjects(KillerRules.MIN_PACMANS, KillerRules.MAX_PACMANS);
    }

    public int genPowerUps() {
        return genObjects(KillerRules.MIN_POWERUPS, KillerRules.MAX_POWERUPS);
    }

    /**
     * @author Christian
     * @param minimum
     * @param maximum
     * @return
     */
    public int genObjects(double min, double max) {
        double number = Math.random() * ((max - min) + 1) + min;
        return (int) number;
    }

    private void newGirdPositions() {

        for (int aux = 0; aux < 4; aux++) {
            for (int i = 0; i < 8; i++) {
                objectMatrixWidth[i][aux] = KillerGame.VIEWER_WIDTH * (i + 1) / 9;
                objectMatrixHeight[i][aux] = KillerGame.VIEWER_HEIGHT * (aux + 1) / 5;
            }
        }
    }

    private void newGirdNebulaPositions() {

        for (int aux = 0; aux < 2; aux++) {
            for (int i = 0; i < 2; i++) {
                nebulaMatrixWidth[i][aux] = KillerGame.VIEWER_WIDTH * (i + 1) / 3;
                nebulaMatrixHeight[i][aux] = KillerGame.VIEWER_HEIGHT * (aux + 1) / 3;
            }
        }
    }

    public int[] selectPosition() {
        boolean exit = true;
        int[] nums;
        do {
            int n = (int) (Math.random() * (8 - 0) + 0);
            int m = (int) (Math.random() * (4 - 0) + 0);
            // Int for position semi-random
            int n2 = (int) (Math.random() * (100 - 0) + 0);
            int m2 = (int) (Math.random() * (100 - 0) + 0);
            // Int for diametre random
            int heightAsteroid = (int) (Math.random() * (65 - 30) + 30);
            //int for
            int vx = 0;
            int vy = 0;
            int powerUp = 0;
            double symbolVx = Math.random();
            double symbolVy = Math.random();
            if (symbolVx <= 0.5) {
                vx = (int) (Math.random() * (KillerRules.MAX_VX_ASTEROIDS - KillerRules.MIN_VX_ASTEROIDS) + KillerRules.MIN_VX_ASTEROIDS) * -1;
                powerUp = 0;
                n2 = n2 * -1;
            } else {
                vx = (int) (Math.random() * (KillerRules.MAX_VX_ASTEROIDS - KillerRules.MIN_VX_ASTEROIDS) + KillerRules.MIN_VX_ASTEROIDS);
                powerUp = 1;
            }
            if (symbolVy > 0.5) {
                vy = (int) (Math.random() * (KillerRules.MAX_VX_ASTEROIDS - KillerRules.MIN_VY_ASTEROIDS) + KillerRules.MIN_VY_ASTEROIDS) * -1;
                m2 = m2 * -1;
            } else {
                vy = (int) (Math.random() * (KillerRules.MAX_VX_ASTEROIDS - KillerRules.MIN_VY_ASTEROIDS) + KillerRules.MIN_VY_ASTEROIDS);
            }
            int dim = (int) (Math.random() * (200 - 100) + 100);
            nums = new int[9];
            if (objectMatrixWidth[n][m] == 0 || objectMatrixHeight[n][m] == 0) {

                exit = false;
            } else {
                nums[0] = objectMatrixWidth[n][m];
                nums[1] = objectMatrixHeight[n][m];
                nums[2] = vx;
                nums[3] = vy;
                nums[4] = dim;
                nums[5] = powerUp;
                nums[6] = n2;
                nums[7] = m2;
                nums[8] = heightAsteroid;
                objectMatrixWidth[n][m] = 0;
                objectMatrixHeight[n][m] = 0;
                exit = true;
            }
        } while (!exit);
        return nums;
    }

    public int[] selectPositionNebulosas() {
        boolean exit = true;
        int[] nums2;
        double symbolVx = Math.random();
        double symbolVy = Math.random();
        int n2;
        int m2;
        if (symbolVx <= 0.5) {
            n2 = (int) (Math.random() * (400 - 0) + 0) * -1;
        } else {
            n2 = (int) (Math.random() * (400 - 0) + 0);
        }
        if (symbolVy <= 0.5) {
            m2 = (int) (Math.random() * (100 - 0) + 0) * -1;
        } else {
            m2 = (int) (Math.random() * (100 - 0) + 0);
        }
        // 
        do {
            int n = (int) (Math.random() * (4 - 0) + 0);
            int m = (int) (Math.random() * (2 - 0) + 0);
            nums2 = new int[4];
            if (nebulaMatrixWidth[n][m] == 0 || nebulaMatrixHeight[n][m] == 0) {

                exit = false;
            } else {
                nums2[0] = nebulaMatrixWidth[n][m];
                nums2[1] = nebulaMatrixHeight[n][m];
                nums2[2] = n2;
                nums2[3] = m2;
                nebulaMatrixWidth[n][m] = 0;
                nebulaMatrixHeight[n][m] = 0;
                exit = true;
            }
        } while (!exit);
        return nums2;
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

    public void sendGameConfiguration() {
        GameConfiguration configuration = GameConfiguration.Builder.builder().soundEffects(this.soundEffects).pacmanActive(this.pacmanActive).soundsMusic(this.soundMusic).ultraPacman(this.ultraPacman).build();
        if (this.nextModule != null) {
            this.nextModule.sendGameConfiguration(configuration);
        }
    }

    /**
     * @author Alvaro
     */
    public void sendStart() {
        if (this.status == Status.ROOM) {
            sendGameConfiguration();
            this.nextModule.sendStart();
            this.status = Status.GAME;
        }
    }

    public void receiveConfiguration(GameConfiguration configuration) {

        this.soundEffects = configuration.getSoundEffects();
        this.soundMusic = configuration.getSoundsMusic();
        if (!soundMusic) {
            this.stopMusic();
        }
        this.pacmanActive = configuration.getPacmanActive();
        this.ultraPacman = configuration.getUltraPacman();
        this.room.setNetworkConf(this.soundEffects, this.soundMusic, this.pacmanActive, this.ultraPacman);

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
        this.setSize(KillerGame.VIEWER_WIDTH, KillerGame.VIEWER_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout());
        this.setResizable(false);
        try {
            this.setUndecorated(true);
        } catch (Exception e) {

        }
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
        if (this.soundMusic) {
            this.radio.setClip(clip);
        }
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
        if (this.soundEffects) {
            this.sound.addSound(sound.createSound(clip));
        }
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
    public void newAsteroid(double x, double y, int imgHeight, int health, double maxspeed) {
        Asteroid asteroid = new Asteroid(this, x, y, imgHeight, health, maxspeed);
        this.objects.add(asteroid);
    }

    private void newBlackHole(int x, int y) {
        // New Black Hole and set values
        BlackHole blackhole = new BlackHole(this, x, y, 80, 80);
        this.objects.add(blackhole);
        // Strat thread
        new Thread(blackhole).start();
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
            if (this.pads.get(ip) != null) {
                this.pads.get(ip).closeSocket();
            }
            KillerPad pad = new KillerPad(this, socket, user, color);
            this.pads.put(ip, pad);
            new Thread(pad).start();
            result = true;
            System.out.println(user);
            this.room.updateUsers(this.pads);
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
        KillerShip ship = new KillerShip(this, KillerGame.VIEWER_WIDTH / 2, KillerGame.VIEWER_HEIGHT / 2, ip, user, type, color);
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
    public void reciveShip(double x, double y, double radians, double dx, double dy, double vx, double vy, double tx, double ty, double lx, double ly, double rx, double ry, String ip, String user, KillerShip.ShipType type, int health, int damage, Color color) {
        KillerShip ship = new KillerShip(this, x, y, radians, dx, dy, vx, vy, tx, ty, lx, ly, rx, ry, ip, user, type, health, damage, color);
        this.ships.put(ip, ship);
        this.objects.add(ship);
        new Thread(ship).start();
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
    public void reciveAsteroid(double x, double y, int imgHeight, double m, int health, double radians, double vx, double vy, double a, int imgFile) {
        Asteroid asteroid = new Asteroid(this, x, y, imgHeight, m, health, radians, vx, vy, a, imgFile);
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
    public void recivePacman(double x, double y, double m, int health, double radians, double vx, double vy, double a, int imgHeight) {
        Pacman pacman = new Pacman(this, x, y, m, health, radians, vx, vy, a, imgHeight);
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

    public int getPadsNum() {
        return this.padsNum;
    }

    public int getPadsSize() {
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

    public boolean getSoundMusic() {
        return soundMusic;
    }

    public boolean getSoundEffects() {
        return soundEffects;
    }

    public boolean getPacmanExistence() {
        return pacmanActive;
    }

    public boolean getUltraPacman() {
        return this.ultraPacman;
    }

    public GameConfiguration getConfiguration() {
        return GameConfiguration.Builder.builder().soundEffects(this.soundEffects).pacmanActive(this.pacmanActive).soundsMusic(this.soundMusic).ultraPacman(this.ultraPacman).build();
    }

    // ***************************************************************************************************** //
    // *************************** [              Methods Set            ] ********************************* //
    // ***************************************************************************************************** //
    public void enableUltrapacman() {
        this.ultraPacman = true;
        sendGameConfiguration();
    }

    public static void exit() {
        KillerGame.exit = true;
    }

    public void decrementPadsNum() {
        this.padsNum--;
        if (this.padsNum == 1) {
            this.nextModule.sendMessage(Message.Builder.builder("win", KillerServer.getId()).build());
        }
    }

    public void setIpPrev(String ip) {
        this.prevModule.setDestinationIp(ip);
    }

    public void setIpNext(String ip) {
        this.nextModule.setDestinationIp(ip);
    }

    public void setPadsNum(int padsSize) {
        this.padsNum = padsSize;
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
    
    /**
     * @author Christian
     */
    public void setSoundEffects(boolean soundEffects) {
        this.soundEffects = soundEffects;
        sendGameConfiguration();
    }
    
    /**
     * @author Christian
     */
    public void setSoundMusic(boolean soundMusic) {
        this.soundMusic = soundMusic;
        sendGameConfiguration();
    }

    public void setSyncronized(boolean synchro) {
        this.synchro = synchro;
        this.room.getKPP().setButtonPlay(synchro);
    }

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }
    
    public void setWorld(int world) {
        this.worldType = world;
    }

    /**
     * @author Christian
     */
    public void setPacmanExistence(boolean pacmanActive) {
        this.pacmanActive = pacmanActive;
        sendGameConfiguration();
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
        this.ships.remove(pad.getId());
        this.room.updateUsers(this.pads);
    }

    /**
     * @author Alvaro
     * @param ship
     */
    public void removeShip(KillerShip ship) {
        try {
            if (this.getShipByIP(ship.getId()).equals(ship)) {
                this.ships.remove(ship.getId());
            }
        } catch (Exception e) {

        }
    }

    /**
     * @author Christian
     * @param object
     */
    public void removeObject(VisibleObject object) {
        this.objects.remove(object);
        if (object instanceof KillerShip) {
            this.removeShip((KillerShip) object);
        }
    }

    // ***************************************************************************************************** //
    // *************************** [             Main Activity           ] ********************************* //
    // ***************************************************************************************************** //
    public static void main(String[] args) {
        // New KillerGame
        KillerGame game = new KillerGame();
        while (true) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {

            }
            if (KillerGame.exit) {
                game.getNextModule().sendMessage(Message.Builder.builder("quit", KillerServer.getId()).build());
                System.exit(0);
            }
        }
    }

}
