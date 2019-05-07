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
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.util.logging.Level;
import java.util.logging.Logger;
import visibleObjects.Alive;
import visibleObjects.VisibleObject;

/**
 *
 * @author pau
 */
public class Animacion {

    // numero de canales dependiendo del tipo de imagen
    private static final int ARGB = 4;
    private static final int RGB = 3;

    // setear el tipo de canales de la imagen
    private static final int NUM_CANALS_PER_PIXEL = ARGB;

    public static void addRotation(Graphics2D g2d, Alive ball) {

        AffineTransform old = g2d.getTransform();

        // setar el angulo en el que esta la imagen
        Animacion.setAngle(g2d, ball);

        // pintar la imagen con el angulo deseado
        // resetear valores del graphics
    }

    public Animacion() {
    }

    public static void animar(Graphics2D g2d, Alive ball) {
//        AffineTransform old = g2d.getTransform();
//        
//        // pintar el angulo en el que esta la imagen
//        Animacion.setAngle(g2d, ball);
//        
//        // crear imagen con dicho angulo
//
////        g2d.setTransform(old);
    }

    /**
     * Añade un efecto de fuego en la imagen
     *
     * @param imgSource Imagen a la que poner efecto del fuego
     */
    public static void addFire(BufferedImage imgSource) {
        int[] imgRaster;
        int[][] imgRaster2D;

        // pillar la raster
        imgRaster = Animacion.setRasterToIntArray(imgSource);

        // convertirla en un array 2D
//        imgRaster2D = Animacion.createTwoDimensionArray(
//                imgRaster,
//                imgSource.getWidth(),
//                imgSource.getHeight());
//
//        // convertir raster en un array 1D
//        imgRaster = Animacion.createOneDimensionalArray(imgRaster2D);
        // setear nueva raster a la imagen
        Animacion.setIntRasterToImage(imgSource, imgRaster);

    }

    public static void blackHole(VisibleObject vObj) {

        System.out.println("Imagen height: " + vObj.getImage().getHeight());
        System.out.println("Imagen width: " + vObj.getImage().getWidth());

        int[] simpleRaster = Animacion.setRasterToIntArray(vObj.getImage());
        simpleRaster = Animacion.modifyAlphaChannel(simpleRaster);

//        int[][] raster2D = Animacion.createTwoDimensionArray(simpleRaster, vObj.getImage().getWidth(), vObj.getImage().getHeight());
//        int[] a = Animacion.createOneDimensionalArray(raster2D);
//        
        Animacion.setIntRasterToImage(vObj.getImage(), simpleRaster);
    }

    public static void pintarCosas(Graphics2D g2d) {
        double x, y;
        int iteracion;

        iteracion = 0;
        g2d.setColor(Color.MAGENTA);

        for (int i = 0; i < 1000; i++) {

            iteracion += 1;
            x = Math.sin(iteracion) * iteracion + 800;
            y = Math.cos(iteracion) * iteracion + 500;
//            System.out.println(pivote);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Animacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            g2d.fillOval((int) x, (int) y, 5, 5);
            System.out.println("x: " + x);
            System.out.println("y: " + y + "\n");

        }

    }

    private static int[] modifyAlphaChannel(int[] raster) {

        //modificar el alfa de la imagen
        for (int pos = 0; pos < raster.length; pos += 4) {
            if (raster[pos] != 0) {
                raster[pos] = 100;
            }
        }

        return raster;
    }

    private static int[] createOneDimensionalArray(int[][] array2D) {
        // array que sera devuelto
        int[] array1D;

        // longitud del array a devolver
        int array1DLength;

        // al ser cuadrado/rectangular todas las filas son iguales (o deberia serlo)
        array1DLength = array2D.length * array2D[0].length;
        array1D = new int[array1DLength];

        int iteracion = 0;
        for (int fil = 0; fil < array2D.length; fil++) {
            for (int col = 0; col < array2D[0].length; col++) {

                array1D[iteracion] = array2D[fil][col];
                iteracion++;
            }
        }

        System.out.println("array 2D length: " + array2D.length);
        System.out.println("array 2D length[]: " + array2D[0].length);
        return array1D;
    }

