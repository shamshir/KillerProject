package communications;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ObjectResponse {

    private String objectType;
    private double posicionX;
    private double posicionYInPercent;
    private int height;
    private int width;
    private double speedX;
    private double speedY;
    private String color;
    private String ip;
    private String userName;
    private int life;
    
    private static final String EMPTY_STRING = "";
    private static final String SHOOT_OBJECT = "shoot";
    
    public ObjectResponse(){
        
    }
    
    private ObjectResponse(final ObjectResponse.Builder builder){ 
        this.objectType = builder.objectType;
        this.posicionX = builder.posicionX;
        this.posicionYInPercent = builder.posicionYInPercent;
        this.height = builder.height;
        this.width = builder.width;
        this.speedX = builder.speedX;
        this.speedY = builder.speedY;
        this.color = builder.color;
        this.userName = builder.userName;
        this.ip = builder.ip;
        this.life = builder.life;
    }
    
    public String getObjectType(){
        return this.objectType;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public double getPosicionYInPercent() {
        return posicionYInPercent;
    }

    public int getHEIGHT() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public String getColor() {
        return color;
    }

    public String getIp() {
        return ip;
    }

    public String getUserName() {
        return userName;
    }

    public int getLife() {
        return life;
    }

    public static class Builder {

        private String objectType;
        private double posicionX;
        private double posicionYInPercent;
        private int height;
        private int width;
        private double speedX;
        private double speedY;
        private String color;
        private String ip;
        private String userName;
        private int life;
        
        public Builder(final String objectType) {
            this.objectType = objectType;
        }
        
        public static Builder builder(final String objectType){     
            return new Builder(objectType);
        }
        
        public Builder withPosicionX( final double posicionX) {
            this.posicionX = posicionX;
            return this;
        }

        public Builder withPosicionYInPercent( final double posicionYInPercent) {
            this.posicionYInPercent = posicionYInPercent;
            return this;
        }

        public Builder withHeight( final int height) {
            this.height = height;
            return this;
        }
        
        public Builder withWidth( final int width) {
            this.width = width;
            return this;
        }

        public Builder withSpeedX( final double speedX) {
            this.speedX = speedX;
            return this;
        }
        
        public Builder withSpeedY( final double speedY) {
            this.speedY = speedY;
            return this;
        }

        public Builder withColor( final String color) {
            this.color = color;
            return this;
        }

        public Builder withUserName( final String name) {
            this.userName = name;
            return this;
        }

        public Builder withIP( final String ip) {
            this.ip = ip;
            return this;
        }
        
        public Builder withLife( final int life) {
            this.life = life;
            return this;
        }
        
        public ObjectResponse build(){
            return new ObjectResponse(this);            
        }
    }
}
