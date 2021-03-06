package communications;

import game.KillerGame;
import java.awt.Color;
import java.net.Socket;
import visibleObjects.Alive;

public class VisualHandler extends ReceptionHandler implements Runnable {

    private KillerClient client;
    private final boolean right;
    private String destinationId;
    private static final String EMPTY_STRING = "";

    private static final String STATUS_REQUEST = "ok";
    private static final String BYE_LINE = "bye";

    private static final String SEND_OBJECT_COMMAND = "object";
    private static final String CLIENT_CONNECTED = "connected";
    private static final String CLIENT_NOT_CONNECTED = "notConnected";
    private static final String SYNC_REQUEST = "sync";
    private static final String SYNC_CONFIRMATION = "sync-confirm";
    private static final String SYNC_CHECK = "sync-check";
    private static final String START_GAME = "start";
    private static final String QUIT_GAME = "quit";
    private static final String PAD_COMMAND = "pad(.*)";
    private static final String HEALTH_COMMAND = "pad_health";
    private static final String DEAD_COMMAND = "pad_dead";
    private static final String KILL_COMMAND = "pad_kill";
    private static final String ACTION_COMMAND = "action";
    private static final String DECREMENT_PADS_NUM = "decrementPadsNum";
    private static final String WIN_COMMAND = "win";
    private static final String PAD_WIN_COMMAND = "pad_win";
    private static final String GAME_CONFIGURATION_COMMAND = "gameConfiguration";
    private static final String WINNER_COMMAND = "winner";
    private static final String RESET_KILLERGAME_COMMAND = "reset";

    private static final String SHIP_TYPE = "ship";
    private static final String PACMAN_TYPE = "pacman";
    private static final String ASTEROID_TYPE = "asteroid";

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

    public void setDestinationId(final String id) {
        this.destinationId = id;
    }

    @Override
    public void setDestinationIp(final String ip) {
        super.setDestinationIp(ip);
        this.closeSocket();
    }

    @Override
    public void setDestinationPort(final int port) {
        super.setDestinationPort(port);
    }

    public KillerClient getClient() {
        return this.client;
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
        boolean done = false;

        while (!done) {
            try {
                done = !this.processLine(this.readLine());
            } catch (Exception ex) {
                this.setSocket(null, this.destinationId);
                done = true;
                System.out.println("VisualHandler -> Error: " + ex.getMessage());
            }                
        }
        this.setSocket(null);
        this.updateRoom(false);
    }

