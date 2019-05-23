package communications;

import game.KillerGame;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final KillerGame kg;

    private static final String CONNECTION_FROM_CLIENT = "connect";
    private static final String CLIENT_CONNECTED = "connected";
    private static final String DENYING_CLIENT_CONNECTION = "notConnected";
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

    private synchronized void connect() {
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
            System.out.println("ConnectionHandler -> Error: Connection handler Connect");
            return Message.Builder.builder(EMPTY_STRING, EMPTY_STRING).build();
        }
    }

    private void clientConnect(final ConnectionResponse connectionResponse, final String senderId) {

        final VisualHandler visualHandler = this.getVisualHandler(connectionResponse.isRight());
        if (this.kg.getStatus() == KillerGame.Status.ROOM) {
            if (!visualHandler.isConnected()) {
                this.setVisualHandler(visualHandler, senderId, connectionResponse);
                this.sendSyncRequest();
            } else {
                this.denyingConnection();
            }
        } else {
            this.reconnectVisualHandler(visualHandler, senderId, connectionResponse);
        }
    }

    private VisualHandler getVisualHandler(final boolean isRight) {
        if (isRight) {
            return this.kg.getNextModule();
        }
        return this.kg.getPrevModule();

    }

    private void setVisualHandler(VisualHandler visualHandler, final String senderId, final ConnectionResponse connectionResponse) {

        visualHandler.setSocket(this.socket, senderId);
        visualHandler.setDestinationPort(connectionResponse.getOriginPort());
        visualHandler.updateRoom(true);
        this.sendReplyingMessage();

        //TODO enviar configuracion
    }

    private void sendSyncRequest() {
        final Message message = Message.Builder.builder(SYNC_REQUEST, KillerServer.getId())
                .withServersQuantity(1)
                .build();
        this.kg.getNextModule().sendMessage(message);
    }

    private void sendReplyingMessage() {
        try {
            final PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            final Message message = Message.Builder.builder(CLIENT_CONNECTED, KillerServer.getId()).build();
            out.println(Message.convertMessageToJson(message));
        } catch (Exception ex) {
            System.out.println("ConnectionHandler -> error sending replying connection message");
        }
    }

    private void denyingConnection() {
        try {
            final PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(Message.convertMessageToJson(Message.Builder.builder(DENYING_CLIENT_CONNECTION, KillerServer.getId()).build()));
            System.out.println("ConnectionHandler -> Este visualHandler ya esta conectado");
        } catch (IOException ex) {
        }
    }

    private void reconnectVisualHandler(final VisualHandler visualHandler,
            final String senderId,
            final ConnectionResponse connectionResponse) {
        if (senderId.equalsIgnoreCase(visualHandler.getDestinationId())) {
            this.setVisualHandler(visualHandler, senderId, connectionResponse);
        }
    }

    private void padConnect(final ConnectionResponse connectionResponse, final String senderId) {
        try {
            final PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            final Message message = this.getReplyMessage(connectionResponse, senderId);
            out.println(Message.convertMessageToJson(message));
        } catch (Exception ex) {
            System.out.println("ConnectionHandler -> error sending replying connection message");
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
