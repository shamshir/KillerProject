/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class KillerClient implements Runnable {

    private VisualHandler visual;

    public KillerClient() {

    }

    public KillerClient(VisualHandler v) {
        visual = v;
    }

    @Override
    public void run() {

        while (true) {
            if (visual.getSock() == null && visual.getIp() != null && visual.getOriginport() != 0) {
                try {
                    System.out.println("Conectando con " + visual.getIp()+"/"+visual.getOriginport());
                    Socket sock = new Socket(visual.getIp(),visual.getOriginport());
                    contact(sock);
                    visual.setSock(sock);
                 //   visual.alert("ok");
                    System.out.println("Conexión establecida desde KillerClient.");
                    
                } catch (IOException ex) {
                    System.err.println("Conexión con "+visual.getIp()+" fallida...");
                }
//            } else if (visual.getSock() != null) {
//                try {
//                 //   System.out.println(System.currentTimeMillis() - visual.time);
//                    System.out.println(System.currentTimeMillis() - visual.time);
//                    if (System.currentTimeMillis() - visual.time >= 5500) {
//                        visual.nullSocket();
//                    } else {
//                        visual.alert("ok");
//                        }
//                } catch (Exception e) {
//
//                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void contact(Socket sock) throws IOException {
        String dir = "R";
        if (!visual.isRight()) {
            dir = "L";
        }

        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        out.println("fromV:" + dir + "&" + visual.getKillergame().getSERVERPORT());

    }

}
