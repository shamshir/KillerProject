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
                this.receiveObject(message.getObject());
                break;
            case START_GAME:
                break;
            case READY_TO_START:
                break;
            case QUIT_GAME:
                break;
            default:
                this.processPadCommand(message);
                break;
        }
    }

    private void processPadCommand(final Message message) {
        final String command = message.getCommand();
        if (command != null && command.matches(PAD_COMMAND)) {
            KillerPad.processMessage(message);            
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
        final Message message = Message.Builder.builder(SEND_OBJECT_COMMAND, this.getKillergame().getServer().getId())
                .withObject(this.convertObjectToObjectResponse(object))
                .build();
        this.sendLine(Message.convertMessageToJson(message));
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
            return this.buildObjectResponseFromShoot((Shoot)object);
        }
        return ObjectResponse.Builder.builder(EMPTY_STRING).build();
    }

    private ObjectResponse buildObjectResponseFromShoot(final Shoot shoot) {
        return ObjectResponse.Builder.builder(SHOOT_TYPE)
                .withPosicionYInPercent(shoot.y / shoot.getKg().getViewer().getHeight())
                .build();
    }
}
