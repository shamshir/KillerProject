package communications;

import game.KillerGame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final KillerGame kg;

    private static final String CONNECTION_FROM_CLIENT = "connect";
    private static final String CONNECTION_FROM_PAD = "pad-connect";
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
            this.padConnect(messageReceived.getConnectionResponse());
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

        VisualHandler visualHandler = null;
        if (connectionResponse.isRight()) {
            //TODO crear SETUP 
            visualHandler = this.kg.getPk();
        } else {
            visualHandler = this.kg.getNk();
        }

        visualHandler.setSocket(this.socket);
        visualHandler.setDestinationPort(connectionResponse.getOriginPort());

        //TODO enviar configuracion
    }

    private void padConnect(final ConnectionResponse connectionResponse) {
        //TODO comprobar si se puede crear un mando y responder al PAD
        //TODO implementar metodo crear PAD en KillerGame

    }
}
