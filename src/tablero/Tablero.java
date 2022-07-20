package tablero;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import jugador.Jugador;
import piezas.Alfil;
import piezas.Caballo;
import piezas.Dama;
import piezas.Peon;
import piezas.Rey;
import piezas.Torre;
import java.util.ArrayList;
import piezas.Pieza;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Tablero de ajedrez
 *
 * @author Pablo García 
 * @version 1.0
 * @see Casilla
 */
public class Tablero {

    // Casillas del tablero:
    private Casilla[][] casillas;

    // Número de movimientos que los jugadores han realizado:
    private int numMovimientos;

    /**
     * Constructor por defecto
     */
    public Tablero() {
        this.casillas = new Casilla[8][8];
        this.numMovimientos = 0;
    }

    /**
     * Constructor a partir de otro tablero
     *
     * @param tablero Tablero que se desea copiar
     */
    public Tablero(Tablero tablero) {
        this.casillas = new Casilla[8][8];
        for (int i = 0; i < tablero.getCasillas().length; i++) {
            for (int j = 0; j < tablero.getCasillas()[i].length; j++) {
                this.casillas[i][j] = tablero.getCasillas()[i][j];
            }
        }

        this.numMovimientos = tablero.getNumMovimientos();
    }

    /**
     * Constructor que crea un tablero con una sola pieza
     *
     * @param casilla Casilla en la que se encuentra la pieza
     */
    public Tablero(Casilla casilla) {

        int fila = casilla.getFila();
        int columna = casilla.getColumna();

        for (int i = 0; i < this.casillas.length; i++) {
            for (int j = 0; j < this.casillas[i].length; j++) {

                if (i != fila || i != columna) {
                    this.casillas[i][j] = new Casilla(i, j);
                } else {
                    this.casillas[i][j] = casilla;
                }
            }
        }
    }

    /**
     * Método que comprueba si alguno de los jugadores está en jaque
     *
     * @param turnoBlancas Un booleano que indica si es el turno del jugador
     * blanco
     * @return Un booleano que confirma que el jugador está en jaque o no
     */
    public boolean enJaque(boolean turnoBlancas) {

        int color;

        if (turnoBlancas) {
            color = Jugador.BLANCO;
        } else {
            color = Jugador.NEGRO;
        }

        // Piezas del jugador rival:
        ArrayList<Casilla> piezasRival = new ArrayList<>();

        for (int i = 0; i < this.casillas.length; i++) {
            for (int j = 0; j < this.casillas[i].length; j++) {

                if (this.casillas[i][j].isOcupada()
                        && this.casillas[i][j].getPieza().getJugador().getColor() != color) {

                    piezasRival.add(this.casillas[i][j]);
                }
            }
        }

        // Posición del rey
        Casilla casillaRey = this.getCasillaRey(turnoBlancas);

        // Para cada una de las piezas, comprobamos si pueden capturar al rey:
        boolean check = false;
        int pos = 0;

        while (!check && pos < piezasRival.size()) {

            check = piezasRival.get(pos).getPieza().mover(
                    piezasRival.get(pos), casillaRey, this) != Pieza.MOVIMIENTO_ILEGAL;

            if (!check) {
                pos++;
            }
        }

        return (check);
    }

    /**
     * Método que devuelve la casilla en la que se encuentra el Rey de un
     * jugador
     *
     * @param turnoBlancas Un booleano que verifica si es el turno de las
     * blancas
     * @return Casilla en la que se encuentra el jugador en cuyo turno nos
     * encontramos
     */
    public Casilla getCasillaRey(boolean turnoBlancas) {
        int color;
        Casilla casillaRey = null;

        if (turnoBlancas) {
            color = Jugador.BLANCO;
        } else {
            color = Jugador.NEGRO;
        }

        int i = 0, j = 0;
        boolean encontrado = false;

        while (!encontrado && i < this.casillas.length) {
            while (!encontrado && j < this.casillas[i].length) {

                if (this.casillas[i][j].isOcupada()
                        && this.casillas[i][j].getPieza() instanceof Rey
                        && this.casillas[i][j].getPieza().getJugador().getColor() == color) {

                    encontrado = true;
                } else {
                    j++;
                }
            }

            if (!encontrado) {
                i++;
                j = 0;
            }
        }

        casillaRey = this.casillas[i][j];

        return (casillaRey);
    }

