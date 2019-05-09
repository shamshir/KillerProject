package communications;

import game.KillerGame;
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
            this.clientConnect(messageReceived.getConnectionResponse());
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

    private void clientConnect(final ConnectionResponse connectionResponse) {

        final VisualHandler visualHandler = getVisualHandler(connectionResponse.isRight());

        visualHandler.setSocket(this.socket);
        visualHandler.setDestinationPort(connectionResponse.getOriginPort());
        //TODO enviar configuracion
    }

    private VisualHandler getVisualHandler(final boolean isRight) {
        if (isRight) {
            return this.kg.getNextModule();
        }
        return this.kg.getPrevModule();

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
        if (this.kg.newKillerPad( this.socket, connectionResponse.getColor(), connectionResponse.getUserName(), senderId)) {
            this.kg.newKillerShip(senderId);
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
