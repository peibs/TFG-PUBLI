
package piezas;

import tablero.*;
import jugador.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Dama (pieza de ajedrez)
 * 
 * @author Martín Mato Búa
 * @version 1.0
 * @see Pieza
 */

public class Dama extends Pieza {
    
    /**
     * Constructor
     * @param jugador Jugador en control de la dama
     */
    public Dama(Jugador jugador){
        super(jugador);
        
        // Color de la pieza:
        String ruta = new String();
        if (jugador.getColor() == 0){
            ruta = "fotos" + File.separator + "dama_blanca.png";
        }
        else if (jugador.getColor() == 1){
            ruta = "fotos" + File.separator + "dama_negra.png";
        }
        
        this.setRutaImagen(ruta);
        this.setLetra('D');
    }

    /**
     * Método que permite mover una Dama
     * 
     * @param origen Casilla en la que se encuentra la Dama
     * @param destino Casilla a la que se pretende mover la Dama
     * @param tablero Situación actual del tablero
     * @return Un entero que confirma si el movimiento de la Dama es válido
     */
    @Override
    public int mover(Casilla origen, Casilla destino, Tablero tablero) {
        
        // Una Dama puede moverse como una Torre o como un Alfil, de modo que su
        // movimiento será válido si también lo es para una Torre o un Alfil:
        int fila = origen.getFila();
        int columna = origen.getColumna();
        
        Torre miTorre = new Torre(origen.getPieza().getJugador());
        Alfil miAlfil = new Alfil(origen.getPieza().getJugador());
        
        boolean movimientoValidoTorre = miTorre.mover(origen, destino, tablero) == Pieza.MOVIMIENTO_LEGAL;
        boolean movimientoValidoAlfil = miAlfil.mover(origen, destino, tablero) == Pieza.MOVIMIENTO_LEGAL;
        
        boolean movimientoValido =  movimientoValidoTorre || movimientoValidoAlfil;
        
        // Salida de datos:
        int salida;
        if (movimientoValido){
            salida = Pieza.MOVIMIENTO_LEGAL;
        }
        else{
            salida = Pieza.MOVIMIENTO_ILEGAL;
        }
        
        return(salida);
    }
    
    /**
     * Método que permite obtener todas las casillas a las que puede mover la Dama
     * 
     * @param origen Casilla actual de la Dama
     * @param tablero Situación global del tablero
     * @return Casillas a las que la Dama puede mover 
     */
    @Override
    public ArrayList<Casilla> getCasillasDisponibles(Casilla origen, Tablero tablero){
        
        ArrayList<Casilla> misCasillas = new ArrayList<>();
        
        // Para cada una de las casillas, comprobamos a cuáles puede ir la Dama:
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
