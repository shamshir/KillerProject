/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class Viewer extends Canvas implements Runnable {

    private KillerGame killer;

    private int fps = 30;
    private double averageFPS;
    private double target = 1000 / fps;

    private BufferedImage fondo;
    private BufferedImage frame;
    private Image offImg;
    public Graphics graph;

    public Viewer(KillerGame k) {
        killer = k;
        setSize(new Dimension(killer.getWidth(), killer.getHeight()));
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocus();

    }

    public void run() {
        images();
        while (true) {
            update();
            try {
                Thread.sleep((long) target);
            } catch (InterruptedException ex) {

            }
        }

    }

    public void drawComponents(Graphics g) {

        for (int i = 0; i < killer.getObjects().size(); i++) {
            killer.getObjects().get(i).draw(g);
        }
        drawConnectionInfo(g);

    }

    public void drawConnectionInfo(Graphics g) {

        double height = (int) getHeight() / 20;

        g.setColor(Color.white);

        g.drawString("Local IP: " + killer.getIplocal(),
                (int) (getWidth() * 0.45), (int) height);
        g.drawString("PORT: " + killer.getSERVERPORT(),
                (int) (getWidth() * 0.45), (int) height + 16);

        g.drawString("Previous - IP: " + killer.getPk().getIp(),
                (int) (getWidth() * 0.1), (int) height);
        g.drawString("PORT: " + killer.getPk().getOriginport(),
                (int) (getWidth() * 0.1), (int) height + 16);

        if (killer.getPk().getSock() != null) {
            g.setColor(Color.green);
            g.drawString("CONNECTED", (int) (getWidth() * 0.1),
                    (int) height + 32);
        } else {
            g.setColor(Color.red);
            g.drawString("DISCONNECTED", (int) (getWidth() * 0.1),
                    (int) height + 32);
        }

        g.setColor(Color.white);

        g.drawString("Next - IP: " + killer.getNk().getIp(),
                (int) (getWidth() * 0.8), (int) height);
        g.drawString("PORT: " + killer.getNk().getOriginport(),
                (int) (getWidth() * 0.8), (int) height + 16);

        if (killer.getNk().getSock() != null) {
            g.setColor(Color.green);
            g.drawString("CONNECTED", (int) (getWidth() * 0.8),
                    (int) height + 32);
        } else {
            g.setColor(Color.red);
            g.drawString("DISCONNECTED", (int) (getWidth() * 0.8),
                    (int) height + 32);
        }

    }

    public void images() {
        //creamos una nueva imagen del tamaño del canvas
        offImg = createImage(getWidth(), getHeight());
        fondo = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    }

    public void update() {

        //cogemos los graficos de la imagen
        Graphics g2d = offImg.getGraphics();
        //asi pintamos el fondo
        g2d.drawImage(fondo, 0, 0, null);

        //pintamos todos los componentes en los graphics de la imagen
        this.drawComponents(g2d);

        //pintamos la imagen en el canvas
        this.getGraphics().drawImage(offImg, 0, 0, null);
    }

}
