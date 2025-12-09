package main;

import databases.ConexionBD;
import java.sql.Connection;
import gui.VentanaInicio;

public class Main {
    public static void main(String[] args) {

        // Conectar a la base de datos (y crear tablas si no existen)
        try (Connection con = ConexionBD.conectar()) {
            if (con != null) {
                System.out.println("Base de datos lista para usar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Abrir ventana principal de la app
        VentanaInicio inicio = new VentanaInicio();
        inicio.setVisible(true);
    }
}
