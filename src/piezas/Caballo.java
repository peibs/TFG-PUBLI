package piezas;

import tablero.*;
import jugador.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Caballo (pieza de ajedrez)
 *
 * @author Pablo García 
 * @version 1.0
 * @see Pieza
 */
public class Caballo extends Pieza {

    /**
     * Constructor
     *
     * @param jugador Jugador en control de la pieza
     */
    public Caballo(Jugador jugador) {
        super(jugador);

        // Color de la pieza:
        String ruta = new String();
        if (jugador.getColor() == 0) {
            ruta = "fotos" + File.separator + "caballo_blanco.png";
        } else if (jugador.getColor() == 1) {
            ruta = "fotos" + File.separator + "caballo_negro.png";
        }

        this.setRutaImagen(ruta);
        this.setLetra('C');
    }

    /**
     * Método que permite mover el Caballo
     *
     * @param origen Casilla en la que se encuentra el Caballo
     * @param destino Casilla a la que se pretende mover el Caballo
     * @param tablero Situación actual del tablero
     * @return Un entero que confirma si el movimiento del Caballo es válido
     */
    @Override
    public int mover(Casilla origen, Casilla destino, Tablero tablero) {

        // En primer lugar comprobamos que la casilla de destino no es la misma
        // que la de origen:
        boolean movimientoValido = !origen.equals(destino);

        if (movimientoValido) {

            // Coordenadas actuales:
            int fila1 = origen.getFila();
            int columna1 = origen.getColumna();

            // Coordenadas futuras:
            int fila2 = destino.getFila();
            int columna2 = destino.getColumna();

            // En segundo lugar, se comprueba que el valor absoluto de la 
            // diferencia de las filas sea 2 y el de las columnas 1 (o viceversa):
            movimientoValido = (Math.abs(fila1 - fila2) == 2 && Math.abs(columna1 - columna2) == 1)
                    || (Math.abs(fila1 - fila2) == 1 && Math.abs(columna1 - columna2) == 2);

            if (movimientoValido) {

                // En tercer lugar, se comprueba que en la casilla de destino no
                // haya una pieza del mismo jugador:
                 movimientoValido = !destino.isOcupada()
                        || !destino.getPieza().getJugador().equals(origen.getPieza().getJugador());
                     
                     //destino.setAtacable(movimientoValido); //Aqui meto el flag de que mi casilla es atacable
                // Finalmente, comprobamos que el movimiento de caballo, aunque sea 
                // válido, no deje a nuestro rey en jaque:
                if (movimientoValido) {

                    Tablero miTablero = new Tablero(tablero);

                    Casilla casilla1 = new Casilla(fila1, columna1);
                    Casilla casilla2 = new Casilla(fila2, columna2, origen.getPieza());

                    miTablero.setCasilla(casilla1);
                    miTablero.setCasilla(casilla2);

                    boolean jugadorBlancas;

                    jugadorBlancas = origen.getPieza().getJugador().getColor() == 0;

                    movimientoValido = !miTablero.enJaque(jugadorBlancas);

                }
            }
        }
        
        // Salida de datos:
        int salida;
        if (movimientoValido){
            salida = Pieza.MOVIMIENTO_LEGAL;
        }
        else{
            salida = Pieza.MOVIMIENTO_ILEGAL;
        }

        return (salida);
    }
    
    /**
     * Método que permite obtener todas las casillas a las que puede mover el Caballo
     * 
     * @param origen Casilla actual del Caballo
     * @param tablero Situación global del tablero
     * @return Casillas a las que el Caballo puede mover 
     */
    @Override
    public ArrayList<Casilla> getCasillasDisponibles(Casilla origen, Tablero tablero){
        
        ArrayList<Casilla> misCasillas = new ArrayList<>();
        
        // Para cada una de las casillas, comprobamos a cuáles puede ir el Caballo:
        for (int i = 0; i < tablero.getCasillas().length; i++) {
            for (int j = 0; j < tablero.getCasillas()[i].length; j++) {
                
                Casilla destino = tablero.getCasilla(i,j);
                if (this.mover(origen, destino, tablero) != Pieza.MOVIMIENTO_ILEGAL){
                    misCasillas.add(destino);
                }
            }
        }
        
        return(misCasillas);
    }

}
