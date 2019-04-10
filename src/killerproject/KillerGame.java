/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.net.*;

/**
 *
 * @author berna
 */
public class KillerGame extends JFrame {

    //Objetos a pintar + Canvas
    private ArrayList<VisibleObject> objects = new ArrayList<>();
    private Viewer viewer;

    //Comunicaciones
    private KillerServer server;
    private String iplocal;
    private int SERVERPORT = 8000;
    private VisualHandler nk = new VisualHandler(this, true);
    private VisualHandler pk = new VisualHandler(this, false);

    //Gamepad (Móvil)
    private ArrayList<KillerPad> kpads = new ArrayList();

    //JFrame connections
    JFrame connection;
    JTextField ipnext;
    JTextField portnext;
    JTextField ipprev;
    JTextField portprev;

    int frameW = 1900;
    int frameH = 700;

    public KillerGame() {
        //    portFrame();
        startServer();
        ipframe();
    }

    public static void main(String[] args) {
        new KillerGame();
    }

    public void checkColision(Alive obj) {

        //colision con limites
        if (obj instanceof Automata) {
            if ((obj.y + obj.dy) >= viewer.getHeight() - obj.HEIGHT || (obj.y + obj.dy) <= 0) {
                obj.dy *= -1;
            } else if (((obj.x + obj.dx) >= viewer.getWidth() - obj.WIDTH && nk.getSock() == null)
                    || ((obj.x + obj.dx) <= 0 && pk.getSock() == null)) {
                System.out.println("choque");
                obj.dx *= -1;

            } else if ((obj.x + obj.dx) >= viewer.getWidth() - (obj.WIDTH * (1f / 4f)) && nk.getSock() != null) {
                System.out.println("send");
                nk.sendMessage(nk.sendAutomata((Automata) obj, false), "d", iplocal);
                obj.setAlive(false);
                objects.remove(obj);
            } else if ((obj.x + obj.dx) <= (-obj.WIDTH * (3f / 4f)) && pk.getSock() != null) {
                System.out.println("send");
                pk.sendMessage(pk.sendAutomata((Automata) obj, true), "d", iplocal);
                obj.setAlive(false);
                objects.remove(obj);
            }

        } else if (obj instanceof Controlled) {
            if ((obj.y + obj.dy) >= viewer.getHeight() - obj.HEIGHT) {
                ((Controlled) obj).setWdown(true);
                System.out.println(obj.x + obj.dx);
            } else if (obj.y + obj.dy <= 0) {
                ((Controlled) obj).setWup(true);
                System.out.println(obj.x + obj.dx);
            }

            if ((obj.x + obj.dx) >= viewer.getWidth() - obj.WIDTH && nk.getSock() == null) {
                ((Controlled) obj).setWright(true);
                System.out.println("pum al lado der");
            } else if ((obj.x + obj.dx) >= viewer.getWidth() - (obj.WIDTH * (1f / 4f)) && nk.getSock() != null) {
                System.out.println("por la derecha!");
                nk.sendMessage(nk.sendPlayer((Controlled) obj, false), "d", iplocal);
                obj.setAlive(false);
                objects.remove(obj);
            }

            if ((obj.x + obj.dx) <= 0 && pk.getSock() == null) {
                ((Controlled) obj).setWleft(true);
                System.out.println("pum al lado izq");

            } else if ((obj.x + obj.dx) <= 0 - obj.WIDTH * (3f / 4f) && pk.getSock() != null) {
                System.out.println("por la izquierda!");
                pk.sendMessage(pk.sendPlayer((Controlled) obj, true), "d", iplocal);
                obj.setAlive(false);
                objects.remove(obj);
            }

        }

        for (int i = 0; i < objects.size(); i++) {

            VisibleObject objCol = null;

            if (objects.get(i) != obj) {
                objCol = objects.get(i);
            }

            if (objCol instanceof Alive) {
                if (obj.nextMove().intersects(((Alive) objCol).hitbox)) {
                    KillerRules.collision(obj, objCol);
                }

            }

        }
    }

    public void checkColision(Shoot obj) {

        //colision con limites
        if (obj.getY() >= viewer.getHeight() || obj.getY() <= 0
                || obj.getX() >= viewer.getWidth() || obj.getX() <= 0) {
            obj.death();
        }

        for (int i = 0; i < objects.size(); i++) {

            VisibleObject objCol = null;
            if (objects.get(i) != obj.getControlled()) {
                objCol = objects.get(i);
            }

            if (objCol instanceof Alive) {
                if (obj.hitbox.intersects(((Alive) objCol).hitbox)) {
                    KillerRules.collisionShoot(obj, (Alive) objCol);

                }

            }

        }
    }

    public void createControlled(Controlled contr) {

        objects.add(contr);
        new Thread(contr).start();

    }

    public void createAutomata(Automata auto) {
        objects.add(auto);
        new Thread(auto).start();

    }

    public void createShoot(Shoot shoot) {

    }

