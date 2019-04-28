package communications;

import game.KillerGame;
import java.io.*;
import java.net.*;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final KillerGame kg;

    private static final String CONNECTION_FROM_CLIENT = "connect";
    private static final String CONNECTION_FROM_PAD = "pad-connect";

    public ConnectionHandler(final Socket socket, final KillerGame kg) {
        this.socket = socket;
        this.kg = kg;
    }

    @Override
    public void run() {
        this.connect();
    }

    private void connect() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            final Message messageReceived = Message.readMessage(in.readLine());

            if (CONNECTION_FROM_CLIENT.equalsIgnoreCase(messageReceived.getCommand())) {
                this.clientConnect(messageReceived);

            } else if (CONNECTION_FROM_PAD.equalsIgnoreCase(messageReceived.getCommand())) {
                this.padConnect(messageReceived);
            }

        } catch (Exception ex) {
            System.out.println("Error: Connection handler Connect");
        }
    }

    private void clientConnect(final Message message) {

        VisualHandler visualHandler = null;
        if (message.isRight()) {
            //TODO cambiar getter
            visualHandler = this.kg.getPk();
        } else {
            visualHandler = this.kg.getNk();
        }

        visualHandler.setSocket(this.socket);
        visualHandler.setDestinationPort(message.getOriginPort());
        
        //TODO enviar configuracion
    }

    private void padConnect(final Message message) {
        //TODO comprobar si se puede crear un mando y responder al PAD
        //TODO implementar metodo crear PAD en KillerGame
        new Thread(new KillerPad(this.kg, this.socket,  
                message.getUserName(),
                message.getColor()
        )).start();
    }
}
