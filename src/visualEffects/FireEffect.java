package visualEffects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import visibleObjects.Alive;
import visibleObjects.Alive.State;
import visibleObjects.KillerShip;
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
    private MyColor[] shipPaCo;
    private MyColor[] paCoB;
    private MyColor[] paCoR;

    public FireEffect(VisibleObject vo, BufferedImage oi) {
        super(vo, oi, 0, oi.getWidth() * 2);

        // setear paleta de colores para el fuego
        this.setPaCoB();
        this.setPaCoR();

        this.shipPaCo = this.paCoB;

        this.heatMap = new int[this.getHeight() - this.getOriginalImage().getHeight()][this.getWidth()];
        this.createDefaultSparks();
    }

    public FireEffect(VisibleObject vo) {
        super(vo, vo.getImg(), 0, vo.getImg().getWidth() * 2);

        // setear paleta de colores para el fuego
        this.setPaCoB();
        this.setPaCoR();

        this.shipPaCo = this.paCoB;

        this.heatMap = new int[this.getHeight() - this.getOriginalImage().getHeight()][this.getWidth()];
        this.createDefaultSparks();
    }

    /**
     * Cambiar j en azul y 255 en rojo para fuego rogo en boost/dash(?)
     */
    public void setPaCoB() {

        this.paCoB = new MyColor[256];
        for (int i = 255; i >= 0; i--) {

            if (i < 256 && i >= 230) {
                paCoB[i] = new MyColor(255, 255, 255, 255);
            }
            for (int j = 255; i < 230 && i >= 220; i--, j -= 10) {
                paCoB[i] = new MyColor(255, 255, 255, j);
            }
            if (i >= 210 && i < 220) {
                paCoB[i] = new MyColor(255, Color.cyan.getBlue(), Color.cyan.getGreen(), Color.cyan.getRed());
            }

            for (int j = 255; i < 210 && i >= 200; i--, j -= 3) {
                paCoB[i] = new MyColor(255, 255, j, 0);
            }

            if (i >= 180 && i < 200) {
                paCoB[i] = new MyColor(255, 255, 200, 0);
            }

            for (int j = 200; i > 100 && i < 180; i--, j -= 4) {
                paCoB[i] = new MyColor(255, 255, j, 0);
            }
            for (int j = 255; i <= 100 && i > 20; i--, j -= 2) {
                paCoB[i] = new MyColor(255, j, 0, 0);
            }
            for (int j = 100; i <= 20 && i > 0; i--, j -= 3) {
                paCoB[i] = new MyColor(255, j, 0, 0);
            }
            if (i == 0) {
                paCoB[i] = new MyColor(255, 0, 0, 0);
            }
        }

        for (int i = 0; i < 100; i++) {
            paCoB[i].setA((int) (i * 2.501D));
        }

        for (int i = 100; i < 256; i++) {
            paCoB[i].setA(255);
        }

        for (int i = 255; i >= 0; i--) {

            if (i < 256 && i >= 230) {
                paCoB[i] = new MyColor(255, 255, 255, 255);
            }
            for (int j = 255; i < 230 && i >= 220; i--, j -= 10) {
                paCoB[i] = new MyColor(255, 255, 255, j);
            }
            if (i >= 210 && i < 220) {
                paCoB[i] = new MyColor(255, Color.yellow.getBlue(), Color.yellow.getGreen(), Color.yellow.getRed());
            }

            for (int j = 255; i < 210 && i >= 200; i--, j -= 3) {
                paCoB[i] = new MyColor(255, 0, j, 255);
            }

            if (i >= 180 && i < 200) {
                paCoB[i] = new MyColor(255, 255, 200, 0);
            }

            for (int j = 200; i > 100 && i < 180; i--, j -= 4) {
                paCoB[i] = new MyColor(255, 255, j, 0);
            }
            for (int j = 255; i <= 100 && i > 20; i--, j -= 2) {
                paCoB[i] = new MyColor(255, j, 0, 0);
            }
            for (int j = 100; i <= 20 && i > 0; i--, j -= 3) {
                paCoB[i] = new MyColor(255, j, 0, 0);
            }
            if (i == 0) {
                paCoB[i] = new MyColor(255, 0, 0, 0);
            }
        }

        for (int i = 0; i < 100; i++) {
            paCoB[i].setA((int) (i * 2.501D));
        }

        for (int i = 100; i < 256; i++) {
            paCoB[i].setA(255);
        }

//        this.paCoR = new MyColor[256];
//        for (int i = 0; i < 256; i++) {
//            this.paCoR[i] = new MyColor(i, i + 10 / 12, i * 2 / 3, i / 24);
//        }
//        this.shipPaCo = this.paCoB;
    }

    public void setPaCoR() {

        this.paCoR = new MyColor[256];
        for (int i = 255; i >= 0; i--) {

            if (i < 256 && i >= 230) {
                paCoR[i] = new MyColor(255, 255, 255, 255);
            }
            for (int j = 255; i < 230 && i >= 220; i--, j -= 10) {
                paCoR[i] = new MyColor(255, 255, 255, j);
            }
            if (i >= 210 && i < 220) {
                paCoR[i] = new MyColor(255, Color.yellow.getBlue(), Color.yellow.getGreen(), Color.yellow.getRed());
            }

            for (int j = 255; i < 210 && i >= 200; i--, j -= 3) {
                paCoR[i] = new MyColor(255, 0, j, 255);
            }

            if (i >= 180 && i < 200) {
                paCoR[i] = new MyColor(255, 0, 200, 255);
            }

            for (int j = 200; i > 100 && i < 180; i--, j -= 4) {
                paCoR[i] = new MyColor(255, 0, j, 255);
            }
            for (int j = 255; i <= 100 && i > 20; i--, j -= 2) {
                paCoR[i] = new MyColor(255, j, 0, 255);
            }
            for (int j = 100; i <= 20 && i > 0; i--, j -= 3) {
                paCoR[i] = new MyColor(255, j, 0, 255);
            }
            if (i == 0) {
                paCoR[i] = new MyColor(255, 0, 0, 255);
            }
        }

        for (int i = 0; i < 100; i++) {
            paCoR[i].setA((int) (i * 2.501D));
        }

        for (int i = 100; i < 256; i++) {
            this.paCoR[i].setA(255);
        }

        for (int i = 255; i >= 0; i--) {

            if (i < 256 && i >= 230) {
                this.paCoR[i] = new MyColor(255, 255, 255, 255);
            }
            for (int j = 255; i < 230 && i >= 220; i--, j -= 10) {
                this.paCoR[i] = new MyColor(255, 255, 255, j);
            }
            if (i >= 210 && i < 220) {
                this.paCoR[i] = new MyColor(255, Color.yellow.getBlue(), Color.yellow.getGreen(), Color.yellow.getRed());
            }

            for (int j = 255; i < 210 && i >= 200; i--, j -= 3) {
                this.paCoR[i] = new MyColor(255, 0, j, 255);
            }

            if (i >= 180 && i < 200) {
                this.paCoR[i] = new MyColor(255, 255, 200, 0);
            }

            for (int j = 200; i > 100 && i < 180; i--, j -= 4) {
                this.paCoR[i] = new MyColor(255, 255, j, 255);
            }
            for (int j = 255; i <= 100 && i > 20; i--, j -= 2) {
                paCoR[i] = new MyColor(255, j, 0, 255);
            }
            for (int j = 100; i <= 20 && i > 0; i--, j -= 3) {
                paCoR[i] = new MyColor(255, j, 0, 255);
            }
            if (i == 0) {
                paCoR[i] = new MyColor(255, 0, 0, 255);
            }
        }

        for (int i = 0; i < 100; i++) {
            paCoR[i].setA((int) (i * 2.501D));
        }

        for (int i = 100; i < 256; i++) {
            paCoR[i].setA(255);
        }

//        this.paCoR = new MyColor[256];
//        for (int i = 0; i < 256; i++) {
//            this.paCoR[i] = new MyColor(i, i + 10 / 12, i * 2 / 3, i / 24);
//        }
//        this.shipPaCo = this.paCoR;
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

                a = this.shipPaCo[heatMapPos].getA();
                b = this.shipPaCo[heatMapPos].getB();
                g = this.shipPaCo[heatMapPos].getG();
                r = this.shipPaCo[heatMapPos].getR();

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
//        this.paintTestHeatmapArea();
        this.paintFireOnImage();

    }

    public void paintTestHeatmapArea() {
        for (int fil = 0; fil < this.heatMap.length; fil++) {
            for (int col = 0; col < this.heatMap[0].length; col++) {
                this.heatMap[fil][col] = 255;
            }
        }
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

    private void updateSparks(int vel) {

        double dX = Math.abs(((Alive) this.visibleObject).getDx());
        double dY = Math.abs(((Alive) this.visibleObject).getDy());
        double d = dX > dY ? dX : dY;

        int intensity;
        double minIntensity;

        minIntensity = d > 0 ? this.DEFAULT_MIN_INTENSITY : 512;

        for (int pos = 0; pos < this.sparks.length; pos++) {
            if (this.sparks[pos] > 1) {

                intensity = (int) (Math.random() * 255);

                if (intensity > minIntensity) {
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

        while (this.hasVisibleObjectThisEffect() && this.isVisibleObjectSafeOrAlive()) {
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
