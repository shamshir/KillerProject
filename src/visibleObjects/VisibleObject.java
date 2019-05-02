package visibleObjects;

import game.KillerGame;
import interfaces.Renderizable;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import visualEffects.KillerImage;

public abstract class VisibleObject implements RenderizableTest {

    protected KillerGame game;
    protected String type; // alvaro

    protected BufferedImage image;
    protected KillerImage killerImage;
    protected double x;
    protected double y;
    protected int width; // Pasar a double?
    protected int height; // Pasar a double?
    protected Rectangle hitbox;
    protected double angle; // pau

    protected double life; // vida o energia del objeto
    protected double damage; // daño que produce el objeto
    protected boolean alive;

    protected Color color;
    protected String colorhex;

    public VisibleObject(KillerGame game) {
        this.game = game;
        this.alive = true;
        this.killerImage = null;
    }

    // *********************
    // * Getters & Setters *
    // *********************
    public void setImage(String url, int fireSpace) {
        try {
            // --cambiar la imagen--
            this.killerImage = new KillerImage(ImageIO.read(new File(url)), fireSpace);

            System.out.println("-- Parece que la imagen ha sido cargada --");

        } catch (IOException ex) {
            System.err.println("-- Imagen no cargada --");
            Logger.getLogger(VisibleObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setKillerImage(String url){
        this.killerImage.setOriginalImage(url);
    }

    public BufferedImage getImage() {
        return this.killerImage;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return this.angle;
    }

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getColorhex() {
        return colorhex;
    }

    public void setColorhex(String colorhex) {
        this.colorhex = colorhex;
    }

}
