package juego;

import java.awt.Image;
import java.util.Random;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {
    private double x;
    private double y;
    private double velocidad;
    private double ancho;
    private double alto;
    private int tiempoBomba = 0;
    private int intervaloBomba;
    private boolean esHorizontal;
    private Random r = new Random();

    // Imágenes para cada dirección del enemigo
    private Image imgArriba;
    private Image imgAbajo;
    private Image imgDerecha;
    private Image imgIzquierda;

    public Enemigo(double x, double y, boolean esHorizontal) {
        this.x = x;
        this.y = y;
        this.ancho = 35;
        this.alto = 35;
        this.velocidad = 2;
        this.esHorizontal = esHorizontal;
        this.intervaloBomba = 120 + r.nextInt(120);

        // Cargamos las imágenes desde la raíz del proyecto
        this.imgArriba = Herramientas.cargarImagen("enemigoArriba.png");
        this.imgAbajo = Herramientas.cargarImagen("enemigoAbajo.png");
        this.imgDerecha = Herramientas.cargarImagen("enemigoDerecha.png");
        this.imgIzquierda = Herramientas.cargarImagen("enemigoIzquierda.png");
    }

    public void movimientoX() {
        this.x += this.velocidad;
        if (this.x > 780 || this.x < 20) {
            this.velocidad = -this.velocidad;
        }
    }

    public void movimientoY() {
        this.y += this.velocidad;
        if (this.y > 580 || this.y < 62) {
            this.velocidad = -this.velocidad;
        }
    }

    public void mover() {
        if (this.esHorizontal) {
            movimientoX();
        } else {
            movimientoY();
        }
    }

    public Bomba soltarBomba() {
        tiempoBomba++;
        if (tiempoBomba >= intervaloBomba) {
            tiempoBomba = 0;
            intervaloBomba = 400 + r.nextInt(400);

            if (this.esHorizontal) {
                return new Bomba(this.x, this.y, 130, 50);
            } else {
                return new Bomba(this.x, this.y, 50, 130);
            }
        }
        return null;
    }

    public void dibujar(Entorno entorno) {
        // Seleccionamos la imagen según el eje y si la velocidad es positiva o negativa
        if (this.esHorizontal) {
            if (this.velocidad > 0) {
                entorno.dibujarImagen(this.imgDerecha, this.x, this.y, 0);
            } else {
                entorno.dibujarImagen(this.imgIzquierda, this.x, this.y, 0);
            }
        } else {
            if (this.velocidad > 0) {
                entorno.dibujarImagen(this.imgAbajo, this.x, this.y, 0);
            } else {
                entorno.dibujarImagen(this.imgArriba, this.x, this.y, 0);
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
}