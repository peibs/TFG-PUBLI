package piezas;

import tablero.*;
import jugador.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Torre (pieza de ajedrez)
 *
 * @author Pablo García 
 * @version 1.0
 * @see Pieza
 */
public class Torre extends Pieza {

    // Atributo que verifica si la pieza se ha movido:
    private boolean movida;

    /**
     * Constructor
     *
     * @param jugador Jugador en control de la Torre
     */
    public Torre(Jugador jugador) {
        super(jugador);

        // Color de la pieza:
        String ruta = new String();

        if (jugador.getColor() == Jugador.BLANCO) {
            ruta = "fotos" + File.separator + "torre_blanca.png";
        } else if (jugador.getColor() == Jugador.NEGRO) {
            ruta = "fotos" + File.separator + "torre_negra.png";
        }

        this.setRutaImagen(ruta);
        this.movida = false;
        this.setLetra('T');
    }

    /**
     * Método que permite mover la torre
     *
     * @param origen Casilla en la que se encuentra la torre
     * @param destino Casilla a la que se pretende mover la torre
     * @param tablero Situación actual del tablero
     * @return Un entero que verifica si el movimiento de la Torre es válido
     */
    @Override
    public int mover(Casilla origen, Casilla destino, Tablero tablero) {

        // En primer lugar comprobamos que la casilla de destino no es la misma
        // que la de origen:
        boolean movimientoValido = !origen.equals(destino);
        boolean piezaDefendida = false;

        if (movimientoValido) {

            // Coordenadas actuales:
            int fila1 = origen.getFila();
            int columna1 = origen.getColumna();

            // Coordenadas futuras:
            int fila2 = destino.getFila();
            int columna2 = destino.getColumna();

            // En segundo lugar, se comprueba que la Torre siga en la misma 
            // fila o en la misma columna:
            movimientoValido = fila1 == fila2 || columna1 == columna2;

            if (movimientoValido) {

                // En tercer lugar, se comprueba que en la casilla de destino no
                // haya una pieza del mismo jugador:
                movimientoValido = !destino.isOcupada()
                        || !destino.getPieza().getJugador().equals(origen.getPieza().getJugador());
                piezaDefendida = destino.isOcupada()
                        && destino.getPieza().getJugador().equals(origen.getPieza().getJugador());
                if (movimientoValido) {

                    // En cuarto lugar, se comprueba que no haya piezas en medio:
                    if (fila1 == fila2) {

                        if (columna1 > columna2) {
                            int temporal = columna1;
                            columna1 = columna2;
                            columna2 = temporal;
                        }

                        int pos = columna1 + 1;

                        while (pos < columna2 && movimientoValido) {
                            movimientoValido = !tablero.getCasilla(fila1, pos).isOcupada();
                            pos++;
                        }
                    } else if (columna1 == columna2) {

                        if (fila1 > fila2) {
                            int temporal = fila1;
                            fila1 = fila2;
                            fila2 = temporal;
                        }

                        int pos = fila1 + 1;

                        while (pos < fila2 && movimientoValido) {
                            movimientoValido = !tablero.getCasilla(pos, columna1).isOcupada();
                            pos++;
                        }
                    }
                    
                    // Finalmente, comprobamos que el movimiento de Torre, aunque sea 
                    // válido, no deje a nuestro Rey en jaque:
                    if (movimientoValido) {
                        Tablero miTablero = new Tablero(tablero);

                        Casilla casilla1 = new Casilla(fila1, columna1);
                        Casilla casilla2 = new Casilla(fila2, columna2, origen.getPieza());

                        miTablero.setCasilla(casilla1);
                        miTablero.setCasilla(casilla2);
                        
                        boolean jugadorBlancas = origen.getPieza().getJugador().getColor() == 0;

                        movimientoValido = !miTablero.enJaque(jugadorBlancas);
                    }
                }
                
                else if (piezaDefendida) {

                    // Se comprueba que no haya mas piezas para que solo marque una
                    if (fila1 == fila2) {

                        if (columna1 > columna2) {
                            int temporal = columna1;
                            columna1 = columna2;
                            columna2 = temporal;
                        }

                        int pos = columna1 + 1;

                        while (pos < columna2 && piezaDefendida) {
                            piezaDefendida = !tablero.getCasilla(fila1, pos).isOcupada();
                            pos++;
                        }
                    } else if (columna1 == columna2) {

                        if (fila1 > fila2) {
                            int temporal = fila1;
                            fila1 = fila2;
                            fila2 = temporal;
                        }

                        int pos = fila1 + 1;

                        while (pos < fila2 && piezaDefendida) {
                            piezaDefendida = !tablero.getCasilla(pos, columna1).isOcupada();
                            pos++;
                        }
                    }
                }
                
            }
        }
        
        // Salida de datos:
        int salida;
        if(piezaDefendida){
            salida = Pieza.PIEZA_DEFENDIDA;
        }
        else if (movimientoValido && !piezaDefendida){
            salida = Pieza.MOVIMIENTO_LEGAL;
        }
        else{
            salida = Pieza.MOVIMIENTO_ILEGAL;
        }

        return (salida);
    }

    /**
     * Método que permite comprobar si la Torre se ha movido
     * @return Un booleano que confirma si la pieza se ha movido
     */
    public boolean isMovida() {
        return (this.movida);
    }

    /**
     * Método que permite definir si la Torre se ha movido o no
     * @param movida Nuevo estado de movimiento de la Torre
     */
    public void setMovida(boolean movida) {
        this.movida = movida;
    }
    
    /**
     * Método que permite obtener todas las casillas a las que puede mover la Torre
     * 
     * @param origen Casilla actual de la Torre
     * @param tablero Situación global del tablero
     * @return Casillas a las que la Torre puede mover 
     */
    @Override
    public ArrayList<Casilla> getCasillasDisponibles(Casilla origen, Tablero tablero){
        
        ArrayList<Casilla> misCasillas = new ArrayList<>();
        
        // Para cada una de las casillas, comprobamos a cuáles puede ir la Torre:
        for (int i = 0; i < tablero.getCasillas().length; i++) {
            for (int j = 0; j < tablero.getCasillas()[i].length; j++) {
                
                Casilla destino = tablero.getCasilla(i,j);
                if (this.mover(origen, destino, tablero) != Pieza.MOVIMIENTO_ILEGAL &&
                        this.mover(origen, destino, tablero) !=Pieza.PIEZA_DEFENDIDA){
                    misCasillas.add(destino);
                }
            }
        }
        
        return(misCasillas);
    }
    
    //Metodo que devuelve las casilas defendidas por la pieza
    //Metodo que devuelve las casilas defendidas por la pieza
    @Override
    public ArrayList<Casilla> getCasillasDefendidas(Casilla origen, Tablero tablero) {

        ArrayList<Casilla> misCasillasDef = new ArrayList<>();

        // Para cada una de las casillas, comprobamos a cuáles puede ir el Peón:
        for (int i = 0; i < tablero.getCasillas().length; i++) {
            for (int j = 0; j < tablero.getCasillas()[i].length; j++) {

                Casilla destino = tablero.getCasilla(i, j);
                if (this.mover(origen, destino, tablero) == Pieza.PIEZA_DEFENDIDA) {
                    misCasillasDef.add(destino);
                }
            }
        }

        return (misCasillasDef);
    }
    
}
