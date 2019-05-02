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

    private KillerGame killer;

    private int fps = 60;
    private double averageFPS;
    private double target = 1000 / fps;

    private BufferedImage fondo;
    private BufferedImage frame;
    private Image offImg;
    public Graphics2D g2d;

    public Viewer(KillerGame k) {
        killer = k;
        setSize(new Dimension(killer.getWidth(), killer.getHeight()));
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocus();

    }
    
    @Override
    public void repaint(){
    }

    @Override
    public void run() {
        this.createBufferStrategy(2);
        paintBackground();
        int iteracion;
        iteracion = 0;
        int vueltas = 385;

//        this.g2d = (Graphics2D) this.offImg.getGraphics();

        while (true) {
            this.updateFrame();

            try {
                Thread.sleep((long) this.target);
            } catch (InterruptedException ex) {

            }
        }
    }

//    public void run() {
//        paintBackground();
//        int iteracion;
//        iteracion = 0;
//        int vueltas = 385;
//
//        while (true) {
//            update();
//
//            try {
//                Thread.sleep((long) 1000);
//            } catch (InterruptedException ex) {
//
//            }
//        }
//    }
    //            for (int i = 0; i < vueltas; i++) {
    //
    //                update();
    //
    //                this.pintarEspiral(Color.magenta, (Graphics2D) this.fondo.getGraphics(), iteracion);
    //                iteracion++;
    //
    //                try {
    //                    Thread.sleep((long) target);
    //                } catch (InterruptedException ex) {
    //
    //                }
    //            }
    //            iteracion = vueltas;
    //            for (int i = 0; i < vueltas; i++) {
    //                update();
    //
    //                this.pintarEspiral(Color.BLUE, (Graphics2D) this.fondo.getGraphics(), iteracion);
    //                iteracion--;
    //
    //                try {
    //                    Thread.sleep((long) target);
    //                } catch (InterruptedException ex) {
    //
    //                }
    //
    //            }
    //
    //            iteracion = 0;
    //
    //            for (int i = 0; i < vueltas; i++) {
    //                update();
    //
    //                this.pintarEspiral(Color.ORANGE, (Graphics2D) this.fondo.getGraphics(), iteracion);
    //                iteracion++;
    //
    //                try {
    //                    Thread.sleep((long) target);
    //                } catch (InterruptedException ex) {
    //
    //                }
    //
    //            }
    //
    //            iteracion = vueltas;
    //
    //            for (int i = 0; i < vueltas; i++) {
    //                update();
    //
    //                this.pintarEspiral(Color.GREEN, (Graphics2D) this.fondo.getGraphics(), iteracion);
    //                iteracion--;
    //
    //                try {
    //                    Thread.sleep((long) target);
    //                } catch (InterruptedException ex) {
    //
    //                }
    //
    //            }
    //
    //            iteracion = 0;
    //
    //            for (int i = 0; i < vueltas; i++) {
    //                update();
    //
    //                this.pintarEspiral(Color.PINK, (Graphics2D) this.fondo.getGraphics(), iteracion);
    //                iteracion++;
    //
    //                try {
    //                    Thread.sleep((long) target);
    //                } catch (InterruptedException ex) {
    //
    //                }
    //
    //            }
    //
    //            iteracion = vueltas;
    //
    //            for (int i = 0; i < vueltas; i++) {
    //                update();
    //
    //                this.pintarEspiral(Color.YELLOW, (Graphics2D) this.fondo.getGraphics(), iteracion);
    //                iteracion--;
    //
    //                try {
    //                    Thread.sleep((long) target);
    //                } catch (InterruptedException ex) {
    //
    //                }
    //
    //            }
    //
    //            iteracion = 0;
    //
    //            for (int i = 0; i < vueltas; i++) {
    //                update();
    //
    //                this.pintarEspiral(Color.CYAN, (Graphics2D) this.fondo.getGraphics(), iteracion);
    //                iteracion++;
    //
    //                try {
    //                    Thread.sleep((long) target);
    //                } catch (InterruptedException ex) {
    //
    //                }
    //
    //            }
    //
    //            iteracion = vueltas;
    //
    //            for (int i = 0; i < vueltas; i++) {
    //                update();
    //
    //                this.pintarEspiral(Color.orange, (Graphics2D) this.fondo.getGraphics(), iteracion);
    //                iteracion--;
    //
    //                try {
    //                    Thread.sleep((long) target);
    //                } catch (InterruptedException ex) {
    //
    //                }
    //
    //            }
    //
    //        }
    //
    //    }
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

        for (int i = 0; i < killer.getObjects().size(); i++) {
            killer.getObjects().get(i).render(g2d);
        }
//        drawConnectionInfo(g2d);

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

    public void paintBackground() {
        //creamos una nueva imagen del tamaño del canvas
        offImg = createImage(getWidth(), getHeight());
        try {
            fondo = ImageIO.read(new File("img/fondoLM.jpg"));

        } catch (IOException ex) {
            Logger.getLogger(Viewer.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateFrame() {
        BufferStrategy bs;

        bs = this.getBufferStrategy();
        if (bs == null) {
            System.out.println("kgd");
            return; //=================================================>>>>>>>>>
        }

        do {
            Graphics2D gg = (Graphics2D) bs.getDrawGraphics();

            //pintamos el fondo
            gg.drawImage(this.fondo, 0, 0, null);

            //pintamos todos los componentes en los graphics de la imagen
            this.drawComponents(gg);

            gg.dispose();
        } while (bs.contentsRestored());

        // mostramos la imagen en el canvas
        bs.show();

    }

    public Image getImage() {
        return this.offImg;
    }

}
