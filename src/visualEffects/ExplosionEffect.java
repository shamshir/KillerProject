package visualEffects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import visibleObjects.Alive;
import visibleObjects.VisibleObject;

/**
 *
 * @author pau
 */
public class ExplosionEffect extends KillerImage {

    private BufferedImage[] framesList;
    private int frame;

    public ExplosionEffect(VisibleObject vo) {
        super(vo, 300, 300); // tamaño imagen explosion

        this.framesList = new BufferedImage[12];
        this.frame = 0;
        this.loadImages();
    }

    private void loadImages() {

        try {
            this.framesList[0] = ImageIO.read(new File("src/visualEffects/xplosion/x1.png"));
            this.framesList[1] = ImageIO.read(new File("src/visualEffects/xplosion/x2.png"));
            this.framesList[2] = ImageIO.read(new File("src/visualEffects/xplosion/x3.png"));
            this.framesList[3] = ImageIO.read(new File("src/visualEffects/xplosion/x4.png"));
            this.framesList[4] = ImageIO.read(new File("src/visualEffects/xplosion/x5.png"));
            this.framesList[5] = ImageIO.read(new File("src/visualEffects/xplosion/x6.png"));
            this.framesList[6] = ImageIO.read(new File("src/visualEffects/xplosion/x7.png"));
            this.framesList[7] = ImageIO.read(new File("src/visualEffects/xplosion/x8.png"));
            this.framesList[8] = ImageIO.read(new File("src/visualEffects/xplosion/x9.png"));
            this.framesList[9] = ImageIO.read(new File("src/visualEffects/xplosion/x10.png"));
            this.framesList[10] = ImageIO.read(new File("src/visualEffects/xplosion/x11.png"));
            this.framesList[11] = ImageIO.read(new File("src/visualEffects/xplosion/x12.png"));

        } catch (IOException e) {
            System.err.println("Xplosion file not found");
        }

    }

    private void paintFrame() {

        this.clearImage();
        this.graphics.drawImage(this.framesList[this.frame], 0, 0, null);

    }

    @Override
    public void run() {
        
        while (this.frame < this.framesList.length) {
            paintFrame();
            this.frame++;
            System.out.println("frame: " + this.frame);

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExplosionEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // kill object
        ((Alive) this.visibleObject).changeState(Alive.State.DEAD);
    }
}
