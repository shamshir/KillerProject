package visibleObjects;

import communications.KillerAction;
import game.KillerGame;
import game.KillerRules;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import physics.PhysicsShip;
import visualEffects.FireEffect;

public class KillerShip extends Controlled {
    
    public enum ShipType {
        OCTANE, BATMOBILE, MARAUDER
    }
    
    private ShipType type;
    private String id;
    private String user;
    private long timer;
    private int damage;
    private PhysicsShip physicsShip;
    private int tiempoEnNebulosa;
    private Color color;

    // Físicas
    private double tx; // posición del morro de la nave
    private double ty; // posición del morro de la nave
    private double lx;
    private double ly;
    private double rx;
    private double ry;

    /**
     *
     * @param game
     * @param x
     * @param y
     * @param id
     * @param user
     * @param type
     * @param color
     */
    public KillerShip(KillerGame game, double x, double y, String id, 
            String user, ShipType type, Color color) {
        super(game, x, y);
        this.id = id;
        this.user = user;
        this.color = color;
        this.type = type;
        this.state = State.SAFE;

        this.configureShip();
        this.setImage();

        this.imgHeight = 60;
        this.setImgSize();
        this.a = 0.07;
        this.m = 100;
        this.physicsShip = new PhysicsShip(this); // han de estar inicializadas todas las variables de fisicas
        this.tiempoEnNebulosa = 0;
        
        this.kImg = new FireEffect(this, this.img);
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
     * @param damage
     * @param color
     */
    public KillerShip(KillerGame game, double x, double y, double radians, double dx, double dy, 
            double vx, double vy, double tx, double ty, double lx, double ly, double rx, double ry, 
            String id, String user, ShipType type, int health, int damage, Color color) {
        super(game, x, y);

        this.id = id;
        this.user = user;
        this.color = color;
        this.type = type;
        // Físicas ---> que parámetros pasan?
        this.a = 0.07;
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
        this.configureSpeed();
        
        //this.state = State.SAFE;
        this.state = State.ALIVE;
        this.health = health;
        this.damage = damage;
        this.setImage();

        this.imgHeight = 60;
        this.setImgSize(); // (Ha de estar cargada la img con setImage)
        this.m = 100;
        this.physicsShip = new PhysicsShip(this); // han de estar inicializadas todas las variables de fisicas
        this.tiempoEnNebulosa = 0;

        this.timer = System.currentTimeMillis();
        
        this.kImg = new FireEffect(this, this.img);
    }

    @Override
    public void run() {
        new Thread(this.kImg).start();
        
        this.timer = System.currentTimeMillis();
        
        while (state != State.DEAD) {

            if (state == State.SAFE) {
                this.checkSafe();
            }

//            if (this.tiempoEnNebulosa == 0) {
//                this.configureSpeed();
//            } else {
//                this.tiempoEnNebulosa--;
//            }

            // Control de la maxspeed cuando se atraviesa una nebulosa
            if (this.tiempoEnNebulosa > 0) {
                this.tiempoEnNebulosa--;
                if (this.tiempoEnNebulosa == 0) {
                    this.configureSpeed();
                }
            }
            
            this.move();
            game.checkColision(this);

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        this.game.removeObject(this);
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
            case "pad_shoot":
                this.shoot();
                break;
            case "pad_move":
                this.moveShip(kAction.getSpeedX(), kAction.getSpeedY());
                break;
            case "pad_dash":
                this.dash();
                break;
            case "pad_powerup":

                break;
            case "pad_turbo_start":
                this.turboStart();
                break;
            case "pad_turbo_end":
                this.turboEnd();
                break;
        }
    }

    /**
     * Método aumentar el daño de la nave al coger el powerUp DAMAGE
     *
     * @param damage
     */
    public void powerUpDamage(int damage) {
        this.damage += damage;
    }

    /**
     * Método para incrementar la salud al coger un powerUp HEALTH
     *
     * @param health cantidad de salud que se suma
     */
    public void powerUpHealth(int health) {
        this.health += health;
    }

    @Override
    protected void move() {
        physicsShip.move();
    }

    @Override
    protected void setImage() {
        switch (type) {
            case OCTANE:
                this.loadImg("src/visibleObjects/img/fastShip.png");
                break;
            case BATMOBILE:
                this.loadImg("src/visibleObjects/img/normalShip.png");
                break;
            case MARAUDER:
                this.loadImg("src/visibleObjects/img/bigShip.png");
                break;
            default:
                break;
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
    private void moveShip(double dx, double dy) {
        this.dx = dx;
        this.dy = -dy;
    }

    private void checkSafe() {
        if (System.currentTimeMillis() - timer > 5000) {
            this.state = State.ALIVE;
            this.setImage();
            // Adapto las coordenadas para la hitbox a la img
            this.setImgSize();
        }
    }

    /**
     * Método para inicializar damage, health y maxSpeed según el tipo de nave
     */
    private void configureShip() {
        this.configureDamage();
        this.configureHealth();
        this.configureSpeed();
    }
    
    private void configureDamage() {
        switch (type) {
            case OCTANE:
                this.damage = KillerRules.OCTANE_DAMAGE;
                break;
            case BATMOBILE:
                this.damage = KillerRules.BATMOBILE_DAMAGE;
                break;
            case MARAUDER:
                this.damage = KillerRules.MARAUDER_DAMAGE;
                break;
            default:
                break;
        }
        
    }
    
    private void configureHealth() {
        switch (type) {
            case OCTANE:
                this.health = KillerRules.OCTANE_HEALTH;
                break;
            case BATMOBILE:
                this.health = KillerRules.BATMOBILE_HEALTH;
                break;
            case MARAUDER:
                this.health = KillerRules.MARAUDER_HEALTH;
                break;
            default:
                break;
        }
        
    }
    
    private void configureSpeed() {
        switch (type) {
            case OCTANE:
                this.maxspeed = KillerRules.OCTANE_MAX_SPEED;
                break;
            case BATMOBILE:
                this.maxspeed = KillerRules.BATMOBILE_MAX_SPEED;
                break;
            case MARAUDER:
                this.maxspeed = KillerRules.MARAUDER_MAX_SPEED;
                break;
            default:
                break;
        }
        
    }
    
    private void dash() {
        this.physicsShip.dash();
    }
    
    private void turboEnd() {
        this.configureSpeed();
    }
    
    private void turboStart() {
        this.maxspeed += KillerRules.MAX_SPEED_INCREMENT;
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    // Interfaz Destructible
    @Override
    public void onDying() {

    }

    @Override
    public void die() {

    }

    // Interfaz Renderizable
    @Override
    public void render(Graphics2D g2d) {
        
        double scale = (double)this.imgWidth / (double)((FireEffect)this.kImg).getWidth();
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.rotate(-radians, this.imgWidth / 2, this.imgHeight / 2);
        transform.scale(scale, scale);
        
        g2d.drawImage(this.kImg, transform, null);
        
        // Pintar indicador de escudo si la nave está SAFE
        if (this.state == Alive.State.SAFE) {
            g2d.setColor(Color.magenta);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval((int) x - 6, (int) y - 6, imgWidth + 12, imgHeight + 12);
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white);
        g2d.drawString(this.user, (int) x, (int) y - 10);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    // *********************
    // * Getters & Setters *
    // *********************

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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public PhysicsShip getPhysicsShip() {
        return physicsShip;
    }

    public void setPhysicsShip(PhysicsShip physicsShip) {
        this.physicsShip = physicsShip;
    }

    public int getTiempoEnNebulosa() {
        return tiempoEnNebulosa;
    }

    public void setTiempoEnNebulosa(int tiempoEnNebulosa) {
        this.tiempoEnNebulosa = tiempoEnNebulosa;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
