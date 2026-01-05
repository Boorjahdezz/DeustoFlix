package main;

import javax.swing.SwingUtilities;
import carga.CargaDatosIniciales;
import databases.ConexionBD;
import gui.VentanaInicio;

public class Main {
    public static void main(String[] args) {
        
        // 1. Inicializar la base de datos y crear tablas
        ConexionBD.inicializarBD();
        System.out.println("Base de datos inicializada.");

        // 2. Cargar datos iniciales
        try {
            CargaDatosIniciales.cargarPeliculasIniciales();
        } catch (Exception e) {
            System.err.println("Error durante la ejecución de la carga inicial de datos: " + e.getMessage());
        }

        // 3. Ejecutar la GUI en el hilo de eventos
        SwingUtilities.invokeLater(() -> {
            VentanaInicio ventanaInicio = new VentanaInicio();
            ventanaInicio.setVisible(true);
        });
    }
}