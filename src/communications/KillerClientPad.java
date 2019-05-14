package communications;

import game.KillerGame;

public class KillerClientPad implements Runnable {

    private final KillerPad pad;
    private final KillerGame killergame;

    private static final String STATUS_REQUEST = "ok";

    public KillerClientPad(KillerPad pad, KillerGame killergame) {
        this.pad = pad;
        this.killergame = killergame;
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
