/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class KillerServer implements Runnable {

    private KillerGame killergame;

    private final int PORT;
    private Socket clientSock;
    private ServerSocket serverSocket;

    private BufferedReader in;
    private PrintWriter out;

    public KillerServer(KillerGame kg, int PORT) throws Exception {

        killergame = kg;
        this.PORT = PORT;
        serverSocket = new ServerSocket(PORT);
    }

    @Override
    public void run() {

        while (true) {
            try {
                System.out.println("Waiting for a connection......");
                contact();
            } catch (IOException ex) {

            }
        }

    }

    public void contact() throws IOException {

        clientSock = serverSocket.accept();
        System.out.println("Connection from " + clientSock.getInetAddress().getHostAddress());
        new Thread(new ConnectionHandler(clientSock, clientSock.getInetAddress().getHostAddress(), killergame)).start();

    }

    public Socket getClientSock() {
        return clientSock;
    }

    public void setClientSock(Socket clientSock) {
        this.clientSock = clientSock;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

}
