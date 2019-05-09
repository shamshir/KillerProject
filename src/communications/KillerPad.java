package communications;

import game.KillerGame;
import visibleObjects.KillerShip;
import java.net.Socket;

public class KillerPad extends ReceptionHandler implements Runnable {

    private KillerClient client;
    private final String id;
    private boolean disconnected = false;
    private int disconnectTime = 300;

    private static final String EMPTY_STRING = "";

    private static final String STATUS_REQUEST = "ok";

    private static final String ACTION_COMMAND = "action";
    private static final String DAMAGE_COMMAND = "pad_damage";
    private static final String DEATH_COMMAND = "pad_death";
    private static final String KILL_COMMAND = "pad_kill";
    private static final String MOVEMENT_COMMAND = "pad_move";
    private static final String SHOOT_COMMAND = "pad_shoot";
    private static final String DASH_COMMAND = "pad_dash";
    private static final String POWERUP_COMMAND = "pad_powerup";
    private static final String TURBO_COMMAND = "pad_turbo";
    private static final String DISCONNECTION_COMMAND = "bye";

    public KillerPad(final KillerGame killergame, final Socket sock, final String user, final String color) {
        super(killergame, sock);
        this.id = sock.getInetAddress().getHostAddress();
    }

    public String getId() {
        return this.id;
    }

    @Override
    public void run() {
        while (!disconnected) {
            if (this.getSocket() != null) {
                disconnectTime = 0;
                System.out.println("PAD-Connected");
                this.listeningMessages();
                System.out.println("PAD-DesConnected");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}

            if(disconnectTime-- <0){
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
                System.out.println(ex.getMessage());
                done = true;
            }

        }
        this.setSocket(null);
    }

    private boolean processLine(final String line) {
        if (line == null || line.trim().equals(DISCONNECTION_COMMAND)) {
            return false;
        }
        if (!STATUS_REQUEST.equalsIgnoreCase(line)) {
            this.processMessage(Message.readMessage(line));
        }else{
            this.sendLine(STATUS_REQUEST);
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
        System.out.println("ACTION RECIBIDA: "+message.getAction().getCommand()+ "   "+ message.getCommand());


        KillerShip player = kg.getShipByIP(message.getSenderId());
        if (player != null) {
            player.doAction(message.getAction());
        } else if (sendNextModule) {
            sendPadCommandToNextModule(message, kg);
        }
    }

    private static void sendPadCommandToNextModule(final Message message, final KillerGame kg) {

        //final Message messageToSend = buildMessageWithRelay(message);
        kg.getNextModule().sendMessage(message);

    }

    /* private static Message buildMessageWithRelay(final Message message){
        if (message.isRelay()) {
            return  message;
        } else {
            return Message.Builder.builder(message.getCommand(), KillerServer.getId())
                    .withAction(message.getAction())
                    .withRelay(Boolean.TRUE)
                    .withDamage(message.getDamage())
                    .build();
        }
    }*/
}
