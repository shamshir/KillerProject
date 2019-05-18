package communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class KillerAction {

    private String command;
    private String direction;
    private double speedX;
    private double speedY;

    public KillerAction(){
        
    }
    
    private KillerAction(final KillerAction.Builder builder){
        this.command = builder.command;
        this.direction = builder.direction;
        this.speedX = builder.speedX;
        this.speedY = builder.speedY;        
    }

    public String getCommand() {
        return command;
    }
    
    public String getDirection() {
        return direction;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }
    
    public static class Builder {

        private String command;
        private String direction;
        private double speedX;
        private double speedY;

        public Builder(final String command) {
            this.command = command;
        }

        public static Builder builder(final String command) {
            return new Builder(command);
        }

        public Builder withDirection(final String direction) {
            this.direction = direction;
            return this;
        }

        public Builder withSpeedX(final double speedX) {
            this.speedX = speedX;
            return this;
        }

        public Builder withSpeedY(final double speedY) {
            this.speedY = speedY;
            return this;
        }

        public KillerAction build() {
            return new KillerAction(this);
        }
    }

}
