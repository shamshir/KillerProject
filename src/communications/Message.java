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
    private KillerAction action;
    private ObjectResponse objectResponse;
    private ConnectionResponse connectionResponse;
    private GameConfiguration gameConfiguration;
    private int health;
    private int serversQuantity;

    private static final String EMPTY_STRING = "";

    public Message() {
    }

    private Message(final Message.Builder builder) {
        this.command = builder.command;
        this.senderId = builder.senderId;
        this.receiverId = builder.receiverId;
        this.action = builder.action;
        this.objectResponse = builder.objectResponse;
        this.connectionResponse = builder.connectionResponse;
        this.health = builder.health;
        this.serversQuantity = builder.serversQuantity;
        this.gameConfiguration = builder.gameConfiguration;
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

    public int getHealth() {
        return health;
    }
    
    public int getServersQuantity(){
        return this.serversQuantity;
    }
    
    public void setServersQuantity(final int quantity){
        this.serversQuantity = quantity;
    }
    
    public GameConfiguration getGameConfiguration(){
        return this.gameConfiguration;
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
         
    public static Message buildInfoMessageToPad(final String command , final String padIp){
        return Message.Builder.builder(command, KillerServer.getId())
                .withReceiverId(padIp)
                .build();
    }
    
    public static Message buildHealthMessageToPad(final String command, final String padIp, final int health){
        return Message.Builder.builder(command, KillerServer.getId())
                .withReceiverId(padIp)
                .withHealth(health)
                .build();
    }

    public static class Builder {

        private String command;
        private String senderId;
        private String receiverId;
        private KillerAction action;
        private ObjectResponse objectResponse;
        private ConnectionResponse connectionResponse;
        private GameConfiguration gameConfiguration;
        private int health;
        private int serversQuantity;

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

        public Builder withHealth(final int health) {
            this.health = health;
            return this;
        }

        public Builder withGameConfiguration(final GameConfiguration gameConfiguration) {
            this.gameConfiguration = gameConfiguration;
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