/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualEffects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author pau
 */
public class KillerImage extends BufferedImage implements Runnable {

    /* TO-DO: 
        · Implementar runnable para hacer la animacion (no pintarla)
        · Cambiar nombres de los metodos
        · Poner int raster como atributo?
     */
    public int numChannels = 4;
    private BufferedImage originalImage;
    private byte[] byteRaster;
    private int[][] heatMap;
    private int[] space;
    private MyColor[] paCo = new MyColor[256];

//    public KillerImage(BufferedImage imgSource, int dummy) {
//        super(imgSource.getColorModel(), imgSource.getRaster(), imgSource.getColorModel().isAlphaPremultiplied(), null);
//
//        this.originalImg = imgSource;
//        this.byteRaster = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
//
//        this.copyOriginalRasterToKillerRaster();
//    }
    /**
     * Carga una imagen y se le suma un valor para el espacio donde se pintará
     * el fuego
     *
     * @param imgSource Imagen a cargar
     * @param spaceHeight Espacio extra para el fuego
     */
    public KillerImage(BufferedImage imgSource, int spaceHeight) {
        super(imgSource.getWidth(), imgSource.getHeight() + spaceHeight, BufferedImage.TYPE_4BYTE_ABGR);
        this.originalImage = imgSource;
        this.byteRaster = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
        this.space = new int[imgSource.getWidth() * spaceHeight * 4];
        this.heatMap = new int[spaceHeight][this.getWidth()];
        this.copyOriginalRasterToKillerRaster();

        this.fillPaCo();

        int[] test = new int[this.getWidth()];

        this.addFireEffect(test);
    }

    public void addFireEffect(int[] chispas) {
        this.setSparksToHeatMap(chispas);
        this.calculateHeat();
        this.paintFire();
    }

    private void fillPaCo() {

        for (int i = 0; i < 120; i++) {
            this.paCo[i] = new MyColor(0, 0, 0, 0);
        }
        for (int i = 120; i < 160; i++) {
            this.paCo[i] = new MyColor(100, 0, 30, 199);
        }
        for (int i = 160; i < 192; i++) {
            this.paCo[i] = new MyColor(200, 10, 120, 251);
        }
        for (int i = 192; i < 256; i++) {
            this.paCo[i] = new MyColor(255, 10, 255, 251);
        }
    }

    class MyColor {

        int a, b, g, r;

        public MyColor(int a, int b, int g, int r) {
            this.a = a;
            this.b = b;
            this.g = g;
            this.r = r;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        public int getG() {
            return g;
        }

        public int getR() {
            return r;
        }

    }

    private void paintFire() {
        int intensidad;
        int currentFil;
        int a, r, g, b;

        for (int fil = 0; fil < this.heatMap.length; fil++) {
            currentFil = (fil * 4 + this.originalImage.getHeight() * 4) * this.getWidth();

            for (int col = 0; col < this.heatMap[0].length; col++) {

                intensidad = this.heatMap[fil][col];

                a = this.paCo[intensidad].getA();
                b = this.paCo[intensidad].getB();
                g = this.paCo[intensidad].getG();
                r = this.paCo[intensidad].getR();

                this.byteRaster[(col * 4) + currentFil] = (byte) a;
                this.byteRaster[(col * 4 + 1) + currentFil] = (byte) b;
                this.byteRaster[(col * 4 + 2) + currentFil] = (byte) g;
                this.byteRaster[(col * 4 + 3) + currentFil] = (byte) r;
            }
        }
    }

    /**
     * Calcula la temperatura de cada pixel. Ojo que va de arriba hacia abajo,
     */
    private void calculateHeat() {
        double enfriamiento = 50;

        for (int fil = 1; fil < this.heatMap.length; fil++) {
            for (int col = 1; col < this.heatMap[0].length - 1; col++) {

                // calcular la media
                this.heatMap[fil][col] = (int) (((this.heatMap[fil - 1][col - 1]
                        + this.heatMap[fil - 1][col]
                        + this.heatMap[fil - 1][col + 1]
                        + this.heatMap[fil][col]) / 3) - enfriamiento);

                if (this.heatMap[fil][col] > 255) {
                    this.heatMap[fil][col] = 255;

                } else if (this.heatMap[fil][col] < 0) {
                    this.heatMap[fil][col] = 0;
                }
            }
        }
    }

