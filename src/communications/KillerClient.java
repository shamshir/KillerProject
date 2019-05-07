package communications;

import game.KillerGame;
import java.io.PrintWriter;
import java.net.*;

public class KillerClient implements Runnable {

    private final ReceptionHandler receptionHandler;
    private final KillerGame killergame;

    private static final String CONNECT_TO_CLIENT = "connect";
    private static final String CONNECT_TO_PAD = "pad-connect";
    private static final String STATUS_REQUEST = "ok";

    public KillerClient(ReceptionHandler receptionHandler, KillerGame killergame) {
        this.receptionHandler = receptionHandler;
        this.killergame = killergame;
    }

    @Override
    public void run() {

        while (true) {
            if (this.disconnected()) {
                this.tryToConnect();
            } else {
                this.sendStatusRequest();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    private boolean disconnected(){
        return this.receptionHandler.getSocket() == null;
    } 
    
    private void tryToConnect() {
        try {
            final Socket sock = new Socket(this.receptionHandler.getDestinationIp(), this.receptionHandler.getDestinationPort());
            this.contact(sock);
            this.receptionHandler.setSocket(sock);

        } catch (Exception ex) {
        }
    }

    private void contact(final Socket sock) throws Exception {

        final ConnectionResponse connectionResponse = ConnectionResponse.Builder.builder()
                .withRight(((VisualHandler) this.receptionHandler).isRight())
                .withOriginPort(this.killergame.getServer().getPort())
                .build();
        
        final Message message = Message.Builder.builder(CONNECT_TO_CLIENT, KillerServer.getId())
                .withConnection(connectionResponse)
                .build();

        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        out.println(Message.convertMessageToJson(message));
    }

    private void sendStatusRequest() {
        this.receptionHandler.sendLine(STATUS_REQUEST);
    }

}
