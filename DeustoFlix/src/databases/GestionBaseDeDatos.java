package databases;

import java.sql.*;
import java.util.ArrayList;


import domain.Genero;
import domain.Pelicula;
import domain.Serie;

public class GestionBaseDeDatos {

    private static Connection con;

    // ----------------- CONEXIÓN -----------------
    public static void conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BaseDeDatos/DeustoFlix.db");
            System.out.println("Conexión establecida.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }

    public static void desconectar() {
        try {
            if (con != null) {
                con.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    // ----------------- CREAR TABLAS -----------------
    public static void crearTablas() {
        if (con == null) conectar();

        try (Statement st = con.createStatement()) {
            String sqlGenero = "CREATE TABLE IF NOT EXISTS GENERO (" +
                    "ID_G INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NOMBRE TEXT UNIQUE);";
            st.execute(sqlGenero);

            String sqlContenido = "CREATE TABLE IF NOT EXISTS CONTENIDO (" +
                    "ID_CONT INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "TIPO TEXT," +
                    "TITULO TEXT NOT NULL," +
                    "DIRECTOR TEXT," +
                    "ANYO_ESTRENO INTEGER," +
                    "DESCRIPCION TEXT," +
                    "DURACION INTEGER);";
            st.execute(sqlContenido);

            String sqlContenidoGenero = "CREATE TABLE IF NOT EXISTS CONTENIDO_GENEROS (" +
                    "ID_C INTEGER," +
                    "ID_G INTEGER," +
                    "PRIMARY KEY(ID_C, ID_G)," +
                    "FOREIGN KEY(ID_C) REFERENCES CONTENIDO(ID_CONT)," +
                    "FOREIGN KEY(ID_G) REFERENCES GENERO(ID_G));";
            st.execute(sqlContenidoGenero);

            System.out.println("Tablas creadas correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----------------- MÉTODOS CONTENIDO -----------------
    public static void registrarGenero(Genero genero) {
        String sql = "INSERT OR IGNORE INTO GENERO (NOMBRE) VALUES (?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, genero.name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registrarContenido(String tipo, String titulo, String director, int anyoEstreno, String descripcion, int duracion) {
        String sql = "INSERT INTO CONTENIDO (TIPO, TITULO, DIRECTOR, ANYO_ESTRENO, DESCRIPCION, DURACION) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tipo);
            stmt.setString(2, titulo);
            stmt.setString(3, director);
            stmt.setInt(4, anyoEstreno);
            stmt.setString(5, descripcion);
            stmt.setInt(6, duracion);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idContenido = rs.getInt(1);
                    System.out.println(tipo + " registrado con ID: " + idContenido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  
}


