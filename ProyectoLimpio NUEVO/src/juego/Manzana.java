package juego;

import java.awt.Image;
import java.util.Random;
import entorno.Entorno;
import entorno.Herramientas;

/* CLASE MANZANA: ES LA ENCARGADA DE GENERAR LOS CUBOS DIVIDIDOS EN 4 PARA
	LA GENERACIÓN ALEATORIA DEL NEGOCIO Y LAS CASAS*/

public class Manzana {
	// VARIABLES DE INSTANCIA
    private double x;
    private double y;
    private double ancho;
    private double alto;
    private int indiceNegocio;
    private Image imgNegocio;
    private Image imgCasa;
    
    // CONSTRUCTOR: METODO ESPECIAL QUE SE EJECUTA AUTO. AL CREAR UN OBJETO, INICIA LAS VARIABLES DE INSTANCIA DEL OBJETO
    public Manzana(double x, double y) {
        this.x = x;
        this.y = y;
        this.ancho = 110;
        this.alto = 90;
        
        // RANDOM PARA ELEGIR UN UNICO NEGOCIO DE LAS 4 PARTES POR MANZANA
        Random r = new Random();
        this.indiceNegocio = r.nextInt(4);
        this.imgNegocio = Herramientas.cargarImagen("Negocio.png");		// TRAE LA IMAGEN DESDE EL ARCHIVO Y LA DEVUELVE DENTRO
        this.imgCasa = Herramientas.cargarImagen("Casa.png");
    }

    public void dibujar(Entorno entorno) { 		// CON ESTE METODO REPRESENTAMOS VISUALMENTE LOS OBJETOS EN CADA TICK
    	
    	// DIVISION DE LAS MANZANAS EN 4 PARTES 
    	
        double xIzq = this.x - (this.ancho / 4);	// DIVISION POR 4 PARA QUE TOME EL CENTRO DE CADA CUADRANTE
        double xDer = this.x + (this.ancho / 4);
        double yArriba = this.y - (this.alto / 4);
        double yAbajo = this.y + (this.alto / 4);
        
        // SE LE ASIGNA A UN CUADRANTE EL INDICE DEL NEGOCIO
        double[] posX = { xIzq, xDer, xIzq, xDer };
        double[] posY = { yArriba, yArriba, yAbajo, yAbajo };

        for (int i = 0; i < 4; i++) {
            if (i == this.indiceNegocio) {
                entorno.dibujarImagen(this.imgNegocio, posX[i], posY[i], 0);
            } else {
                entorno.dibujarImagen(this.imgCasa, posX[i], posY[i], 0);
            }
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public int getIndiceNegocio() {
        return indiceNegocio;
    }
}