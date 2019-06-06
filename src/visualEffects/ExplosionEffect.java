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

    private static BufferedImage[] framesList;
    private int frame;

    public ExplosionEffect(VisibleObject vo) {
        super(vo, 300, 300); // tamaño imagen explosion

        ExplosionEffect.framesList = new BufferedImage[12];
        this.frame = 0;
        this.loadImages();
    }

    private void loadImages() {

        try {
            int numImg;

            for (int numFrame = 0; numFrame < ExplosionEffect.framesList.length; numFrame++) {
                numImg = numFrame + 1;
                ExplosionEffect.framesList[numFrame] = ImageIO.read(this.getClass().getResource("xplosionImgs/x" + numImg + ".png"));
            }
        } catch (IOException e) {
            System.err.println("Xplosion file not found");
        }

    }

    private void paintFrame() {

        this.clearImage();
        this.graphics.drawImage(ExplosionEffect.framesList[this.frame], 0, 0, null);

    }

    @Override
    public void run() {
        
        while (this.frame < ExplosionEffect.framesList.length) {
            paintFrame();
            this.frame++;

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
