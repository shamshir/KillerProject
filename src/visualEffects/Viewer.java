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
import java.awt.Image;
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

    private int fps = 60;
    private double averageFPS;
    private double target = 1000 / fps;

    private BufferedImage fondo;
    private BufferedImage backgroundImg;
    public Graphics2D g2dBackground;

    public Viewer(KillerGame k) {
        killerGame = k;
        setSize(new Dimension(killerGame.getWidth(), killerGame.getHeight()));
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocus();

    }

    @Override
    public void repaint() {
    }

    @Override
    public void run() {
       
        this.createBufferStrategy(2);

        while (true) {
            this.updateFrame();

            try {
                Thread.sleep((long) this.target);
            } catch (InterruptedException ex) {

            }
        }
    }

    public void drawComponents(Graphics2D g2d) {

        for (int i = 0; i < killerGame.getObjects().size(); i++) {
            this.killerGame.getObjects().get(i).render(g2d);
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
    
    public void loadBackgroundImage(){
        try {
            this.backgroundImg = ImageIO.read(new File("img/fondoLM.jpg"));
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

//        g2d.setColor(Color.PINK);
//        g2d.fillRect(0, 0, 1920, 1080);

        //pintamos todos los componentes en los graphics de la imagen
        this.drawComponents(g2d);

        //pintamos la imagen en el canvas
        bs.show();
        
        g2d.dispose();
    }


}
