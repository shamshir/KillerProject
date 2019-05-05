package communications;

import visibleObjects.Controlled;
import game.KillerGame;
import java.net.Socket;

public class KillerPad extends ReceptionHandler implements Runnable {

    private KillerClient client;

    private static final String EMPTY_STRING = "";

    private static final String STATUS_REQUEST = "ok";

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
        //TODO crear metodo crear controlled en killergame    ( crear controlled y thread del pad)     
    }

    @Override
    public void run() {
        try {

            this.listeningMessages();

        } catch (Exception ex) {

        }

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
            processMessage(Message.readMessage(line));
        }
        return true;
    }

    public static void processMessage(final Message message) {

        Controlled player = null;
        // player = findShip(this.getDestinationIp());
        if (player != null) {
            switch (message.getCommand()) {
                //TODO 
                case DEATH_COMMAND:
                    killPlayer();
                    break;
                case KILL_COMMAND:
                    sumKillToPlayer();
                    break;
                case MOVEMENT_COMMAND:
                    movePlayer();
                    break;
                case SHOOT_COMMAND:
                    shoot();
                    break;
                case DASH_COMMAND:
                    dash();
                    break;
                case POWERUP_COMMAND:
                    powerUp();
                    break;
                case TURBO_COMMAND:
                    turbo();
                    break;
                default:
                    System.out.println("ERROR: MENSAJE DESCONOCIDO" + message.getCommand());
                    break;
            }
        } else {
            sendPadCommandToTheNextVisualHandler(message);
        }
    }

    private static void sendPadCommandToTheNextVisualHandler(final Message message) {
        
        final Message messageToSend;
        
        if (message.isRelay()) {
            messageToSend = message;
        } else {
            messageToSend = Message.Builder.builder(message.getCommand(), KillerServer.getId())
                    .withAction(message.getAction())
                    .withRelay(Boolean.TRUE)
                    .build();
        }
        //TODO llamar al nextVisualHandler.sendMessage()
    }

    private static void killPlayer() {

    }

    private static void sumKillToPlayer() {

    }

    private static void movePlayer() {

    }

    private static void shoot() {

    }

    private static void dash() {

    }

    private static void powerUp() {

    }

    private static void turbo() {

    }
}
