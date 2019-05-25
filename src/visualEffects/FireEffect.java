/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualEffects;

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
public class FireEffect extends KillerImage {

    private final int NUM_CHANNELS = 4;
    private final int MIN_INTENSITY = 20;
    private final double COOLING = 5.5;

    private int[] sparks;
    private int[][] heatMap;
    private MyColor[] paCo;

    public FireEffect(VisibleObject vo, BufferedImage oi, int[] sourceSparks) {
        super(vo, oi, 0, 400);

        // setear paleta de colores para el fuego
        this.setPaCo();

        this.sparks = sourceSparks;
        this.heatMap = new int[this.getHeight() - this.getOriginalImage().getHeight()][this.getWidth()];

        // pintar imagen para tener algo que mostrar si el hilo no se ha iniciado
        this.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
    }

    public FireEffect(VisibleObject vo, BufferedImage oi) {
        super(vo, oi, 0, 400);

        // setear paleta de colores para el fuego
        this.setPaCo();

        this.heatMap = new int[this.getHeight() - this.getOriginalImage().getHeight()][this.getWidth()];
        this.createDefaultSparks();

        // pintar imagen para tener algo que mostrar si el hilo no se ha iniciado
        this.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
    }

    /**
     * Setea la paleta de colores a usar
     */
    public void setPaCo() {
        this.paCo = new MyColor[256];
        for (int i = 0; i < 256; i++) {
            this.paCo[i] = new MyColor(i, i + 10 / 12, i * 2 / 3, i / 24);
        }
    }

    private void createDefaultSparks() {

        this.sparks = new int[this.getWidth()];

        for (int pos = 0; pos < this.sparks.length; pos++) {
            this.sparks[pos] = 255;
        }
    }

    /**
     * Crea la imagen resltante que se verá al final
     */
    private void construirImg() {
        this.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
//        this.effectImg.getGraphics().drawImage(this.testImageForEffect, 0, 0, null);
    }

    /**
     * Mira si el objeto padre sigue teniendo dicho efecto
     *
     * @return True si lo sigue teniendo, false si no
     */
    private boolean checkObjectEffect() {
        if (this.visibleObject.getKillerImage().equals(this)) {
            return true;
        }

        return false;
    }

    private void updateSparks() {
        int intensity;
        for (int pos = 0; pos < this.sparks.length; pos++) {
            if (this.sparks[pos] > 1) {
                intensity = (int) (Math.random() * 255);

                if (intensity > this.MIN_INTENSITY) {
                    this.heatMap[0][pos] = 255;
                } else {
                    this.heatMap[0][pos] = 0;
                }
            }
        }
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
        this.updateSparks();
        this.updateHeatMap();
        this.paintFireOnImage();

    }

    @Override
    public void run() {

        while (this.checkObjectEffect()) {
            this.updateFire();

//            this.construirImg();
//            this.getGraphics().drawImage(this.effectImg, 0, 0, null);
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(FireEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
