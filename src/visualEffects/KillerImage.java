/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualEffects;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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
public class KillerImage extends BufferedImage implements Runnable {

    protected VisibleObject visibleObject;
    protected BufferedImage originalImage;
    protected byte[] raster;
    protected int[][] colorMap;
    public AffineTransform  at;

    public KillerImage(VisibleObject vo, BufferedImage oi) {
        super(oi.getWidth() + 124, oi.getHeight() + 256, BufferedImage.TYPE_4BYTE_ABGR);
        this.visibleObject = vo;
        this.originalImage = oi;
//        this.colorMap = new int[this.getWidth()][this.getHeight()];
        this.raster = this.getKillerRaster(this);
    }

    /**
     * Accede y recupera la raster de la bufferedImage en concreto
     *
     * @return
     */
    private byte[] getKillerRaster(BufferedImage bi) {
        return ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
    }

    /**
     * Devuelve en forma de array de ints una copia de la raster de una imagen
     *
     * @param bi Imagen para copiar la raster
     * @return Copia de la raster de la imagen en un array de ints
     */
    public int[] getIntRaster(BufferedImage bi) {
        byte[] sourceByteRaster;
        int[] intRaster;

        sourceByteRaster = this.getKillerRaster(bi);
        intRaster = new int[sourceByteRaster.length];

        for (int pos = 0; pos < sourceByteRaster.length; pos++) {
            intRaster[pos] = Byte.toUnsignedInt(sourceByteRaster[pos]);
        }

        return intRaster;
    }

    public AffineTransform getAffine() {
//        at = new AffineTransform();
//        at.translate(this.visibleObject.getX(), this.visibleObject.getY());
//        at.rotate(0, this.getWidth() / 2, this.getHeight() / 2);

        return at;
    }

    /**
     * Devuelve una copia de la raster en forma de array bidimensional de ints
     * dada una imagen.
     *
     * @param bi Imagen para copiar la raster
     * @return Copia de la raster en array bidimensional de ints
     */
    public int[][] getIntRaster2d(BufferedImage bi) {
        int[] sourceIntRaster;
        int[][] intRaster2d;

        sourceIntRaster = this.getIntRaster(bi);
        intRaster2d = new int[this.getWidth() * 4][this.getHeight()];

        // pasar cada valor a su posicion correspondiente
        for (int fil = 0; fil < intRaster2d.length; fil++) {
            for (int col = 0; col < intRaster2d[0].length; col++) {
                intRaster2d[fil][col] = sourceIntRaster[col + (intRaster2d[0].length * fil)];
            }
        }

        return intRaster2d;
    }

    /**
     * Setea una nueva raster a la killerImage desde un array de ints
     *
     * @param sourceRaster Arrays de ints para setear a la raster
     */
    public void setKillerRaster(int[] sourceRaster) {
        for (int pos = 0; pos < sourceRaster.length; pos++) {
            this.raster[pos] = (byte) sourceRaster[pos];
        }
    }

    /**
     * Setea una nueva raster a la killerImage desde un array de ints
     * bidimensional
     *
     * @param sourceRaster Arrays de ints bidimensional para setear a la raster
     */
    public void setKillerRaster2d(int[][] sourceRaster) {
        for (int fil = 0; fil < sourceRaster.length; fil++) {
            for (int col = 0; col < sourceRaster[0].length; col++) {
                this.raster[col + (col * fil)] = (byte) sourceRaster[fil][col];
            }
        }
    }

    @Override
    public void run() {

    }

    public BufferedImage getOriginalImage() {
        return this.originalImage;
    }

