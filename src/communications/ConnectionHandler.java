package communications;

import game.KillerGame;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final KillerGame kg;

    private static final String CONNECTION_FROM_CLIENT = "connect";
    private static final String CONNECTION_FROM_PAD = "pad-connect";
    private static final String PAD_CONNECTED = "padConnected";
    private static final String PAD_NOT_CONNECTED = "padNotConnected";
    private static final String SYNC_REQUEST = "sync";
    private static final String EMPTY_STRING = "";

    public ConnectionHandler(final Socket socket, final KillerGame kg) {
        this.socket = socket;
        this.kg = kg;
    }

    @Override
    public void run() {
        this.connect();
    }

    private void connect() {
        final Message messageReceived = this.readConnectionMessage();
        if (CONNECTION_FROM_CLIENT.equalsIgnoreCase(messageReceived.getCommand())) {
            this.clientConnect(messageReceived.getConnectionResponse(), messageReceived.getSenderId());
        } else if (CONNECTION_FROM_PAD.equalsIgnoreCase(messageReceived.getCommand())) {
            this.padConnect(messageReceived.getConnectionResponse(), messageReceived.getSenderId());
        }
    }

    private Message readConnectionMessage() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            return Message.readMessage(in.readLine());
        } catch (Exception ex) {
            System.out.println("Error: Connection handler Connect");
            return Message.Builder.builder(EMPTY_STRING, EMPTY_STRING).build();
        }
    }

    private void clientConnect(final ConnectionResponse connectionResponse, final String senderId) {

        final VisualHandler visualHandler = getVisualHandler(connectionResponse.isRight());
        if (this.kg.getStatus() == KillerGame.Status.ROOM) {
            this.setVisualHandler(visualHandler, connectionResponse);
            this.sendSyncRequest();
        } else {
            if (visualHandler.getDestinationId().equalsIgnoreCase(senderId)) {
                this.setVisualHandler(visualHandler, connectionResponse);
            }
        }
    }

    private VisualHandler getVisualHandler(final boolean isRight) {
        if (isRight) {
            return this.kg.getNextModule();
        }
        return this.kg.getPrevModule();

    }

    private void setVisualHandler(VisualHandler visualHandler, final ConnectionResponse connectionResponse) {

        visualHandler.setSocket(this.socket);
        visualHandler.setDestinationPort(connectionResponse.getOriginPort());
        //TODO enviar configuracion
    }

    private void sendSyncRequest() {
        final Message message = Message.Builder.builder(SYNC_REQUEST, KillerServer.getId())
                .withServersQuantity(1)
                .build();
        this.kg.getNextModule().sendMessage(message);
    }

    private void padConnect(final ConnectionResponse connectionResponse, final String senderId) {
        try {
            final PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            final Message message = this.getReplyMessage(connectionResponse, senderId);
            out.println(Message.convertMessageToJson(message));

        } catch (Exception ex) {
            System.out.println("error sending replying connection message");
        }
    }

    private Message getReplyMessage(final ConnectionResponse connectionResponse, final String senderId) {
        final Message message;
        if (this.kg.newPad(senderId, this.socket, connectionResponse.getUserName(), connectionResponse.getColor())) {
            this.kg.newShip(senderId, Color.decode(connectionResponse.getColor()),
                    connectionResponse.getUserName(), connectionResponse.getShipType());
//            this.kg.newShip(senderId,
//                    connectionResponse.getUserName(), connectionResponse.getShipType());

            message = Message.Builder.builder(PAD_CONNECTED, KillerServer.getId()).build();
            System.out.println("Connectado:" + senderId + " " + connectionResponse.getUserName());
        } else {
            KillerPad pad = this.kg.getPadByIP(senderId);
            if (pad != null) {
                pad.setSocket(socket);
                message = Message.Builder.builder(PAD_CONNECTED, KillerServer.getId()).build();
            } else {
                message = Message.Builder.builder(PAD_NOT_CONNECTED, KillerServer.getId()).build();
            }
        }
        return message;
    }
}
