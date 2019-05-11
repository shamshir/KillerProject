package visibleObjects;

import communications.KillerAction;
import game.KillerGame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import physics.KillerPhysics;

public class KillerShip extends Controlled {

    public enum ShipState {
        SAFE, ALIVE, DIE
    }

    public enum ShipType {
        FAST, BIG
    }

    protected ShipState state;
    protected ShipType type;
    protected String id;
    protected String user;
    protected long timer;

    // Físicas
    protected double tx; // posición del morro de la nave
    protected double ty; // posición del morro de la nave
    protected double lx;
    protected double ly;
    protected double rx;
    protected double ry;

    /**
     *
     * @param game
     * @param x
     * @param y
     * @param id
     * @param user
     * @param type
     */
    public KillerShip(KillerGame game, double x, double y, String id, String user, ShipType type) {
        super(game, x, y);
        this.id = id;
        this.user = user;
        this.type = type;
        this.state = ShipState.SAFE;

        this.configureShip(); // health y maxspeed según el tipo de nave
        this.setImage();

        this.imgHeight = 80;
        this.setImgSize();
        this.m = 100;

        this.timer = System.currentTimeMillis();
    }

    /**
     * Constructor para instanciar la nave si viene de otro pc; por defecto son
     * invencibles.
     *
     * @param game
     * @param x
     * @param y
     * @param radians
     * @param dx
     * @param dy
     * @param vx 
     * @param vy
     * @param tx hitbox
     * @param ty hitbox
     * @param lx hitbox
     * @param ly hitbox
     * @param rx hitbox
     * @param ry hitbox
     * @param id
     * @param user
     * @param type
     * @param health
     */
    public KillerShip(KillerGame game, double x, double y, double radians,
            double dx, double dy, double vx, double vy, double tx, double ty, double lx, double ly,
            double rx, double ry, String id, String user, ShipType type, int health) {
        super(game, x, y);
        // Físicas ---> que parámetros pasan?
        this.a = 0.01;
        this.radians = radians;
        this.dx = dx;
        this.dy = dy;
        this.vx = vx;
        this.vy = vy;
        this.tx = tx;
        this.ty = ty;
        this.lx = lx;
        this.ly = ly;
        this.rx = rx;
        this.ry = ry;
        //-------

        this.id = id;
        this.user = user;
        this.type = type;
        this.state = ShipState.SAFE;
        this.configureShip(); // --> TO DO
        this.health = health; // --> TO DO, escribe dos veces
        this.setImage();

        this.imgHeight = 80;
        this.setImgSize();
        this.m = 100;

        this.timer = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (state != ShipState.DIE) {

            if (state != ShipState.SAFE) {
                this.checkSafe();
            }

            this.move();
            game.checkColision(this);

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Método llamado por el KillerPad para mandar la info que llega del mando.
     * El KillerShip la ha de descodificsr. Ajustar valores a lo que se envía
     *
     * @param kAction
     */
    public void doAction(KillerAction kAction) {
        String action = kAction.getCommand();
        switch (action) {
            case "shoot":
                this.shoot();
                break;
            case "move":
                this.moveShip();
                break;
        }
    }

    /**
     * Método para incrementar la salud al coger un powerUp HEALTH
     *
     * @param health cantidad de salud que se suma
     */
    public void increaseHealth(int health) {
        this.health += health;
    }

    /**
     * Método para poner escudo a la nave durante un tiempo al coger el powerUp
     * SAFE
     *
     */
    public void beSafe() {
        this.state = ShipState.SAFE;
        this.timer = System.currentTimeMillis();
    }

    @Override
    public void collision() {
        // TO DO
        System.out.println("KillerShip: collision()");
    }

    @Override
    protected void move() {
        KillerPhysics.move(this);
    }

    @Override
    protected void setImage() {
        if (type == ShipType.FAST) {
            this.loadImg("./img/fastShip.png");
        } else if (type == ShipType.BIG) {
            this.loadImg("./img/bigShip.png");
        }
    }

    /**
     * Método para decirle al KG que cree un obj Shoot para la nave
     */
    private void shoot() {
        this.game.newShoot(this);
    }

    /**
     * Cambiará los valores de dirección y ángulo en función de la info enviada
     * por el mando. Parámetros aún por decidir según la info recibida
     */
    private void moveShip() {
        // TO DO
        // Modificar dx, dy o lo que sea
        // Modificar ángulo para Animación
    }

    private void checkSafe() {
        if (System.currentTimeMillis() - timer > 5000) {
            this.state = ShipState.ALIVE;
            this.setImage();
            // Adapto las coordenadas para la hitbox a la img
            this.setImgSize();
        }
    }

    /**
     * Método para inicializar health y maxSpeed según el tipo de nave
     */
    private void configureShip() {
        switch (this.type.name()) {
            case "FAST":
                this.health = 100;
                this.maxspeed = 6;
                break;
            case "BIG":
                this.health = 150;
                this.maxspeed = 4;
                break;
        }
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    // Interfaz Destructible    
    @Override
    public void quitarVida(int damage) {
        // No quitamos vida si la nave está muriendo o si está a salvo
        if (state != ShipState.SAFE) {
            this.health -= damage;
        }
    }

    @Override
    public void onDying() {

    }

    @Override
    public void die() {

    }

    // Interfaz Renderizable
    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.drawString(this.user, (int) x, (int) y - 10);
        g2d.drawImage(this.img, (int) x, (int) y, imgWidth, imgHeight, null);
        // Pintar indicador de escudo si la nave está SAFE
        if (this.state == ShipState.SAFE) {
            g2d.setColor(Color.magenta);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval((int) x - 6, (int) y - 6, imgWidth + 12, imgHeight + 12);
        }
    }

    // *********************
    // * Getters & Setters *
    // *********************
    public ShipState getState() {
        return state;
    }

    public void setState(ShipState state) {
        this.state = state;
    }

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public double getTx() {
        return tx;
    }

    public void setTx(double tx) {
        this.tx = tx;
    }

    public double getTy() {
        return ty;
    }

    public void setTy(double ty) {
        this.ty = ty;
    }

    public double getLx() {
        return lx;
    }

    public void setLx(double lx) {
        this.lx = lx;
    }

    public double getLy() {
        return ly;
    }

    public void setLy(double ly) {
        this.ly = ly;
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

}
