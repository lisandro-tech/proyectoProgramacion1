package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.Image;

/* CLASE MAPA: CREAMOS SUS VARIABLES CON COORDENADAS E IMAGEN PARA OTORGARLE
	UN FONDO BASE A NUESTRO JUEGO*/
public class Mapa {
	// VARIABLES DE INSTANCIA
	private double x;
	private double y;
	private Image imgFondo;
	
	// CONSTRUCTOR: METODO ESPECIAL QUE SE EJECUTA AUTO. AL CREAR UN OBJETO, INICIA LAS VARIABLES DE INSTANCIA DEL OBJETO
	public Mapa(double x, double y){
		this.x = x;
		this.y = y;
		this.imgFondo = Herramientas.cargarImagen("mapaFondo.png");		// TRAE LA IMAGEN DESDE EL ARCHIVO Y LA DEVUELVE DENTRO
	}
		
	public void dibujar(Entorno entorno) {							// CON ESTE METODO REPRESENTAMOS VISUALMENTE LOS OBJETOS EN CADA TICK
		entorno.dibujarImagen(this.imgFondo,this.x, this.y, 0);		// SEGUN LAS COORDENADAS DADAS
	}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}