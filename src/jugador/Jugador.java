
package jugador;

import java.util.ArrayList;
import tablero.Casilla;
import tablero.Tablero;

/**
 * Jugador de ajedrez
 * 
 * @author Pablo García 
 * @version 1.0
 */
public class Jugador {
    
    // Constantes estáticas para el color:
    public final static int BLANCO = 0;
    public final static int NEGRO = 1;
    
    // Constantes estáticas para el estado del jugador:
    public final static int JAQUE_MATE = 2;
    public final static int AHOGADO = 3;
    public final static int EN_JUEGO = 4;
    
    // Nombre del jugador:
    private String nombre;
    
    // Color de las piezas (0 para blancas, 1 para negras):
    private int color;
    
    
    
        
    /**
     * Constructor
     * @param nombre Nombre del jugador
     * @param color Color de las piezas (0 para blancas, 1 para negras)
     *  
     */
    public Jugador(String nombre, int color){
        this.nombre = nombre;
        this.color = color;
      
    }
    
    /**
     * Método para obtener el nombre del jugador
     * @return Nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer el nombre del jugador
     * @param nombre Nuevo nombre para el jugador
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para obtener el color del jugador
     * @return Color del jugador
     */
    public int getColor() {
        return color;
    }

    /**
     * Método para establecer el color del jugador
     * @param color Nuevo color para el jugador
     */
    public void setColor(int color) {
        this.color = color;
    }
        
    /**
     * Método para comparar dos jugadores, serán iguales si nombre y color coinciden
     * 
     * @param objeto Objeto que se desea comparar con el jugador actual
     * @return Un booleano que confirma si el objeto es igual a este jugador
     */
    @Override
    public boolean equals(Object objeto){
        boolean output = false;
        
        if (objeto instanceof Jugador){
            Jugador miJugador = (Jugador) objeto;
            output = miJugador.getNombre().equals(this.nombre);
        }
        
        return(output);
    }
    
    /**
     * Método que comprueba si un jugador puede realizar movimientos
     * 
     * @param tablero Situación actual del tablero
     * @return Un booleano que confirma si el jugador tiene movimientos disponibles
     */
    private boolean tieneMovimientos(Tablero tablero){
        
        boolean output = false;
        
        // Piezas del jugador:
        ArrayList<Casilla> piezas = this.getPiezasJugador(tablero);
        ArrayList<Casilla> piezasR = this.getPiezasJugadorRival(tablero);
        
        // Para cada pieza comprobamos si tiene al menos una casilla libre:
        if (piezas != null){
            
            int pos=0;
        
            while(pos<piezas.size() && !output){
                
                output = piezas.get(pos).getPieza().getCasillasDisponibles(
                        piezas.get(pos), tablero).size()>0;
                if (!output){
                    pos++;
                }
            }
        }
        
        return(output);
    }
    
    /**
     * Método que devuelve todas las casillas en las que se encuentran las piezas del jugador
     * @param tablero Situación actual del tablero
     * @return Todas las casillas en las que se encuentran las piezas del jugador
     */
    public ArrayList<Casilla> getPiezasJugador(Tablero tablero){
        
        ArrayList<Casilla> piezas = new ArrayList<>();
        Casilla [][] casillas = tablero.getCasillas();
        
        for (Casilla[] casilla : casillas) {
            for (Casilla casilla1 : casilla) {
                if (casilla1.isOcupada() && casilla1.getPieza().getJugador().equals(this)) {
                    piezas.add(casilla1);
                }
            }
        }
        
        return(piezas);
    }
    
    /**
     * Método que devuelve todas las casillas en las que se encuentran las piezas del jugador rival
     * @param tablero Situación actual del tablero
     * @return Todas las casillas en las que se encuentran las piezas del jugador rival
     */
    
    public ArrayList<Casilla> getPiezasJugadorRival(Tablero tablero){
        
        ArrayList<Casilla> piezasR = new ArrayList<>();
        Casilla [][] casillas = tablero.getCasillas();
        
        for (Casilla[] casilla : casillas) {
            for (Casilla casilla1 : casilla) {
                if (casilla1.isOcupada() && !casilla1.getPieza().getJugador().equals(this)) {
                    piezasR.add(casilla1);
                }
            }
        }
        
        return(piezasR);
    }
    /**
     * Método que devuelve el estado del jugador
     * 
     * @param tablero Situación actual del tablero
     * @return 2 para Jaque Mate, 3 para Ahogado y 4 para En Juego
     */
    public int getEstadoJugador(Tablero tablero){
        
        // Comprobamos si el jugador tiene movimientos:
        boolean tieneOpciones = this.tieneMovimientos(tablero);
        
        // Comprobamos si el jugador está en Jaque:
        boolean turnoBlancas = tablero.getNumMovimientos()%2==0;
        boolean enJaque = tablero.enJaque(turnoBlancas);
        
        int output;
        
        if (tieneOpciones){
            output = Jugador.EN_JUEGO;
        }
        else{
            if (enJaque){
                output = Jugador.JAQUE_MATE;
            }
            else{
                output = Jugador.AHOGADO;
            }
        }
        
        return(output);
    }

    
}
