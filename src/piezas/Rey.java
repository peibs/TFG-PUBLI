package piezas;

import tablero.*;
import jugador.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Rey (pieza de ajedrez)
 *
 * @author Pablo García 
 * @version 1.0
 * @see Pieza
 */
public class Rey extends Pieza {

    // Atributo que verifica si la pieza se ha movido:
    private boolean movida;

    /**
     * Constructor
     *
     * @param jugador Jugador en control del Rey
     */
    public Rey(Jugador jugador) {
        super(jugador);

        // Color de la pieza:
        String ruta = new String();
        if (jugador.getColor() == 0) {
            ruta = "fotos" + File.separator + "rey_blanco.png";
        } else if (jugador.getColor() == 1) {
            ruta = "fotos" + File.separator + "rey_negro.png";
        }

        this.setRutaImagen(ruta);
        this.movida = false;
        this.setLetra('R');
    }

    /**
     * Método que permite mover al Rey
     *
     * @param origen Casilla en la que se encuentra el Rey
     * @param destino Casilla a la que se desea mover el Rey
     * @param tablero Situación actual del tablero
     * @return Un número que confirma el tipo de movimiento
     */
    @Override
    public int mover(Casilla origen, Casilla destino, Tablero tablero) {

        // En primer lugar comprobamos que la casilla de destino no es la misma
        // que la de origen:
        boolean movimientoValido = !origen.equals(destino);
        boolean piezaDefendida = false;

        boolean enroque = false;
        boolean enroqueCorto = false;

        if (movimientoValido) {

            // Coordenadas actuales:
            
            
            int fila1 = origen.getFila();
            int columna1 = origen.getColumna();

            // Coordenadas futuras:
            int fila2 = destino.getFila();
            int columna2 = destino.getColumna();

            // Color de las piezas:
            int color1 = origen.getPieza().getJugador().getColor();

            int color2 = -1;

            if (destino.isOcupada()) {
                color2 = destino.getPieza().getJugador().getColor();
            }

            // Si la casilla de destino está en la misma fila que la de origen y a 
            // dos columnas de distancia, o bien contiene una Torre del mismo color,
            // comprobamos si se puede hacer el enroque:
            if ((fila1 == fila2 && Math.abs(columna1 - columna2) == 2)
                    || (destino.isOcupada() && destino.getPieza() instanceof Torre && color1 == color2)) {

                enroque = true;

                if (columna1 < columna2) {
                    enroqueCorto = true;

                    // Comprobamos que no hay jaques ni en la casilla final ni
                    // en la intermedia. Tampoco puede haber piezas en medio:
                    if (!tablero.getCasilla(fila1, columna1+1).isOcupada()
                            && !tablero.getCasilla(fila1, columna1+2).isOcupada()) {

                        Tablero tablero1 = new Tablero(tablero);
                        Tablero tablero2 = new Tablero(tablero);

                        Casilla casilla1 = new Casilla(fila1, columna1+1, origen.getPieza());
                        Casilla casilla2 = new Casilla(fila1, columna1+2, origen.getPieza());
                        Casilla vacia = new Casilla(fila1, columna1);

                        tablero1.setCasilla(casilla1);
                        tablero1.setCasilla(vacia);

                        tablero2.setCasilla(casilla2);
                        tablero2.setCasilla(vacia);

                        boolean blancas = false;

                        if (color1 == Jugador.BLANCO) {
                            blancas = true;
                        } else if (color1 == Jugador.NEGRO) {
                            blancas = false;
                        }

                        movimientoValido = !tablero1.enJaque(blancas)
                                && !tablero2.enJaque(blancas);
                    }
                    else{
                        enroque = false;
                        movimientoValido = false;
                    }

                } else {
                    enroqueCorto = false;

                    // Comprobamos que no hay jaques ni en la casilla final ni
                    // en la intermedia. Tampoco puede haber piezas en medio:
                    if (!tablero.getCasilla(fila1, columna1-1).isOcupada()
                            && !tablero.getCasilla(fila1, columna1-2).isOcupada() && 
                            !tablero.getCasilla(fila1, columna1-3).isOcupada()) {

                        Tablero tablero1 = new Tablero(tablero);
                        Tablero tablero2 = new Tablero(tablero);
                        Tablero tablero3 = new Tablero(tablero);

                        Casilla casilla1 = new Casilla(fila1, columna1-1, origen.getPieza());
                        Casilla casilla2 = new Casilla(fila1, columna1-2, origen.getPieza());
                        Casilla casilla3 = new Casilla(fila1, columna1-3, origen.getPieza());
                        Casilla vacia = new Casilla(fila1, columna1);

                        tablero1.setCasilla(casilla1);
                        tablero1.setCasilla(vacia);

                        tablero2.setCasilla(casilla2);
                        tablero2.setCasilla(vacia);
                        
                        tablero3.setCasilla(casilla3);
                        tablero3.setCasilla(vacia);

                        boolean blancas = false;

                        if (color1 == Jugador.BLANCO) {
                            blancas = true;
                        } else if (color1 == Jugador.NEGRO) {
                            blancas = false;
                        }

                        movimientoValido = !tablero1.enJaque(blancas)
                                && !tablero2.enJaque(blancas) && !tablero3.enJaque(blancas);
                    }
                    else{
                        enroque = false;
                        movimientoValido = false;
                    }
                }

            } else {

                // En segundo lugar, se comprueba que el valor absoluto de la 
                // diferencia de las filas es igual a 0 o 1, ígual para las columnas:
                if ((Math.abs(fila1 - fila2) == 0 || Math.abs(fila1 - fila2) == 1)
                        && (Math.abs(columna1 - columna2) == 0 || Math.abs(columna1 - columna2) == 1)) {

                    // En tercer lugar, se comprueba que en la casilla de destino no
                    // haya una pieza del mismo jugador:
                    movimientoValido = !destino.isOcupada()
                            || !destino.getPieza().getJugador().equals(origen.getPieza().getJugador());
                     piezaDefendida = destino.isOcupada()
                        && destino.getPieza().getJugador().equals(origen.getPieza().getJugador());
                     
                    if (movimientoValido) {
                        // Finalmente, comprobamos que el movimiento de rey, aunque sea 
                        // válido, no deje al jugador en jaque:
                        Tablero miTablero = new Tablero(tablero);

                        Casilla casilla1 = new Casilla(fila1, columna1);
                        Casilla casilla2 = new Casilla(fila2, columna2, origen.getPieza());

                        miTablero.setCasilla(casilla1);
                        miTablero.setCasilla(casilla2);;

                        boolean jugadorBlancas;

                        jugadorBlancas = origen.getPieza().getJugador().getColor() == 0;

                        movimientoValido = !miTablero.enJaque(jugadorBlancas);
                    }

                } else {
                    movimientoValido = false;
                }

            }
        }

        // Salida de datos:
        int salida;
        
        if(piezaDefendida){
            salida = Pieza.PIEZA_DEFENDIDA;
        }       
        else if (movimientoValido && !piezaDefendida) {
            if (enroque) {
                if (enroqueCorto) {
                    salida = Pieza.ENROQUE_CORTO;
                } else {
                    salida = Pieza.ENROQUE_LARGO;
                }
            } else {
                salida = Pieza.MOVIMIENTO_LEGAL;
            }
        } else {
            salida = Pieza.MOVIMIENTO_ILEGAL;
        }

        return (salida);
    }

