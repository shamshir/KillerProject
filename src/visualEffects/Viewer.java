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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

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
        paintBackground();
        this.g2dBackground = (Graphics2D) this.backgroundImg.getGraphics();

//        this.g2d = (Graphics2D) this.offImg.getGraphics();
        while (true) {
            this.updateFrame();

            try {
                Thread.sleep((long) this.target);
            } catch (InterruptedException ex) {

            }
        }
    }

    private void pintarEspiral(Color color, Graphics2D g2d, int iteracion) {
        double x, y;

        g2d.setColor(color);

        iteracion += 1;
        x = Math.sin(iteracion) * iteracion * 3 + 960;
        y = Math.cos(iteracion) * iteracion * 3 + 520;
//            System.out.println(pivote);

        g2d.fillOval((int) x, (int) y, 1 + (iteracion / 3), 1 + (iteracion / 3));
        System.out.println("x: " + x);
        System.out.println("y: " + y + "\n");

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
    public void paintBackground() {
        //creamos una nueva imagen del tamaño del canvas
        backgroundImg = (BufferedImage) createImage(getWidth(), getHeight());
//        try {
//            fondo = ImageIO.read(new File("img/fondoLM.jpg"));
//
//        } catch (IOException ex) {
//            Logger.getLogger(Viewer.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void updateFrame() {

        //cogemos los graficos de la imagen
//      
//        g2dBackground.drawImage(this.fondo, 0, 0, null);
        this.g2dBackground.setColor(Color.PINK);
        this.g2dBackground.fillRect(0, 0, 1920, 1080);

        //pintamos todos los componentes en los graphics de la imagen
        this.drawComponents(this.g2dBackground);

        //pintamos la imagen en el canvas
        this.getGraphics().drawImage(this.backgroundImg, 0, 0, null);
    }

    public Image getImage() {
        return this.backgroundImg;
    }

}
