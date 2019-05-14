package communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@JsonInclude(Include.NON_DEFAULT)
public class Message {

    private String command;
    private String senderId;
    private String receiverId;
    private Boolean relay;
    private KillerAction action;
    private ObjectResponse objectResponse;
    private ConnectionResponse connectionResponse;
    private int damage;
    private int serversQuantity;
    private String configRoom;

    private static final String EMPTY_STRING = "";

    public Message() {
    }

    private Message(final Message.Builder builder) {
        this.command = builder.command;
        this.senderId = builder.senderId;
        this.receiverId = builder.receiverId;
        this.relay = builder.relay;
        this.action = builder.action;
        this.objectResponse = builder.objectResponse;
        this.connectionResponse = builder.connectionResponse;
        this.damage = builder.damage;
        this.serversQuantity = builder.serversQuantity;
        this.configRoom = builder.configRoom;
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

    public KillerAction getAction() {
        return action;
    }

    public ObjectResponse getObjectResponse() {
        return objectResponse;
    }
    
    public ConnectionResponse getConnectionResponse(){
        return this.connectionResponse;
    }

    public Boolean isRelay() {
        return relay;
    }

    public int getDamage() {
        return damage;
    }

    public String getConfigRoom() {
        return configRoom;
    }
    
    public int getServersQuantity(){
        return this.serversQuantity;
    }
    
    public void setServersQuantity(final int quantity){
        this.serversQuantity = quantity;
    }
    
    public static Message readMessage(final String jsonStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonStr, Message.class);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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
        private Boolean relay;
        private KillerAction action;
        private ObjectResponse objectResponse;
        private ConnectionResponse connectionResponse;
        private int damage;
        private int serversQuantity;
        private String configRoom;

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

        public Builder withAction(final KillerAction action) {
            this.action = action;
            return this;
        }
        
        public Builder withConnection(final ConnectionResponse connectionResponse) {
            this.connectionResponse = connectionResponse;
            return this;
        }

        public Builder withObject(final ObjectResponse objectResponse) {
            this.objectResponse = objectResponse;
            return this;
        }

        public Builder withRelay(final Boolean isRelay) {
            this.relay = isRelay;
            return this;
        }

        public Builder withDamage(final int damage) {
            this.damage = damage;
            return this;
        }

        public Builder withConfigRoom(final String configRoom) {
            this.configRoom = configRoom;
            return this;
        }
        
        public Builder withServersQuantity(final int quantity){
            this.serversQuantity = quantity;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}