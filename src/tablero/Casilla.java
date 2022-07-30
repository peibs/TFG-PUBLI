
package tablero;

import piezas.Alfil;
import piezas.Caballo;
import piezas.Dama;
import piezas.Peon;
import piezas.Pieza;
import piezas.Rey;
import piezas.Torre;

/**
 * Casilla de un tablero de ajedrez
 * 
 * @author Pablo García 
 * @version 1.0
 * @see Tablero
 */
public class Casilla {
    
    // Fila de la casilla:
    private int fila;
    
    // Columna de la casilla:
    private int columna;
        
    // Pieza que está en la casilla:
    private Pieza pieza;
    
    // Propiedad que comprueba si una casilla está ocupada por una pieza:
    private boolean ocupada;
    
    //propiedad que comprueba si una casilla es atacada por una pieza
    private boolean atacable;
    
    //Propiedad que comprueba si una casilla es defendida por mi pieza
    private boolean defendida;
    
    /**
     * Constructor que crea una casilla incluyendo en ella una pieza de ajedrez
     * 
     * @param fila Fila de la casilla
     * @param columna Columna de la casilla
     * @param pieza Pieza que ocupa la casilla (null en caso de que no haya)
     */
    public Casilla(int fila, int columna, Pieza pieza){
        this.fila = fila;
        this.columna = columna;
        this.pieza = pieza;        
        this.ocupada = this.pieza != null;
        this.atacable = false;
        this.defendida = false;
    }
    
    public Casilla(Casilla casilla){
        this.fila = casilla.getFila();
        this.columna = casilla.getColumna();
        this.ocupada = casilla.ocupada;
        
        Pieza oldPieza = casilla.getPieza();
        
        if (oldPieza instanceof Peon){
            this.pieza = new Peon(oldPieza.getJugador());
        }
        else if (oldPieza instanceof Torre){
            this.pieza = new Torre(oldPieza.getJugador());
        }
        else if (oldPieza instanceof Caballo){
            this.pieza = new Caballo(oldPieza.getJugador());
        }
        else if (oldPieza instanceof Alfil){
            this.pieza = new Alfil(oldPieza.getJugador());
        }
        else if (oldPieza instanceof Dama){
            this.pieza = new Dama(oldPieza.getJugador());
        }
        else if (oldPieza instanceof Rey){
            this.pieza = new Rey(oldPieza.getJugador());
        }
    }
    
    /**
     * Constructor que crea una casilla sin incluir en ella una pieza de ajedrez
     * 
     * @param fila Fila de la casilla
     * @param columna Columna de la casilla
     */
    public Casilla(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
        this.pieza = null;
        this.ocupada = false;
        this.atacable = false;
        this.defendida = false;
    }
        
    /**
     * Método que permite comprobar si dos casillas son iguales (sus coordenadas deben coincidir)
     * 
     * @param objeto Objeto que se desea comparar con esta casilla
     * @return 
     */
    @Override
    public boolean equals(Object objeto){
        boolean output = false;
        
        if (objeto instanceof Casilla){
            
            Casilla miCasilla = (Casilla) objeto;
            
            output = this.fila == miCasilla.getFila() && 
                     this.columna == miCasilla.getColumna();
        }
        
        return(output);
    }
    
    /**
     * Método que permite obtener la fila de la casilla
     * @return Fila de la casilla
     */
    public int getFila() {
        return fila;
    }

    /**
     * Método que permite modificar la fila de la casilla
     * @param fila Nueva fila para la casilla
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Método que permite obtener la columna de la casilla
     * @return Columna de la casilla
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Método que permite modificar la columna de la casilla
     * @param columna Nueva columna para la casilla
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }

    /**
     * Método que permite obtener la pieza que se encuentra en la casilla
     * @return Pieza de la casilla
     */
    public Pieza getPieza() {
        return pieza;
    }

    /**
     * Método que permite modificar la pieza que se encuentra en la casilla
     * @param pieza Nueva pieza para la casilla
     */
    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    /**
     * Método que verifica si la casilla está ocupada
     * @return Un booleano que comprueba si la casilla está ocupada
     */
    public boolean isOcupada() {
        return ocupada;
    }

    /**
     * Método que permite modificar el estado de la casilla
     * @param ocupada Nuevo estado de la casilla
     */
    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }
        
     /**
     * Método que permite modificar el estado de la casilla
     * @param atacable Nuevo estado de la casilla
     */
      public void setAtacable(boolean atacable) {
        this.atacable = atacable;
    }
    
     /**
     * Método que devuelve si la casilla es atacable
     * @return Un booleano que comprueba si la casilla está ocupada
     */
    public boolean isAtacable() {
        return atacable;
    }
    
       /**
     * Método que devuelve si la casilla es defendida
     * @return Un booleano que comprueba si la casilla está ocupada
     */
    public boolean isDefendida() {
        return defendida;
    }
    
    
         /**
     * Método que permite modificar el estado de la casilla
     * @param defendida Nuevo estado de la casilla
     */
      public void setDefendida(boolean defendida) {
        this.defendida = defendida;
    }
}
