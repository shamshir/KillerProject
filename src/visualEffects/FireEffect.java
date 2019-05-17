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

    private BufferedImage fireImage;
    private BufferedImage testImageForEffect;

    private BufferedImage effectImg;

    private int[] sparks;
    private int[][] heatMap;
    private MyColor[] paCo;

    public FireEffect(VisibleObject vo, BufferedImage oi, int[] sourceSparks) {
        super(vo, oi, 0, 400);
        try {
            this.testImageForEffect = ImageIO.read(new File("/home/pau/NetBeansProjects/KillerProjectDevelopment/src/visibleObjects/img/wrapper.png"));
        } catch (IOException ex) {
            Logger.getLogger(FireEffect.class.getName()).log(Level.SEVERE, null, ex);
        }

        // setear paleta de colores para el fuego
        this.setPaCo();

        this.fireImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        this.sparks = sourceSparks;
        this.heatMap = new int[this.getHeight() - this.getOriginalImage().getHeight()][this.getWidth()];
//        this.effectImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        // pintar imagen para tener algo que mostrar si el hilo no se ha iniciado
        this.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
    }

    public FireEffect(VisibleObject vo, BufferedImage oi) {
        super(vo, oi, 0, 400);
        try {
            this.testImageForEffect = ImageIO.read(new File("/home/pau/NetBeansProjects/KillerProjectDevelopment/src/visibleObjects/img/wrapper.png"));
        } catch (IOException ex) {
            Logger.getLogger(FireEffect.class.getName()).log(Level.SEVERE, null, ex);
        }

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
            this.paCo[i] = new MyColor(i, i+ 10/12, i * 2/3, i/24);
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
                this.heatMap[fil][col] = (int)(((this.heatMap[fil][col]
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

    //  codigo vetusto VvV
    /**
     * Actualiza la raster del fuego TODO: especificar de que objeto en concreto
     * //
     */
//    private void updateRaster() {
//        int a, b, g, r;
//        int[][] raster2d = this.getIntRaster2d(this.fireImage);
//
//        for (int fil = 0; fil < this.heatMap.length; fil++) {
//            for (int col = 0; col < this.heatMap[0].length; col++) {
//                int h = this.heatMap[fil][col];
//
//                a = this.paCo[h].getA();
//                b = this.paCo[h].getB();
//                g = this.paCo[h].getG();
//                r = this.paCo[h].getR();
//
//                raster2d[fil + this.getOriginalImage().getHeight()][(col * 4)] = a;
//                raster2d[fil + this.getOriginalImage().getHeight()][(col * 4) + 1] = b;
//                raster2d[fil + this.getOriginalImage().getHeight()][(col * 4) + 2] = g;
//                raster2d[fil + this.getOriginalImage().getHeight()][(col * 4) + 3] = r;
//            }
//        }
//
//        this.setKillerRaster2d(raster2d);
//    }
//
//    /**
//     * Ejecuta todos los pasos para pintar el fuego
//     */
//    private void updateFire() {
//        this.updateSparks();
//        this.updateHeatMap();
//        this.updateRaster();
//    }
//
//    /**
//     * Actualiza el mapa de calor
//     */
//    private void updateHeatMap() {
//        for (int fil = 1; fil < this.heatMap.length; fil++) {
//            for (int col = 1; col < this.heatMap[0].length - 1; col++) {
//                this.heatMap[fil][col] = ((this.heatMap[fil][col]
//                        + this.heatMap[fil - 1][col - 1]
//                        + this.heatMap[fil - 1][col]
//                        + this.heatMap[fil - 1][col + 1]) / 4) - this.COOLING;
//
//                if (this.heatMap[fil][col] > 254) {
//                    this.heatMap[fil][col] = 254;
//
//                } else if (this.heatMap[fil][col] < 0) {
//                    this.heatMap[fil][col] = 0;
//                }
//            }
//        }
//    }
//
//    /**
//     * Actualiza las chispas
//     */
//    private void updateSparks() {
//        int sparkHeat;
//        for (int pos = 0; pos < this.sparks.length; pos++) {
//
//            // mira si esta especificado el haber chispas
//            if (this.sparks[pos] > 0) {
//                sparkHeat = ((int) Math.random() * 255);
//
//                // comprueba la intensidad minima
//                if (sparkHeat > this.INTENSITY) {
//                    this.heatMap[this.getOriginalImage().getHeight()][pos] = sparkHeat;
//                }
//            }
//        }
//    }
}
