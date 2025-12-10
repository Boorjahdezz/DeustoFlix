package databases;

import java.sql.*;

import domain.*;

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
            // Crear tabla contenido si no existe(Para las peliculas y series)
            String sqlContenido = "CREATE TABLE IF NOT EXISTS contenido ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "titulo TEXT NOT NULL, "
                    + "tipo TEXT NOT NULL, "
                    + "genero TEXT, "
                    + "categoria TEXT, "
                    + "descripcion TEXT, "
                    + "duracion INTEGER, "
                    + "valoracion REAL)";
            stmt.execute(sqlContenido);
            
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
    public static void insertarContenido(MediaItem item) {
        String sql = "INSERT INTO contenido(titulo, tipo, genero, categoria, descripcion, duracion, valoracion) VALUES(?,?,?,?,?,?,?)";
        
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, item.getTitulo());
            pst.setString(2, item.getTipo()); // "Pelicula" o "Serie"
            pst.setString(3, item.getGenero().name());
            pst.setString(4, item.getCategoria().getNombre());
            pst.setString(5, item.getDescripcion());
            pst.setInt(6, item.getDuracion());
            
            // Obtenemos la valoración dependiendo del tipo
            double val = 0.0;
            if (item instanceof Pelicula) val = ((Pelicula) item).getValoracion();
            else if (item instanceof Serie) val = ((Serie) item).getValoracion();
            pst.setDouble(7, val);

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

