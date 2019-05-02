/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communications;

import communications.KillerPad;
import communications.KillerClient;
import visibleObjects.Asteroid;
import visibleObjects.KillerShip;
import game.KillerGame;
import visibleObjects.Shoot;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 *
 * @author Bernat2
 */
public class VisualHandler implements Runnable {

    private Socket sock;
    private String ip;
    private int originport;
    private KillerGame killergame;
    private BufferedReader in;
    private PrintWriter out;
    private KillerClient kc;

    private boolean right;

    //  long time;
    public VisualHandler(KillerGame kg, boolean right) {
        kc = new KillerClient(this);
        this.right = right;
        killergame = kg;
        startClient();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (sock != null) {

                    processMessage(in, out);

                }
                Thread.sleep(200);
            } catch (InterruptedException ex) {

            }
        }
    }

    public void alert(String msg) {
        try {
            out.println(msg);
        } catch (Exception ex) {
            System.out.println("null de verdad");
            nullSocket();
        }
    }

    public void nullSocket() {

        try {
            sock.close();
            sock = null;
        } catch (Exception ex1) {
            System.out.println(ex1.getCause() + ex1.getMessage());
            System.out.println("hola");
        }
    }

    public String notifyPad(String msg, String idkp) {

        return ("topad/" + idkp + "/" + msg);

    }

    public String notifyVisual(String msg, String idkp) {
        return ("tovisual/" + idkp + "/" + msg);
    }

    public void processMessage(BufferedReader in, PrintWriter out) {
        boolean done = false;
        String line;

        while (!done) {
            try {

                line = in.readLine();
                if (line != null) {
                    request(line);
                } else {
                    done = true;
                }

            } catch (IOException ex) {
                System.out.println(ex.getClass());
                System.out.println(ex.getCause());
                System.out.println(ex.getMessage());
                System.out.println("Time out");
                done = true;
            }

        }
        nullSocket();
    }

    public void request(String line) {
        //  System.out.println(line);

        if (line.equals("ok")) {
            out.println("ok");
//            time = System.currentTimeMillis();

        } else {

            String[] params = line.split("&");

            String status = params[0];
            String ipmsg = params[1];
            String portmsg = params[2];
            String[] info = params[3].split("/");

            if (status.equals("r")) {
                System.out.println("R!!");
//
//            if ((portmsg.equals(killergame.getSERVERPORT()))) {
//                //if ((ipmsg.equals(killergame.getIplocal()))) {
//                if (info[0].trim().equals("kpad")) {
//
//                    String kpid = info[1].trim();
//                    KillerPad.sendMessageToPad("notfound", killergame, kpid, ipmsg);
//
//                } else if (info[0].trim().equals("topad")) {
//
//                    String kpid = info[1].trim();
//                    KillerPad.sendMessageToPad("notfound", killergame, kpid, ipmsg);
//
//                }
//              }

                if (!(portmsg.equals(killergame.getSERVERPORT()))
                        || !(portmsg.equals(killergame.getIplocal()))) {
                    if (info[0].trim().equals("kpad")) {

                        String kpid = info[1].trim();
                        String kpmsg = info[2].trim();
                        KillerPad.request(kpmsg, killergame, kpid, ipmsg);

                    } else if (info[0].trim().equals("topad")) {

                        String kpid = info[1].trim();
                        String kpmsg = info[2].trim();

                        KillerPad.sendMessageToPad(kpmsg, killergame, kpid, ipmsg);

                    } else if (info[0].trim().equals("tovisual")) {

                        String kpid = info[1].trim();
                        String kpmsg = info[2].trim();
                        System.out.println(kpid + "," + kpmsg);

                        if (kpmsg.equals("death")) {
                            KillerPad.lifeShip(kpmsg, killergame, kpid, ipmsg);
                        } else if (kpmsg.equals("replay")) {
                            KillerPad.lifeShip(kpmsg, killergame, kpid, ipmsg);
                        } else if (kpmsg.equals("remove")) {
                            KillerPad.removeShip(kpmsg, killergame, kpid, ipmsg);
                        }
                    }

                }

            } else if (status.equals("d")) {

                if (info[0].trim().equals("player")) {

                    String ip = info[1];
                    String user = info[2];
                    String color = info[3];
                    String percnt = info[4];
                    String speed = info[5];
                    String WIDTH = info[6];
                    String HEIGHT = info[7];
                    String dir = info[8];
                    int axisX = Integer.parseInt(info[9]);
                    int axisY = Integer.parseInt(info[10]);

                    KillerShip contr = new KillerShip(killergame, Color.decode(color), ip, user);
                    contr.setY(killergame.getViewer().getHeight() * Double.valueOf(percnt));
                    contr.setSpeed(Double.valueOf(speed));
                    contr.setWidth(Integer.parseInt(WIDTH));
                    contr.setHeight(Integer.parseInt(HEIGHT));

                    if (dir.equals("right")) {
                        contr.setX(killergame.getViewer().getWidth() - contr.getWidth() / 2);
                        //contr.right = false;
                        contr.setRight(false);
                    } else if (dir.equals("left")) {
                        contr.setX(-contr.getWidth() / 2);
                        //contr.right = true;
                        contr.setRight(true);
                    }

                    if (axisX == 1) {
                        //contr.right = true;
                        contr.setRight(true);
                    } else if (axisX == -1) {
                        //contr.left = true;
                        contr.setLeft(true);
                    }

                    if (axisY == 1) {
                        //contr.up = true;
                        contr.setUp(true);

                    } else if (axisY == -1) {
                        //contr.down = true;
                        contr.setDown(true);
                    }
                    System.out.println(percnt);
                    System.out.println(contr.getY());
                    killergame.createControlled(contr);

                } else if (info[0].trim().equals("automata")) {

                    String color = info[1];
                    String percnt = info[2];
                    String speed = info[3];
                    String WIDTH = info[4];
                    String HEIGHT = info[5];
                    String dirx = info[6];
                    String diry = info[7];

//                  Asteroid auto = new Asteroid(killergame, Color.decode(color));
                    Asteroid auto = new Asteroid(killergame);

                    auto.setY(killergame.getViewer().getHeight() * Double.valueOf(percnt));
                    auto.setSpeed(Double.valueOf(speed));
                    auto.setWidth(Integer.parseInt(WIDTH));
                    auto.setHeight(Integer.parseInt(HEIGHT));

                    if (dirx.equals("right")) {
                        auto.setX(killergame.getViewer().getWidth() - auto.getWidth() / 2);
                        auto.setDx(-Double.valueOf(speed));
                    } else if (dirx.equals("left")) {
                        auto.setX(-auto.getWidth() / 2);
                        auto.setDx(Double.valueOf(speed));
                    }
                    auto.setDy(Integer.parseInt(diry) * Double.valueOf(speed));
                    killergame.createAutomata(auto);

                } else if (info[0].trim().equals("shoot")) {

                }
            }
        }

    }

    public void sendMessage(String info, String status, String ip) {

        String iptosend = null;
        if (status.equals("r")) {
            iptosend = ip;
        } else if (status.equals("d")) {
            iptosend = killergame.getIplocal();
        }

        String msg = status + "&" + iptosend + "&"
                + killergame.getSERVERPORT() + "&" + info;
        out.println(msg);
    }

    public String sendPlayer(KillerShip obj, boolean right) {

        double percentSc = (obj.getY() / killergame.getViewer().getHeight());
        String dir = null;
        if (right) {
            dir = "right";
        } else {
            dir = "left";
        }

        int axisX = 0;
        int axisY = 0;

        if (obj.isLeft()) {
            axisX = -1;
        }
        if (obj.isRight()) {
            axisX = 1;
        }
        if (obj.isUp()) {
            axisY = 1;
        }
        if (obj.isDown()) {
            axisY = -1;
        }

        String msg = "player/" + obj.getIp() + "/" + obj.getUser() + "/" + obj.getColorhex()
                + "/" + percentSc + "/" + obj.getSpeed() + "/"
                + obj.getWidth() + "/" + obj.getHeight() + "/" + dir + "/"
                + axisX + "/" + axisY;
        return msg;
    }

    public String sendAutomata(Asteroid obj, boolean right) {
        double percentSc = (obj.getY() / killergame.getViewer().getHeight());
        String dirx = null;
        int diry = 0;
        if (right) {
            dirx = "right";
        } else {
            dirx = "left";
        }

        if (obj.getDy() >= 0) {
            diry = 1;
        } else if (obj.getDy() < 0) {
            diry = -1;
        }

        String msg = "automata/" + obj.getColorhex()
                + "/" + percentSc + "/" + obj.getSpeed() + "/"
                + obj.getWidth() + "/" + obj.getHeight() + "/" + dirx + "/" + diry;
        return msg;
    }

    public String sendShoot(Shoot obj, boolean right) {

        double percentSc = (obj.getY() / killergame.getViewer().getHeight());
        String dir = null;
        if (right) {
            dir = "right";
        } else {
            dir = "left";
        }

        String msg = "shoot/" + obj.getShip().getIp() + "/" + obj.getColor()
                + "/" + percentSc + "/" + obj.getSpeed() + "/"
                + obj.getWidth() + "/" + obj.getHeight() + "/" + dir;
        return msg;

    }

    public String sendPadAction(String msg, String idkp) {

        return ("kpad/" + idkp + "/" + msg);

    }

    public void startClient() {
        new Thread(this.kc).start();
    }

    public synchronized Socket getSock() {
        return sock;

    }

    public synchronized void setSock(Socket sock) {
        this.sock = sock;
        this.ip = sock.getInetAddress().getHostAddress();
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

            if (right) {
                killergame.getIpnext().setEnabled(false);
                killergame.getPortnext().setEnabled(false);
                killergame.getIpnext().setText("Connected!");
            } else {
                killergame.getIpprev().setEnabled(false);
                killergame.getPortprev().setEnabled(false);
                killergame.getIpprev().setText("Connected!");
            }
//            time = System.currentTimeMillis();
            alert("ok");
            this.sock.setSoTimeout(3500);

        } catch (Exception ex) {

        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;

    }

    public KillerGame getKillergame() {
        return killergame;
    }

    public void setKillergame(KillerGame killergame) {
        this.killergame = killergame;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public KillerClient getKc() {
        return kc;
    }

    public void setKc(KillerClient kc) {
        this.kc = kc;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public int getOriginport() {
        return originport;
    }

    public void setOriginport(int originport) {
        this.originport = originport;
    }

}
