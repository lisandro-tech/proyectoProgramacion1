package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Disparo {
    private double x;
    private double y;
    private double velocidad;
    private double ancho;
    private double alto;
    private String direccion;
    
    private Image imgArriba;
    private Image imgAbajo;
    private Image imgIzquierda;
    private Image imgDerecha;

    public Disparo(double x, double y, String direccion) {
        this.x = x;
        this.y = y;
        this.direccion = direccion;
        this.velocidad = 6;
        this.ancho = 10;
        this.alto = 10;
        
        // Cargamos las imágenes directamente en 10x10 píxeles
        this.imgArriba = Herramientas.cargarImagen("balaArriba.png"); // TAMAÑO DE IMAGEN 50x16
        this.imgAbajo = Herramientas.cargarImagen("balaAbajo.png");
        this.imgIzquierda = Herramientas.cargarImagen("balaIzquierda.png");
        this.imgDerecha = Herramientas.cargarImagen("balaDerecha.png");
    }

    public void mover() {
        if (this.direccion.equals("derecha")) {
            this.x += this.velocidad;
        }
        if (this.direccion.equals("izquierda")) {
            this.x -= this.velocidad;
        }
        if (this.direccion.equals("arriba")) {
            this.y -= this.velocidad;
        }
        if (this.direccion.equals("abajo")) {
            this.y += this.velocidad;
        }
        // Nota: El control de límites y desactivación se maneja en Juego.java asignando null.
    }

    public void dibujar(Entorno entorno) {
        if (this.direccion.equals("arriba")) {
            entorno.dibujarImagen(this.imgArriba, this.x, this.y, 0);
        } else if (this.direccion.equals("abajo")) {
            entorno.dibujarImagen(this.imgAbajo, this.x, this.y, 0);
        } else if (this.direccion.equals("izquierda")) {
            entorno.dibujarImagen(this.imgIzquierda, this.x, this.y, 0);
        } else if (this.direccion.equals("derecha")) {
            entorno.dibujarImagen(this.imgDerecha, this.x, this.y, 0);
        }
    }

    // Getters necesarios para que Juego.java pueda acceder a sus posiciones y dimensiones en las colisiones
    public double getX() { return x; }
    public double getY() { return y; }
    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
}