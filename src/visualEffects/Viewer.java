package visualEffects;

import game.KillerGame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author berna
 */
public class Viewer extends Canvas implements Runnable {

    private KillerGame killerGame;

    private final int FPS = 60;
    private final int TOTAL_BACKGOUND_IMGS = 4;
    private double target = 1000 / FPS;

    private BufferedImage backgroundImg;
    public Graphics2D g2dBackground;

    public Viewer(KillerGame kg) {
        this.killerGame = kg;
        this.setSize(new Dimension(this.killerGame.getWidth(), this.killerGame.getHeight()));
        this.setFocusable(true);
        this.requestFocus();

//        HashMap hs = new HashMap();
//        hs.put(RenderingHints.KEY_COLOR_RENDERING,
//                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        
//        hs.put(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//               
//        ((Graphics2D) this.getGraphics()).setRenderingHints(hs);

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
    }

    public void loadBackgroundImage() {

        int numBackground = (this.killerGame.getWindowNumber() % this.TOTAL_BACKGOUND_IMGS);

        try {
            this.backgroundImg = ImageIO.read(new File("src/visualEffects/backgroundImages/b" + numBackground + ".jpeg"));
        } catch (IOException ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateFrame() {
        BufferStrategy bs;

        bs = this.getBufferStrategy();
        if (bs == null) {
            System.out.println("no tira");
            return; //=====================================================>>>>>
        }

        // en que se diferencia con el createGraphics?
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
//        
//        
        HashMap hs = new HashMap();
//        hs.put(RenderingHints.KEY_COLOR_RENDERING,
//                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        
//        hs.put(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
               
//        ((Graphics2D) this.getGraphics()).setRenderingHints(hs);


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(this.backgroundImg, 0, 0, null);

        //pintamos todos los componentes en los graphics de la imagen
        this.drawComponents(g2d);

        //mostramos la imagen del canvas
        bs.show();

        // liberamos recursos
        g2d.dispose();
    }

}