    /**
     * Método que permite comprobar si la pieza se ha movido
     *
     * @return
     */
    public boolean isMovida() {
        return (this.movida);
    }

    /**
     * Método que permite establecer si la pieza se ha movido
     *
     * @param movida Nuevo estado de la pieza en cuanto al movimiento
     */
    public void setMovida(boolean movida) {
        this.movida = movida;
    }
    
    /**
     * Método que permite obtener todas las casillas a las que puede mover el Rey
     * 
     * @param origen Casilla actual del Rey
     * @param tablero Situación global del tablero
     * @return Casillas a las que el Rey puede mover 
     */
    @Override
    public ArrayList<Casilla> getCasillasDisponibles(Casilla origen, Tablero tablero){
        
        ArrayList<Casilla> misCasillas = new ArrayList<>();
        
        // Para cada una de las casillas, comprobamos a cuáles puede ir el Rey:
        for (int i = 0; i < tablero.getCasillas().length; i++) {
            for (int j = 0; j < tablero.getCasillas()[i].length; j++) {
                
                Casilla destino = tablero.getCasilla(i,j);
                if (this.mover(origen, destino, tablero) != Pieza.MOVIMIENTO_ILEGAL &&
                        this.mover(origen, destino, tablero) != Pieza.PIEZA_DEFENDIDA){
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
