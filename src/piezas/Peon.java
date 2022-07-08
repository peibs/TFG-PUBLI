package piezas;

import tablero.*;
import jugador.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Peón (pieza de ajedrez)
 *
 * @author Pablo García Domínguez
 * @version 1.0
 * @see Pieza
 */
public class Peon extends Pieza {

    /**
     * Constructor
     *
     * @param jugador Jugador en control del Peón
     */
    public Peon(Jugador jugador) {
        super(jugador);

        // Color de la pieza:
        String ruta = new String();
        if (jugador.getColor() == 0) {
            ruta = "fotos" + File.separator + "peon_blanco.png";
        } else if (jugador.getColor() == 1) {
            ruta = "fotos" + File.separator + "peon_negro.png";
        }

        this.setRutaImagen(ruta);
    }

    /**
     * Método que permite mover un Peón
     *
     * @param origen Casilla en la que se encuentra el Peón
     * @param destino Casilla a la que se pretende mover el Peón
     * @param tablero Situación actual del tablero
     * @return Un valor entero que representa el tipo de movimiento
     */
    @Override
    public int mover(Casilla origen, Casilla destino, Tablero tablero) {

        boolean enPassant = false;
        boolean promocion = false;

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

            // A continuación, se comprueba si el peón quiere avanzar o comer:
            // Avanzar:
            if (columna2 == columna1) {

                // Se combrueba también que no haya otra pieza en esa casilla:
                movimientoValido = destino.getPieza() == null;

                if (movimientoValido) {

                    // A continuación, se comprueba que la columna sea la misma, pero
                    // que la fila haya variado en una unidad (o en dos, si partía de 
                    // la segunda o séptima fila). Si el peón es blanco, la fila debe 
                    // aumentar, y si es negro, disminuir. Si es la primera jugada, 
                    // podrá mover dos casillas:
                    movimientoValido = columna1 == columna2
                            && (((fila2 == (fila1 + 1) && origen.getPieza().getJugador().getColor() == Jugador.BLANCO)
                            || (fila2 == (fila1 - 1) && origen.getPieza().getJugador().getColor() == Jugador.NEGRO))
                            || (fila2 == (fila1 + 2) && fila1 == 1 && origen.getPieza().getJugador().getColor() == Jugador.BLANCO
                            && tablero.getCasilla(2, columna1).getPieza() == null)
                            || fila2 == (fila1 - 2) && fila1 == 6 && origen.getPieza().getJugador().getColor() == Jugador.NEGRO
                            && tablero.getCasilla(5, columna1).getPieza() == null);
                }

            } // Comer:
            else if (Math.abs(fila1 - fila2) == 1 && Math.abs(columna1 - columna2) == 1) {

                // Si la casilla de destino está vacía, comprobamos si es posible
                // comer al paso:
                if (tablero.getNumMovimientos() > 0 && destino.isOcupada()
                        && !destino.getPieza().getJugador().equals(origen.getPieza().getJugador())) {
                    movimientoValido = ((origen.getPieza().getJugador().getColor() == Jugador.BLANCO
                            && (fila2 == (fila1 + 1))
                            || (origen.getPieza().getJugador().getColor() == Jugador.NEGRO
                            && (fila2 == (fila1 - 1)))));
                } else {
                    movimientoValido = this.enPassant(origen, destino, tablero);
                    enPassant = movimientoValido;
                }

            } else {
                movimientoValido = false;
            }

            if (movimientoValido) {

                promocion = (destino.getFila() == 0
                        && origen.getPieza().getJugador().getColor() == 1)
                        || (destino.getFila() == 7 && origen.getPieza().getJugador().getColor() == 0);

                // Finalmente, comprobamos que el movimiento de peón, aunque sea 
                // válido, no deje a nuestro rey en jaque:
                Tablero miTablero = new Tablero(tablero);

                Casilla casilla1 = new Casilla(fila1, columna1);
                Casilla casilla2 = new Casilla(fila2, columna2, origen.getPieza());

                miTablero.setCasilla(casilla1);
                miTablero.setCasilla(casilla2);

                boolean jugadorBlancas = origen.getPieza().getJugador().getColor() == Jugador.BLANCO;

                movimientoValido = !miTablero.enJaque(jugadorBlancas);

                if (!movimientoValido) {

                    enPassant = false;
                    promocion = false;
                }

            }
        }

        // Salida de datos:
        int salida;
        if (promocion) {
            salida = Pieza.PROMOCION;
        } else if (enPassant) {
            salida = Pieza.EN_PASSANT;
        } else if (movimientoValido) {
            salida = Pieza.MOVIMIENTO_LEGAL;
        } else {
            salida = Pieza.MOVIMIENTO_ILEGAL;
        }

        return (salida);
    }

    /**
     * Método que comprueba si la captura al paso es posible
     *
     * @param origen Casilla en la que se encuentra el Peón
     * @param destino Casilla a la que se desea mover el Peón
     * @param tablero Situación actual del tablero
     * @return Un booleano que confirma si es posible comer al paso
     */
    private boolean enPassant(Casilla origen, Casilla destino, Tablero tablero) {
        
        boolean output = false;

        if (tablero.getNumMovimientos() > 0) {
            // Última jugada:
            String ultimoMovimiento = tablero.getUltimaJugada();
            String movimientoEsperado = new String();

            if (origen.getPieza().getJugador().getColor() == Jugador.BLANCO) {

                movimientoEsperado += Integer.toString(1);
                movimientoEsperado += "P";

                Casilla casilla1 = new Casilla(destino.getFila() + 1, destino.getColumna());
                Casilla ultimaJugada = new Casilla(destino.getFila() - 1, destino.getColumna());

                movimientoEsperado += tablero.traduceCoordenadas(casilla1);
                movimientoEsperado += tablero.traduceCoordenadas(ultimaJugada);
            } else {

                movimientoEsperado += Integer.toString(0);
                movimientoEsperado += "P";

                Casilla casilla1 = new Casilla(destino.getFila() - 1, destino.getColumna());
                Casilla ultimaJugada = new Casilla(destino.getFila() + 1, destino.getColumna());

                movimientoEsperado += tablero.traduceCoordenadas(casilla1);
                movimientoEsperado += tablero.traduceCoordenadas(ultimaJugada);
            }
            
            output = ultimoMovimiento.equals(movimientoEsperado);
        }

        return (output);
    }

    /**
     * Método que permite obtener todas las casillas a las que puede mover el
     * Peón
     *
     * @param origen Casilla actual del Peón
     * @param tablero Situación global del tablero
     * @return Casillas a las que el Peón puede mover
     */
    @Override
    public ArrayList<Casilla> getCasillasDisponibles(Casilla origen, Tablero tablero) {

        ArrayList<Casilla> misCasillas = new ArrayList<>();

        // Para cada una de las casillas, comprobamos a cuáles puede ir el Peón:
        for (int i = 0; i < tablero.getCasillas().length; i++) {
            for (int j = 0; j < tablero.getCasillas()[i].length; j++) {

                Casilla destino = tablero.getCasilla(i, j);
                if (this.mover(origen, destino, tablero) != Pieza.MOVIMIENTO_ILEGAL) {
                    misCasillas.add(destino);
                }
            }
        }

        return (misCasillas);
    }

}
