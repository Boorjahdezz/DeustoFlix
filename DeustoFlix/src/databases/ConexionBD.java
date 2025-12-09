package databases;

import java.sql.*;

public class ConexionBD {

    private static final String URL = "jdbc:sqlite:usuarios.db";

    // Inicializa la base de datos y la tabla
    public static void inicializarBD() {
        try (Connection con = DriverManager.getConnection(URL);
             Statement stmt = con.createStatement()) {
            System.out.println("Conexión a la base de datos exitosa.");

            // Crear tabla 'usuarios' si no existe
            String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre TEXT NOT NULL UNIQUE, "
                    + "gmail TEXT NOT NULL, "
                    + "contrasenya TEXT NOT NULL)";
            stmt.execute(sql);
            System.out.println("Tabla 'usuarios' creada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para registrar usuario
    public static boolean crearUsuario(String nombre, String gmail, String contrasenya) {
        String sql = "INSERT INTO usuarios(nombre, gmail, contrasenya) VALUES(?,?,?)";
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            pst.setString(2, gmail);
            pst.setString(3, contrasenya);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    // Método para login
    public static boolean loginUsuario(String nombre, String contrasenya) {
        String sql = "SELECT * FROM usuarios WHERE nombre=? AND contrasenya=?";
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            pst.setString(2, contrasenya);
            ResultSet rs = pst.executeQuery();
            return rs.next(); // true si encontró usuario
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
            return false;
        }
    }
}

