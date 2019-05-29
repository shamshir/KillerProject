package gameRoom;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Yeray
 */
public class KillerCredits extends JPanel {
    
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    
    private GradientPaint gradientPaing;
    private BufferedImage textImage;
    private double textY;
    
    static JFrame frame; 
    
    public KillerCredits() {
        setBackground(Color.BLACK);
        Point2D pa = new Point2D.Double(0, 50);
        Point2D pb = new Point2D.Double(0, 300);
        Color ca = Color.BLACK;
        Color cb = new Color(0, 0, 0, 0);
        gradientPaing = new GradientPaint(pa, ca, pb, cb);
        try {
            textImage = ImageIO.read(getClass().getResourceAsStream("/gameRoom/img/text.png"));
        } catch (IOException ex) {
            Logger.getLogger(KillerCredits.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        
        
        
    }
    
    public void start() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                update();
                repaint();
            }
        }, 100, 1000 / 60);
    }
    
    private void update() {
        textY += 0.5;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        draw3DScrollingText(g2d);
        makeDeeperTextDarker(g2d);
    }
    
    private void draw3DScrollingText(Graphics2D g) {
        double horizontalNarrowing = 0;
        for (int screenY = 0; screenY < 600; screenY++) {
            // 1.39626 = 80 degrees
            int textureY = (int) (Math.tan(1.39626 * (screenY / 600.0)) * -150 + textY);
            int dx1 = (int) (horizontalNarrowing - 100);
            int dx2 = (int) (SCREEN_WIDTH + 100 - horizontalNarrowing);
            g.drawImage(textImage, dx1, 600 - screenY, dx2, 599 - screenY
                    , 0, textureY, textImage.getWidth(), textureY + 1, this);
            horizontalNarrowing += 0.675;
        }
    }
    
    private void makeDeeperTextDarker(Graphics2D g) {
        g.setPaint(gradientPaing);
        g.fillRect(0, 0, 800, 300);
        g.setPaint(Color.BLACK);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                KillerCredits view = new KillerCredits();
                frame = new JFrame();
                frame.setTitle("Killer Game Credits");
                frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.getContentPane().add(view);
                frame.setUndecorated(true);
                frame.setVisible(true);
                view.start();
                
                
            }
        });
        
        javax.swing.Timer t;
        t = new javax.swing.Timer(70000, new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("win");
            frame.dispose();
               
            }
        }); 
        t.start(); 
        
    }
    
}
