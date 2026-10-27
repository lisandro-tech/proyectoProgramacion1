package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class ObjetoEspecial {
    private double x;
    private double y;
    private double ancho;
    private double alto;
    private Image imgPoder;

    public ObjetoEspecial(double x, double y) {
        this.x = x;
        this.y = y;
        this.ancho = 20;
        this.alto = 20;
        this.imgPoder = Herramientas.cargarImagen("Poder.png");
    }

    public void dibujar(Entorno entorno) {
    	entorno.dibujarImagen(this.imgPoder, this.x, this.y, 0);
    }

    // Getters para acceder a sus propiedades desde Juego.java
    public double getX() { return x; }
    public double getY() { return y; }
    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
}