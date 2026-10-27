package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;
import java.util.Random;

public class Juego extends InterfaceJuego {
	// DECLARACIÓN DE VARIABLES DE INSTANCIA
    private Entorno entorno;
    private Mapa Fondo;
    private Pedido pedidoActual;
    private Disparo Disparo;
    private Rayo Rayo;
    private Repartidor Repartidor;
    private Manzana[] Manzanas;
    private Enemigo[] Enemigos;
    private int[] callesX = {36, 764, 36, 764, 218, 400, 582, 764};
    private int[] callesY = {159, 297, 435, 574, 65, 574, 65, 574};
    private Bomba[] Bombas;
    private ObjetoEspecial[] PowerUps = new ObjetoEspecial[4];
    private Random r = new Random();        
    private int pedidosEntregados = 0;
    private int pedidosObjetivo = 8;
    private int pedidosPerdidos = 0;
    private int delayPerdiste = 0;
    private int tiempoInmunidad = 0;
    private int enemigosEliminados = 0;
    private boolean juegoTerminado = false;

    
    // CREACION DE OBJETOS
    Juego() {
    	
        entorno = new Entorno(this, "Proyecto para TP", 800, 600);
        
        // SE DEFINEN LAS COORDENADAS Y CANTIDADES DE OBJETOS
        
        Fondo = new Mapa(400, 300);
        Repartidor = new Repartidor(400, 300);
        Enemigos = new Enemigo[4];
        Bombas = new Bomba[10];
        Manzanas = new Manzana[16];
        
        // CREACION DE LAS MANZANAS EN FORMA DE CUADRILLA 4x4
        
        for (int i = 0; i < Manzanas.length; i++) {
            int x = 127 + ((i % 4) * 182);	// x = 01230123
            int y = 90 + ((i / 4) * 138);	// y = 00001111
            Manzanas[i] = new Manzana(x, y);
        }
        pedidoActual = new Pedido(Manzanas);

        this.entorno.iniciar(); // INICIA EL ENTORNO Y EJECUTA EL METODO TICK
    }
    
    // TICK: ACTUALIZACIÓN CONSTANTE DE FOTOGRAMAS INCLUYENDO SI HAY CAMBIOS O NO EN EL JUEGO
    
