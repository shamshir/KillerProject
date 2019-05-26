package visualEffects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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

    private BufferedImage[] frames;
    private int frame;

    public ExplosionEffect(VisibleObject vo) {
        super(vo);

        this.frames = new BufferedImage[12];
        this.frame = 0;
        this.loadImages();
    }

    private void loadImages() {

        try {
            this.frames[0] = ImageIO.read(new File("src/visualEffects/xplosion/x1.png"));
            this.frames[1] = ImageIO.read(new File("src/visualEffects/xplosion/x2.png"));
            this.frames[2] = ImageIO.read(new File("src/visualEffects/xplosion/x3.png"));
            this.frames[3] = ImageIO.read(new File("src/visualEffects/xplosion/x4.png"));
            this.frames[4] = ImageIO.read(new File("src/visualEffects/xplosion/x5.png"));
            this.frames[5] = ImageIO.read(new File("src/visualEffects/xplosion/x6.png"));
            this.frames[6] = ImageIO.read(new File("src/visualEffects/xplosion/x7.png"));
            this.frames[7] = ImageIO.read(new File("src/visualEffects/xplosion/x8.png"));
            this.frames[8] = ImageIO.read(new File("src/visualEffects/xplosion/x9.png"));
            this.frames[9] = ImageIO.read(new File("src/visualEffects/xplosion/x10.png"));
            this.frames[10] = ImageIO.read(new File("src/visualEffects/xplosion/x11.png"));
            this.frames[11] = ImageIO.read(new File("src/visualEffects/xplosion/x12.png"));

        } catch (IOException e) {
            System.err.println("Xplosion file not found");
        }

    }

    private void paintFrame() {
        byte[] frameData = ((DataBufferByte) this.frames[frame].
                getRaster().
                getDataBuffer()).
                getData();

        byte[] frameDataCopy = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
        System.arraycopy(frameData, 0, frameDataCopy, 0, frameData.length);
    }

    @Override
    public void run() {
        while (frame < this.frames.length) {
            paintFrame();
            this.frame++;

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExplosionEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // kill object
        ((Alive)this.visibleObject).changeState(Alive.State.DEAD);
    }
}
