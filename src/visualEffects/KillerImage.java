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
    public AffineTransform at;

    public KillerImage(VisibleObject vo, BufferedImage oi, int plusWidth, int plusHeight) {
        super(oi.getWidth() + plusWidth, oi.getHeight() + plusHeight, BufferedImage.TYPE_4BYTE_ABGR);
        this.visibleObject = vo;
        this.originalImage = oi;
        this.raster = this.getKillerRaster(this);

        // pintar imagen para tener algo que mostrar si el hilo no se ha iniciado
        this.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
    }

    @Override
    public void run() {

    }

    // ***********************
    // ** Getters & Setters **
    // ***********************
    public int getRenderHeight(){
        return (this.visibleObject.getImgWidth() * this.getHeight()) / this.getWidth();
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
    
   
    

}
