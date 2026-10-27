package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

/* CLASE REPARTIDOR: ES LA ENCARGADA DE GENERAR AL PERSONAJE PRINCIPAL CON SUS MOVIMIENTOS
 * SU VIDA Y SUS HABILIDADES
 */
public class Repartidor {
	
	// VARIABLES DE INSTANCIA
	private double x;
	private double y;
	private double velocidad;
	private double alto;
	private double ancho;
	private String direccion = "derecha";
	
	private Image Arriba;
	private Image Abajo;
	private Image Derecha;
	private Image Izquierda;
	private Image imgDestruida;
	private int vidas;
	private Image corazon;
	
	// CONSTRUCTOR: METODO ESPECIAL QUE SE EJECUTA AUTO. AL CREAR UN OBJETO, INICIA LAS VARIABLES DE INSTANCIA DEL OBJETO
	public Repartidor(int x, int y) {
		// SE DETERMINAN LOS TAMAÑOS, VELOCIDAD, VIDAS
		this.x = x;
		this.y = y;
		alto = 30;
		ancho = 30;
		this.velocidad = 2;		
		this.vidas = 3;
		
		this.corazon = Herramientas.cargarImagen("corazon.png"); 			// TRAE LA IMAGEN DESDE EL ARCHIVO Y LA DEVUELVE DENTRO
		this.Arriba = Herramientas.cargarImagen("personajeArriba.png");
		this.Abajo = Herramientas.cargarImagen("personajeAbajo.png");
		this.Derecha = Herramientas.cargarImagen("personajeDerecha.png");
		this.Izquierda = Herramientas.cargarImagen("personajeIzquierda.png");
		this.imgDestruida = Herramientas.cargarImagen("motoDestruida.png");
	}
	
	public void movimientoRepartidor(Entorno entorno, boolean puedeMoverse) {
		if (!puedeMoverse) {
			return;
		}

		if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			this.y -= this.velocidad;
			this.direccion = "arriba";
		}
		
		if (entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			this.y += this.velocidad;
			this.direccion = "abajo";
		}
		
		if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			this.x += this.velocidad;
			this.direccion = "derecha";
		}
		
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			this.x -= this.velocidad;
			this.direccion = "izquierda";
		}
	
		if (this.x < 15) {
			this.x = 15;
		}
		
		if (this.x > 785) {
			this.x = 785;
		}
		
		if (this.y < 62) {
			this.y = 62;
		}
		
		if (this.y > 585) {
			this.y = 585;
		}
	}
	
	public Disparo disparar() {
		return new Disparo(this.x, this.y, this.direccion);
	}
	
	public Rayo lanzarRayo() {
		return new Rayo(this.x, this.y, this.direccion);
	}
	
	public void perderVida() {
		if (this.vidas > 0) {
			this.vidas--;
		}
	}

	public void dibujarHUD(Entorno entorno) {
		for (int i = 0; i < this.vidas; i++) {
			entorno.dibujarImagen(this.corazon, 15 + (i * 32), 15, 0);
		}
	}

	public void dibujar(Entorno entorno) {
		if (this.vidas <= 0) {
	        // Dibuja la imagen de destrucción si ya no le quedan vidas
	        entorno.dibujarImagen(this.imgDestruida, this.x, this.y, 0);
	    } else {
	        if (this.direccion.equals("arriba")) {
	            entorno.dibujarImagen(this.Arriba, this.x, this.y, 0);
	        } else if (this.direccion.equals("abajo")) {
	            entorno.dibujarImagen(this.Abajo, this.x, this.y, 0);
	        } else if (this.direccion.equals("derecha")) {
	            entorno.dibujarImagen(this.Derecha, this.x, this.y, 0);
	        } else if (this.direccion.equals("izquierda")) {
	            entorno.dibujarImagen(this.Izquierda, this.x, this.y, 0);
	        }
	    }
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

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }
}