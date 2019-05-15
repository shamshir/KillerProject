/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualEffects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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

    private final int INTENSITY = 100;
    private final int COOLING = 3;

    private BufferedImage fireImage;
    private BufferedImage testImageForEffect;

    private BufferedImage effectImg;

    private int[] sparks;
    private int[][] heatMap;
    private MyColor[] paCo;

    public FireEffect(VisibleObject vo, BufferedImage oi, int[] sourceSparks) {
        super(vo, oi);
        try {
            this.testImageForEffect = ImageIO.read(new File("/home/pau/NetBeansProjects/KillerProjectDevelopment/src/visibleObjects/img/wrapper.png"));
        } catch (IOException ex) {
            Logger.getLogger(FireEffect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // setear paleta de colores para el fuego
        this.setPaCo();

        this.fireImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        this.sparks = sourceSparks;
        this.heatMap = new int[this.getHeight() - this.getOriginalImage().getHeight()][this.getWidth() - this.getOriginalImage().getWidth()];
        this.effectImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        
        // pintar imagen para tener algo que mostrar si el hilo no se ha iniciado
        this.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
    }

    public void setPaCo() {
        this.paCo = new MyColor[256];
        for (int i = 0; i < 256; i++) {
            this.paCo[i] = new MyColor(i, i, i, i);
        }
    }

    private void updateRaster() {
        int a, b, g, r;
        int[][] raster2d = this.getIntRaster2d(this.fireImage);

        for (int fil = 0; fil < this.heatMap.length; fil++) {
            for (int col = 0; col < this.heatMap[0].length; col++) {
                int h = this.heatMap[fil][col];

                a = this.paCo[h].getA();
                b = this.paCo[h].getB();
                g = this.paCo[h].getG();
                r = this.paCo[h].getR();

                raster2d[fil + this.getOriginalImage().getHeight()][(col * 4)] = a;
                raster2d[fil + this.getOriginalImage().getHeight()][(col * 4) + 1] = b;
                raster2d[fil + this.getOriginalImage().getHeight()][(col * 4) + 2] = g;
                raster2d[fil + this.getOriginalImage().getHeight()][(col * 4) + 3] = r;
            }
        }

        this.setKillerRaster2d(raster2d);
    }

    private void updateFire() {
        this.updateSparks();
        this.updateHeatMap();
        this.updateRaster();
    }

    private void updateHeatMap() {
        for (int fil = 1; fil < this.heatMap.length; fil++) {
            for (int col = 1; col < this.heatMap[0].length - 1; col++) {
                this.heatMap[fil][col] = ((this.heatMap[fil][col]
                        + this.heatMap[fil - 1][col - 1]
                        + this.heatMap[fil - 1][col]
                        + this.heatMap[fil - 1][col + 1]) / 4) - this.COOLING;

                if (this.heatMap[fil][col] > 254) {
                    this.heatMap[fil][col] = 254;

                } else if (this.heatMap[fil][col] < 0) {
                    this.heatMap[fil][col] = 0;
                }
            }
        }
    }

    private void updateSparks() {
        int sparkHeat;
        for (int pos = 0; pos < this.sparks.length; pos++) {

            // mira si esta especificado el haber chispas
            if (this.sparks[pos] > 0) {
                sparkHeat = ((int) Math.random() * 255);

                // comprueba la intensidad minima
                if (sparkHeat > this.INTENSITY) {
                    this.heatMap[this.getOriginalImage().getHeight()][pos] = sparkHeat;
                }
            }
        }
    }

    private void construirImg() {
        this.effectImg.getGraphics().drawImage(this.getOriginalImage(), 0, 0, null);
        this.effectImg.getGraphics().drawImage(this.testImageForEffect, 0, 0, null);

    }

    private boolean checkObjectEffect() {
        if (this.visibleObject.getKillerImage().equals(this)) {
            return true;
        }

        return false;
    }

    @Override
    public void run() {

        while (this.checkObjectEffect()) {

            this.construirImg();
            this.getGraphics().drawImage(this.effectImg, 0, 0, null);

            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(FireEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
