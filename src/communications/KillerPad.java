package communications;

import game.KillerGame;
import visibleObjects.KillerShip;
import java.net.Socket;

public class KillerPad extends ReceptionHandler implements Runnable {

    private KillerClientPad client;
    private final String id;
    private final String user;
    private boolean disconnected = false;
    private int disconnectTime;

    private static final String STATUS_REQUEST = "ok";

    private static final String ACTION_COMMAND = "action";
    private static final String DISCONNECTION_COMMAND = "bye";

    public KillerPad(final KillerGame killergame, final Socket sock, final String user, final String color) {
        super(killergame, sock);
        this.user = user;
        this.id = sock.getInetAddress().getHostAddress();
    }
    

    public String getId() {
        return this.id;
    }

    public String getUser() {
        return this.user;
    }
    
    @Override
    public void run() {
        while (!this.disconnected) {
            if (this.getSocket() != null) {
                this.disconnectTime = 200;
                System.out.println("Killerpad -> PAD-Connected con id: " + this.id);
                this.listeningMessages();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            if (this.getKillergame().getStatus() == KillerGame.Status.ROOM) {
                this.disconnected = true;
            } else {
                this.disconnectTime--;
                if (this.disconnectTime < 0) {
                    this.disconnected = true;
                }
            }
        }
        System.out.println("Killerpad -> PAD-Disconnected con id: " + this.id);
        if (this.getKillergame().getStatus() == KillerGame.Status.GAME) {
            this.getKillergame().getNextModule().sendPadDecrement();
        }
        this.getKillergame().removePad(this);
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
        if (line == null) {
            return false;
        }
        if (DISCONNECTION_COMMAND.equalsIgnoreCase(line.trim())) {
            this.disconnected = true;
            return false;
        }
        if (STATUS_REQUEST.equalsIgnoreCase(line)) {
            this.sendLine(STATUS_REQUEST);
        } else {
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
        KillerShip player = kg.getShipByIP(message.getSenderId());
        if (player != null && kg.getStatus() == KillerGame.Status.GAME) {
            player.doAction(message.getAction());
        } else if (sendNextModule) {
            kg.getNextModule().sendMessage(message);
        }
    }

    public void closeSocket() {
        try {
            this.sendLine(DISCONNECTION_COMMAND);
            this.disconnected = true;
            this.getSocket().close();
        } catch (Exception ex) {
            System.out.println("KillerPad -> ya cerrado");
        }
    }

    @Override
    public void sendMessage(final Message message) {
        System.out.println("KillerPad -> " + message.getCommand() + " sended to " + message.getReceiverId());
        super.sendMessage(message);
        if (message.getCommand().equalsIgnoreCase("pad_dead")) {
            this.closeSocket();
        }
    }
}