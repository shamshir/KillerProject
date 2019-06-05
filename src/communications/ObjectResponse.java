package communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.awt.Color;
import visibleObjects.Alive;
import visibleObjects.Asteroid;
import visibleObjects.KillerShip;
import visibleObjects.Pacman;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ObjectResponse {

    private String objectType;
    private double x;
    private double y;    
    private double radians;
    private double dx;
    private double dy;
    private double vx;
    private double vy;    
    private double tx;
    private double ty;
    private double lx;
    private double ly;
    private double rx;    
    private double ry;
    private double m;
    private double a;
    private KillerShip.ShipType type;
    private String id;    
    private String user;
    private int health;
    private int damage;
    private String color;
    private int imgHeight;
    private int imgFile;

    private static final String EMPTY_STRING = "";
    private static final String SHOOT_TYPE = "shoot";
    private static final String SHIP_TYPE = "ship";
    private static final String PACMAN_TYPE = "pacman";
    private static final String ASTEROID_TYPE = "asteroid";

    public ObjectResponse() {

    }

    private ObjectResponse(final ObjectResponse.Builder builder) {
        this.objectType = builder.objectType;
        this.x = builder.x;
        this.y = builder.y;
        this.radians = builder.radians;
        this.dx = builder.dx;
        this.vx = builder.vx;
        this.tx = builder.tx;
        this.lx = builder.lx;
        this.rx = builder.rx;
        this.dy = builder.dy;
        this.vy = builder.vy;        
        this.ty = builder.ty;
        this.ly = builder.ly;
        this.ry = builder.ry;
        this.id = builder.id;
        this.user = builder.user;        
        this.type = builder.type;
        this.health = builder.health;
        this.damage = builder.damage;
        this.color = builder.color;
        this.imgHeight = builder.imgHeight;        
        this.m = builder.m;
        this.a = builder.a;
        this.imgFile = builder.imgFile;
    }

    public String getObjectType() {
        return objectType;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadians() {
        return radians;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getTx() {
        return tx;
    }

    public double getTy() {
        return ty;
    }

    public double getLx() {
        return lx;
    }

    public double getLy() {
        return ly;
    }

    public double getRx() {
        return rx;
    }

    public double getRy() {
        return ry;
    }

    public KillerShip.ShipType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

   public String getColor() {
        return color;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public double getM() {
        return m;
    }

    public double getA() {
        return a;
    }

    public int getImgFile() {
        return imgFile;
    }
    
    public static ObjectResponse convertObjectToObjectResponse(final Alive object) {
        if (object instanceof KillerShip) {
            return buildObjectResponseFromKillerShip((KillerShip) object);        
        } else if (object instanceof Asteroid) {
            return buildObjectResponseFromAsteroid((Asteroid) object);
        } else if (object instanceof Pacman) {
            return buildObjectResponseFromPacman((Pacman) object);  
        }
        return ObjectResponse.Builder.builder(EMPTY_STRING).build();
    }

    private static ObjectResponse buildObjectResponseFromKillerShip(KillerShip killerShip) {
        return ObjectResponse.Builder.builder(SHIP_TYPE)
                .x(killerShip.getX())
                .y(killerShip.getY())
                .dx(killerShip.getDx())
                .dy(killerShip.getDy())
                .vx(killerShip.getVx())
                .vy(killerShip.getVy())
                .rx(killerShip.getRx())
                .ry(killerShip.getRy())
                .lx(killerShip.getLx())
                .ly(killerShip.getLy())
                .tx(killerShip.getTy())
                .ty(killerShip.getTy())
                .health(killerShip.getHealth())
                .radians(killerShip.getRadians())
                .type(killerShip.getType())
                .user(killerShip.getUser())
                .id(killerShip.getId())
                .damage(killerShip.getDamage())
                .color(killerShip.getColor())
                .build();
    }

    private static ObjectResponse buildObjectResponseFromAsteroid(final Asteroid asteroid) {
        return ObjectResponse.Builder.builder(ASTEROID_TYPE)
                .x(asteroid.getX())
                .y(asteroid.getY())
                .imgHeight(asteroid.getImgHeight())
                .m(asteroid.getM())
                .health(asteroid.getHealth())
                .vx(asteroid.getVx())
                .vy(asteroid.getVy())
                .radians(asteroid.getRadians())
                .a(asteroid.getA())
                .imgFile(asteroid.getImgFile())
                .build();
    }
    
    private static ObjectResponse buildObjectResponseFromPacman(final Pacman pacman) {
        return ObjectResponse.Builder.builder(PACMAN_TYPE)
                .x(pacman.getX())
                .y(pacman.getY())
                .m(pacman.getM())
                .health(pacman.getHealth())
                .vx(pacman.getVx())
                .vy(pacman.getVy())
                .radians(pacman.getRadians())
                .a(pacman.getA())
                .imgHeight(pacman.getImgHeight())
                .build();
    }

    public static class Builder {

        private String objectType;
        private double x;
        private double y;
        private double radians;
        private double dx;
        private double dy;
        private double vx;
        private double vy;
        private double tx;
        private double ty;
        private double lx;
        private double ly;
        private double rx;
        private double ry;
        private KillerShip.ShipType type;
        private String id;
        private String user;
        private int health;
        private int damage;
        private String color;
        private int imgHeight;
        private int imgFile;
        private double m;
        private double a;

        public Builder(final String objectType) {
            this.objectType = objectType;
        }

        public static Builder builder(final String objectType) {
            return new Builder(objectType);
        }

        public Builder x(final double x) {
            this.x = x;
            return this;
        }

        public Builder y(final double y) {
            this.y = y;
            return this;
        }

        public Builder radians(final double radians) {
            this.radians = radians;
            return this;
        }

        public Builder dx(final double dx) {
            this.dx = dx;
            return this;
        }

        public Builder vx(final double vx) {
            this.vx = vx;
            return this;
        }

        public Builder tx(final double tx) {
            this.tx = tx;
            return this;
        }

        public Builder rx(final double rx) {
            this.rx = rx;
            return this;
        }

        public Builder lx(final double lx) {
            this.lx = lx;
            return this;
        }

        public Builder dy(final double dy) {
            this.dy = dy;
            return this;
        }

        public Builder vy(final double vy) {
            this.vy = vy;
            return this;
        }

        public Builder ty(final double ty) {
            this.ty = ty;
            return this;
        }

        public Builder ry(final double ry) {
            this.ry = ry;
            return this;
        }

        public Builder ly(final double ly) {
            this.ly = ly;
            return this;
        }

        public Builder type(final KillerShip.ShipType type) {
            this.type = type;
            return this;
        }

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder user(final String user) {
            this.user = user;
            return this;
        }

        public Builder health(final int health) {
            this.health = health;
            return this;
        }

        public Builder damage(final int damage) {
            this.damage = damage;
            return this;
        }

        public Builder color(final Color color) {
            this.color = "#" + Integer.toHexString(color.getRGB()).substring(2);
            return this;
        }

        public Builder imgHeight(final int imgHeight) {
            this.imgHeight = imgHeight;
            return this;
        }

        public Builder imgFile(final int imgFile) {
            this.imgFile = imgFile;
            return this;
        }
        
        public Builder m(final double m) {
            this.m = m;
            return this;
        }

        public Builder a(final double a) {
            this.a = a;
            return this;
        }

        public ObjectResponse build() {
            return new ObjectResponse(this);
        }
    }
}