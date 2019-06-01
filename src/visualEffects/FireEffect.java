package visualEffects;

import java.awt.Color;
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
public class FireEffect extends KillerImage {

    private final int NUM_CHANNELS = 4;
    private final int DEFAULT_MIN_INTENSITY = 20;
    private final double COOLING = 5.6;

    private int[] sparks;
    private int[][] heatMap;
    private MyColor[] paCo;
    private MyColor[] paCoR;
    private MyColor[] paCoB;

    public FireEffect(VisibleObject vo, BufferedImage oi, int[] sourceSparks) {
        super(vo, oi, 0, 400);

        // setear paleta de colores para el fuego
        this.setPaCo();

        this.sparks = sourceSparks;
        this.heatMap = new int[this.getHeight() - this.getOriginalImage().getHeight()][this.getWidth()];

        // pintar imagen para tener algo que mostrar si el hilo no se ha iniciado
//        this.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
    }

    public FireEffect(VisibleObject vo, BufferedImage oi) {
        super(vo, oi, 0, 400);

        // setear paleta de colores para el fuego
        this.setPaCo();

        this.heatMap = new int[this.getHeight() - this.getOriginalImage().getHeight()][this.getWidth()];
        this.createDefaultSparks();

        // pintar imagen para tener algo que mostrar si el hilo no se ha iniciado
//        this.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
    }

    /**
     * Setea la paleta de colores a usar
     */
//    public void setPaCo() {
//        this.paCo = new MyColor[256];
//        for (int i = 0; i < 256; i++) {
//            this.paCo[i] = new MyColor(i, i + 10 / 12, i * 2 / 3, i / 24);
//        }
//    }

    public void setPaCo() {

        this.paCoR = new MyColor[256];
        for (int i = 255; i >= 0; i--) {

            if (i < 256 && i >= 230) {
                paCoR[i] = new MyColor(255, 255, 255, 255);
            }
            for (int j = 255; i < 230 && i >= 220; i--, j -= 10) {
                paCoR[i] = new MyColor(255, 255, 255, j);
            }
            if (i >= 210 && i < 220) {
                paCoR[i] = new MyColor(255, Color.cyan.getBlue(), Color.cyan.getGreen(), Color.cyan.getRed());
            }

            for (int j = 255; i < 210 && i >= 200; i--, j -= 3) {
                paCoR[i] = new MyColor(255, 255, j, 0);
            }

            if (i >= 180 && i < 200) {
                paCoR[i] = new MyColor(255, 255, 200, 0);
            }

            for (int j = 200; i > 100 && i < 180; i--, j -= 4) {
                paCoR[i] = new MyColor(255, 255, j, 0);
            }
            for (int j = 255; i <= 100 && i > 20; i--, j -= 2) {
                paCoR[i] = new MyColor(255, j, 0, 0);
            }
            for (int j = 100; i <= 20 && i > 0; i--, j -= 3) {
                paCoR[i] = new MyColor(255, j, 0, 0);
            }
            if (i == 0) {
                paCoR[i] = new MyColor(255, 0, 0, 0);
            }
        }

        for (int i = 0; i < 100; i++) {
            paCoR[i].setA((int) (i * 2.501D));
        }

        for (int i = 100; i < 256; i++) {
            paCoR[i].setA(255);
        }

        this.paCoB = new MyColor[256];
        for (int i = 0; i < 256; i++) {
            this.paCoB[i] = new MyColor(i, i + 10 / 12, i * 2 / 3, i / 24);
        }

        this.paCo = this.paCoR;
    }

    /**
     * Mira si el objeto padre sigue teniendo dicho efecto
     *
     * @return True si lo sigue teniendo, false si no
     */
    protected boolean checkObjectEffect() {
        if (this.visibleObject.getKillerImage().equals(this)) {
            return true;
        }

        return false;
    }

    private int corregirIntensidad(int intensidad) {
        if (intensidad < 0) {
            return 0;
        }

        if (intensidad > 255) {
            return 255;
        }

        return intensidad;
    }

