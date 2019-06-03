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
import java.awt.event.KeyListener;
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
    public static final int VIEWER_WIDTH = 1920;
    public static final int VIEWER_HEIGHT = 1080;
    
    // Gen objects
    private int matrizWidth[][] = new int[8][4];
    private int matrizHeight[][] = new int[8][4];
    
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
            if (CollidePhysics.collisionTxC(ship, (Asteroid) object)) {
                KillerRules.collisionShipWithAsteroid(this, ship, (Asteroid) (object));
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (BlackHole) object)) {
                KillerRules.collisionShipWithBlackHole(this, ship);
            }
        }

        // Collision with Nebulosa
        if (object instanceof Nebulosa && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Nebulosa) object)) {
                KillerRules.collisionShipWithNebulosa(ship);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Pacman) object)) {
                KillerRules.collisionShipWithPacman(this, ship, (Pacman) (object));
            }
        }

        // Collision with Planeta
        if (object instanceof Planeta && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (Planeta) object)) {
                KillerRules.collisionShipWithPlaneta(this, ship, (Planeta) (object));
            }
        }

        // Collision with PowerUp
        if (object instanceof PowerUp && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxC(ship, (PowerUp) object)) {
                KillerRules.collisionShipWithPowerUp(this, ship, (PowerUp) (object));
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip && !ship.equals((KillerShip) object) && ship.getState() == Alive.State.ALIVE) {
            if (CollidePhysics.collisionTxT(ship, (KillerShip) object)) {
                KillerRules.collisionShipWithShip(this, ship, (KillerShip) (object));
            }
        }

        // Collision with Shot
        if (object instanceof Shoot && ship.getState() == Alive.State.ALIVE) {
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
        if (object instanceof Asteroid) {
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

        // Collision with Nebulosa
        if (object instanceof Nebulosa) {
            if (CollidePhysics.collisionCxC(shoot, (Nebulosa) object)) {
                KillerRules.collisionShootWithNebulosa(shoot);
            }
        }

        // Collision with Pacman
        if (object instanceof Pacman) {
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
        if (object instanceof Shoot && !shoot.equals((Shoot) object)) {
            if (CollidePhysics.collisionCxC(shoot, (Shoot) object)) {
                KillerRules.collisionShootWithShoot(this, shoot, (Shoot) (object));
            }
        }

        // Collision with Ship
        if (object instanceof KillerShip) {
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
        if (object instanceof Asteroid && !asteroid.equals(object)) {
            if (CollidePhysics.collisionCxC(asteroid, (Asteroid) object)) {
                KillerRules.collisionAsteroidWithAsteroid(this, asteroid, (Asteroid) (object));
            }
        }

        // Collision with BlackHole
        if (object instanceof BlackHole) {
            if (CollidePhysics.collisionCxC(asteroid, (BlackHole) object)) {
                KillerRules.collisionAsteroidWithBlackHole(this, asteroid);
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
                KillerRules.collisionAsteroidWithPlaneta(asteroid, (Planeta) object);
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
                KillerRules.collisionPacmanWithBlackHole(this, pacman);
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

        this.addObjects();

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
    
    // ***************************************************************************************************** //
    // *************************** [             Add Methods             ] ********************************* //
    // ***************************************************************************************************** //
    
    /**
     * @author Christian
     */
    public void addObjects() {

        this.newRejillaPositions();
        this.addPlanets(genPlanets());
        this.addAsteroids(genAsteroids());
        this.addNebulosas(genNebulosas());
        this.addPacmans(genPacmans());
        this.addBlackHoles(genBlackHoles());
        this.addPowerUps(genPowerUps());

    }

    private void addPlanets(int number) {
        for (int i = 0; i < number; i++) {
            // Get Planet values
            int[] values = selectPosition();
            int x = values[0];
            int y = values[1];
            int height = values[4];
            // New asteroid and set values
            this.objects.add(new Planeta(this, x, y, height));
        }
    }

    private void addAsteroids(int number) {
        for (int i = 0; i < number; i++) {
            // Get Asteroid values
            int[] values = selectPosition();
            int x = values[0];
            int y = values[1];
            int vx = values[2];
            int vy = values[3];
            // New asteroid and set values
            Asteroid asteroid = new Asteroid(this, x, y, 40, 6, 2);
            asteroid.setVx(vx);
            asteroid.setVy(vy);
            this.objects.add(asteroid);
        }
    }

    private void addBlackHoles(int number) {
        for (int i = 0; i < number; i++) {
            // Get Black Hole values
            int[] values = selectPosition();
            int x = values[0];
            int y = values[1];
            // New Black Hole and set values
            this.objects.add(new BlackHole(this, x, y, 80, 80));
        }
    }

    private void addNebulosas(int number) {
        for (int i = 0; i < number; i++) {
            // Get Nebulosa values
            int[] values = selectPosition();
            int x = values[0];
            int y = values[1];
            // New Nebulosa
            this.objects.add(new Nebulosa(this, x, y, 120, 90));
        }
    }

    private void addPacmans(int number) {
        if (this.pacmanActive) {
            for (int i = 0; i < number; i++) {
                // Get Pacman values
                int[] values = selectPosition();
                int x = values[0];
                int y = values[1];
                // New Pacman
                this.objects.add(new Pacman(this, x, y));
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
            
            if (powerUpType < 0.5) {
                this.objects.add(new PowerUp(this, x, y, 70, 70, Power.HEALTH));
            } else {
                this.objects.add(new PowerUp(this, x, y, 70, 70, Power.DAMAGE));
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

    private void newRejillaPositions() {

        for (int aux = 0; aux < 4; aux++) {
            for (int i = 0; i < 8; i++) {
                matrizWidth[i][aux] = KillerGame.VIEWER_WIDTH * (i + 1) / 9;
                matrizHeight[i][aux] = KillerGame.VIEWER_HEIGHT * (aux + 1) / 5;
            }
        }

    }

    public int[] selectPosition() {
        boolean exit = true;
        int[] nums;
        do {
            int n = (int) (Math.random() * (8 - 0) + 0);
            int m = (int) (Math.random() * (4 - 0) + 0);
            int n2 = (int) (Math.random() * (8 - 0) + 0);
            int m2 = (int) (Math.random() * (4 - 0) + 0);
            int vx = 0;
            int vy = 0;
            int powerUp = 0;

            double symbolVx = Math.random();
            double symbolVy = Math.random();
            if (symbolVx <= 0.5) {
                vx = (int) (Math.random() * (KillerRules.MAX_VX_ASTEROIDS - KillerRules.MIN_VX_ASTEROIDS) + KillerRules.MIN_VX_ASTEROIDS) * -1;
                powerUp = 0;
            } else {
                vx = (int) (Math.random() * (KillerRules.MAX_VX_ASTEROIDS - KillerRules.MIN_VX_ASTEROIDS) + KillerRules.MIN_VX_ASTEROIDS);
                powerUp = 1;
            }
            if (symbolVy > 0.5) {
                vy = (int) (Math.random() * (KillerRules.MAX_VX_ASTEROIDS - KillerRules.MIN_VY_ASTEROIDS) + KillerRules.MIN_VY_ASTEROIDS) * -1;

            } else {
                vy = (int) (Math.random() * (KillerRules.MAX_VX_ASTEROIDS - KillerRules.MIN_VY_ASTEROIDS) + KillerRules.MIN_VY_ASTEROIDS);

            }

            int dim = (int) (Math.random() * (200 - 100) + 100);

            nums = new int[6];

            if (matrizWidth[n][m] == 0 || matrizHeight[n][m] == 0) {

                exit = false;

            } else {
                nums[0] = matrizWidth[n][m];
                nums[1] = matrizHeight[n][m];
                nums[2] = vx;
                nums[3] = vy;
                nums[4] = dim;
                nums[5] = powerUp;

                matrizWidth[n][m] = 0;
                matrizHeight[n2][m2] = 0;
                exit = true;
            }
        } while (!exit);

        return nums;
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
        this.nextModule.sendGameConfiguration(configuration);
        
    }

    /**
     * @author Alvaro
     */
    public void sendStart() {
        if (this.status == Status.ROOM) {
            this.nextModule.sendStart(GameConfiguration.Builder.builder().soundEffects(this.soundEffects).pacmanActive(this.pacmanActive).soundsMusic(this.soundMusic).ultraPacman(this.ultraPacman).build());
            this.status = Status.GAME;
        }
    }

    public void receiveConfiguration(GameConfiguration configuration) {
        
        this.soundEffects = configuration.getSoundEffects();
        this.soundMusic = configuration.getSoundsMusic();
        if (!soundMusic) {this.stopMusic();}
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
        } catch(Exception e) {
            
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
    public void reciveAsteroid(double x, double y, int imgHeight, double m, int health, double radians, double vx, double vy, double a) {
        Asteroid asteroid = new Asteroid(this, x, y, imgHeight, m, health, radians, vx, vy, a);
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

    // ***************************************************************************************************** //
    // *************************** [              Methods Set            ] ********************************* //
    // ***************************************************************************************************** //
    public void enableUltrapacman() {
        this.ultraPacman = true;
    }

    public static void exit() {
        KillerGame.exit = true;
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

    public void decrementPadsNum() {
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

    /**
     * @author Christian
     */
    public void setSoundMusic(boolean soundMusic) {
        this.soundMusic = soundMusic;
    }

    /**
     * @author Christian
     */
    public void setSoundEffects(boolean soundEffects) {
        this.soundEffects = soundEffects;
    }

    /**
     * @author Christian
     */
    public void setPacmanExistence(boolean pacmanActive) {
        this.pacmanActive = pacmanActive;
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