    private void setSparksToHeatMap(int[] chispas) {
        // setear las chispas
        int intensidad;

        for (int pos = 0; pos < chispas.length; pos++) {
            if (chispas[pos] > 0) {
                intensidad = (int) (Math.random() * 255);

                if (intensidad > 100) {
                    this.heatMap[0][pos] = 255;
                } else {
                    this.heatMap[0][pos] = 0;
                }
            }
        }
    }

    private void copyOriginalRasterToKillerRaster() {

        // raster de la imagen original
        byte[] originalRaster;
        originalRaster = ((DataBufferByte) this.originalImage.getRaster().getDataBuffer()).getData();;

        // setea la imagen original en la imagen mas grande
        for (int pos = 0; pos < originalRaster.length; pos++) {
            this.byteRaster[pos] = originalRaster[pos];
        }

    }

    //********************** 
    // * Getters & Setters * 
    //**********************
    /**
     * Devuelve la raster en una sola fila de integers. Es la manera mas comun
     * de obtener la raster de la imagen
     *
     * @return Array de ints que representa la raster de la imagen
     */
    public int[] getIntRaster(BufferedImage sourceImage) {
        byte[] byteSourceRaster;
        int[] intRaster;

        byteSourceRaster = ((DataBufferByte) sourceImage.getRaster().getDataBuffer()).getData();
        intRaster = new int[byteSourceRaster.length];

        for (int pos = 0; pos < byteSourceRaster.length; pos++) {
            intRaster[pos] = Byte.toUnsignedInt(byteSourceRaster[pos]);
        }

        return intRaster;
    }

    /**
     * Modifica la raster a partir de un array de ints de una sola dimension.
     *
     * @param sourceRaster array de int de una dimension
     */
    public void setKillerRaster(int[] sourceRaster) {
        for (int pos = 0; pos < sourceRaster.length; pos++) {
            this.byteRaster[pos] = (byte) sourceRaster[pos];
        }
    }

    /**
     * Devuelve la raster en dos dimensiones para ahorrar calculos.
     *
     * @return Array de dos dimensiones que representa la imagen.
     */
    public int[][] getKillerRaster2D() {
        int[][] intRaster2D;
        int iteracion;

        intRaster2D = new int[this.getHeight()][this.getWidth() * this.numChannels];
        iteracion = 0;

        for (int fil = 0; fil < intRaster2D.length; fil++) {
            for (int col = 0; col < intRaster2D[0].length; col++) {
                intRaster2D[fil][col] = Byte.toUnsignedInt(this.byteRaster[iteracion]);
                iteracion++;
            }
        }

        return intRaster2D;
    }

    /**
     * Modifica la raster a parti de un array de ints de 2 dimensiones
     *
     * @param sourceRaster
     */
    public void setRaster2D(int[][] sourceRaster) {
        int fil, col;

        for (int pos = 0; pos < this.byteRaster.length; pos++) {
            fil = pos / this.byteRaster.length;
            col = pos % this.byteRaster.length;
            this.byteRaster[pos] = (byte) sourceRaster[fil][col];
        }
    }

    /**
     * Cargar una imagen desde una URL.
     *
     * @param Archivo que contiene la imagen. OJO, aparte de ser tipo .png, ha
     * de tener un canal alpha!!
     */
    public void setOriginalImage(String url) {
        try {
            this.originalImage = ImageIO.read(new File(url));
        } catch (IOException ex) {
            Logger.getLogger(KillerImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Devuelve la imagen original
     *
     * @return
     */
    public BufferedImage getOriginalImage() {
        return this.originalImage;
    }

    @Override
    public void run() {
        int[] test = new int[this.getWidth()];
        for (int i = 0; i < test.length; i++) {
            test[i] = 2;
            
        }

        System.out.println(Arrays.toString(test));
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(KillerImage.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.addFireEffect(test);
        }

    }

}
