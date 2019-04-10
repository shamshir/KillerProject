/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class KillerPad implements Runnable {

    Socket sock;
    String ip;
    int port;
    KillerGame killergame;
    BufferedReader in;
    PrintWriter out;

    public KillerPad(Socket sock, String ip, KillerGame killergame, String user, String color) {
        killergame.getKpads().add(this);
        this.sock = sock;
        this.ip = ip;
        this.killergame = killergame;
        port = sock.getPort();
        Controlled player = new Controlled(killergame, Color.decode("#" + color), ip, user);
        killergame.getObjects().add(player);
        killergame.getKpads().add(this);
        new Thread(player).start();
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            processClient(in, out);
            removeShip("remove", killergame, ip, killergame.getIplocal());
            KillerPad.removePad(killergame, ip, killergame.getIplocal());
            System.out.println(ip + " connection closed");

        } catch (IOException ex) {

        }

    }

    public static void lifeShip(String msg, KillerGame kg, String ipShip, String ipOrig) {

        Controlled player = null;

        for (int i = 0; i < kg.getObjects().size(); i++) {
            if (kg.getObjects().get(i) instanceof Controlled) {
                Controlled temporal = (Controlled) kg.getObjects().get(i);
                if (temporal.getIp().equals(ipShip)) {
                    player = temporal;
                }
            }
        }

        if (player != null) {
            if (msg.equals("death")) {
                player.setDeath(true);
                player.stop();
            } else if (msg.equals("restart")) {
                player.setDeath(false);
            }
        } else {
            kg.getNk().sendMessage(kg.getNk().notifyVisual(msg, ipShip), "r", msg);
        }
    }

    public void processClient(BufferedReader in, PrintWriter out) {
        boolean done = false;
        String line;
        while (!done) {
            try {
                line = in.readLine();
                if (line != null) {
                    if (line.trim().equals("bye")) {
                        done = true;
                    } else {
                        request(line, killergame, ip, killergame.getIplocal());
                    }
                } else {
                    done = true;
                }

            } catch (Exception ex) {
                done = true;
            }
        }

    }

    public static void request(String msg, KillerGame kg, String ipShip, String ipOrig) {

        System.out.println(msg);

        Controlled player = null;

        for (int i = 0; i < kg.getObjects().size(); i++) {
            if (kg.getObjects().get(i) instanceof Controlled) {
                Controlled temporal = (Controlled) kg.getObjects().get(i);
                if (temporal.getIp().equals(ipShip)) {
                    player = temporal;
                }
            }
        }

        if (msg.equals("hola?")) {
            System.out.println(msg);
            sendMessageToPad("ok", kg, ipShip, ipOrig);
        }

        if (msg.equals("replay")) {
            if (player != null) {
                player.setDeath(false);
            } else {
                kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
            }
        }

        if (msg.equals("death")) {
            if (player != null) {
                player.setDeath(true);
            } else {
                kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
            }

        }

        if (msg.equals("shoot")) {
            if (player != null) {
                if (!player.isDeath()) {
                    player.shoot();
                }
            } else {
                kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
            }
        } else if (msg.equals("up") || msg.equals("down")
                || msg.equals("left") || msg.equals("right")
                || msg.equals("downright") || msg.equals("downleft")
                || msg.equals("upright") || msg.equals("upleft")
                || msg.equals("idle")) {
            if (player != null) {
                if (!player.isDeath()) {
                    System.out.println("entra");
                    player.setDirections(msg);
                }
            } else {
                kg.getNk().sendMessage(kg.getNk().sendPadAction(msg, ipShip), "r", ipOrig);
            }
        }
    }

    public static void removeShip(String msg, KillerGame kg, String ipShip, String ipOrig) {

        Controlled player = null;

        for (int i = 0; i < kg.getObjects().size(); i++) {
            if (kg.getObjects().get(i).kg.getObjects().get(i) instanceof Controlled) {
                Controlled temporal = (Controlled) kg.getObjects().get(i);
                System.out.println("vuelta");
                if (temporal.getIp().equals(ipShip)) {
                    System.out.println("lo tengo");
                    player = temporal;
                }
            }
        }

        if (player != null) {
            player.death();
            kg.getObjects().remove(player);
        } else {
            kg.getNk().sendMessage(kg.getNk().notifyVisual(msg, ipShip), "r", msg);
        }
    }

    public static boolean removePad(KillerGame kg, String ipKpad, String ipOrig) {
        for (int i = 0; i < kg.getKpads().size(); i++) {
            if (kg.getKpads().get(i).ip.equals(ipKpad)) {
                KillerPad kp = kg.getKpads().get(i);
                kp.nullSock();
                System.out.println("tengo el pad");
                kg.getKpads().remove(i);
                System.out.println(ipKpad + " borrado");
                return true;
            }
        }
        return false;
    }

    public static void sendMessageToPad(String msg, KillerGame kg, String ipPad, String ipOrig) {

        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }
        KillerPad pad = null;

        for (int i = 0; i < kg.getKpads().size(); i++) {
            if (kg.getKpads().get(i) != null) {
                KillerPad temporal = kg.getKpads().get(i);
                if (temporal.ip.equals(ipPad)) {
                    pad = temporal;
                }
            }
        }

        if (pad != null) {
            pad.out.println(msg);
        } else {
            kg.getNk().sendMessage(kg.getNk().notifyPad(msg, ipPad), "r", ipOrig);
        }

    }

    public Socket getSock() {
        return sock;
    }

    public void nullSock() {
        try {
            this.sock.close();
            this.sock = null;
            this.in = null;
            this.out = null;
        } catch (NullPointerException | IOException ex) {
        }
    }

    public void setSock(Socket sock) {
        this.sock = sock;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.out = new PrintWriter(this.sock.getOutputStream(), true);
        } catch (IOException ex) {

        }
    }

}
