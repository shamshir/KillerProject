package communications;

import communications.ConnectionHandler;
import game.KillerGame;
import java.io.*;
import java.net.*;

public class KillerServer implements Runnable {

    private KillerGame killergame;
    private final int PORT;
    private static String id;
    private ServerSocket serverSocket;

    public KillerServer(final KillerGame kg, final int port) throws Exception {

        this.killergame = kg;
        this.tryToCreateServerSocket(port);

        if (this.serverSocket == null) {
            throw new Exception("Ningun puerto ha sido encontrado, inserte otro puerto");
        }

        this.PORT = this.serverSocket.getLocalPort();
        this.id = InetAddress.getLocalHost().getHostAddress() + " - " + this.PORT;
        System.out.println("KillerServer -> Server iniciado en: " + this.id);
    }

    @Override
    public void run() {

        while (true) {
            try {
                System.out.println("KillerServer -> Waiting for a connection......");
                this.contact();
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
        }

    }

    //Intentar crear un servidor en el puerto establecido
    private void tryToCreateServerSocket(final int port) {
        boolean ok = false;
        for (int i = 0; !ok && i < 10; i++) {

            try {
                this.serverSocket = new ServerSocket(port + i);
                ok = true;
            } catch (Exception ex) {
            }
        }
    }

    //Crear un thread con el que tratar la petición
    private void contact() {

        try {
            final Socket clientSock = this.serverSocket.accept();
            new Thread(new ConnectionHandler(clientSock, this.killergame)).start();
        } catch (Exception ex) {
            System.out.println("Fallo al contactar");
        }
    }

    public int getPort() {
        return this.PORT;
    }

    public static String getId() {
        return id;
    }

}
