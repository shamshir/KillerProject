/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualEffects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author pau
 */
public class KillerImage extends BufferedImage {

    /* TO-DO: 
        · Implementar runnable para hacer la animacion (no pintarla)
        · Cambiar nombres de los metodos
     */
    public int numChannels;
    private BufferedImage originalImg;
    private byte[] byteRaster;
    private int[] heatMap;

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
        this.originalImg = imgSource;
        this.byteRaster = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
        this.heatMap = new int[imgSource.getWidth() * spaceHeight * 4];
//        this.copyOriginalRasterToKillerRaster();
    }

    private void copyOriginalRasterToKillerRaster() {

        // raster de la imagen original
        int[] originalRaster;
        originalRaster = this.getIntRaster(this.originalImg);

        // setea la imagen original en la imagen mas grande
        for (int pos = 0; pos < originalRaster.length; pos++) {
            this.byteRaster[pos] = (byte) originalRaster[pos];
        }

        // test        
        for (int pos = 0; pos < this.heatMap.length; pos += 4) {

            this.heatMap[pos] = 200;
            this.byteRaster[originalRaster.length + pos] = (byte) 200;
            this.byteRaster[originalRaster.length + pos + 1] = (byte) 0;
            this.byteRaster[originalRaster.length + pos + 2] = (byte) 0;
            this.byteRaster[originalRaster.length + pos + 3] = (byte) 200;
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

        intRaster2D = new int[this.getWidth()][this.getHeight() * this.numChannels];
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
            this.originalImg = ImageIO.read(new File(url));
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
        return this.originalImg;
    }

}
