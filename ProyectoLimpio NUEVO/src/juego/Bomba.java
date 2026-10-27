package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Bomba {
    private double x;
    private double y;
    private double anchoFinal;
    private double altoFinal;
    private int tiempoAdvertencia; 
    private int tiempoExplosion;   
    private boolean explotando;    
    private boolean terminada;     
    
    private Image imgAdvertencia;
    private Image imgExplosionHorizontal; // 1. Imagen para explosión horizontal
    private Image imgExplosionVertical;   // 1. Imagen para explosión vertical

    public Bomba(double x, double y, double ancho, double alto) {
        this.x = x;
        this.y = y;
        this.anchoFinal = ancho;  
        this.altoFinal = alto;
        this.tiempoAdvertencia = 220;
        this.tiempoExplosion = 80;   
        this.explotando = false;
        this.terminada = false;
        
        // Cargamos los recursos gráficos
        this.imgAdvertencia = Herramientas.cargarImagen("bombaAdvertencia.gif");
        this.imgExplosionHorizontal = Herramientas.cargarImagen("bombaExplosionHorizontal.gif"); // 2. Carga el archivo horizontal
        this.imgExplosionVertical = Herramientas.cargarImagen("bombaExplosionVertical.gif");     // 2. Carga el archivo vertical
    }

    public void actualizar() {
        if (!this.explotando) {
            if (this.tiempoAdvertencia > 0) {
                this.tiempoAdvertencia--;
            } else {
                this.explotando = true;
            }
        } else {
            if (this.tiempoExplosion > 0) {
                this.tiempoExplosion--;
            } else {
                this.terminada = true;
            }
        }
    }

    public void dibujar(Entorno entorno) {
        if (!this.isExplotando() && !this.isTerminada()) {
            entorno.dibujarImagen(this.imgAdvertencia, this.x, this.y, 0);
        } else if (this.isExplotando() && !this.isTerminada()) {
            // 3. Evaluamos qué imagen dibujar según las dimensiones finales de la explosión
            if (this.anchoFinal > this.altoFinal) {
                entorno.dibujarImagen(this.imgExplosionHorizontal, this.x, this.y, 0);
            } else {
                entorno.dibujarImagen(this.imgExplosionVertical, this.x, this.y, 0);
            }
        }
    }

    // Getters y Setters que ya tenías
    public double getX() { 
        return x; 
    }
    
    public double getY() { 
        return y; 
    }
    
    public double getAncho() { 
        return !this.explotando ? 40 : this.anchoFinal; 
    }
    
    public double getAlto() { 
        return !this.explotando ? 40 : this.altoFinal; 
    }

    public boolean isTerminada() {
        return terminada;
    }

    public boolean isExplotando() {
        return explotando;
    }
}