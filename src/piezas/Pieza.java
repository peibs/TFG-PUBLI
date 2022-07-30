
package piezas;

import java.util.ArrayList;
import jugador.Jugador;
import tablero.*;

/**
 * Pieza de ajedrez
 * 
 * @author Pablo García 
 * @version 1.0
 * @see Peon
 * @see Torre
 * @see Caballo
 * @see Alfil
 * @see Dama
 * @see Rey
 */
public abstract class Pieza {
    
    // Constantes estáticas que puede devolver el método mover():
    
    /**
     * Valor que confirma que el movimiento es ilegal
     */
    public final static int MOVIMIENTO_ILEGAL = 0;
    
    /**
     * Valor que confirma que el movimiento es legal
     */
    public final static int MOVIMIENTO_LEGAL = 1;
    
    /**
     * Valor que confirma que la captura al paso es legal
     */
    public final static int EN_PASSANT = 2;        
      
    /**
     * Valor que confirma que la promoción del peón es legal
     */
    public final static int PROMOCION = 3;
    
    /**
     * Valor que confirma que el enroque corto del rey es legal
     */
    public final static int ENROQUE_CORTO = 4;
    
    /**
     * Valor que confirma que el enroque largo del rey es legal
     */
    public final static int ENROQUE_LARGO = 5;
    
    /**
     * Valor que confirma que el enroque largo del rey es legal
     */
    public final static int PIEZA_DEFENDIDA = 6;
    
    // Jugador en control de la pieza:
    private Jugador jugador;
    
    // Ruta de la imagen que representa a la pieza:
    private String rutaImagen;
    
    // Letra que representa a la pieza:
    private char letra;
    
    /**
     * Constructor
     * 
     * @param jugador Jugador en control de la pieza
     */
    public Pieza(Jugador jugador){
        this.jugador = jugador;
    }
    
    /**
     * Método que permite mover la pieza
     * 
     * @param origen Casilla en la que se encuentra la pieza
     * @param destino Casilla a la que se desea mover la pieza
     * @param tablero Situación actual del tablero
     * @return Un entero que se corresponde con una de las constantes estáticas de la clase Pieza
     */
    public abstract int mover(Casilla origen, Casilla destino, Tablero tablero);
    
    /**
     * Método que devuelve las casillas a las que puede mover la pieza de la casilla actual
     * 
     * @param casillaActual Casilla en la que se encuentra la pieza
     * @param tablero Situación global del tablero
     * @return Casillas a las que puede mover la pieza
     */
    public abstract ArrayList<Casilla> getCasillasDisponibles(Casilla casillaActual, Tablero tablero);
    
    
      /**
     * Método que devuelve las casillas a las que defiende la pieza de la casilla actual
     * 
     * @param casillaActual Casilla en la que se encuentra la pieza
     * @param tablero Situación global del tablero
     * @return Casillas a las que puede mover la pieza
     */
    public abstract ArrayList<Casilla> getCasillasDefendidas(Casilla casillaActual, Tablero tablero);
    
    /**
     * Método que permite obtener el jugador en control de la pieza
     * @return Jugador en control de la pieza
     */    
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Método que permite modificar el jugador en control de la pieza
     * @param jugador Nuevo jugadore en control de la pieza
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Método que permite obtener la ruta de la imagen
     * @return Ruta de la imagen
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    /**
     * Método que permite modificar la ruta de la imagen
     * @param rutaImagen Nueva ruta de la imagen
     */
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    
    public char getLetra(){
        return(this.letra);
    }
    
    public void setLetra(char letra){
        this.letra = letra;
    }
}
