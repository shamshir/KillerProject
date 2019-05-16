package communications;

import communications.KillerClient;
import visibleObjects.Automata;
import visibleObjects.Controlled;
import game.KillerGame;
import java.net.Socket;
import visibleObjects.Shoot;
import visibleObjects.Alive;
import visibleObjects.KillerShip;

public class VisualHandler extends ReceptionHandler implements Runnable {

    private KillerClient client;
    private final boolean right;
    private String destinationId;
    private static final String EMPTY_STRING = "";

    private static final String STATUS_REQUEST = "ok";

    private static final String SEND_OBJECT_COMMAND = "object";
    private static final String SYNC_REQUEST = "sync";
    private static final String SYNC_CONFIRMATION = "sync-confirm";
    private static final String SYNC_CHECK = "sync-check";
    private static final String START_GAME = "start";
    private static final String QUIT_GAME = "quit";
    private static final String PAD_COMMAND = "pad(.*)";
    private static final String DAMAGE_COMMAND = "pad_damage";
    private static final String DEATH_COMMAND = "pad_death";
    private static final String KILL_COMMAND = "pad_kill";
    private static final String GET_POWERUP_COMMAND = "pad_get_powerup";
    private static final String ACTION_COMMAND = "action";

    private static final String SHOOT_TYPE = "shoot";
    private static final String SHIP_TYPE = "ship";

    public VisualHandler(final KillerGame killergame, final boolean right) {
        super(killergame);
        this.right = right;
        this.startClient();
    }

    public boolean isRight() {
        return this.right;
    }

    public String getDestinationId() {
        return this.destinationId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (this.getSocket() != null) {
                    this.listeningMessages();
                }
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
        }
    }

    private void listeningMessages() {
        System.out.println("Connected is right:" + right);
        this.updateRoom(true);
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
        this.updateRoom(false);
        System.out.println("Disconnected is right:" + right);
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
            case QUIT_GAME:
                this.processQuitGame(message);
                break;
            case ACTION_COMMAND:
                this.processPadCommand(message);
                break;
            case SYNC_REQUEST:
                this.processSyncRequest(message.getSenderId(), message.getServersQuantity());
                break;
            case SYNC_CONFIRMATION:
                this.processSyncConfirmation(message);
                break;
            case SYNC_CHECK:
                this.processSyncCheck(message);
                break;
            default:
                final String command = message.getCommand();
                if (command != null && command.matches(PAD_COMMAND)) {
                    if (!this.isMessageMine(message.getSenderId())) {
                        this.processInfoMessageToPad(message);
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
            case SHIP_TYPE:
                this.createShip(object);
                break;
            case SHOOT_TYPE:
                this.createShoot(object);
                break;
            default:
                System.out.println("ERROR: OBJETO DESCONOCIDO" + object.getObjectType());
                break;

            //TODO los demas objetos
        }
    }

    public void sendObject(final Alive object) {
        final Message message = Message.Builder.builder(SEND_OBJECT_COMMAND, KillerServer.getId())
                .withObject(ObjectResponse.convertObjectToObjectResponse(object))
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

    public synchronized boolean setSocket(final Socket sock, final String destinationId) {

        if (this.setSocket(sock)) {
            this.destinationId = destinationId;
            try {
                this.getSocket().setSoTimeout(3500);
            } catch (Exception ex) {
            }
            return true;
        }
        return false;
    }

    public void sendStart() {
        this.sendMessage(Message.Builder.builder(START_GAME, KillerServer.getId()).build());
    }

    private void processStart(final Message message) {
        if (!isMessageMine(message.getSenderId())) {
            this.sendMessage(message);
        }
        this.getKillergame().startGame();
    }

    private void processQuitGame(final Message message) {
        if (!isMessageMine(message.getSenderId())) {
            this.sendMessage(message);
            //TODO this.getKillergame().quitGame();
        }
    }

    private void processInfoMessageToPad(final Message message) {
        final KillerPad pad = this.getKillergame().getPadByIP(message.getReceiverId());
        if (pad != null) {
            pad.sendMessage(message);
        } else {
            this.getKillergame().getNextModule().sendMessage(message);
        }
    }    
    
    public void sendInfoMessageToPad(final String command, final String padIp){
        this.processInfoMessageToPad(Message.buildInfoMessageToPad(command, padIp));        
    }
    
    public void sendInfoDamageMessageToPad(final String padIp, final int damage){
        this.processInfoMessageToPad(Message.buildDamageMessageToPad(DAMAGE_COMMAND, padIp, damage));     
    }
    
    private void processSyncRequest(final String senderId, final int quantity) {
        final Message messageToSend;
        if (this.isMessageMine(senderId)) {
            this.client.resetSyncTimeOut();
            this.getKillergame().setSyncronized(true);
            this.getKillergame().setServersQuantity(quantity);
            messageToSend = Message.Builder.builder(SYNC_CONFIRMATION, senderId)
                    .withServersQuantity(quantity)
                    .build();
        } else {
            messageToSend = Message.Builder.builder(SYNC_REQUEST, senderId)
                    .withServersQuantity(quantity + 1)
                    .build();
        }
        this.getKillergame().getNextModule().sendMessage(messageToSend);
    }

    private void processSyncConfirmation(final Message message) {
        if (!this.isMessageMine(message.getSenderId())) {
            this.getKillergame().getNextModule().sendMessage(message);
            this.client.resetSyncTimeOut();
            this.getKillergame().setSyncronized(true);
            this.getKillergame().setServersQuantity(message.getServersQuantity());
        }
    }

    private void processSyncCheck(final Message message) {
        if (!this.isMessageMine(message.getSenderId())) {
            this.getKillergame().getNextModule().sendMessage(message);
        }
        this.client.resetSyncTimeOut();
    }

    private boolean isMessageMine(final String id) {
        return KillerServer.getId().equals(id);
    }

    private void createShip(ObjectResponse object) {
        this.getKillergame().reciveKillerShip(object.getX(), object.getY(), object.getRadians(),
                object.getDx(), object.getDy(),
                object.getVx(), object.getVy(),
                object.getTx(), object.getTy(),
                object.getLx(), object.getLy(),
                object.getRx(), object.getRy(),
                object.getId(), object.getUser(),
                object.getType(), object.getHealth());
    }

    private void createShoot(ObjectResponse object) {
        this.getKillergame().reciveShoot(object.getX(), object.getY(), object.getRadians(),
                object.getDx(), object.getDy(), object.getId());
    }
    
    private void updateRoom(boolean connected){
        if(this.right){
            //TODO this.getKillergame().getRoom().setFeedbackConnetionRight(connected);
        }else{
            //TODO this.getKillergame().getRoom().setFeedbackConnetionLeft(connected);            
        }
    }
}
