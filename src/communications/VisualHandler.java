package communications;

import communications.KillerClient;
import visibleObjects.Automata;
import visibleObjects.Controlled;
import game.KillerGame;
import visibleObjects.Shoot;
import java.net.*;
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
                    this.listeningMessages();
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
                done = processLine(this.readLine());

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                done = true;
            }

        }
        this.setSocket(null);
    }

    private boolean processLine(final String line) {
        if (line == null) {
            return true;
        }
        if (!STATUS_REQUEST.equalsIgnoreCase(line)) {
            processMessage(Message.readMessage(line));
        }
        return false;
    }

    private void processMessage(final Message message) {

        switch (message.getCommand()) {
            case SEND_OBJECT_COMMAND:
                this.receiveObject(ObjectResponse.readObjectResponse(message.getObject()));
                break;
            case START_GAME:
                break;
            case READY_TO_START:
                break;
            case QUIT_GAME:
                break;
            default:
                processPadCommand(message);
                break;
        }
    }

    private void processPadCommand(final Message message) {
        String command = message.getCommand();
        if (command != null && command.matches(PAD_COMMAND)) {
            if (!KillerPad.processMessage(message)) {
                //TODO ennviar  instruccion al siguiente visual
            }
        }
    }

    private void receiveObject(final ObjectResponse response) {

        switch (response.getObjectType()) {
            case SHOOT_TYPE:
                //TODO crear bala con los datos del response
                break;
            default:
                System.out.println("ERROR: OBJETO DESCONOCIDO" + response.getObjectType());
                break;

            //TODO los demas objetos
        }
    }

    public void sendObject(final Alive object) {
        //TODO rellenar con los datos que se pida
        final String objectJson = ObjectResponse.convertObjectToString(object);
        final Message message = Message.Builder.builder(SEND_OBJECT_COMMAND, this.getKillergame().getServer().getId())
                .withObject(objectJson)
                .build();
        this.send(Message.convertMessageToJson(message));
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

            if (this.right) {
                this.getKillergame().getIpnext().setEnabled(false);
                this.getKillergame().getPortnext().setEnabled(false);
                this.getKillergame().getIpnext().setText("Connected!");
            } else {
                this.getKillergame().getIpprev().setEnabled(false);
                this.getKillergame().getPortprev().setEnabled(false);
                this.getKillergame().getIpprev().setText("Connected!");
            }
            try {
                this.getSocket().setSoTimeout(3500);
            } catch (SocketException ex) {
            }
            return true;
        }
        return false;
    }
}
