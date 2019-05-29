/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualEffects;

import game.KillerGame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author berna
 */
public class Viewer extends Canvas implements Runnable {

    private KillerGame killerGame;

    private final int FPS = 610;
    private double target = 1000 / FPS;

    private BufferedImage backgroundImg;
    public Graphics2D g2dBackground;

    public Viewer(KillerGame kg) {
        this.killerGame = kg;
        this.setSize(new Dimension(this.killerGame.getWidth(), this.killerGame.getHeight()));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void repaint() {
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void run() {

        this.createBufferStrategy(2);
        this.loadBackgroundImage();

        while (true) {
            this.updateFrame();

            try {
                Thread.sleep((long) this.target);
            } catch (InterruptedException ex) {

            }
        }
    }

    public void drawComponents(Graphics2D g2d) {

        for (int i = 0; i < this.killerGame.getObjects().size(); i++) {
            try {
                this.killerGame.getObjects().get(i).render(g2d);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        }
//        drawConnectionInfo(g2d);

    }

//    public void drawConnectionInfo(Graphics2D g) {
//
//        double height = (int) getHeight() / 20;   
//
//        g.setColor(Color.white);
//
//        g.drawString("Local IP: " + killer.getIplocal(),
//                (int) (getWidth() * 0.45), (int) height);
//        g.drawString("PORT: " + killer.getSERVERPORT(),
//                (int) (getWidth() * 0.45), (int) height + 16);
//
//        g.drawString("Previous - IP: " + killer.getPk().getIp(),
//                (int) (getWidth() * 0.1), (int) height);
//        g.drawString("PORT: " + killer.getPk().getOriginport(),
//                (int) (getWidth() * 0.1), (int) height + 16);
//
//        if (killer.getPk().getSock() != null) {
//            g.setColor(Color.green);
//            g.drawString("CONNECTED", (int) (getWidth() * 0.1),
//                    (int) height + 32);
//        } else {
//            g.setColor(Color.red);
//            g.drawString("DISCONNECTED", (int) (getWidth() * 0.1),
//                    (int) height + 32);
//        }
//
//        g.setColor(Color.white);
//
//        g.drawString("Next - IP: " + killer.getNk().getIp(),
//                (int) (getWidth() * 0.8), (int) height);
//        g.drawString("PORT: " + killer.getNk().getOriginport(),
//                (int) (getWidth() * 0.8), (int) height + 16);
//
//        if (killer.getNk().getSock() != null) {
//            g.setColor(Color.green);
//            g.drawString("CONNECTED", (int) (getWidth() * 0.8),
//                    (int) height + 32);
//        } else {
//            g.setColor(Color.red);
//            g.drawString("DISCONNECTED", (int) (getWidth() * 0.8),
//                    (int) height + 32);
//        }
//
//    }
    public void loadBackgroundImage() {
        // comprobar el numero de monitor del kgame
//        int monitorNumber = this.killerGame.getMonitorNumber();

        try {
            this.backgroundImg = ImageIO.read(new File("src/visualEffects/img/fondoLM.png"));
//            this.backgroundImg = ImageIO.read(new File("src/b" + monitorNumber + ".jpeg"));
//            this.backgroundImg = ImageIO.read(new File("src/visualEffects/backgroundImages/b" + 1 + ".jpeg"));
        } catch (IOException ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateFrame() {
        BufferStrategy bs;

        bs = this.getBufferStrategy();
        if (bs == null) {
            System.out.println("no tira");
            return; //============================================>>>>>
        }

        // en que se diferencia con el createGraphics?
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();

        g2d.drawImage(this.backgroundImg, 0, 0, null);

        //pintamos todos los componentes en los graphics de la imagen
        this.drawComponents(g2d);

        //mostramos la imagen del canvas
        bs.show();

        // liberamos recursos
        g2d.dispose();
    }

}