    /**
     * Método para iniciar el tablero
     *
     * @param blanco Conductor de las piezas blancas
     * @param negro Conductor de las piezas negras
     */
    public void inicializarTablero(Jugador blanco, Jugador negro) {

        // Peones blancos:
        Peon miPeon = new Peon(blanco);

        for (int i = 0; i < 8; i++) {
            Casilla miCasilla = new Casilla(1, i, miPeon);
            this.setCasilla(miCasilla);
        }

        // Torres blancas:
        Torre miTorre = new Torre(blanco);
        Casilla miCasilla = new Casilla(0, 0, miTorre);
        this.setCasilla(miCasilla);
        miTorre = new Torre(blanco);
        miCasilla = new Casilla(0, 7, miTorre);
        this.setCasilla(miCasilla);

        // Caballos blancos:
        Caballo miCaballo = new Caballo(blanco);
        miCasilla = new Casilla(0, 1, miCaballo);
        this.setCasilla(miCasilla);
        miCaballo = new Caballo(blanco);
        miCasilla = new Casilla(0, 6, miCaballo);
        this.setCasilla(miCasilla);

        // Alfiles blancos:
        Alfil miAlfil = new Alfil(blanco);
        miCasilla = new Casilla(0, 2, miAlfil);
        this.setCasilla(miCasilla);
        miAlfil = new Alfil(blanco);
        miCasilla = new Casilla(0, 5, miAlfil);
        this.setCasilla(miCasilla);

        // Dama blanca:
        Dama miDama = new Dama(blanco);
        miCasilla = new Casilla(0, 3, miDama);
        this.setCasilla(miCasilla);

        // Rey blanco:
        Rey miRey = new Rey(blanco);
        miCasilla = new Casilla(0, 4, miRey);
        this.setCasilla(miCasilla);

        // Peones negros:
        miPeon = new Peon(negro);

        for (int i = 0; i < 8; i++) {
            miCasilla = new Casilla(6, i, miPeon);
            this.setCasilla(miCasilla);
        }

        // Torres negras:
        miTorre = new Torre(negro);
        miCasilla = new Casilla(7, 0, miTorre);
        this.setCasilla(miCasilla);
        miTorre = new Torre(negro);
        miCasilla = new Casilla(7, 7, miTorre);
        this.setCasilla(miCasilla);

        // Caballos negros:
        miCaballo = new Caballo(negro);
        miCasilla = new Casilla(7, 1, miCaballo);
        this.setCasilla(miCasilla);
        miCaballo = new Caballo(negro);
        miCasilla = new Casilla(7, 6, miCaballo);
        this.setCasilla(miCasilla);

        // Alfiles negros:
        miAlfil = new Alfil(negro);
        miCasilla = new Casilla(7, 2, miAlfil);
        this.setCasilla(miCasilla);
        miAlfil = new Alfil(negro);
        miCasilla = new Casilla(7, 5, miAlfil);
        this.setCasilla(miCasilla);

        // Dama negra:
        miDama = new Dama(negro);
        miCasilla = new Casilla(7, 3, miDama);
        this.setCasilla(miCasilla);

        // Rey negro:
        miRey = new Rey(negro);
        miCasilla = new Casilla(7, 4, miRey);
        this.setCasilla(miCasilla);

        // Resto de casillas:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i != 1 && i != 6 && i != 0 && i != 7) {
                    Casilla nueva = new Casilla(i, j, null);
                    this.setCasilla(nueva);
                }
            }
        }
    }

    /**
     * Método que permite obtener las casillas del tablero
     *
     * @return Casillas del tablero
     */
    public Casilla[][] getCasillas() {
        return casillas;
    }

    /**
     * Método que permite modificar la totalidad de las casillas del tablero
     *
     * @param casillas Nuevo array bidimensional de casillas
     */
    public void setCasillas(Casilla[][] casillas) {
        this.casillas = casillas;
    }

    /**
     * Método que permite obtener una casilla situada en unas coordenadas
     * concretas
     *
     * @param fila Fila en la que se encunetra la casilla
     * @param columna Columna en la que se encuentra la casilla
     * @return Casilla de las coordenadas indicadas
     */
    public Casilla getCasilla(int fila, int columna) {
        return (this.casillas[fila][columna]);
    }

    /**
     * Método que permite modificar una casilla
     *
     * @param casilla Nueva casilla
     */
    public void setCasilla(Casilla casilla) {
        this.casillas[casilla.getFila()][casilla.getColumna()] = casilla;
    }

    /**
     * Método que incrementa el número de movimientos
     */
    public void addMovimiento() {
        this.numMovimientos++;
    }

    /**
     * Método que permite obtener el número de movimientos
     *
     * @return Número de movimientos
     */
    public int getNumMovimientos() {
        return (this.numMovimientos);
    }

    /**
     * Método que permite registrar la partida en un fichero
     *
     * @param origen Casilla de origen de la última pieza movida
     * @param destino Casilla de destino de la última pieza movida
     * @param pieza Última pieza movida
     */
    public void guardaPartida(Casilla origen, Casilla destino, Pieza pieza) {

        File fichero = new File("archivos" + File.separator + "partida.dat");

        String linea = new String();

        // Añadimos el jugador:
        linea += Integer.toString(pieza.getJugador().getColor());

        // Añadimos la pieza:
        if (pieza instanceof Peon) {

            linea += "P";
        } else if (pieza instanceof Torre) {

            linea += "T";
        } else if (pieza instanceof Caballo) {

            linea += "C";
        } else if (pieza instanceof Alfil) {

            linea += "A";
        } else if (pieza instanceof Dama) {

            linea += "D";
        } else if (pieza instanceof Rey) {

            linea += "R";
        }

        // Añadimos la casilla de origen:
        linea += this.traduceCoordenadas(origen);

        // Añadimos la casilla de destino:
        linea += this.traduceCoordenadas(destino);

        // Añadimos una nueva línea al fichero:
        try ( FileOutputStream fis = new FileOutputStream(fichero, true);  BufferedOutputStream bfos = new BufferedOutputStream(fis);  DataOutputStream dos = new DataOutputStream(fis);) {

            // Escribimos los datos en el fichero:      
            dos.writeUTF(linea);

            dos.close();
            bfos.close();
            fis.close();

        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    /**
     * Método que traduce las coordenadas de una casilla a la notación
     * algebraica del ajedrez
     *
     * @param casilla Casilla cuyas coordenadas se desea traducir
     * @return Coordenadas de la casilla en notación algebraica
     */
    public String traduceCoordenadas(Casilla casilla) {

        String output = new String();

        char letra = 'a';

        // Traducimos la columna:
        output += Character.toString(letra + casilla.getColumna());

        // Traducimos la fila:
        output += Integer.toString(casilla.getFila() + 1);

        return (output);
    }

    /**
     * Método que obtiene la última jugada
     *
     * @return Última jugada
     */
    public String getUltimaJugada() {

        String movimiento = new String();

        // Fichero aleatorio:
        try ( RandomAccessFile fichAleatorio = new RandomAccessFile("Archivos"
                + File.separator + "partida.dat", "r");) {

            // Bytes:
            // 2 bytes para el tamaño
            // 1 byte por caracter
            fichAleatorio.seek(8 * (this.getNumMovimientos() - 1));
            byte[] modelo = new byte[8];
            fichAleatorio.read(modelo);

            movimiento = new String(modelo).substring(2);

        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        return (movimiento);
    }

    /**
     * Método que borra la partida anterior
     */
    public void borrarPartida() {

        File fichero1 = new File("archivos" + File.separator + "partida.dat");
        File fichero2 = new File("archivos" + File.separator + "partida.txt");

        if (fichero1.exists()) {
            fichero1.delete();
            try {
                fichero1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        if (fichero2.exists()) {
            fichero2.delete();
            try {
                fichero2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Método que devuelve la casilla original de la torre del enroque
     *
     * @param turnoBlancas Un booleano que confirma si es el turno de las
     * blancas
     * @param enroqueCorto Un booleano que confirma si el enroque es en corto
     * @return Casilla original de la torre del enroque
     */
    public Casilla getTorreEnroque(boolean turnoBlancas, boolean enroqueCorto) {

        Casilla miTorre = null;

        if (turnoBlancas && enroqueCorto) {
            miTorre = this.getCasilla(0, 7);
        } else if (turnoBlancas && !enroqueCorto) {
            miTorre = this.getCasilla(0, 0);
        } else if (!turnoBlancas && enroqueCorto) {
            miTorre = this.getCasilla(7, 7);
        } else if (!turnoBlancas && !enroqueCorto) {
            miTorre = this.getCasilla(7, 0);
        }

        return (miTorre);
    }

    /**
     * Método que obtiene la nueva casilla del Rey tras el enroque
     *
     * @param jugador Jugador que controla al Rey de este color
     * @param enroqueCorto Booleano que comprueba si el enroque es en corto
     * @return Casilla en la que se encuentra el Rey tras el enroque
     */
    public Casilla casillaReyEnroque(Jugador jugador, boolean enroqueCorto) {

        Casilla miRey = null;

        if (jugador.getColor() == Jugador.BLANCO && enroqueCorto) {
            miRey = this.getCasilla(0, 6);
        } else if (jugador.getColor() == Jugador.BLANCO && !enroqueCorto) {
            miRey = this.getCasilla(0, 2);
        } else if (jugador.getColor() == Jugador.NEGRO && enroqueCorto) {
            miRey = this.getCasilla(7, 6);
        } else if (jugador.getColor() == Jugador.NEGRO && !enroqueCorto) {
            miRey = this.getCasilla(7, 2);
        }

        miRey.setPieza(new Rey(jugador));
        miRey.setOcupada(true);

        return (miRey);
    }

    /**
     * Método que obtiene la nueva casilla de la torre tras el enroque
     *
     * @param jugador Jugador en control de la Torre
     * @param enroqueCorto Booleano que confirma si el enroque es en corto
     * @return Casilla en la que se encuentra la Torre tras el enroque
     */
    public Casilla casillaTorreEnroque(Jugador jugador, boolean enroqueCorto) {

        Casilla miTorre = null;

        if (jugador.getColor() == Jugador.BLANCO && enroqueCorto) {
            miTorre = this.getCasilla(0, 5);
        } else if (jugador.getColor() == Jugador.BLANCO && !enroqueCorto) {
            miTorre = this.getCasilla(0, 3);
        } else if (jugador.getColor() == Jugador.NEGRO && enroqueCorto) {
            miTorre = this.getCasilla(7, 5);
        } else if (jugador.getColor() == Jugador.NEGRO && !enroqueCorto) {
            miTorre = this.getCasilla(7, 3);
        }

        miTorre.setPieza(new Torre(jugador));
        miTorre.setOcupada(true);

        return (miTorre);
    }

    /**
     * Método que registrar todos los movimientos con notación algebraica:
     *
     * @param origen Casilla de origen de la pieza
     * @param destino Casilla de destino de la pieza
     * @param movimiento Tipo de movimiento que se ha producido
     * @param promocion Pieza a la que se desea promocionar
     * @param jaque Booleano que confirma si el jugador está en jaque
     * @param jaqueMate Booleano que confirma si el jugador está en jaque mate
     */
    public void registraMovimiento(Casilla origen, Casilla destino, int movimiento, 
            String promocion, boolean jaque, boolean jaqueMate) {

        String linea = new String();
        Pieza pieza = origen.getPieza();

        // Comprobamos si ha habido un enroque:
        if (movimiento == Pieza.ENROQUE_CORTO) {
            linea = "0-0";
        } else if (movimiento == Pieza.ENROQUE_LARGO) {
            linea = "0-0-0";
        } else {
            if (!(pieza instanceof Peon)){
                linea += Character.toString(pieza.getLetra());
            }

            // Si en la casilla de destino hay una pieza o hay una captura al
            // paso se produce una captura:
            if (destino.isOcupada() || movimiento == Pieza.EN_PASSANT) {

                if (pieza instanceof Peon) {
                    linea += this.traduceCoordenadas(origen).substring(0, 1);
                }

                linea += "x";
            }

            linea += this.traduceCoordenadas(destino);
            
            // Comprobamos si ha habido promoción:
            if (movimiento == Pieza.PROMOCION){
                
                switch(promocion){
                    case "Dama":
                        linea += "=D";
                        break;
                    case "Torre":
                        linea += "=T";
                        break;
                    case "Alfil":
                        linea += "=A";
                        break;
                    case "Caballo":
                        linea += "=C";
                        break;
                }
            }

        }        
        
        // Comprobamos si hay jaque o jaque mate:
        
        if (jaqueMate){
            linea += "#";
        }
        else if (jaque){
            linea += "+";
        }

        File fichero = new File("archivos" + File.separator + "partida.txt");

        try ( FileWriter fw = new FileWriter(fichero, true);  BufferedWriter bfw = new BufferedWriter(fw)) {

            // Si es el turno del jugador negro, se imprime un salto de línea:
            if (pieza.getJugador().getColor() == Jugador.NEGRO) {
                linea = "  " + linea;
                bfw.write(linea);
                bfw.newLine();
            } // Si es el turno del jugador blanco, se añade el nº de movimiento:
            else {
                int tam = linea.length();
                linea = Integer.toString(this.numMovimientos / 2 + 1) + ". " + linea;

                for (int i = tam; i < 8; i++) {
                    linea += " ";
                }
                bfw.write(linea);
            }

        } catch (IOException ex) {
            System.err.printf("Error:%s", ex.getMessage());
        }

    }
}