    private boolean processLine(final String line) {
        if (line == null) {
            return false;
        }
        if (BYE_LINE.equalsIgnoreCase(line)) {
            this.disconnect();
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
            case CLIENT_CONNECTED:                
                this.sendGameConfiguration(this.getKillergame().getConfiguration());
                this.destinationId = message.getSenderId();
                this.updateRoom(true);
                break;
            case CLIENT_NOT_CONNECTED:
                this.disconnect();
                break;
            case DECREMENT_PADS_NUM:
                this.processPadDecrement(message);
                break;
            case WIN_COMMAND:
                this.processWin(message);
                break;
            case GAME_CONFIGURATION_COMMAND:
                this.processGameConfiguration(message);
                break;
            case WINNER_COMMAND:
                this.processWinner(message);
                break;
            case RESET_KILLERGAME_COMMAND:
                this.processReset(message);
                break;
            default:
                final String command = message.getCommand();
                if (command != null && command.matches(PAD_COMMAND)) {
                    if (!this.isMessageMine(message.getSenderId())) {
                        this.processInfoMessageToPad(message);
                    }
                } else {
                    System.out.println("VISUALHANDLER -> COMANDO DESCONOCIDO");
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
            case ASTEROID_TYPE:
                this.createAsteroid(object);
                break;
            case PACMAN_TYPE:
                this.createPacman(object);
                break;
            default:
                System.out.println("VISUALHANDLER -> ERROR: OBJETO DESCONOCIDO" + object.getObjectType());
                break;
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

    private void closeSocket() {
        try {
            if (this.isConnected()) {
                this.sendLine(BYE_LINE);
                this.getSocket().close();
            }
        } catch (Exception ex) {
            System.out.println("VisualHandler ya cerrado");
        }
    }

    public synchronized boolean setSocket(final Socket sock, final String destinationId) {

        if (super.setSocket(sock)) {
            this.destinationId = destinationId;
            return true;
        }
        return false;
    }

    private void disconnect() {
        this.setSocket(null, EMPTY_STRING);
        super.setDestinationIp(EMPTY_STRING);
    }

    public void sendStart() {
        this.getKillergame().setPadsNum(0);
        this.sendMessage(Message.Builder.builder(START_GAME, KillerServer.getId())
                .withServersQuantity(this.getKillergame().getPadsSize())
                .build());
    }

    private void processStart(final Message message) {
        if (!isMessageMine(message.getSenderId())) {
            this.getKillergame().setPadsNum(0);
            this.getKillergame().getNextModule().sendMessage(Message.Builder.builder(START_GAME, message.getSenderId())
                    .withServersQuantity(message.getServersQuantity() + this.getKillergame().getPadsSize())
                    .build());
        } else {
            this.getKillergame().setPadsNum(message.getServersQuantity());
        }
        this.getKillergame().startGame();
    }

    private void processQuitGame(final Message message) {
        if (!isMessageMine(message.getSenderId())) {
            this.getKillergame().getNextModule().sendMessage(message);
            System.exit(0);
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

    public void sendInfoMessageToPad(final String command, final String padIp) {
        this.processInfoMessageToPad(Message.buildInfoMessageToPad(command, padIp));
    }

    public void sendInfoHealthMessageToPad(final String padIp, final int health) {
        this.processInfoMessageToPad(Message.buildHealthMessageToPad(HEALTH_COMMAND, padIp, health));
    }

    private void processSyncRequest(final String senderId, final int quantity) {
        final Message messageToSend;
        if (this.isMessageMine(senderId)) {
            this.getKillergame().getNextModule().getClient().resetSyncTimeOut();
            this.getKillergame().setServersQuantity(quantity);
            this.getKillergame().setSyncronized(true);
            messageToSend = Message.Builder.builder(SYNC_CONFIRMATION, senderId)
                    .withServersQuantity(quantity)
                    .build();
        } else {
            this.getKillergame().setWindowNumber(quantity + 1);
            messageToSend = Message.Builder.builder(SYNC_REQUEST, senderId)
                    .withServersQuantity(quantity + 1)
                    .build();
        }
        this.getKillergame().getNextModule().sendMessage(messageToSend);
    }

    private void processSyncConfirmation(final Message message) {
        if (!this.isMessageMine(message.getSenderId())) {
            this.getKillergame().getNextModule().sendMessage(message);
            this.getKillergame().getNextModule().getClient().resetSyncTimeOut();
            this.getKillergame().setServersQuantity(message.getServersQuantity());
            this.getKillergame().setSyncronized(true);
        }
    }

    private void processSyncCheck(final Message message) {
        if (!this.isMessageMine(message.getSenderId())) {
            this.getKillergame().getNextModule().sendMessage(message);
        }
        this.getKillergame().getNextModule().getClient().resetSyncTimeOut();
    }

    private boolean isMessageMine(final String id) {
        return KillerServer.getId().equals(id);
    }

    private void createShip(ObjectResponse object) {
        this.getKillergame().reciveShip(object.getX(), object.getY(), object.getRadians(),
                object.getDx(), object.getDy(),
                object.getVx(), object.getVy(),
                object.getTx(), object.getTy(),
                object.getLx(), object.getLy(),
                object.getRx(), object.getRy(),
                object.getId(), object.getUser(),
                object.getType(), object.getHealth(),
                object.getDamage(), Color.decode(object.getColor()));
    }

    private void createAsteroid(ObjectResponse object) {
        this.getKillergame().reciveAsteroid(object.getX(), object.getY(),
                object.getImgHeight(), object.getM(),
                object.getHealth(), object.getRadians(),
                object.getVx(), object.getVy(),
                object.getA(), object.getImgFile());
    }

    private void createPacman(ObjectResponse object) {
        this.getKillergame().recivePacman(object.getX(), object.getY(),
                object.getM(), object.getHealth(),
                object.getRadians(), object.getVx(),
                object.getVy(), object.getA(), object.getImgHeight());
    }

    public void updateRoom(final boolean connected) {
        if (this.right) {
            this.getKillergame().getRoom().setFeedBackConnectionRight(connected);
        } else {
            this.getKillergame().getRoom().setFeedBackConnectionLeft(connected);
        }
    }

    public void sendPadDecrement() {
        this.sendMessage(Message.Builder.builder(DECREMENT_PADS_NUM, KillerServer.getId()).build());
    }

    private void processPadDecrement(final Message message) {
        if (this.getKillergame().getPadsNum() > 0) {
            this.getKillergame().decrementPadsNum();
        }
        if (!this.isMessageMine(message.getSenderId())) {
            this.getKillergame().getNextModule().sendMessage(message);
        }
    }

    private void processWin(final Message message) {
        KillerPad pad = this.getKillergame().getLastPad();
        if (pad != null) {
            pad.sendMessage(Message.Builder.builder(PAD_WIN_COMMAND, KillerServer.getId())
                    .withReceiverId(pad.getId())
                    .build());
            //anunciar ganador al resto de ordenadores
            this.getKillergame().getNextModule().sendMessage(Message.Builder.builder(WINNER_COMMAND, KillerServer.getId())
                    .withReceiverId(pad.getUser())
                    .build());
            this.getKillergame().setWinner(pad.getUser());
        } else if (!this.isMessageMine(message.getSenderId())) {
            this.getKillergame().getNextModule().sendMessage(message);
        }
    }
    
    private void processWinner(final Message message){
        if (!this.isMessageMine(message.getSenderId())) {
            this.getKillergame().setWinner(message.getReceiverId());
            this.getKillergame().getNextModule().sendMessage(message);
        }
    }

    public void sendGameConfiguration(final GameConfiguration gameConfiguration) {
        this.sendMessage(Message.Builder.builder(GAME_CONFIGURATION_COMMAND, KillerServer.getId())
                .withGameConfiguration(gameConfiguration)
                .build());
    }

    private void processGameConfiguration(final Message message) {
        if (!this.isMessageMine(message.getSenderId())) {
            this.getKillergame().receiveConfiguration(message.getGameConfiguration());
            this.getKillergame().getNextModule().sendMessage(message);
        }
    }
    
    private void sendReset(){
        this.sendMessage(Message.Builder.builder(RESET_KILLERGAME_COMMAND, KillerServer.getId())
            .build());
    }
    
    private void processReset(final Message message){
        if (!this.isMessageMine(message.getSenderId())) {
            //this.getKillergame().reset()
            this.getKillergame().getNextModule().sendMessage(message);
        }
    }
}
