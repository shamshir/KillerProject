/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualEffects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import visibleObjects.VisibleObject;

/**
 *
 * @author pau
 */
public class ExplosionEffect extends KillerImage {

    private BufferedImage testImage;

    public ExplosionEffect(VisibleObject vo, BufferedImage oi) {
        super(vo, oi, 100, 100);
        this.loadTestImage();
    }

    public void loadTestImage() {
        try {
            this.testImage = ImageIO.read(new File("src/visibleObjects/img/safe.png"));
        } catch (IOException ex) {
            Logger.getLogger(ExplosionEffect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateImage() {
        this.createDisplayImage();
    }

    private void createDisplayImage() {
        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        img.getGraphics().drawImage(this.testImage, 0, 0, null);
        img.getGraphics().drawImage(this.originalImage, this.getWidth() / 2, this.getHeight() / 2, null);

        this.getGraphics().drawImage(img, 0, 0, null);
    }

    @Override
    public void run() {

        while (true) {
            ((Graphics2D) this.getGraphics()).setColor(Color.red);
            ((Graphics2D) this.getGraphics()).setStroke(new BasicStroke(3));

            ((Graphics2D) this.getGraphics()).drawRect(1, 1, 100, 100);
//            this.updateImage();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExplosionEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // kill object
    }

}
