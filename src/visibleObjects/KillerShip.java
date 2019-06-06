package visibleObjects;

import communications.KillerAction;
import game.KillerGame;
import game.KillerRules;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import physics.PhysicsShip;
import visualEffects.ExplosionEffect;
import visualEffects.FireEffect;
import visualEffects.KillerImage;

public class KillerShip extends Alive {

    public enum ShipType {
        BATMOBILE, OCTANE, MARAUDER
    }

    private ShipType type;
    private String id;
    private String user;
    private long timer;
    private int damage;
    private PhysicsShip physicsShip;
    private int tiempoEnNebulosa;
    private Color color;
    private KillerImage xplosion;

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

        this.imgHeight = this.checkImgHeight();
        this.setImgSize();
        this.a = 0.07;
        this.m = (this.imgWidth * this.imgHeight) / 2;
        this.physicsShip = new PhysicsShip(this); // han de estar inicializadas todas las variables de fisicas
        this.tiempoEnNebulosa = 0;

        this.kImg = new FireEffect(this);
        this.xplosion = new ExplosionEffect(this);
    }

    /**
     * Constructor para instanciar la nave si viene de otro pc
     *
     * @param game
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
    public KillerShip(KillerGame game, double x, double y, double radians, double dx, double dy,
            double vx, double vy, double tx, double ty, double lx, double ly, double rx, double ry,
            String id, String user, ShipType type, int health, int damage, Color color) {
        super(game, x, y);

        this.id = id;
        this.user = user;
        this.color = color;
        this.type = type;
        
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
        
        this.state = State.ALIVE;
        this.health = health;
        this.damage = damage;
        this.setImage();

        this.imgHeight = this.checkImgHeight();
        this.setImgSize(); // (Ha de estar cargada la img con setImage)
        this.m = (this.imgWidth * this.imgHeight) / 2;
        this.physicsShip = new PhysicsShip(this); // han de estar inicializadas todas las variables de fisicas
        this.tiempoEnNebulosa = 0;

        this.timer = System.currentTimeMillis();

        this.kImg = new FireEffect(this);
        this.xplosion = new ExplosionEffect(this);
    }

    @Override
    public void run() {
        new Thread(this.kImg).start();

        this.timer = System.currentTimeMillis();

        while (state != State.DEAD) {

            if (state == State.SAFE) {
                this.checkSafe();
            }

            // Control de la maxspeed cuando se atraviesa una nebulosa
            if (this.tiempoEnNebulosa > 0) {
                this.tiempoEnNebulosa--;
                if (this.tiempoEnNebulosa == 0) {
                    this.configureSpeed();
                }
            }

            if (this.state != State.DYING) {
                this.move();
                game.checkColision(this);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        this.game.removeObject(this);
    }

    /**
     * Método llamado por el KillerPad para hacer la acción que llega del mando.
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
            case "pad_turbo_start":
                this.turboStart();
                break;
            case "pad_turbo_end":
                this.turboEnd();
                break;
        }
    }

    /**
     * Método para aumentar el daño de la nave al coger el powerUp DAMAGE
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

    /**
     * Método para cargar imagen de la nave según el tipo
     */
    @Override
    protected void setImage() {
        switch (type) {
            case BATMOBILE:
                this.loadImg("img/fastShip.png");
                break;
            case OCTANE:
                this.loadImg("img/normalShip.png");
                break;
            case MARAUDER:
                this.loadImg("img/bigShip.png");
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
     * por el mando.
     */
    private void moveShip(double dx, double dy) {
        this.dx = dx;
        this.dy = -dy;
    }

    /**
     * Método para quitar el escudo a la nave cuando pasan 5 segundos
     */
    private void checkSafe() {
        if (System.currentTimeMillis() - timer > 5000) {
            this.state = State.ALIVE;
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
            case BATMOBILE:
                this.damage = KillerRules.BATMOBILE_DAMAGE;
                break;
            case OCTANE:
                this.damage = KillerRules.OCTANE_DAMAGE;
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
            case BATMOBILE:
                this.health = KillerRules.BATMOBILE_HEALTH;
                break;
            case OCTANE:
                this.health = KillerRules.OCTANE_HEALTH;
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
            case BATMOBILE:
                this.maxspeed = KillerRules.BATMOBILE_MAX_SPEED;
                break;
            case OCTANE:
                this.maxspeed = KillerRules.OCTANE_MAX_SPEED;
                break;
            case MARAUDER:
                this.maxspeed = KillerRules.MARAUDER_MAX_SPEED;
                break;
            default:
                break;
        }

    }

    /**
     * Método para hacer dash cuando el jugador pulsa el botón dash
     */
    private void dash() {
        this.physicsShip.dash();
    }

    /**
     * Método para recuperar la velocidad máxima de la nave cuando el jugador termina el turbo
     */
    private void turboEnd() {
        this.configureSpeed();
    }

    /**
     * Método para aumentar la velocidad máxima de la nave mientras dura el turbo
     */
    private void turboStart() {
        this.maxspeed += KillerRules.MAX_SPEED_INCREMENT;
    }
    
    /**
     * Método para definir la altura de la nave según el tipo
     * @return altura de la nave en px
     */
    private int checkImgHeight() {
        int height;
        if (null == this.type) {            
            height = 60;
        } else switch (this.type) {
            case MARAUDER:
                height = 100;
                break;
            case BATMOBILE:
                height = 75;
                break;
            default:
                height = 60;
                break;
        }
        
        return height;
    }

    /**
     * Método que devuelve la salud de la nave según el tipo
     * @return salud de la nave
     */
    private int getInitHealth() {
        int initHealth = 0;
        switch (type) {
            case BATMOBILE:
                initHealth = KillerRules.BATMOBILE_HEALTH;
                break;
            case OCTANE:
                initHealth = KillerRules.OCTANE_HEALTH;
                break;
            case MARAUDER:
                initHealth = KillerRules.MARAUDER_HEALTH;
                break;
            default:
                break;
        }
        return initHealth;
    }

    // *************************
    // *    Drawing methods    *
    // *************************
    /**
     * Método para rotar escalar y pintar la imagen de la nave con el fuego
     * @param g2d 
     */
    private void drawFireEffect(Graphics2D g2d) {
        
        double scale = (double) this.imgWidth / (double) this.kImg.getWidth(); // Cálculo del factor de escalado de la imagen en función de las dimensiones de la nave
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y); // Coordenadas donde pintar
        transform.rotate(-radians, this.imgWidth / 2, this.imgHeight / 2); // Ángulo de rotación a aplicar
        transform.scale(scale, scale); // Factor de escalado a aplicar
        g2d.drawImage(this.kImg, transform, null);
    }

    /**
     * Método para pintar el nombre del usuario
     * @param g2d 
     */
    private void drawUserName(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.drawString(this.user, (int) x, (int) y - 36);
    }

    /**
     * Método para pintar la barra de vida
     * @param g2d 
     */
    private void drawLifeBar(Graphics2D g2d) {
        double barWidth;
        Color c;
        if (this.health <= this.getInitHealth()) {
            // Barra de vida proporcional a la salud, del color elegido por el jugador
            c = this.color;
            barWidth = (this.imgWidth * this.health) / this.getInitHealth();
        } else {
            // Si el jugador tiene más vida que su vida máxima, la barra cse pone de color blanco
            c = Color.white;
            barWidth = this.imgWidth;
        }
        g2d.setColor(c);
        g2d.fillRect((int) x, (int) y - 29, (int) barWidth, 8);
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect((int) x, (int) y - 29, this.imgWidth, 8);

    }

    /**
     * Método para pintar el escudo cuando la nave es invulnerable
     * @param g2d 
     */
    private void drawSafe(Graphics2D g2d) {
        g2d.setColor(this.color);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) x - 12, (int) y - 10, this.imgWidth + 24, this.imgHeight + 24);
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    // Interfaz Destructible
    /**
     * Método para iniciar el ExplosionEffect
     */
    @Override
    public void onDying() {
        (new Thread(this.xplosion)).start();
    }

    @Override
    public void die() {

    }

    // Interfaz Renderizable
    /**
     * Método para pintar la nave. Si está en estado SAFE pinta el FireEffect, los datos de la nave y el escudo,
     * en estado ALIVE pinta el FireEffect y los datos de la nave, y en estado DYING pinta la ExplosionEffect
     * @param g2d 
     */
    @Override
    public void render(Graphics2D g2d) {

        switch (this.state) {
            case SAFE:
                this.drawFireEffect(g2d);
                this.drawSafe(g2d);
                this.drawUserName(g2d);
                this.drawLifeBar(g2d);
                break;
            case ALIVE:
                this.drawFireEffect(g2d);
                this.drawUserName(g2d);
                this.drawLifeBar(g2d);
                break;
            case DYING:
                g2d.drawImage(this.xplosion, (int) this.x, (int) this.y, this.imgHeight, this.imgHeight, null);
                break;
            default:
                //System.out.println("KillerShip render --> DEAD, rendering DEFAULT");
                break;
        }

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

    public KillerImage getXplosion() {
        return xplosion;
    }

    public void setXplosion(KillerImage xplosion) {
        this.xplosion = xplosion;
    }

}