    /**
     * Pos eso, crea un array de dos dimensiones
     *
     * @param originalArray array para copiar y crear uno nuevo de dos
     * dimensiones
     * @param width Anchira del nuevo array
     * @param height Altura del nuevo array
     * @return Nuevo array de dos dimensiones
     */
    private static int[][] createTwoDimensionArray(int[] originalArray, int width, int height) {
        int[][] array2D;

        array2D = new int[height][width];

        System.out.println("array height: " + array2D.length);
        System.out.println("array width: " + array2D[0].length);

        for (int fil = 0; fil < array2D.length; fil++) {
            for (int col = 0; col < array2D[0].length; col++) {

                int valor = originalArray[col + (fil * width)];
                array2D[fil][col] = valor;
            }
        }

        return array2D;
    }

    /**
     * TODO: HACER Q DEVUELVA INT[] Devuelve el array de informacion de la
     * raster en formato int sin singnos
     *
     * @param imgSource Imagen de donde extraer la raster
     * @return raster
     */
    public static int[] setRasterToIntArray(BufferedImage imgSource) {
        if (imgSource.getRaster().getDataBuffer().getDataType() != DataBuffer.TYPE_BYTE) {
            System.err.println("error en getRaster");
        }

        byte[] rasterSource = ((DataBufferByte) imgSource.getRaster().getDataBuffer()).getData();
        int[] raster = new int[rasterSource.length];

        System.out.println("length raster imgSource: " + raster.length);

        //casteo a int de la raster
        for (int pos = 0; pos < rasterSource.length; pos++) {
            raster[pos] = Byte.toUnsignedInt(rasterSource[pos]);
        }

        return raster;
    }

    /**
     * Posiciona el angulo de la imagen
     */
    private static void setAngle(Graphics2D g2d, Alive ball) {
        //  sumar angulo al objeto
//        ball.setAngle(ball.getAngle() + 1.5);

        // pintar eje de giro:
        // setear color
        g2d.setColor(Color.red);

        // setear posicion eje
        double xRotationAxis, yRotationAxis;
        xRotationAxis = ball.getX() + (ball.getWidth() / 2);
        yRotationAxis = ball.getY() + (ball.getHeight() / 2);

        g2d.fillOval((int) xRotationAxis, (int) yRotationAxis, 10, 10);

        // rotar la imagen
        g2d.rotate(
                // convertir el angulo en radianes
                ball.getAngle() * (Math.PI / 180),
                (xRotationAxis),
                (yRotationAxis)
        );
    }

    /**
     * Setea la informacion de la raster a la imagen desde array de int
     *
     * @param imgSource Imagen a la cual setear la raster nueva
     * @param raster Raster a setear
     */
    private static void setIntRasterToImage(BufferedImage imgSource, int[] raster) {
        // raster de la imagen a cambiar
        byte[] rasterSource = ((DataBufferByte) imgSource.getRaster().getDataBuffer()).getData();

        // bucle que actualiza la raster de la imagen
        for (int pos = 0; pos < rasterSource.length; pos++) {
            rasterSource[pos] = (byte) raster[pos];

        }
    }

    private static void setRasterToImage(BufferedImage imgSource, int[][] raster2D) {

        // raster de la imagen a cambiar
        byte[] rasterSource = ((DataBufferByte) imgSource.getRaster().getDataBuffer()).getData();

        int iteracion = 0;
        for (int fil = 0; fil < raster2D.length; fil++) {
            for (int col = 0; col < raster2D[fil].length; col++) {

                rasterSource[iteracion] = (byte) raster2D[fil][col];
                iteracion++;
            }
        }
    }

    private void setHeatMap(int[] raster, int rowInit, int colInit, int width, int height) {

        // indicar fila inicio de mapa calor
        for (int i = 0; i < raster.length; i++) {
            int j = raster[i];

        }

    }

}