    public void setOriginalImage(String url) {
        try {
            this.originalImage = ImageIO.read(new File(url));
        } catch (IOException ex) {
            Logger.getLogger(KillerImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] getKillerRaster() {
        return this.raster;
    }

    public void setKillerRaster(byte[] raster) {
        this.raster = raster;
    }

    public int[][] getColorMap() {
        return this.colorMap;
    }

    public void setColorMap(int[][] colorMap) {
        this.colorMap = colorMap;
    }

    /**
     * Getters & Setters
     */
    // ------------------ VvV codigo vetusto VvV -----------------------------//
//    
//    public void addFireEffect(int[] chispas) {
//        this.setSparksToHeatMap(chispas);
//        this.calculateHeat();
//        this.paintFire();
//    }
//
//    private void fillPaCo() {
//
//        int b, g, r;
//
//        b = (1 / 8);
//        g = (1);
//        r = 1;
//
//        for (int i = 0; i < 256; i++) {
//            this.paCo[i] = new MyColor(i, i * b, i * g, i * r);
//
//        }
//    }
//
//    class MyColor {
//
//        int a, b, g, r;
//
//        public MyColor(int a, int b, int g, int r) {
//            this.a = a;
//            this.b = b;
//            this.g = g;
//            this.r = r;
//        }
//
//        public int getA() {
//            return a;
//        }
//
//        public int getB() {
//            return b;
//        }
//
//        public int getG() {
//            return g;
//        }
//
//        public int getR() {
//            return r;
//        }
//
//    }
//
//    private void paintFire() {
//        int intensidad;
//        int currentFil;
//        int a, r, g, b;
//
//        for (int fil = 0; fil < this.colorMap.length; fil++) {
//            currentFil = (fil * 4 + this.originalImage.getHeight() * 4) * this.getWidth();
//
//            for (int col = 0; col < this.colorMap[0].length; col++) {
//
//                intensidad = this.colorMap[fil][col];
//
//                a = this.paCo[intensidad].getA();
//                b = this.paCo[intensidad].getB();
//                g = this.paCo[intensidad].getG();
//                r = this.paCo[intensidad].getR();
//
//                this.byteRaster[(col * 4) + currentFil] = (byte) a;
//                this.byteRaster[(col * 4 + 1) + currentFil] = (byte) b;
//                this.byteRaster[(col * 4 + 2) + currentFil] = (byte) g;
//                this.byteRaster[(col * 4 + 3) + currentFil] = (byte) r;
//            }
//        }
//    }
//
//    /**
//     * Calcula la temperatura de cada pixel. Ojo que va de arriba hacia abajo,
//     */
//    private void calculateHeat() {
//        double enfriamiento = -0.5;
//
//        for (int fil = 1; fil < this.colorMap.length; fil++) {
//            for (int col = 1; col < this.colorMap[0].length - 1; col++) {
//
//                // calcular la media
//                this.colorMap[fil][col] = (int) (((this.colorMap[fil - 1][col - 1]
//                        + this.colorMap[fil - 1][col]
//                        + this.colorMap[fil - 1][col + 1]
//                        + this.colorMap[fil][col]) / 4) - enfriamiento);
//
//                if (this.colorMap[fil][col] > 255) {
//                    this.colorMap[fil][col] = 255;
//
//                } else if (this.colorMap[fil][col] < 0) {
//                    this.colorMap[fil][col] = 0;
//                }
//            }
//        }
//    }
//
//    private void setSparksToHeatMap(int[] chispas) {
//        // setear las chispas
//        int intensidad;
//
//        for (int pos = 0; pos < chispas.length; pos++) {
//            if (chispas[pos] > 0) {
//                intensidad = (int) (Math.random() * 255);
//
//                if (intensidad > 90) {
//                    this.colorMap[0][pos] = 255;
//                } else {
//                    this.colorMap[0][pos] = 0;
//                }
//            }
//        }
//    }
//
//    private void copyOriginalRasterToKillerRaster() {
//
//        // raster de la imagen original
//        byte[] originalRaster;
//        originalRaster = ((DataBufferByte) this.originalImage.getRaster().getDataBuffer()).getData();;
//
//        // setea la imagen original en la imagen mas grande
//        for (int pos = 0; pos < originalRaster.length; pos++) {
//            this.byteRaster[pos] = originalRaster[pos];
//        }
//
//    }
//
//    //********************** 
//    // * Getters & Setters * 
//    //**********************
//    /**
//     * Devuelve la raster en una sola fila de integers. Es la manera mas comun
//     * de obtener la raster de la imagen
//     *
//     * @return Array de ints que representa la raster de la imagen
//     */
//    public int[] getIntRaster(BufferedImage sourceImage) {
//        byte[] byteSourceRaster;
//        int[] intRaster;
//
//        byteSourceRaster = ((DataBufferByte) sourceImage.getRaster().getDataBuffer()).getData();
//        intRaster = new int[byteSourceRaster.length];
//
//        for (int pos = 0; pos < byteSourceRaster.length; pos++) {
//            intRaster[pos] = Byte.toUnsignedInt(byteSourceRaster[pos]);
//        }
//
//        return intRaster;
//    }
//
//    /**
//     * Modifica la raster a partir de un array de ints de una sola dimension.
//     *
//     * @param sourceRaster array de int de una dimension
//     */
//    public void setKillerRaster(int[] sourceRaster) {
//        for (int pos = 0; pos < sourceRaster.length; pos++) {
//            this.byteRaster[pos] = (byte) sourceRaster[pos];
//        }
//    }
//
//    /**
//     * Devuelve la raster en dos dimensiones para ahorrar calculos.
//     *
//     * @return Array de dos dimensiones que representa la imagen.
//     */
//    public int[][] getKillerRaster2D() {
//        int[][] intRaster2D;
//        int iteracion;
//
//        intRaster2D = new int[this.getHeight()][this.getWidth() * this.numChannels];
//        iteracion = 0;
//
//        for (int fil = 0; fil < intRaster2D.length; fil++) {
//            for (int col = 0; col < intRaster2D[0].length; col++) {
//                intRaster2D[fil][col] = Byte.toUnsignedInt(this.byteRaster[iteracion]);
//                iteracion++;
//            }
//        }
//
//        return intRaster2D;
//    }
//
//    /**
//     * Modifica la raster a parti de un array de ints de 2 dimensiones
//     *
//     * @param sourceRaster
//     */
//    public void setRaster2D(int[][] sourceRaster) {
//        int fil, col;
//
//        for (int pos = 0; pos < this.byteRaster.length; pos++) {
//            fil = pos / this.byteRaster.length;
//            col = pos % this.byteRaster.length;
//            this.byteRaster[pos] = (byte) sourceRaster[fil][col];
//        }
//    }
//
//    /**
//     * Cargar una imagen desde una URL.
//     *
//     * @param Archivo que contiene la imagen. OJO, aparte de ser tipo .png, ha
//     * de tener un canal alpha!!
//     */
//    public void setOriginalImage(String url) {
//        try {
//            this.originalImage = ImageIO.read(new File(url));
//        } catch (IOException ex) {
//            Logger.getLogger(KillerImage.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    /**
//     * Devuelve la imagen original
//     *
//     * @return
//     */
//    public BufferedImage getOriginalImage() {
//        return this.originalImage;
//    }
//
//    @Override
//    public void run() {
//        int[] test = new int[this.getWidth()];
//        for (int i = 0; i < test.length / 4; i++) {
//            test[i] = 2;
//
//        }
//
//        for (int i = test.length * 3 / 4; i < test.length; i++) {
//            test[i] = 2;
//
//        }
//
//        System.out.println(Arrays.toString(test));
//        while (true) {
//            try {
//                Thread.sleep(3);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(KillerImage.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            this.addFireEffect(test);
//        }
//
//    }
}