    private void createDefaultSparks() {

        this.sparks = new int[this.getWidth()];

        for (int pos = this.sparks.length / 3; pos < this.sparks.length - (this.sparks.length / 3); pos++) {
            this.sparks[pos] = 255;
        }
    }

    private void paintFireOnImage() {
        // colores y alpha
        int a, b, g, r;

        // posicion de la raster
        int rasterPos;
        int heatMapPos;

        for (int fil = 0; fil < this.heatMap.length; fil++) {
            for (int col = 0; col < this.heatMap[0].length; col++) {
                heatMapPos = this.heatMap[fil][col];
                rasterPos = (col * this.NUM_CHANNELS) + ((fil + this.originalImage.getHeight()) * this.heatMap[0].length * this.NUM_CHANNELS);

                a = this.paCo[heatMapPos].getA();
                b = this.paCo[heatMapPos].getB();
                g = this.paCo[heatMapPos].getG();
                r = this.paCo[heatMapPos].getR();

                this.raster[rasterPos] = (byte) a;
                this.raster[rasterPos + 1] = (byte) b;
                this.raster[rasterPos + 2] = (byte) g;
                this.raster[rasterPos + 3] = (byte) r;
            }
        }
    }

    private void updateFire() {
        this.updateSparks(0);
        this.updateHeatMap();
        this.paintFireOnImage();

    }

    private void updateHeatMap() {
        for (int fil = 1; fil < this.heatMap.length; fil++) {
            for (int col = 1; col < this.heatMap[0].length - 1; col++) {
                this.heatMap[fil][col] = (int) (((this.heatMap[fil][col]
                        + this.heatMap[fil - 1][col - 1]
                        + this.heatMap[fil - 1][col]
                        + this.heatMap[fil - 1][col + 1]) / 3.9) - this.COOLING);

                this.heatMap[fil][col] = this.corregirIntensidad(this.heatMap[fil][col]);

            }
        }
    }

    private void updateSparks() {
        int intensity;
        for (int pos = 0; pos < this.sparks.length; pos++) {
            if (this.sparks[pos] > 1) {
                intensity = (int) (Math.random() * 255);

                if (intensity > this.DEFAULT_MIN_INTENSITY) {
                    this.heatMap[0][pos] = 255;
                } else {
                    this.heatMap[0][pos] = 0;
                }
            }
        }
    }

    private void updateSparks(int vel) {

        double dX = Math.abs(((Alive) this.visibleObject).getDx());
        double dY = Math.abs(((Alive) this.visibleObject).getDy());
        double d = dX > dY ? dX : dY; // ojo revisar

        int intensity;
        double minIntensity, currIntensity;

//        currIntensity = (int) (d * 90);
//        if (currIntensity < 0) {
//            System.out.println((d * 120) + " ----------------- \n");
//      
//        }
//        if (d > 128) {
//            minIntensity = this.DEFAULT_MIN_INTENSITY - 2;
//        } else if (d > 10){
//            minIntensity = this.DEFAULT_MIN_INTENSITY + 5;
//        } else {
//            minIntensity = 256;
//        }
        minIntensity = d > 0 ? this.DEFAULT_MIN_INTENSITY : 512;

        for (int pos = 0; pos < this.sparks.length; pos++) {
            if (this.sparks[pos] > 1) {

                intensity = (int) (Math.random() * 255);

                if (intensity > minIntensity) {
//                    System.out.println("FE: intensidad > min");
                    this.heatMap[0][pos] = 255;
                } else {
                    this.heatMap[0][pos] = 0;
                }
            }
        }

    }

    // run method
    @Override
    public void run() {

        while (this.checkObjectEffect()) {
            this.updateFire();

            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(FireEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // getters y setters
    @Override
    protected void setRenderWidth() {
        this.renderWidth = this.visibleObject.getImgWidth();
    }

    @Override
    protected void setRenderHeight() {
        this.renderHeight = (this.visibleObject.getImgWidth() * this.getHeight()) / this.getWidth();
    }

}
