package visibleObjects;

import game.KillerGame;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import visualEffects.KillerImage;

public abstract class VisibleObject implements Renderizable {

    protected double radians;
    protected BufferedImage img;
    protected KillerGame game;
    protected int imgHeight;
    protected int imgWidth;
    protected double m; // masa
    protected double x;
    protected double y;
    protected double radius;
    // Añadido para error en KI
    protected KillerImage kImg = null;

    public VisibleObject() {
    }

    public VisibleObject(KillerGame game, double x, double y) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.radians = 0;
    }

    /**
     * Método para pasar a loadImg la url que corresponde
     */
    protected abstract void setImage();

    /**
     * Método para cargar la imagen que se pintará
     *
     * @param url
     */
    protected void loadImg(String url) {

        try {
            this.img = ImageIO.read(new File(url));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    /**
     * El ancho de la img que se renderiza se calcula en proporción a las
     * dimensiones de la img original
     */
    protected void setImgSize() {
        this.imgWidth = this.imgHeight * this.img.getWidth() / this.img.getHeight();
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    // Interfaz Renderizable
    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(this.img, (int) x, (int) y, imgWidth, imgHeight, null);
    }

    // *********************
    // * Getters & Setters *
    // *********************
    public KillerGame getGame() {
        return game;
    }

    public void setGame(KillerGame game) {
        this.game = game;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public double getRadians() {
        return radians;
    }

    public void setRadians(double radians) {
        this.radians = radians;
    }

    public double getRadius() {
        return radius;
    }
    
    public KillerImage getKillerImage() {
        return this.kImg;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

}
