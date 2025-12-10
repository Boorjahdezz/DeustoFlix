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

        // 2. ¡NUEVO CÓDIGO! LLAMAR AL CARGADOR DE DATOS INICIALES
        // Esto lee el CSV y guarda los 100 registros en la tabla 'contenido'.
        try {
            CargaDatosIniciales.cargarPeliculasIniciales();
        } catch (Exception e) {
            // Este catch es solo de precaución; la excepción real se manejará dentro de CargaDatosIniciales
            System.err.println("Error durante la ejecución de la carga inicial de datos: " + e.getMessage());
        }

        // 3. Ejecutar la GUI en el hilo de eventos (como lo tenías)
        SwingUtilities.invokeLater(() -> {
            VentanaInicio ventanaInicio = new VentanaInicio();
            ventanaInicio.setVisible(true);
        });
    }
}