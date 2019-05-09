package communications;

import communications.KillerClient;
import visibleObjects.Automata;
import visibleObjects.Controlled;
import game.KillerGame;
import java.net.Socket;
import visibleObjects.Shoot;
import visibleObjects.Alive;

public class VisualHandler extends ReceptionHandler implements Runnable {

    private KillerClient client;
    private final boolean right;

    private static final String EMPTY_STRING = "";

    private static final String STATUS_REQUEST = "ok";

    private static final String SEND_OBJECT_COMMAND = "object";
    private static final String START_GAME = "start";
    private static final String READY_TO_START = "ready";
    private static final String QUIT_GAME = "quit";
    private static final String PAD_COMMAND = "pad(.*)";
    private static final String DAMAGE_COMMAND = "pad_damage";
    private static final String DEATH_COMMAND = "pad_death";
    private static final String KILL_COMMAND = "pad_kill";
    private static final String ACTION_COMMAND = "action";

    private static final String SHOOT_TYPE = "shoot";

    public VisualHandler(final KillerGame killergame, final boolean right) {
        super(killergame);
        this.right = right;
        this.startClient();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (this.getSocket() != null) {
                    System.out.println("Connected is right:" + right);
                    this.listeningMessages();
                    System.out.println("Disconnected is right:" + right);
                }
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
        }
    }

    public boolean isRight() {
        return this.right;
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
        if (line == null) {
            return false;
        }
        if (!STATUS_REQUEST.equalsIgnoreCase(line)) {
            this.processMessage(Message.readMessage(line));
        }
        return true;
    }

    private void processMessage(final Message message) {

        switch (message.getCommand()) {
            case SEND_OBJECT_COMMAND:
                this.receiveObject(message.getObjectResponse());
                break;
            case START_GAME:
                this.processStart(message);
                break;
            case READY_TO_START:
                this.processReady(message);
                break;
            case QUIT_GAME:
                this.processQuitGame(message);
                break;
            case ACTION_COMMAND:
                this.processPadCommand(message);
                break;
            default:
                final String command = message.getCommand();
                if (command != null && command.matches(PAD_COMMAND)) {
                    if (!KillerServer.getId().equals(message.getSenderId())) {
                        this.sendInfoMessageToPad(message);
                    }
                } else {
                    System.out.println("COMANDO DESCONOCIDO");
                }
                break;
        }
    }

    private void processPadCommand(final Message message) {

        boolean sendNextModule = this.getKillergame().getPadByIP(message.getSenderId()) == null;
        KillerPad.sendActionToPlayer(message, this.getKillergame(), sendNextModule);
    }

    private void receiveObject(final ObjectResponse object) {

        switch (object.getObjectType()) {
            case SHOOT_TYPE:
                //TODO crear bala con los datos del response
                //new (object.getId)
                break;
            default:
                System.out.println("ERROR: OBJETO DESCONOCIDO" + object.getObjectType());
                break;

            //TODO los demas objetos
        }
    }

    public void sendObject(final Alive object) {
        final Message message = Message.Builder.builder(SEND_OBJECT_COMMAND, KillerServer.getId())
                .withObject(this.convertObjectToObjectResponse(object))
                .build();
        this.sendMessage(message);
    }

    private void startClient() {
        this.client = new KillerClient(this, this.getKillergame());
        new Thread(this.client).start();
    }

    @Override
    public synchronized Socket getSocket() {
        return super.getSocket();
    }

    @Override
    public synchronized boolean setSocket(final Socket sock) {

        if (super.setSocket(sock)) {

            //TODO Llamar método para actualizar el panel
            try {
                this.getSocket().setSoTimeout(3500);
            } catch (Exception ex) {
            }
            return true;
        }
        return false;
    }

    private ObjectResponse convertObjectToObjectResponse(final Alive object) {
        //TODO rellenar con los datos que se pida
        if (object instanceof Shoot) {
            return this.buildObjectResponseFromShoot((Shoot) object);
        }
        return ObjectResponse.Builder.builder(EMPTY_STRING).build();
    }

    private ObjectResponse buildObjectResponseFromShoot(final Shoot shoot) {
        return ObjectResponse.Builder.builder(SHOOT_TYPE)
                .withPosicionYInPercent(shoot.y / shoot.getKg().getViewer().getHeight())
                .build();
    }

    public void startGame() {
        this.sendMessage(Message.Builder.builder(READY_TO_START, KillerServer.getId()).build());
    }

    private void processReady(final Message message) {
        if (KillerServer.getId().equals(message.getSenderId())) {
            this.sendMessage(Message.Builder.builder(START_GAME, KillerServer.getId()).build());
        } else {
            this.getKillergame().getNextModule().sendMessage(message);
        }
    }

    private void processStart(final Message message) {
        if (!KillerServer.getId().equals(message.getSenderId())) {
            this.sendMessage(message);
            this.getKillergame().start();
        }
    }

    private void processQuitGame(final Message message){
        if (!KillerServer.getId().equals(message.getSenderId())) {
            this.sendMessage(message);
           //TODO this.getKillergame().quitGame();
        }
    }

    public void sendInfoMessageToPad(final Message message) {
        final KillerPad pad = this.getKillergame().getPadByIP(message.getReceiverId());
        if (pad != null) {
            pad.sendMessage(message);
        } else {
            this.getKillergame().getNextModule().sendMessage(message);
        }
    }
}
