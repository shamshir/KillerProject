package communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(Include.NON_DEFAULT)
public class Message {

    private String command;
    private String senderId;
    private String receiverId;
    private String action;
    private String object;
    private Boolean relay;
    private String color;
    private String userName;
    private String shipType;
    private String direction;
    private int velX;
    private int velY;
    private int damage;
    private boolean right;
    private String configRoom;
    private int originPort;

    private static final String EMPTY_STRING = "";

    public Message() {
    }

    private Message(final Message.Builder builder) {
        this.command = builder.command;
        this.senderId = builder.senderId;
        this.receiverId = builder.receiverId;
        this.action = builder.action;
        this.object = builder.object;
        this.relay = builder.right;
        this.color = builder.color;
        this.userName = builder.userName;
        this.shipType = builder.shipType;
        this.direction = builder.direction;
        this.velX = builder.velX;
        this.velY = builder.velY;
        this.damage = builder.damage;
        this.right = builder.right;
        this.configRoom = builder.configRoom;
        this.originPort = builder.originPort;
    }

    public String getCommand() {
        return command;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getAction() {
        return action;
    }

    public String getObject() {
        return object;
    }

    public Boolean isRelay() {
        return relay;
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

    public String getDirection() {
        return direction;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isRight() {
        return right;
    }

    public String getConfigRoom() {
        return configRoom;
    }

    public int getOriginPort() {
        return originPort;
    }

    public static Message readMessage(final String jsonStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonStr, Message.class);
        } catch (Exception ex) {
            System.out.println("MESSAGE: Error leer mensaje");
            return Message.Builder.builder(EMPTY_STRING, EMPTY_STRING).build();
        }

    }

    public static String convertMessageToJson(final Message message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(message);
        } catch (Exception ex) {
            System.out.println("Error al convertir mensaje a Json");
        }
        return EMPTY_STRING;
    }

    public static class Builder {

        private String command;
        private String senderId;
        private String receiverId;
        private String action;
        private String object;
        private boolean relay;
        private String color;
        private String userName;
        private String shipType;
        private String direction;
        private int velX;
        private int velY;
        private int damage;
        private boolean right;
        private String configRoom;
        private int originPort;

        public Builder(final String command, final String senderId) {
            this.command = command;
            this.senderId = senderId;
        }

        public static Builder builder(final String command, final String senderId) {
            return new Builder(command, senderId);
        }

        public Builder withReceiverId(final String receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        public Builder withAction(final String action) {
            this.action = action;
            return this;
        }

        public Builder withObject(final String object) {
            this.object = object;
            return this;
        }

        public Builder withRelay(final Boolean isRelay) {
            this.right = isRelay;
            return this;
        }

        public Builder withColor(final String color) {
            this.color = color;
            return this;
        }

        public Builder withUserName(final String name) {
            this.userName = name;
            return this;
        }

        public Builder withShipType(final String shipType) {
            this.shipType = shipType;
            return this;
        }

        public Builder withDirection(final String direction) {
            this.direction = direction;
            return this;
        }

        public Builder withVelX(final int velX) {
            this.velX = velX;
            return this;
        }

        public Builder withVelY(final int velY) {
            this.velY = velY;
            return this;
        }

        public Builder withDamage(final int damage) {
            this.damage = damage;
            return this;
        }

        public Builder withRight(final boolean isRight) {
            this.right = isRight;
            return this;
        }

        public Builder withConfigRoom(final String configRoom) {
            this.configRoom = configRoom;
            return this;
        }

        public Builder withOriginPort(final int originPort) {
            this.originPort = originPort;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