    public void tick() {
    	// DELAY PARA QUE AL MOMENTO DE MORIR POR UNA EXPLOSION, SE MUESTRE COMO EL PERSONAJE MUERE
    	if (delayPerdiste > 0) {
    	    delayPerdiste--;
    	    if (delayPerdiste == 0 && (Repartidor.getVidas() <= 0 || pedidosPerdidos >= 4)) {
    	        juegoTerminado = true;
    	    }
    	}
    	
    	if (juegoTerminado) {
            if (pedidosEntregados >= pedidosObjetivo) {
                entorno.cambiarFont("Arial", 40, java.awt.Color.GREEN);
                entorno.escribirTexto("¡GANASTE!", 280, 280);
            } else {
                entorno.cambiarFont("Arial", 40, java.awt.Color.RED);
                entorno.escribirTexto("¡PERDISTE!", 280, 280);
            }

    	    entorno.cambiarFont("Arial", 24, java.awt.Color.WHITE);
    	    entorno.escribirTexto("Pedidos entregados: " + pedidosEntregados, 250, 330);
    	    entorno.escribirTexto("Presiona ENTER para volver a jugar", 200, 380);

    	    if (entorno.sePresiono(entorno.TECLA_ENTER)) {
    	        reiniciar();
    	    }
    	    return;
    	}
    	
    	Fondo.dibujar(this.entorno);
    	
        for (int i = 0; i < Enemigos.length; i++) {
            if (Enemigos[i] == null) {
            	int indice = (i % 2 == 0) ? r.nextInt(4) : 4 + r.nextInt(4);
                int xAzar = callesX[indice];
                int yAzar = callesY[indice];

                // Verificamos si la calle está libre antes de instanciar
                if (!calleOcupada(xAzar, yAzar, i)) {
                    Enemigos[i] = new Enemigo(xAzar, yAzar, i % 2 == 0);
                } else {
                    i--; // Solo reintenta si la posición exacta está ocupada
                }
            }
        }
        
        
        double xAnterior = Repartidor.getX();
        double yAnterior = Repartidor.getY();

        boolean puedeMoverse = !juegoTerminado && delayPerdiste == 0;
        Repartidor.movimientoRepartidor(this.entorno, puedeMoverse);

        for (int i = 0; i < Enemigos.length; i++) {
            if (Enemigos[i] != null) {
                Enemigos[i].mover();

                Bomba nuevaBomba = Enemigos[i].soltarBomba();
                if (nuevaBomba != null) {
                    for (int j = 0; j < Bombas.length; j++) {
                        if (Bombas[j] == null) {
                            Bombas[j] = nuevaBomba;
                            break;
                        }
                    }
                }
            }
        }

        if (pedidoActual != null) {
            pedidoActual.actualizar(this.entorno, Repartidor.getX(), Repartidor.getY());

            // MODIFICACIÓN: Si el tiempo expira, suma un pedido perdido en lugar de matar al repartidor instantáneamente
            if (pedidoActual.isTiempoExpirado()) {
                pedidosPerdidos++;
                System.out.println("¡Se agotó el tiempo del pedido! Pedidos perdidos: " + pedidosPerdidos);

                if (pedidosPerdidos >= 4) {
                    juegoTerminado = true;
                    delayPerdiste = 120;
                } else {
                    pedidoActual = new Pedido(Manzanas); // Genera un nuevo pedido automáticamente
                }
            } else if (pedidoActual.isEntregado()) {
                pedidosEntregados++;
                
                if (pedidosEntregados >= pedidosObjetivo) {
                    juegoTerminado = true;
                } else {
                    pedidoActual = new Pedido(Manzanas);
                }
            }
        }
        for (int i = 0; i < Bombas.length; i++) {
            if (Bombas[i] != null) {
                Bombas[i].actualizar();

                if (Bombas[i].isExplotando() && !Bombas[i].isTerminada()) {
                    if (hayColision(Repartidor.getX(), Repartidor.getY(), Repartidor.getAncho(), Repartidor.getAlto(),
                                    Bombas[i].getX(), Bombas[i].getY(), Bombas[i].getAncho(), Bombas[i].getAlto())) {
                        if (tiempoInmunidad == 0) {
                            Repartidor.setVidas(0);
                            delayPerdiste = 120;
                        } else {
                            System.out.println("¡Inmunidad activa! El repartidor resistió la explosión.");
                        }
                    }
                }

                if (Bombas[i].isTerminada()) {
                    Bombas[i] = null;
                }
            }
        }

        for (int i = 0; i < PowerUps.length; i++) {
            if (PowerUps[i] != null) {
                if (hayColision(Repartidor.getX(), Repartidor.getY(), Repartidor.getAncho(), Repartidor.getAlto(),
                                PowerUps[i].getX(), PowerUps[i].getY(), PowerUps[i].getAncho(), PowerUps[i].getAlto())) {
                    tiempoInmunidad = 300;
                    PowerUps[i] = null; 
                }
            }
        }

        if (tiempoInmunidad > 0) {
            tiempoInmunidad--;
        }

        for (int i = 0; i < Manzanas.length; i++) {
            if (hayColision(Repartidor.getX(), Repartidor.getY(), Repartidor.getAncho(), Repartidor.getAlto(),
                            Manzanas[i].getX(), Manzanas[i].getY(), Manzanas[i].getAncho(), Manzanas[i].getAlto())) {
                Repartidor.setX(xAnterior);
                Repartidor.setY(yAnterior);
                break;
            }
        }

        for (int i = 0; i < Enemigos.length; i++) {
            if (Enemigos[i] != null &&
                hayColision(Repartidor.getX(), Repartidor.getY(), Repartidor.getAncho(), Repartidor.getAlto(),
                            Enemigos[i].getX(), Enemigos[i].getY(), Enemigos[i].getAncho(), Enemigos[i].getAlto())) {

            	if (tiempoInmunidad == 0) {
                    Repartidor.perderVida();

                    if (pedidoActual != null && pedidoActual.isRetirado()) {
                        pedidosPerdidos++;
                        pedidoActual = new Pedido(Manzanas);
                        System.out.println("¡Chocaste con un enemigo y perdiste el pedido actual!");
                    }
                } else {
                    System.out.println("¡Inmunidad activa! No perdiste vida contra el enemigo.");
                }
                
                Enemigos[i] = null;

                if (Repartidor.getVidas() <= 0 || pedidosPerdidos >= 4) {
                    juegoTerminado = true;
                    delayPerdiste = 120;
                }
                break;
            }
        }

        if (entorno.sePresionoBoton(1) && Disparo == null) {
            Disparo = Repartidor.disparar();
        }

        if (Disparo != null) {
            Disparo.mover();

            if (Disparo.getX() < 0 || Disparo.getX() > 800 || Disparo.getY() < 0 || Disparo.getY() > 600) {
                Disparo = null;
            } else {
                for (int i = 0; i < Enemigos.length; i++) {
                    if (Enemigos[i] != null && hayColision(Disparo.getX(), Disparo.getY(), Disparo.getAncho(), Disparo.getAlto(),
                                                           Enemigos[i].getX(), Enemigos[i].getY(), Enemigos[i].getAncho(), Enemigos[i].getAlto())) {
                        
                        if (r.nextBoolean()) {
                            for (int p = 0; p < PowerUps.length; p++) {
                                if (PowerUps[p] == null) {
                                    PowerUps[p] = new ObjetoEspecial(Enemigos[i].getX(), Enemigos[i].getY());
                                    break;
                                }
                            }
                        }
                        
                        enemigosEliminados++;
                        Enemigos[i] = null;
                        Disparo = null;
                        break;
                    }
                }
            }
        }
        
        if (entorno.sePresionoBoton(3) && Rayo == null) {
            Rayo = Repartidor.lanzarRayo();
        }

        if (Rayo != null) {
            Rayo.mover();

            if (Rayo.getX() < 0 || Rayo.getX() > 800 || Rayo.getY() < 0 || Rayo.getY() > 600) {
                Rayo = null;
            } else {
                for (int i = 0; i < Enemigos.length; i++) {
                    if (Enemigos[i] != null && hayColision(Rayo.getX(), Rayo.getY(), Rayo.getAncho(), Rayo.getAlto(),
                                                           Enemigos[i].getX(), Enemigos[i].getY(), Enemigos[i].getAncho(), Enemigos[i].getAlto())) {
                    	enemigosEliminados++;
                    	Enemigos[i] = null;
                    }
                }
            }
        }

        for (int i = 0; i < Manzanas.length; i++) {
            Manzanas[i].dibujar(this.entorno);
        }

        for (int i = 0; i < PowerUps.length; i++) {
            if (PowerUps[i] != null) {
                PowerUps[i].dibujar(this.entorno);
            }
        }

        if (pedidoActual != null) {
            pedidoActual.dibujar(this.entorno, Repartidor.getX(), Repartidor.getY());
        }

        for (int i = 0; i < Enemigos.length; i++) {
            if (Enemigos[i] != null) {
                Enemigos[i].dibujar(this.entorno);
            }
        }

        for (int i = 0; i < Bombas.length; i++) {
            if (Bombas[i] != null) {
                Bombas[i].dibujar(this.entorno);
            }
        }

        if (Disparo != null) {
            Disparo.dibujar(this.entorno);
        }
        
        if (Rayo != null) {
            Rayo.dibujar(this.entorno);
        }

        Repartidor.dibujar(this.entorno);
        Repartidor.dibujarHUD(this.entorno);

        entorno.cambiarFont("Arial", 15, java.awt.Color.WHITE);
        
        if (pedidoActual != null) {
            entorno.escribirTexto("Estado: " + pedidoActual.obtenerEstado(), 215, 32);
        }

        entorno.escribirTexto("Pedidos: " + pedidosEntregados + "/" + pedidosObjetivo, 385, 32);
        entorno.escribirTexto("Perdidos: " + pedidosPerdidos + "/4", 500, 32);
        entorno.escribirTexto("Eliminados: " + enemigosEliminados, 615, 32);

        if (tiempoInmunidad > 0) {
            entorno.escribirTexto("¡INMUNE!", 725, 32);
        }
    }

