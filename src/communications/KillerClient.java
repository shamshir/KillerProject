package communications;

import game.KillerGame;
import java.io.PrintWriter;
import java.net.*;

public class KillerClient implements Runnable {

    private final VisualHandler visualHandler;
    private final KillerGame killergame;
    private int syncTimeOut;

    private static final String CONNECT_TO_CLIENT = "connect";
    private static final String SYNC_CHECK = "sync-check";
    private static final String STATUS_REQUEST = "ok";

    public KillerClient(VisualHandler visualHandler, KillerGame killergame) {
        this.visualHandler = visualHandler;
        this.killergame = killergame;
        this.syncTimeOut = 0;
    }

    public void resetSyncTimeOut() {
        this.syncTimeOut = 0;
    }

    @Override
    public void run() {
        for (int i = 0; true; i = (i % 3) + 1) {
            if (this.disconnected()) {
                this.tryToConnect();
            } else {
                this.sendStatusRequest();
            }
            this.checkSyncTimeOut(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    private boolean disconnected() {
        return this.visualHandler.getSocket() == null;
    }

    private void tryToConnect() {
        try {
            final Socket sock = new Socket(this.visualHandler.getDestinationIp(), this.visualHandler.getDestinationPort());
            this.contact(sock);
            this.visualHandler.setSocket(sock);
            this.syncTimeOut=0;
        } catch (Exception ex) {
        }

    }

    private void contact(final Socket sock) throws Exception {

        final ConnectionResponse connectionResponse = ConnectionResponse.Builder.builder()
                .withRight(!this.visualHandler.isRight())
                .withOriginPort(this.killergame.getServer().getPort())
                .build();

        final Message message = Message.Builder.builder(CONNECT_TO_CLIENT, KillerServer.getId())
                .withConnection(connectionResponse)
                .build();

        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        out.println(Message.convertMessageToJson(message));
    }

    private void sendStatusRequest() {
        this.visualHandler.sendLine(STATUS_REQUEST);
    }

    private void checkSyncTimeOut(final int i) {
        if (this.killergame.getStatus() == KillerGame.Status.room
                && this.killergame.isSynchronized()) {
            if (i >= 3 && !this.disconnected()) {
                this.sendSyncCheck();
            }
            if (this.syncTimeOut >= 10) {
                this.syncTimeOut();
            }
            this.syncTimeOut++;
        }
    }

    private void sendSyncCheck() {
        final Message message = Message.Builder.builder(SYNC_CHECK, KillerServer.getId())
                .build();
        this.visualHandler.sendMessage(message);

    }

    private void syncTimeOut() {
        this.killergame.setSyncronized(false);
        this.killergame.setServersQuantity(0);
    }
}
