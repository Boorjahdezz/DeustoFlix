package main;

import javax.swing.SwingUtilities;
import databases.ConexionBD;
import gui.VentanaInicio;

public class Main {
    public static void main(String[] args) {
        // 1. Inicializar la base de datos
        ConexionBD.inicializarBD();

        // 2. Ejecutar la GUI en el hilo de eventos
        SwingUtilities.invokeLater(() -> {
            VentanaInicio ventanaInicio = new VentanaInicio();
            ventanaInicio.setVisible(true);
        });
    }
}