    public boolean calleOcupada(int x, int y, int indiceActual) {
        for (int i = 0; i < Enemigos.length; i++) {
            if (Enemigos[i] != null) {
                if (indiceActual % 2 == 0 && i % 2 == 0 && Enemigos[i].getY() == y) {
                    return true;
                }
                if (indiceActual % 2 != 0 && i % 2 != 0 && Enemigos[i].getX() == x) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hayColision(double x1, double y1, double ancho1, double alto1,
                               double x2, double y2, double ancho2, double alto2) {
        boolean anchorDelObjeto = (x1 + ancho1 / 2 > x2 - ancho2 / 2) &&
                                  (x1 - ancho1 / 2 < x2 + ancho2 / 2);

        boolean altoDelObjeto = (y1 + alto1 / 2 > y2 - alto2 / 2) &&
                                (y1 - alto1 / 2 < y2 + alto2 / 2);

        return anchorDelObjeto && altoDelObjeto;
    }

    public void reiniciar() {
        pedidosEntregados = 0;
        pedidosPerdidos = 0;
        juegoTerminado = false;
        delayPerdiste = 0;
        Disparo = null;
        Rayo = null;
        tiempoInmunidad = 0;
        pedidoActual = new Pedido(Manzanas);
        Repartidor.setX(400);
        Repartidor.setY(300);
        Repartidor.setVidas(3);
        enemigosEliminados = 0;
        
        for (int i = 0; i < Enemigos.length; i++) {
        	Enemigos[i] = null;
        }

        for (int i = 0; i < Bombas.length; i++) {
            Bombas[i] = null;
        }
        
        for (int i = 0; i < PowerUps.length; i++) {
            PowerUps[i] = null;
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}