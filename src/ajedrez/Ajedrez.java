/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajedrez;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import jugador.*;
import piezas.*;
import tablero.*;
import javax.swing.Timer;
import tablero.Paleta;

/**
 *
 * @author MARTÍN
 */
public class Ajedrez extends javax.swing.JFrame implements ActionListener {

    // Consantes para el resultado de la partida:
    private final int GANA_BLANCAS = 1;
    private final int GANA_NEGRAS = 2;
    private final int TABLAS = 3;
    private final int JUGANDO = 0;
    private int estado;

    private JButton[][] botonera;
    private Jugador jugadorBlancas;
    private Jugador jugadorNegras;
    private final Tablero tablero;
    private Casilla casillaOrigen = null;
    private Casilla casillaDestino = null;
    private boolean turnoBlancas = true;
    private PantallaInicio inicio;
    private Casilla casillaMasReciente = null;
    private ArrayList<Casilla> casillasDisponibles;
    private boolean partidaActiva;
    private Timer t;
    private String promocion;
    private Casilla copiaOrigen;
    private Casilla copiaDestino;
    private boolean jaqueMate;

    /**
     * Creates new form Ajedrez
     */
    public Ajedrez() {
        initComponents();
        this.tablero = new Tablero();
    }

    public Ajedrez(String jugador1, String jugador2, int opcion) {
        this();

        this.partidaActiva = true;
        this.estado = JUGANDO;

        // Jugadores:
        jugadorBlancas = new Jugador(jugador1, Jugador.BLANCO);
        jugadorNegras = new Jugador(jugador2, Jugador.NEGRO);

      

        nombreJugadorBlancas.setText(jugador1);
        nombreJugadorBlancas.repaint();
        nombreJugadorNegras.setText(jugador2);
        nombreJugadorNegras.repaint();
        

        this.setLocationRelativeTo(null);

        botonera = new JButton[8][8];

        // Se borra la partida previa, en caso de que la haya:
        tablero.borrarPartida();

        // Se inicializan los colores de las casillas:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botonera[i][j] = new JButton();

                if ((i + j) % 2 == 0) {

                    botonera[i][j].setBackground(new Color(
                    Paleta.MARRON_OSCURO1,Paleta.MARRON_OSCURO2,Paleta.MARRON_OSCURO3));
                } else {

                    botonera[i][j].setBackground(new Color(
                    Paleta.MARRON_CLARO1,Paleta.MARRON_CLARO2,Paleta.MARRON_CLARO3));
                }
                botonera[i][j].setName(Integer.toString(7 - i) + Integer.toString(j));
                panelTablero.add(botonera[i][j]);

                botonera[i][j].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        procesaClick(evt);
                    }
                });
            }
        }

        this.inicializaTablero();
        this.setVisible(true);
       
   
    }

    // Método que procesa un click en una casilla:
    private void procesaClick(java.awt.event.ActionEvent evt) {

        if (this.partidaActiva) {
            synchronized (tablero) {

                String nomBoton = ((JButton) evt.getSource()).getName();

                int fila = Integer.parseInt(nomBoton.substring(0, 1));
                int columna = Integer.parseInt(nomBoton.substring(1, 2));

                // Comprobamos si es el turno del jugador:
                if ((turnoBlancas && (casillaOrigen == null || (casillaOrigen != null
                        && casillaOrigen.getPieza().getJugador().getColor() == Jugador.BLANCO)))
                        || (!turnoBlancas && (casillaOrigen == null || (casillaOrigen != null
                        && casillaOrigen.getPieza().getJugador().getColor() == Jugador.NEGRO)))) {

                    // No ha habido un click previo:
                    if (casillaOrigen == null && tablero.getCasilla(fila, columna).getPieza() != null
                            && ((turnoBlancas && tablero.getCasilla(fila, columna).getPieza().getJugador().getColor() == Jugador.BLANCO)
                            || (!turnoBlancas && tablero.getCasilla(fila, columna).getPieza().getJugador().getColor() == Jugador.NEGRO))) {

                        casillaOrigen = tablero.getCasilla(fila, columna);

                        // Marcamos la casilla:
                        botonera[7 - fila][columna].setBackground(new Color(
                        Paleta.VERDE1,Paleta.VERDE2,Paleta.VERDE3));

                        // Marcamos las casillas a las que puede ir la pieza:
                        this.coloreaCasillasDisponibles(casillaOrigen, tablero);
                    } else {

                        if (casillaOrigen != null) {

                            casillaDestino = tablero.getCasilla(fila, columna);

                            int filaOrigen = casillaOrigen.getFila();
                            int columnaOrigen = casillaOrigen.getColumna();

                            // Comprobamos si el movimiento es posible:
                            int resultado = casillaOrigen.getPieza().mover(
                                    casillaOrigen, casillaDestino, tablero);

                            if (resultado != Pieza.MOVIMIENTO_ILEGAL) {

                                // Copiamos las casillas para poder guardar el movimiento:
                                copiaOrigen = new Casilla(casillaOrigen);
                                copiaDestino = new Casilla(casillaDestino);

                                // Se incrementa el número de movimientos:
                                tablero.addMovimiento();

                                tablero.setCasilla(new Casilla(filaOrigen, columnaOrigen));

                                // Comprobamos si ha habido un enroque:
                                if (resultado != Pieza.ENROQUE_CORTO
                                        && resultado != Pieza.ENROQUE_LARGO) {

                                    // Si la pieza es una torre o un rey, se registra que 
                                    // se ha movido:
                                    if (casillaOrigen.getPieza() instanceof Rey) {
                                        ((Rey) casillaOrigen.getPieza()).setMovida(true);
                                    } else if (casillaOrigen.getPieza() instanceof Torre) {
                                        ((Torre) casillaOrigen.getPieza()).setMovida(true);
                                    }

                                    // Cambiamos el color de la casilla de origen:
                                    this.desmarcaCasilla(casillaOrigen);

                                    casillaOrigen.setFila(fila);
                                    casillaOrigen.setColumna(columna);
                                    tablero.setCasilla(casillaOrigen);

                                    // Se actualizan los iconos:
                                    botonera[7 - filaOrigen][columnaOrigen].setIcon(null);
                                    botonera[7 - fila][columna].setIcon(new ImageIcon(
                                            tablero.getCasillas()[fila][columna].getPieza().getRutaImagen()));

                                    if (resultado == Pieza.EN_PASSANT && casillaMasReciente != null
                                            && casillaOrigen.getPieza() instanceof Peon) {

                                        botonera[7 - casillaMasReciente.getFila()][casillaMasReciente.getColumna()].setIcon(null);

                                        tablero.setCasilla(new Casilla(casillaMasReciente.getFila(),
                                                casillaMasReciente.getColumna()));
                                    }
                                } else {

                                    boolean enroqueCorto = resultado == Pieza.ENROQUE_CORTO;

                                    // Casilla de la torre:
                                    Casilla casillaOriginalTorre = tablero.getTorreEnroque(turnoBlancas, enroqueCorto);

                                    if (!((Rey) casillaOrigen.getPieza()).isMovida()
                                            && !((Torre) casillaOriginalTorre.getPieza()).isMovida()) {

                                        // Si la pieza es una torre o un rey, se registra que 
                                        // se ha movido:
                                        if (casillaOrigen.getPieza() instanceof Rey) {
                                            ((Rey) casillaOrigen.getPieza()).setMovida(true);
                                        } else if (casillaOrigen.getPieza() instanceof Torre) {
                                            ((Torre) casillaOrigen.getPieza()).setMovida(true);
                                        }

                                        // Nuevas casillas para el rey y la torre:
                                        Casilla casillaRey = tablero.casillaReyEnroque(
                                                casillaOrigen.getPieza().getJugador(), enroqueCorto);
                                        Casilla casillaTorre = tablero.casillaTorreEnroque(
                                                casillaOrigen.getPieza().getJugador(), enroqueCorto);

                                        // Eliminamos la torre y el rey de las casillas originales:
                                        casillaOrigen.setPieza(null);
                                        casillaOriginalTorre.setPieza(null);
                                        casillaOriginalTorre.setOcupada(false);

                                        casillaOrigen.setOcupada(false);

                                        // Introducimos las columnas en el tablero:
                                        tablero.setCasilla(casillaRey);
                                        tablero.setCasilla(casillaTorre);
                                        tablero.setCasilla(casillaOrigen);
                                        tablero.setCasilla(casillaOriginalTorre);

                                        // Actualizamos las casillas:
                                        int fOrigenRey = casillaOrigen.getFila();
                                        int cOrigenRey = casillaOrigen.getColumna();
                                        int fOrigenTorre = casillaOriginalTorre.getFila();
                                        int cOrigenTorre = casillaOriginalTorre.getColumna();
                                        int fDestinoRey = casillaRey.getFila();
                                        int cDestinoRey = casillaRey.getColumna();
                                        int fDestinoTorre = casillaTorre.getFila();
                                        int cDestinoTorre = casillaTorre.getColumna();

                                        botonera[7 - fOrigenRey][cOrigenRey].setIcon(null);
                                        botonera[7 - fOrigenTorre][cOrigenTorre].setIcon(null);
                                        botonera[7 - fDestinoRey][cDestinoRey].setIcon(
                                                new ImageIcon(casillaRey.getPieza().getRutaImagen()));
                                        botonera[7 - fDestinoTorre][cDestinoTorre].setIcon(
                                                new ImageIcon(casillaTorre.getPieza().getRutaImagen()));

                                        // Cambiamos el color de la casilla de origen:
                                        this.desmarcaCasilla(casillaOrigen);
                                    }

                                }

                                // Es el turno del otro jugador:
                                turnoBlancas = !turnoBlancas;

                                // Se actualiza la última jugada
                                casillaMasReciente = tablero.getCasilla(fila, columna);

                                // Comprobamos si hay jaque mate o ahogado:
                                Jugador jugador;

                                if (turnoBlancas) {
                                    jugador = this.jugadorBlancas;
                                } else {
                                    jugador = this.jugadorNegras;
                                }

                                // Comprobamos si hay jaque mate o ahogado:
                                if (jugador.getEstadoJugador(tablero) == Jugador.AHOGADO) {

                                    this.partidaActiva = false;
                                    this.estado = TABLAS;

                                } else if (jugador.getEstadoJugador(tablero) == Jugador.JAQUE_MATE) {

                                    if (jugador.getColor() == Jugador.BLANCO) {
                                        this.finalizarPartida(GANA_NEGRAS);
                                    } else {
                                        this.finalizarPartida(GANA_BLANCAS);
                                    }

                                    jaqueMate = true;
                                }

                                // Si hay promoción hay que cambiar de pieza:
                                if (resultado == Pieza.PROMOCION) {

                                    Object txt = JOptionPane.showInputDialog(null, "Selecciona una pieza:",
                                            "Promoción", JOptionPane.QUESTION_MESSAGE, null,
                                            new String[]{"Dama", "Torre", "Caballo", "Alfil"},
                                            "Dama");

                                    Pieza nuevaPieza = null;
                                    Jugador miJugador = casillaOrigen.getPieza().getJugador();

                                    promocion = txt.toString();

                                    switch (promocion) {
                                        case "Dama":
                                            nuevaPieza = new Dama(miJugador);
                                            break;
                                        case "Torre":
                                            nuevaPieza = new Torre(miJugador);
                                            break;
                                        case "Caballo":
                                            nuevaPieza = new Caballo(miJugador);
                                            break;
                                        case "Alfil":
                                            nuevaPieza = new Alfil(miJugador);
                                            break;
                                    }

                                    tablero.setCasilla(new Casilla(fila, columna, nuevaPieza));

                                    String ruta = nuevaPieza.getRutaImagen();

                                    botonera[7 - fila][columna].setIcon(new ImageIcon(ruta));
                                }

                                // Comprobamos si hay jaques:
                                boolean jaque[] = this.compruebaJaques();

                                boolean enJaque;

                                if (turnoBlancas) {
                                    enJaque = jaque[0];
                                } else {
                                    enJaque = jaque[1];
                                }

                                if (copiaOrigen != null && copiaDestino != null) {
                                    // Guardamos la partida:
                                    tablero.guardaPartida(copiaOrigen, copiaDestino, copiaOrigen.getPieza());
                                    tablero.registraMovimiento(copiaOrigen, copiaDestino,
                                            resultado, promocion, enJaque, jaqueMate);
                                }

                            }

                            // Desmarcamos la casilla:
                            if (resultado != Pieza.ENROQUE_CORTO
                                    && resultado != Pieza.ENROQUE_LARGO) {

                                this.desmarcaCasilla(casillaOrigen);

                            }

                            casillaOrigen = null;
                            casillaDestino = null;
                        }

                        // Desmarcamos las casillas disponibles:
                        if (this.casillasDisponibles != null) {
                            for (int i = 0; i < casillasDisponibles.size(); i++) {
                                this.desmarcaCasilla(casillasDisponibles.get(i));
                            }
                        }

                    }
                } else {

                    if (casillaOrigen != null) {
                        int filaOrigen = casillaOrigen.getFila();
                        int columnaOrigen = casillaOrigen.getColumna();

                        // Desmarcamos la casilla:
                        this.desmarcaCasilla(casillaOrigen);
                    }

                }
            }
        }

    }

    // Método que inicializa el tablero:
    private void inicializaTablero() {

        tablero.inicializarTablero(jugadorBlancas, jugadorNegras);

        Casilla[][] casillas = tablero.getCasillas();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (casillas[7 - i][j].isOcupada()) {
                    String ruta = casillas[7 - i][j].getPieza().getRutaImagen();
                    botonera[i][j].setIcon(new ImageIcon(ruta));
                }
            }
        }
    }

    // Método que comprueba si algún rey está en jaque: 
    private boolean[] compruebaJaques() {

        boolean blancoEnJaque = false;
        boolean negroEnJaque = false;

        // Posición de los reyes:
        Casilla reyBlanco = this.tablero.getCasillaRey(true);
        Casilla reyNegro = this.tablero.getCasillaRey(false);

        int fBlanco = reyBlanco.getFila();
        int cBlanco = reyBlanco.getColumna();

        int fNegro = reyNegro.getFila();
        int cNegro = reyNegro.getColumna();

        // Comprobamos si el rey blanco está en jaque:
        if (this.tablero.enJaque(true)) {

            blancoEnJaque = true;

            // Desmarcamos el rey negro:
            this.desmarcaCasilla(reyNegro);

            // Marcamos el rey blanco:
            botonera[7 - fBlanco][cBlanco].setBackground(new Color(
            Paleta.ROJO1,Paleta.ROJO2,Paleta.ROJO3));
        } // Comprobamos si el rey negro está en jaque:
        else if (this.tablero.enJaque(false)) {

            negroEnJaque = true;

            // Desmarcamos el rey blanco:
            this.desmarcaCasilla(reyBlanco);

            // Marcamos el rey negro:
            botonera[7 - fNegro][cNegro].setBackground(new Color(
            Paleta.ROJO1,Paleta.ROJO2,Paleta.ROJO3));
        } else {

            // Desmarcamos el rey negro:
            this.desmarcaCasilla(reyNegro);

            // Desmarcamos el rey blanco:
            this.desmarcaCasilla(reyBlanco);
        }

        return (new boolean[]{blancoEnJaque, negroEnJaque});
    }

    /**
     * Método que devuelve una casilla a su color original
     *
     * @param casilla Casilla que se desea desmarcar
     */
    private void desmarcaCasilla(Casilla casilla) {

        int fila = casilla.getFila();
        int columna = casilla.getColumna();

        if ((7 - fila + columna) % 2 == 0) {
            botonera[7 - fila][columna].setBackground(new Color(
            Paleta.MARRON_OSCURO1,Paleta.MARRON_OSCURO2,Paleta.MARRON_OSCURO3));
        } else {
            botonera[7 - fila][columna].setBackground(new Color(
            Paleta.MARRON_CLARO1,Paleta.MARRON_CLARO2,Paleta.MARRON_CLARO3));
        }
    }

    /**
     * Método que colorea de verde las casillas a las que puede ir una pieza
     *
     * @param origen Casilla en la que se encuentra la pieza
     * @param tablero Situación global del tablero
     */
    private void coloreaCasillasDisponibles(Casilla origen, Tablero tablero) {

        // Obtención de las casillas disponibles:
        casillasDisponibles = origen.getPieza().getCasillasDisponibles(origen, tablero);

        // Se colorean las casillas:
        for (int i = 0; i < casillasDisponibles.size(); i++) {

            int fila = casillasDisponibles.get(i).getFila();
            int columna = casillasDisponibles.get(i).getColumna();

            this.botonera[7 - fila][columna].setBackground(new Color(
            Paleta.VERDE1,Paleta.VERDE2,Paleta.VERDE3));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSuperior = new javax.swing.JPanel();
        nombreJugadorNegras = new javax.swing.JLabel();
        relojNegras = new javax.swing.JLabel();
        panelInferior = new javax.swing.JPanel();
        nombreJugadorBlancas = new javax.swing.JLabel();
        panelTablero = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(500, 600));

        panelSuperior.setBackground(new java.awt.Color(0, 0, 0));
        panelSuperior.setMinimumSize(new java.awt.Dimension(500, 50));
        panelSuperior.setPreferredSize(new java.awt.Dimension(500, 50));
        panelSuperior.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        nombreJugadorNegras.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        nombreJugadorNegras.setForeground(new java.awt.Color(255, 255, 255));
        nombreJugadorNegras.setText("JUGADOR NEGRAS");
        nombreJugadorNegras.setMaximumSize(new java.awt.Dimension(410, 29));
        nombreJugadorNegras.setMinimumSize(new java.awt.Dimension(410, 29));
        nombreJugadorNegras.setPreferredSize(new java.awt.Dimension(410, 29));
        panelSuperior.add(nombreJugadorNegras);

        relojNegras.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        relojNegras.setForeground(new java.awt.Color(255, 255, 255));
        panelSuperior.add(relojNegras);

        getContentPane().add(panelSuperior, java.awt.BorderLayout.PAGE_START);

        panelInferior.setBackground(new java.awt.Color(0, 0, 0));
        panelInferior.setMaximumSize(new java.awt.Dimension(500, 50));
        panelInferior.setMinimumSize(new java.awt.Dimension(500, 50));
        panelInferior.setPreferredSize(new java.awt.Dimension(500, 50));
        panelInferior.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        nombreJugadorBlancas.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        nombreJugadorBlancas.setForeground(new java.awt.Color(255, 255, 255));
        nombreJugadorBlancas.setText("JUGADOR BLANCAS");
        nombreJugadorBlancas.setMaximumSize(new java.awt.Dimension(410, 29));
        nombreJugadorBlancas.setMinimumSize(new java.awt.Dimension(410, 29));
        nombreJugadorBlancas.setPreferredSize(new java.awt.Dimension(410, 29));
        panelInferior.add(nombreJugadorBlancas);

        getContentPane().add(panelInferior, java.awt.BorderLayout.PAGE_END);

        panelTablero.setBackground(new java.awt.Color(0, 0, 0));
        panelTablero.setMinimumSize(new java.awt.Dimension(500, 500));
        panelTablero.setPreferredSize(new java.awt.Dimension(500, 500));
        panelTablero.setLayout(new java.awt.GridLayout(8, 8));
        getContentPane().add(panelTablero, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ajedrez.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ajedrez.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ajedrez.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ajedrez.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ajedrez().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nombreJugadorBlancas;
    private javax.swing.JLabel nombreJugadorNegras;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JPanel panelTablero;
    private javax.swing.JLabel relojNegras;
    // End of variables declaration//GEN-END:variables

    /**
     * Método que pinta en las etiquetas el tiempo de cada jugador
     */

    private void finalizarPartida(int estado) {

        this.partidaActiva = false;

        String mensaje = new String();

        switch (estado) {
            case GANA_BLANCAS:
                mensaje = "Ganan las blancas";
                break;
            case GANA_NEGRAS:
                mensaje = "Ganan las negras";
                break;
            case TABLAS:
                mensaje = "Tablas";
                break;
        }

        this.actualizaJugadores(estado);

        JOptionPane.showMessageDialog(null, mensaje, "Fin de la partida",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizaJugadores(int resultado) {

        String txt1 = this.nombreJugadorBlancas.getText();
        String txt2 = this.nombreJugadorNegras.getText();

        switch (resultado) {
            case GANA_BLANCAS:

                this.nombreJugadorBlancas.setText(txt1 + " (1)");
                this.nombreJugadorNegras.setText(txt2 + " (0)");
                break;
            case GANA_NEGRAS:

                this.nombreJugadorBlancas.setText(txt1 + " (0)");
                this.nombreJugadorNegras.setText(txt2 + " (1)");
                break;
            case TABLAS:

                this.nombreJugadorBlancas.setText(txt1 + " (1/2)");
                this.nombreJugadorNegras.setText(txt2 + " (1/2)");
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
