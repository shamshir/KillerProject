package communications;

import game.KillerGame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceptionHandler {

    private Socket sock;
    private String destinationIp;
    private int destinationPort;
    private final KillerGame killergame;
    private BufferedReader in;
    private PrintWriter out;
    private static final String EMPTY_STRING = "";

    public ReceptionHandler(final KillerGame killergame) {
        this.killergame = killergame;
    }

    public ReceptionHandler(final KillerGame killergame, final Socket socket) {
        this.killergame = killergame;
        this.setSocket(socket);
    }
    
    public synchronized Socket getSocket() {
        return this.sock;
    }

    public String getDestinationIp() {
        return this.destinationIp;
    }

    public int getDestinationPort() {
        return this.destinationPort;
    }

    public KillerGame getKillergame() {
        return this.killergame;
    }

    public void setDestinationIp(final String ip) {
        this.destinationIp = ip;
    }

    public void setDestinationPort(final int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String readLine() throws Exception {
        return this.in.readLine();
    }

    public void sendLine(final String line) {
       if(out!=null) this.out.println(line);
    }
    
    public void sendMessage(final Message message) {
        if(out!=null) this.out.println(Message.convertMessageToJson(message));
    }

    public synchronized boolean setSocket(final Socket socket) {
        this.sock = socket;

        if (socket == null) {
            this.closeSocket();
            this.destinationIp = EMPTY_STRING;
            return false;
        }

        try {
            this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.out = new PrintWriter(sock.getOutputStream(), true);
        } catch (Exception ex) {
            System.out.println("ERROR setsocket rh");
            this.destinationIp = EMPTY_STRING;
            return false;
        }

        this.destinationIp = socket.getInetAddress().getHostAddress();
        return true;
    }

    private void closeSocket() {
        try {
            this.sock.close();
            this.setSocket(null);
        } catch (Exception ex) {
            System.out.println("error al cerrar socket (ReceptionHandler: closeSocket)");
        }
    }
}
