package communications;

import game.KillerGame;

public class KillerClientPad implements Runnable {

    private final KillerPad pad;

    private static final String STATUS_REQUEST = "ok";

    public KillerClientPad(KillerPad pad) {
        this.pad = pad;
    }

    @Override
    public void run() {
        while (true) {
            this.sendStatusRequest();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    private boolean disconnected() {
        return this.pad.getSocket() == null;
    }

    private void sendStatusRequest() {
        if (!this.disconnected()) {
            this.pad.sendLine(STATUS_REQUEST);
        }
    }
}
