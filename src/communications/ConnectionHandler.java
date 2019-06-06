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

    //Lista de comandos posibles
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

    //metodo para procesar la conexión
    private synchronized void connect() {
        final Message messageReceived = this.readConnectionMessage();
        if (CONNECTION_FROM_CLIENT.equalsIgnoreCase(messageReceived.getCommand())) {
            //Conexión de otra pantalla
            this.clientConnect(messageReceived.getConnectionResponse(), messageReceived.getSenderId());
        } else if (CONNECTION_FROM_PAD.equalsIgnoreCase(messageReceived.getCommand())) {
            //Conexión de un PAD
            this.padConnect(messageReceived.getConnectionResponse(), messageReceived.getSenderId());
        }
    }
    
    //metodo para leer el mensaje recibido
    private Message readConnectionMessage() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            return Message.readMessage(in.readLine());
        } catch (Exception ex) {
            System.out.println("ConnectionHandler -> Error: Connection handler Connect");
            return Message.Builder.builder(EMPTY_STRING, EMPTY_STRING).build();
        }
    }

    //Procesar la conexión de un cliente
    private void clientConnect(final ConnectionResponse connectionResponse, final String senderId) {

        final VisualHandler visualHandler = this.getVisualHandler(connectionResponse.isRight());
        if (this.kg.getStatus() == KillerGame.Status.ROOM) {
            if (!visualHandler.isConnected()) {
                //Conexion posible
                this.setVisualHandler(visualHandler, senderId, connectionResponse);
                this.sendSyncRequest();
            } else {
                //Ya esta conectado con otro
                this.denyingConnection();
            }
        } else {
            //Reconexión
            this.reconnectVisualHandler(visualHandler, senderId, connectionResponse);
        }
    }

    //Obtener el VisualHandlerCorrespondiente
    private VisualHandler getVisualHandler(final boolean isRight) {
        if (isRight) {
            return this.kg.getNextModule();
        }
        return this.kg.getPrevModule();

    }
    
    //Establecer la conexión
    private void setVisualHandler(VisualHandler visualHandler, final String senderId, final ConnectionResponse connectionResponse) {

        visualHandler.setSocket(this.socket, senderId);
        visualHandler.setDestinationPort(connectionResponse.getOriginPort());
        visualHandler.updateRoom(true);
        this.sendReplyingMessage();

        //TODO enviar configuracion
    }

    //Enviar petición de syncronización al resto
    private void sendSyncRequest() {
        final Message message = Message.Builder.builder(SYNC_REQUEST, KillerServer.getId())
                .withServersQuantity(1)
                .build();
        this.kg.setWindowNumber(1);
        this.kg.getNextModule().sendMessage(message);
    }

    //Enviar mensaje de respuesta a la conexión
    private void sendReplyingMessage() {
        try {
            final PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            final Message message = Message.Builder.builder(CLIENT_CONNECTED, KillerServer.getId()).build();
            out.println(Message.convertMessageToJson(message));
        } catch (Exception ex) {
            System.out.println("ConnectionHandler -> error sending replying connection message");
        }
    }

    //Denegar la conexión
    private void denyingConnection() {
        try {
            final PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(Message.convertMessageToJson(Message.Builder.builder(DENYING_CLIENT_CONNECTION, KillerServer.getId()).build()));
            System.out.println("ConnectionHandler -> Este visualHandler ya esta conectado");
        } catch (IOException ex) {
        }
    }

    //Reconexion de los modulos solo si contienen la misma ID
    private void reconnectVisualHandler(final VisualHandler visualHandler,
            final String senderId,
            final ConnectionResponse connectionResponse) {
        if (senderId.equalsIgnoreCase(visualHandler.getDestinationId())) {
            this.setVisualHandler(visualHandler, senderId, connectionResponse);
        }
    }

    //Peticion de conexión de un PAD
    private void padConnect(final ConnectionResponse connectionResponse, final String senderId) {
        try {
            final PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            final Message message = this.getReplyMessage(connectionResponse, senderId);
            out.println(Message.convertMessageToJson(message));
        } catch (Exception ex) {
            System.out.println("ConnectionHandler -> error sending replying connection message");
        }
    }

    //Realizar conexión del Pad y obtener mensaje de respuesta
    private Message getReplyMessage(final ConnectionResponse connectionResponse, final String senderId) {
        final Message message;
        if (this.kg.newPad(senderId, this.socket, connectionResponse.getUserName(), connectionResponse.getColor())) {
            //Conexión posible
            this.kg.newShip(senderId, Color.decode(connectionResponse.getColor()),
                    connectionResponse.getUserName(), connectionResponse.getShipType());
            message = Message.Builder.builder(PAD_CONNECTED, KillerServer.getId()).build();
        } else {
            KillerPad pad = this.kg.getPadByIP(senderId);
            if (pad != null) {
                //El pad ya existe, se reestablece la conexion
                pad.setSocket(socket);
                message = Message.Builder.builder(PAD_CONNECTED, KillerServer.getId()).build();
            } else {
                //No es posible conectarse
                message = Message.Builder.builder(PAD_NOT_CONNECTED, KillerServer.getId()).build();
            }
        }
        return message;
    }
}