    public void frame() {

        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height));
        //setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        getContentPane().add(viewer = new Viewer(this));
        viewer.images();
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        new Thread(viewer).start();

    }

    public void ipframe() {
        connection = new JFrame("Killer Game: Set IP's");
        connection.setSize(500, 100);
        connection.setLocationRelativeTo(null);
        connection.setResizable(false);
        connection.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container cp = connection.getContentPane();
        connection.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(3, 4, 4, 3);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        JLabel ip1 = new JLabel("IP Previous:");
        connection.add(ip1, c);
        c.gridx = 1;
        JLabel ip2 = new JLabel("PORT:");
        connection.add(ip2, c);
        c.gridx = 3;
        JLabel ip3 = new JLabel("IP Next:");
        connection.add(ip3, c);
        c.gridx = 4;
        JLabel ip4 = new JLabel("PORT:");
        connection.add(ip4, c);
        ipprev = new JTextField(10);
        portprev = new JTextField(5);
        ipnext = new JTextField(10);
        portnext = new JTextField(5);

        c.gridx = 0;
        c.gridy = 1;

        connection.add(ipprev, c);
        c.gridx = 1;
        connection.add(portprev, c);

        c.gridx = 2;
        c.insets = new Insets(3, 6, 6, 3);
        JButton start = new JButton("START");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (nk.getIp() == null && nk.getOriginport() == 0) {
                        nk.setIp(ipnext.getText());
                        if (!portnext.getText().trim().equals("")) {
                            nk.setOriginport(Integer.parseInt(portnext.getText()));
                        }
                    }
                    //
                    if (pk.getIp() == null && pk.getOriginport() == 0) {
                        pk.setIp(ipprev.getText());
                        if (!portprev.getText().trim().equals("")) {
                            pk.setOriginport(Integer.parseInt(portprev.getText()));
                        }
                    }

                    // frame.dispose();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Nada de letras.");
                }
            }
        });

        start.setSize(100, 100);
        connection.add(start, c);

        c.insets = new Insets(3, 4, 4, 3);
        c.gridx = 3;
        connection.add(ipnext, c);
        c.gridx = 4;
        connection.add(portnext, c);
        connection.setVisible(true);

    }

    public void portFrame() {
        JFrame portframe = new JFrame("Killer Game: Set port");
        portframe.setSize(240, 150);
        Container cp = portframe.getContentPane();
        portframe.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(3, 3, 3, 3);
        JLabel jl = new JLabel("Introduce puerto:");
        cp.add(jl, c);
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        JTextField text = new JTextField();
        cp.add(text, c);
        JButton butt = new JButton("OK");
        butt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!text.getText().isEmpty()) {
                    try {
                        SERVERPORT = Integer.parseInt(text.getText());
                        ipframe();
                        portframe.dispose();
                    } catch (Exception ex) {
                        text.setText("Nada de letras");
                    }
                }
            }

        });

        c.gridy = 2;
        cp.add(butt, c);

        portframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
        portframe.setResizable(false);
        portframe.setLocationRelativeTo(null);
        portframe.setVisible(true);
    }

    public void startServer() {

        try {
            iplocal = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            System.err.println("Error de red..");
        }

        boolean ok = false;
        while (!ok) {

            try {
                server = new KillerServer(this, SERVERPORT);
                if (server.getServerSocket() != null) {
                    ok = true;
                    System.out.println("Server iniciado");
                }
            } catch (Exception ex) {
                SERVERPORT++;
                System.err.println("Socket ocupado, reconectando en el siguiente..");
            }
        }

        new Thread(server).start();
        new Thread(nk).start();
        new Thread(pk).start();

        frame();
        for (int i = 0; i < 3; i++) {
            objects.add(new Automata(this, Color.red));
        }

        // objects.add(new Controlled(this, Color.pink, 1));
        startThreads();
    }

    public void startThreads() {
        for (int i = 0; i < objects.size(); i++) {
            new Thread((Alive) objects.get(i)).start();

        }

    }

    public Viewer getViewer() {
        return viewer;
    }

    public ArrayList<VisibleObject> getObjects() {
        return objects;
    }

    public VisualHandler getNk() {
        return nk;
    }

    public void setNk(VisualHandler nk) {
        this.nk = nk;
    }

    public VisualHandler getPk() {
        return pk;
    }

    public void setPk(VisualHandler pk) {
        this.pk = pk;
    }

    public KillerServer getServer() {
        return server;
    }

    public void setServer(KillerServer server) {
        this.server = server;
    }

    public int getSERVERPORT() {
        return SERVERPORT;
    }

    public void setSERVERPORT(int SERVERPORT) {
        this.SERVERPORT = SERVERPORT;
    }

    public ArrayList<KillerPad> getKpads() {
        return kpads;
    }

    public void setKpads(ArrayList<KillerPad> kpads) {
        this.kpads = kpads;
    }

    public JFrame getConnection() {
        return connection;
    }

    public void setConnection(JFrame connection) {
        this.connection = connection;
    }

    public JTextField getIpnext() {
        return ipnext;
    }

    public void setIpnext(JTextField ipnext) {
        this.ipnext = ipnext;
    }

    public JTextField getPortnext() {
        return portnext;
    }

    public void setPortnext(JTextField portnext) {
        this.portnext = portnext;
    }

    public JTextField getIpprev() {
        return ipprev;
    }

    public void setIpprev(JTextField ipprev) {
        this.ipprev = ipprev;
    }

    public JTextField getPortprev() {
        return portprev;
    }

    public void setPortprev(JTextField portprev) {
        this.portprev = portprev;
    }

    public String getIplocal() {
        return iplocal;
    }

    public void setIplocal(String iplocal) {
        this.iplocal = iplocal;
    }

}
