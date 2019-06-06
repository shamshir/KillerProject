package visualEffects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import visibleObjects.Alive;
import visibleObjects.KillerShip;
import visibleObjects.Shoot;
import visibleObjects.VisibleObject;

/**
 *
 * @author pau
 */
public class KillerImage extends BufferedImage implements Runnable {

    protected VisibleObject visibleObject;
    protected BufferedImage originalImage;
    protected Graphics2D graphics;
    protected byte[] raster;
    protected int[][] colorMap;
    protected int renderWidth;
    protected int renderHeight;
    protected Timer timer;

    /**
     * Con un solo parametro
     *
     * @param vo
     */
    public KillerImage(VisibleObject vo) {
        super(vo.getImgWidth(), vo.getImgHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        // objeto e imagen original
        this.visibleObject = vo;
        this.originalImage = vo.getImg();

        // raster y graphics de la iamgen
        this.raster = this.getKillerRaster(this);
        this.graphics = (Graphics2D) this.getGraphics();

        this.graphics.drawImage(this.originalImage, 0, 0, this.getWidth(), this.getHeight(), null);

        // pintar la imagen almentos una vez
        if (vo instanceof KillerShip) {
            Color shipColor = ((KillerShip) vo).getColor();
            this.paintObjectColor(shipColor);

        } else if (vo instanceof Shoot) {
            Color objectColor = ((Shoot) vo).getColor();
            this.paintObjectColor(objectColor);
        }

        this.blink();

        this.setRenderHeight();
        this.setRenderWidth();
    }

    /**
     * CORREGITLO DE PLUS WITDH
     *
     * @param vo
     * @param width
     * @param plusHeigth
     */
    public KillerImage(VisibleObject vo, int width, int height) {
        super(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        // objeto e imagen original
        this.visibleObject = vo;
        this.originalImage = vo.getImg();

        // raster y graphics de la iamgen
        this.raster = this.getKillerRaster(this);
        this.graphics = (Graphics2D) this.getGraphics();

        // pintar la imagen de la nave almentos una vez
        this.graphics.drawImage(this.getOriginalImage(), 0, 0, null);

        // pintar la imagen almentos una vez
        if (vo instanceof KillerShip) {
            Color shipColor = ((KillerShip) vo).getColor();
            this.paintObjectColor(shipColor);

        } else if (vo instanceof Shoot) {
            Color objectColor = ((Shoot) vo).getColor();
            this.paintObjectColor(objectColor);
        }

//        this.blink();
        this.setRenderHeight();
        this.setRenderWidth();
    }

    public class RestoreColorizedShipImage implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {


            paintColorizedShipImage();


            timer.stop();
            timer = null;
            System.out.println("timer null: " + timer);
        }
    }

    public void blink() {
        if (this.visibleObject instanceof KillerShip
                && this.timer == null) {
            this.paintDamagedShip();

            this.timer = new Timer(100, new RestoreColorizedShipImage());
            this.timer.start();
        }
    }

    private void paintOriginalimage() {
        this.graphics.drawImage(this.getOriginalImage(), 0, 0, null);
    }

    private void paintColorizedShipImage() {
        this.paintOriginalimage();
        Color shipColor = ((KillerShip) visibleObject).getColor();
        this.paintObjectColor(shipColor);
    }

    private void paintDamagedShip() {
        int a, b, g, r;

        for (int pos = 0; pos < this.raster.length; pos += 4) {
            a = pos;
            b = pos + 1;
            g = pos + 2;
            r = pos + 3;

            if (Byte.toUnsignedInt(this.raster[a]) > 0) {
                this.raster[b] = (byte) 10;
                this.raster[g] = (byte) 10;
                this.raster[r] = (byte) 255;
            }
        }
    }

    @Override
    public void run() {
    }

    public void paintObjectColor(Color c) {
        int aRasterValue, bRastreValue, gRasterValue, rRasterValue;

        // falta pillar el color del usuario
        MyColor userColor = new MyColor(255, c.getBlue(), c.getGreen(), c.getRed());

        for (int pos = 0; pos < this.raster.length; pos += 4) {
            aRasterValue = Byte.toUnsignedInt(this.raster[pos]);
            bRastreValue = Byte.toUnsignedInt(this.raster[pos + 1]);
            gRasterValue = Byte.toUnsignedInt(this.raster[pos + 2]);
            rRasterValue = Byte.toUnsignedInt(this.raster[pos + 3]);

            if ((bRastreValue == 0) && (gRasterValue == 255) && (rRasterValue == 0)) {
                this.raster[pos] = (byte) userColor.getA();
                this.raster[pos + 1] = (byte) userColor.getB();
                this.raster[pos + 2] = (byte) userColor.getG();
                this.raster[pos + 3] = (byte) userColor.getR();
            }
        }
    }

    protected boolean isVisibleObjectSafeOrAlive() {
        return (((KillerShip) this.visibleObject).getState() == Alive.State.ALIVE)
                || (((KillerShip) this.visibleObject).getState() == Alive.State.SAFE);
    }

    /**
     * Mira si el objeto padre sigue teniendo dicho efecto
     *
     * @return True si lo sigue teniendo, false si no
     */
    protected boolean hasVisibleObjectThisEffect() {
        return this.visibleObject.getKillerImage().equals(this);
    }

    // ***********************
    // ** Getters & Setters **
    // ***********************
    public int getRenderWidth() {
        return this.renderWidth;
    }

    public int getRenderHeight() {
        return renderHeight;
    }

    protected void setRenderWidth() {
        this.renderWidth = this.visibleObject.getImgWidth();
    }

    protected void setRenderHeight() {
        this.renderHeight = this.visibleObject.getImgHeight();
    }

    /**
     * Accede y recupera la raster de la bufferedImage en concreto
     *
     * @return
     */
    protected byte[] getKillerRaster(BufferedImage bi) {
        return ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
    }

    /**
     * Makes this image totally transparent
     */
    protected void clearImage() {
        for (int pos = 0; pos < this.raster.length; pos += 4) {
            this.raster[pos] = 0;
        }
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
