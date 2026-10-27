package juego;

import java.awt.Image;
import java.util.Random;
import entorno.Entorno;
import entorno.Herramientas;

public class Pedido {
    private double origenX;
    private double origenY;
    private double destinoX;
    private double destinoY;

    private boolean retirado = false;
    private boolean entregado = false;
    private boolean tiempoExpirado = false; 

    private int tiempoRestanteTicks;
    private int contadorFotogramas = 0;
    
    private Image imgFlecha;

    public Pedido(Manzana[] manzanas) {
        Random r = new Random();
        this.tiempoRestanteTicks = 1800; // Tiempo límite que empezará a correr una vez retirado
        this.contadorFotogramas = 0;
        this.tiempoExpirado = false;
        this.retirado = false;
        this.entregado = false;

        Manzana manzanaOrigen = manzanas[r.nextInt(manzanas.length)];
        double[] posNegocio = obtenerCoordenadasCuadrante(manzanaOrigen, true);
        this.origenX = posNegocio[0];
        this.origenY = posNegocio[1];

        Manzana manzanaDestino = manzanas[r.nextInt(manzanas.length)];
        double[] posCasa = obtenerCoordenadasCuadrante(manzanaDestino, false);
        this.destinoX = posCasa[0];
        this.destinoY = posCasa[1];
        
        this.imgFlecha = Herramientas.cargarImagen("flecha.gif");
    }

    double[] obtenerCoordenadasCuadrante(Manzana m, boolean esNegocio) {
        double xIzq = m.getX() - (m.getAncho() / 4);
        double xDer = m.getX() + (m.getAncho() / 4);
        double yArriba = m.getY() - (m.getAlto() / 4);
        double yAbajo = m.getY() + (m.getAlto() / 4);

        double[] posX = { xIzq, xDer, xIzq, xDer };
        double[] posY = { yArriba, yArriba, yAbajo, yAbajo };

        if (esNegocio) {
            return new double[] { posX[m.getIndiceNegocio()], posY[m.getIndiceNegocio()] };
        } else {
            Random r = new Random();
            int indiceCasa = r.nextInt(4);
            while (indiceCasa == m.getIndiceNegocio()) {
                indiceCasa = r.nextInt(4);
            }
            return new double[] { posX[indiceCasa], posY[indiceCasa] };
        }
    }
    
    public boolean estaCerca(double repartidorX, double repartidorY, double pedidoX, double pedidoY, double margen) {
        double distanciaX = Math.abs(repartidorX - pedidoX);
        double distanciaY = Math.abs(repartidorY - pedidoY);
        return distanciaX <= margen && distanciaY <= margen;
    }
    public void actualizar(Entorno entorno, double repX, double repY) {
        // MODIFICACIÓN: El tiempo solo descuenta si ya fue retirado (está "En camino") y no se ha entregado o expirado
        if (retirado && !entregado && !tiempoExpirado) {
            contadorFotogramas++;

            if (contadorFotogramas >= 2) {
                tiempoRestanteTicks--;
                contadorFotogramas = 0;
            }

            if (tiempoRestanteTicks <= 0) {
                tiempoRestanteTicks = 0;
                tiempoExpirado = true;
            }
        }

        if (!retirado) {
            if (estaCerca(repX, repY, origenX, origenY, 50.0) && entorno.sePresiono(entorno.TECLA_ESPACIO)) {
                retirado = true; // Al ponerse en true, en el siguiente tick comienza a correr el tiempo
            }
        } 
        else if (!entregado) {
            if (estaCerca(repX, repY, destinoX, destinoY, 50.0) && entorno.sePresiono(entorno.TECLA_ESPACIO)) {
                entregado = true;
            }
        }
    }

    public void dibujar(Entorno entorno, double repX, double repY) {
        if (!retirado) {
            entorno.dibujarImagen(this.imgFlecha, origenX, origenY, 0);
        } 
        else if (!entregado) {
            entorno.dibujarImagen(this.imgFlecha, destinoX, destinoY, 0);
        }
    }
    
    public String obtenerEstado() {
        int segundosVisuales = tiempoRestanteTicks / 60;

        // MODIFICACIÓN: Mientras no esté retirado, muestra "Esperando" sin mostrar cuenta regresiva de segundos
        if (!retirado) {
            return "Esperando";
        } else if (!entregado) {
            return "En camino (" + segundosVisuales + "s)";
        } else {
            return "Entregado";
        }
    }

    public boolean isRetirado() {
        return retirado;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public boolean isTiempoExpirado() {
        return tiempoExpirado;
    }
}