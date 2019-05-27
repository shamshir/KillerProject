package communications;

import game.KillerGame;
import visibleObjects.KillerShip;
import java.net.Socket;

public class KillerPad extends ReceptionHandler implements Runnable {

    private KillerClientPad client;
    private final String id;
    private boolean disconnected = false;
    private int disconnectTime = 300;

    private static final String STATUS_REQUEST = "ok";

    private static final String ACTION_COMMAND = "action";
    private static final String DISCONNECTION_COMMAND = "bye";

    public KillerPad(final KillerGame killergame, final Socket sock, final String user, final String color) {
        super(killergame, sock);
        this.id = sock.getInetAddress().getHostAddress();
        this.startClient();
    }

    public String getId() {
        return this.id;
    }
    
    private void startClient() {
        this.client = new KillerClientPad(this);
        new Thread(this.client).start();
    }

    @Override
    public void run() {
        while (!this.disconnected) {
            if (this.getSocket() != null) {
                disconnectTime = 0;
                System.out.println("Killerpad -> PAD-Connected con id: " + this.id);
                this.listeningMessages();
                System.out.println("Killerpad -> PAD-Disconnected con id: " + this.id);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }

            if (disconnectTime-- < 0) {
                this.disconnected = true;
            }
        }
        //TODO eliminar jugador desconectado
    }

    private void listeningMessages() {
        boolean done = false;

        while (!done) {
            try {
                done = !this.processLine(this.readLine());
            } catch (Exception ex) {
                System.out.println("KillerPad -> listeningMessages: " + ex.getMessage());
                done = true;
            }

        }
        this.setSocket(null);
    }

    private boolean processLine(final String line) {
        if (line == null ) {
            return false;
        }
        if(DISCONNECTION_COMMAND.equalsIgnoreCase(line.trim())){
            this.disconnected=true;
            return false;
        }
        if (!STATUS_REQUEST.equalsIgnoreCase(line)) {
            this.processMessage(Message.readMessage(line));
        }
        return true;
    }

    private void processMessage(final Message message) {

        if (ACTION_COMMAND.equalsIgnoreCase(message.getCommand())) {
            sendActionToPlayer(message, this.getKillergame(), true);
        }
    }

    public static void sendActionToPlayer(final Message message,
            final KillerGame kg,
            final boolean sendNextModule) {
        //System.out.println("Killerpad -> ACTION RECIBIDA: " + message.getAction().getCommand() );

        KillerShip player = kg.getShipByIP(message.getSenderId());
        if (player != null && kg.getStatus() == KillerGame.Status.GAME) {
            player.doAction(message.getAction());
        } else if (sendNextModule) {
            kg.getNextModule().sendMessage(message);
        }
    }
}
