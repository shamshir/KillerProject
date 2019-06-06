package visibleObjects;

import game.KillerGame;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class BlackHole extends Static implements Runnable {

    private double radiansIncrement;
    private boolean alive;

    /**
     *
     * @param game
     * @param x
     * @param y
     * @param height
     * @param m
     */
    public BlackHole(KillerGame game, double x, double y, int height, int m) {
        super(game, x, y);

        this.setImage();

        this.imgHeight = height;
        this.imgWidth = imgHeight;
        this.radius = this.imgHeight / 2;
        this.m = m;
        this.radiansIncrement = 0.004;
        this.alive = true;

    }

    /**
     * Método para actualizar el ángulo que se aplicará para pintar la imagen.
     */
    private void updateRadians() {
        // Si el ángulo está a punto de llegar a los 360 grados (en radianes), el incremento disminuye
        if (this.radians + this.radiansIncrement > Math.PI * 2) {
            this.radiansIncrement -= 0.001;
        }

        // Si el incremento a aplicar llega a ser 0 se volverá a aplicar un incremento de 0.04 y se reinicia el ángulo a 0
        if (this.radiansIncrement == 0.0) {
            this.radians = 0;
            this.radiansIncrement = 0.004;
        }

        this.radians += this.radiansIncrement;
    }

    @Override
    public void run() {
        while (this.alive) {

            this.updateRadians();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void setImage() {
        this.loadImg("img/blackhole.png");
    }

    // ********************************************************
    // *                     Interfaces                       *
    // ********************************************************
    // Interfaz Renderizable
    /**
     * Método para pintar la imagen del BlackHole con una animación de rotación.
     * @param g2d 
     */
    @Override
    public void render(Graphics2D g2d) {
        
        double scale = (double) this.imgWidth / (double) this.img.getWidth();
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.rotate(radians, this.imgWidth / 2, this.imgHeight / 2);
        transform.scale(scale, scale);
        
        g2d.drawImage(this.img, transform, null);
    }
    
    // *********************
    // * Getters & Setters *
    // *********************

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
}
