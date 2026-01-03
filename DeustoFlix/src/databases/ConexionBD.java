package databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import domain.*;

public class ConexionBD {

    // Ruta a la carpeta y archivo
    private static final String URL = "jdbc:sqlite:Basededatos/deustoflix.db";

    private static Connection getConnection() throws SQLException {
        try { 
            Class.forName("org.sqlite.JDBC"); 
        } catch (ClassNotFoundException e) { 
            throw new SQLException("Driver SQLite no encontrado", e); 
        }

        File dbFile = new File("Basededatos/deustoflix.db");
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); 
        }

        return DriverManager.getConnection(URL);
    }

    public static void inicializarBD() {
        try (Connection con = getConnection(); Statement stmt = con.createStatement()) {
            String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre TEXT NOT NULL UNIQUE, "
                    + "gmail TEXT NOT NULL, "
                    + "contrasenya TEXT NOT NULL, "
                    + "foto TEXT)"; 
            stmt.execute(sqlUsuarios);

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

        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    public static boolean crearUsuario(String nombre, String gmail, String contrasenya, String foto) {
        String sql = "INSERT INTO usuarios(nombre, gmail, contrasenya, foto) VALUES(?,?,?,?)";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre); 
            pst.setString(2, gmail); 
            pst.setString(3, contrasenya);
            pst.setString(4, foto); 
            pst.executeUpdate(); 
            return true;
        } catch (SQLException e) { 
            System.err.println("Error SQL al crear usuario: " + e.getMessage());
            return false; 
        }
    }

    public static boolean loginUsuario(String nombre, String contrasenya) {
        String sql = "SELECT * FROM usuarios WHERE nombre=? AND contrasenya=?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre); 
            pst.setString(2, contrasenya);
            ResultSet rs = pst.executeQuery(); 
            return rs.next();
        } catch (SQLException e) { 
            System.err.println("Error en login: " + e.getMessage()); 
            return false; 
        }
    }

    public static String obtenerFotoUsuario(String nombre) {
        String sql = "SELECT foto FROM usuarios WHERE nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("foto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    // --- MÉTODOS AÑADIDOS PARA EDITAR DATOS ---
    
    // 1. Obtener datos actuales (para rellenar los campos al editar)
    public static String[] obtenerDatosUsuario(String nombre) {
        String sql = "SELECT gmail, contrasenya FROM usuarios WHERE nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new String[]{ rs.getString("gmail"), rs.getString("contrasenya") };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2. Actualizar usuario
    public static boolean actualizarUsuario(String nombre, String nuevoGmail, String nuevaPass) {
        String sql = "UPDATE usuarios SET gmail = ?, contrasenya = ? WHERE nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nuevoGmail); 
            pst.setString(2, nuevaPass);
            pst.setString(3, nombre);
            int rows = pst.executeUpdate(); 
            return rows > 0;
        } catch (SQLException e) { 
            System.err.println("Error SQL al actualizar usuario: " + e.getMessage());
            return false; 
        }
    }
    // ------------------------------------------

    public static boolean eliminarUsuario(String nombre) {
        String sql = "DELETE FROM usuarios WHERE nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0; 
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public static void insertarContenido(MediaItem item) {
        String sql = "INSERT INTO contenido(titulo,tipo,genero,categoria,descripcion,duracion,valoracion) VALUES(?,?,?,?,?,?,?)";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, item.getTitulo());
            pst.setString(2, item.getTipo());
            pst.setString(3, item.getGenero() != null ? item.getGenero().name() : null);
            pst.setString(4, item.getCategoria().getNombre());
            pst.setString(5, item.getDescripcion());
            pst.setInt(6, item.getDuracion());
            double val = 0.0;
            if (item instanceof Pelicula) val = ((Pelicula)item).getValoracion();
            else if (item instanceof Serie) val = ((Serie)item).getValoracion();
            pst.setDouble(7, val);
            pst.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    public static ArrayList<MediaItem> cargarContenido() {
        ArrayList<MediaItem> lista = new ArrayList<>();
        String sql = "SELECT * FROM contenido";
        try (Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String tipo = rs.getString("tipo");
                Genero genero = Genero.fromString(rs.getString("genero"));
                Categoria categoria = new Categoria(rs.getString("categoria"));
                String desc = rs.getString("descripcion");
                int dur = rs.getInt("duracion");
                double val = rs.getDouble("valoracion");
                if ("Pelicula".equals(tipo)) lista.add(new Pelicula(titulo, desc, genero, categoria, val, dur));
                else lista.add(new Serie(titulo, desc, genero, categoria, val, dur));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return lista;
    }

    public static void cargarPeliculasDesdeCSV() {
        var stream = ConexionBD.class.getClassLoader().getResourceAsStream("peliculas.csv");
        if (stream == null) {
            System.err.println("No se encontró 'peliculas.csv'.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            br.readLine(); 
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (campos.length >= 6) {
                    String titulo = campos[0].trim();
                    String descripcion = campos[1].trim();
                    Genero genero = Genero.fromString(campos[2]);
                    Categoria categoria = new Categoria(campos[3].trim());
                    int dur = Integer.parseInt(campos[4].trim());
                    double val = Double.parseDouble(campos[5].trim());
                    insertarContenido(new Pelicula(titulo, descripcion, genero, categoria, val, dur));
                }
            }
        } catch (Exception e) { 
            System.err.println("Error cargando CSV: " + e.getMessage()); 
        }
    }
}