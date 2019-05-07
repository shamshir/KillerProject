package communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class ConnectionResponse {

    private boolean right;
    private int originPort;
    private String color;
    private String userName;
    private String shipType;

    public ConnectionResponse() {

    }

    private ConnectionResponse(final ConnectionResponse.Builder builder) {
        this.right = builder.right;
        this.originPort = builder.originPort;
        this.color = builder.color;
        this.userName = builder.userName;
        this.shipType = builder.shipType;
    }

    public boolean isRight() {
        return right;
    }

    public int getOriginPort() {
        return originPort;
    }

    public String getColor() {
        return color;
    }

    public String getUserName() {
        return userName;
    }

    public String getShipType() {
        return shipType;
    }
    
    public static class Builder {

        private boolean right;
        private int originPort;
        private String color;
        private String userName;
        private String shipType;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withRight(final boolean isRight) {
            this.right = isRight;
            return this;
        }

        public Builder withOriginPort(final int originPort) {
            this.originPort = originPort;
            return this;
        }

        public Builder withColor(final String color) {
            this.color = color;
            return this;
        }
        
        public Builder withUserName(final String userName){
            this.userName = userName;
            return this;
        }

        public Builder withShipType(final String shipType){
            this.shipType = shipType;
            return this;
        }
        
        public ConnectionResponse build() {
            return new ConnectionResponse(this);
        }
    }
}
