/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class KillerAction {

    private String direction;
    private int speedX;
    private int speedY;

    public KillerAction(){
        
    }
    
    private KillerAction(final KillerAction.Builder builder){
        this.direction = builder.direction;
        this.speedX = builder.speedX;
        this.speedY = builder.speedY;        
    }

    public String getDirection() {
        return direction;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }
    
    public static class Builder {

        private String direction;
        private int speedX;
        private int speedY;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withDirection(final String direction) {
            this.direction = direction;
            return this;
        }

        public Builder withSpeedX(final int speedX) {
            this.speedX = speedX;
            return this;
        }

        public Builder withSpeedY(final int speedY) {
            this.speedY = speedY;
            return this;
        }

        public KillerAction build() {
            return new KillerAction(this);
        }
    }

}
