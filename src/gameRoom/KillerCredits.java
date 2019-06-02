package gameRoom;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    //Variables para el tamaño de la ventana
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    //Variables para el texto de créditos
    private GradientPaint gradientPaing;
    private BufferedImage textImage;
    private double textY;

    //Variable jFrame
    static JFrame frame;

    //Timer
    static javax.swing.Timer t;

    /**
     * Constructor de KillerCredits
     */
    public KillerCredits() {
        addKeyEventListener();
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

    /**
     * Start que con un timer hace update y repaint del texto de créditos
     */
    public void start() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                update();
                repaint();
            }
        }, 100, 1000 / 60);
    }

    /**
     * Actualiza la posición Y del texto
     */
    private void update() {
        textY += 0.5;
    }

    /**
     * Pinta todos los componentes
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        draw3DScrollingText(g2d);
        makeDeeperTextDarker(g2d);
    }

    /**
     * Pinta el texto en angulo
     *
     * @param g
     */
    private void draw3DScrollingText(Graphics2D g) {
        double horizontalNarrowing = 0;
        for (int screenY = 0; screenY < 600; screenY++) {
            // 1.39626 = 80 degrees
            int textureY = (int) (Math.tan(1.39626 * (screenY / 600.0)) * -150 + textY);
            int dx1 = (int) (horizontalNarrowing - 100);
            int dx2 = (int) (SCREEN_WIDTH + 100 - horizontalNarrowing);
            g.drawImage(textImage, dx1, 600 - screenY, dx2, 599 - screenY, 0, textureY, textImage.getWidth(), textureY + 1, this);
            horizontalNarrowing += 0.675;
        }
    }

    /**
     * Hace desaparecer el texto al alejarse
     *
     * @param g
     */
    private void makeDeeperTextDarker(Graphics2D g) {
        g.setPaint(gradientPaing);
        g.fillRect(0, 0, 800, 300);
        g.setPaint(Color.BLACK);
    }

    /**
     * Método listener para salir de los créditos con dispose y 
     * volver al menú principal cambiando la música a la vez
     */
    private void addKeyEventListener() {
        // Key listener
        KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(final KeyEvent e) {
                if (e.getKeyCode() == 27) {      
                    frame.dispose();
                    KillerPanelPrincipal.menuRadio();
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    /**
     * main de la clase que crea la ventana y añade el jPanel de los créditos
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                KillerCredits view = new KillerCredits();
                frame = new JFrame();
                frame.setTitle("Killer Game Credits");
                frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.getContentPane().add(view);
                frame.setUndecorated(true);
                frame.setVisible(true);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        frame.dispose();
                        KillerPanelPrincipal.menuRadio();
                    }
                });
                view.start();
            }
        });

        //Timer que cierra los créditos sincronizado con el tiempo 
        //que tarda el texto en dejar de verse
        //También vuelve a poner la música de menú
        t = new javax.swing.Timer(70000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                KillerPanelPrincipal.menuRadio();
                t.stop();
            }
        });
        t.start();
    }
}
