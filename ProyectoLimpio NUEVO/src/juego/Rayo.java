package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Rayo {
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

    public Rayo(double x, double y, String direccion) {
        this.x = x;
        this.y = y;
        this.direccion = direccion;
        this.velocidad = 8; // Ajusta la velocidad si lo necesitas
        this.ancho = 10;    // Ajusta el ancho según el tamaño de tu recurso
        this.alto = 10;     // Ajusta el alto según el tamaño de tu recurso
        
        // Cargamos las imágenes para cada dirección (asegúrate de tener los archivos png en la raíz)
        this.imgArriba = Herramientas.cargarImagen("rayoArriba.png"); // TAMAÑO IMAGEN 80x30
        this.imgAbajo = Herramientas.cargarImagen("rayoAbajo.png");
        this.imgIzquierda = Herramientas.cargarImagen("rayoIzquierda.png");
        this.imgDerecha = Herramientas.cargarImagen("rayoDerecha.png");
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

    // Getters necesarios para las colisiones en Juego.java
    public double getX() { return x; }
    public double getY() { return y; }
    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
}
