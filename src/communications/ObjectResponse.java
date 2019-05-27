package communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import visibleObjects.Alive;
import visibleObjects.KillerShip;
import visibleObjects.Shoot;

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
    private KillerShip.ShipType type;
    private String id;
    private String user;
    private int health;
    private int damage;

    private static final String EMPTY_STRING = "";
    private static final String SHOOT_TYPE = "shoot";
    private static final String SHIP_TYPE = "ship";

    public ObjectResponse() {

    }

    private ObjectResponse(final ObjectResponse.Builder builder) {
        this.objectType = builder.objectType;
        this.x= builder.x;
        this.dx = builder.dx;
        this.vx = builder.vx;
        this.tx = builder.tx;
        this.lx = builder.lx;
        this.rx = builder.rx;
        this.y= builder.y;
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

    public static ObjectResponse convertObjectToObjectResponse(final Alive object) {
        //TODO rellenar con los datos que se pida
        if (object instanceof KillerShip) {
            return buildObjectResponseFromKillerShip((KillerShip) object);
        } else if (object instanceof Shoot) {
            return buildObjectResponseFromShoot((Shoot) object);

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
                .build();

    }

    private static ObjectResponse buildObjectResponseFromShoot(final Shoot shoot) {
        return ObjectResponse.Builder.builder(SHOOT_TYPE)
                .x(shoot.getX())
                .y(shoot.getY())
                .dx(shoot.getDx())
                .dy(shoot.getDy())
                .radians(shoot.getRadians())
                .id(shoot.getId())
                .damage(shoot.getDamage())
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

        public Builder(final String objectType) {
            this.objectType = objectType;
        }

        public static Builder builder(final String objectType) {
            return new Builder(objectType);
        }

        public Builder x(final double x){
            this.x = x;
            return this;
        }

        public Builder y(final double y){
            this.y = y;
            return this;
        }
        
        public Builder radians(final double radians){
            this.radians = radians;
            return this;
        }
        
        public Builder dx(final double dx){
            this.dx = dx;
            return this;
        }
        
        public Builder vx(final double vx){
            this.vx = vx;
            return this;
        }
        
        public Builder tx(final double tx){
            this.tx = tx;
            return this;
        }
        
        public Builder rx(final double rx){
            this.rx = rx;
            return this;
        }
        
        public Builder lx(final double lx){
            this.lx = lx;
            return this;
        }
        
        public Builder dy(final double dy){
            this.dy = dy;
            return this;
        }
        
        public Builder vy(final double vy){
            this.vy = vy;
            return this;
        }
        
        public Builder ty(final double ty){
            this.ty = ty;
            return this;
        }
        
        public Builder ry(final double ry){
            this.ry = ry;
            return this;
        }
        
        public Builder ly(final double ly){
            this.ly = ly;
            return this;
        }
                
        public Builder type(final KillerShip.ShipType type){
            this.type = type;
            return this;
        }
        
        public Builder id(final String id){
            this.id = id;
            return this;
        }
        
        public Builder user(final String user){
            this.user = user;
            return this;
        }
        
        public Builder health(final int health){
            this.health = health;
            return this;
        }

        public Builder damage(final int damage){
            this.damage = damage;
            return this;
        }

        public ObjectResponse build() {
            return new ObjectResponse(this);
        }
    }
}
