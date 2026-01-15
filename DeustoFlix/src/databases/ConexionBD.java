package databases;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import domain.*;

public class ConexionBD {

    private static final String URL = "jdbc:sqlite:Basededatos/deustoflix.db";

    private static Connection getConnection() throws SQLException {
        try { 
            Class.forName("org.sqlite.JDBC"); 
        } catch (ClassNotFoundException e) { 
            System.err.println(">>> [ERROR CRÍTICO] No se encontró el driver de SQLite.");
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
            // Usuarios
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre TEXT NOT NULL UNIQUE, "
                    + "gmail TEXT NOT NULL, "
                    + "contrasenya TEXT NOT NULL, "
                    + "foto TEXT)");

            // Contenido
            stmt.execute("CREATE TABLE IF NOT EXISTS contenido ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "titulo TEXT NOT NULL, "
                    + "tipo TEXT NOT NULL, "
                    + "genero TEXT, "
                    + "categoria TEXT, "
                    + "descripcion TEXT, "
                    + "duracion INTEGER, "
                    + "valoracion REAL)");
            
            // Favoritos
            stmt.execute("CREATE TABLE IF NOT EXISTS favoritos ("
                    + "usuario_nombre TEXT, "
                    + "contenido_id INTEGER, "
                    + "PRIMARY KEY (usuario_nombre, contenido_id), "
                    + "FOREIGN KEY(usuario_nombre) REFERENCES usuarios(nombre), "
                    + "FOREIGN KEY(contenido_id) REFERENCES contenido(id))");

            // Valoraciones
            stmt.execute("CREATE TABLE IF NOT EXISTS valoraciones ("
                    + "usuario_nombre TEXT, "
                    + "contenido_id INTEGER, "
                    + "nota INTEGER, " 
                    + "PRIMARY KEY (usuario_nombre, contenido_id), "
                    + "FOREIGN KEY(usuario_nombre) REFERENCES usuarios(nombre), "
                    + "FOREIGN KEY(contenido_id) REFERENCES contenido(id))");

        } catch (SQLException e) { 
            System.err.println(">>> [ERROR] Fallo al iniciar la BD: " + e.getMessage());
        }
    }

    // --- GESTIÓN DE USUARIOS ---

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
            if (e.getMessage().contains("UNIQUE") || e.getErrorCode() == 19) {
                System.out.println(">>> [AVISO] Intento de registro duplicado. El usuario '" + nombre + "' ya existe.");
            } else {
                System.err.println(">>> [ERROR] Error SQL desconocido al crear usuario: " + e.getMessage());
                e.printStackTrace();
            }
            return false; 
        }
    }

    public static boolean loginUsuario(String nombre, String contrasenya) {
        String sql = "SELECT * FROM usuarios WHERE nombre=? AND contrasenya=?";
        
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre); 
            pst.setString(2, contrasenya);
            ResultSet rs = pst.executeQuery(); 
            
            if (rs.next()) {
                return true;
            } else {
                System.out.println(">>> [AVISO] Login fallido para '" + nombre + "'. Contraseña incorrecta o usuario no existe.");
                return false;
            }

        } catch (SQLException e) { 
            System.err.println(">>> [ERROR] Fallo técnico en el login: " + e.getMessage()); 
            return false; 
        }
    }

    public static String[] obtenerDatosUsuario(String nombre) {
        String sql = "SELECT gmail, contrasenya FROM usuarios WHERE nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new String[]{ rs.getString("gmail"), rs.getString("contrasenya") };
            }
        } catch (SQLException e) {
            System.err.println(">>> [ERROR] Al obtener datos de usuario: " + e.getMessage());
        }
        return null;
    }

    public static boolean actualizarUsuario(String nombre, String nuevoGmail, String nuevaPass) {
        String sql = "UPDATE usuarios SET gmail = ?, contrasenya = ? WHERE nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nuevoGmail); 
            pst.setString(2, nuevaPass);
            pst.setString(3, nombre);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { 
            System.err.println(">>> [ERROR] Al actualizar usuario: " + e.getMessage());
            return false; 
        }
    }

    public static boolean actualizarFotoUsuario(String nombre, String nuevaRutaFoto) {
        String sql = "UPDATE usuarios SET foto = ? WHERE nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nuevaRutaFoto);
            pst.setString(2, nombre);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(">>> [ERROR] Al cambiar foto: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarUsuario(String nombre) {
        String sqlBorrarFavoritos = "DELETE FROM favoritos WHERE usuario_nombre = ?";
        String sqlBorrarValoraciones = "DELETE FROM valoraciones WHERE usuario_nombre = ?";
        String sqlBorrarUsuario = "DELETE FROM usuarios WHERE nombre = ?";
        
        try (Connection con = getConnection()) {
            con.setAutoCommit(false); 

            try (PreparedStatement pst = con.prepareStatement(sqlBorrarFavoritos)) {
                pst.setString(1, nombre);
                pst.executeUpdate();
            }
            
            try (PreparedStatement pst = con.prepareStatement(sqlBorrarValoraciones)) {
                pst.setString(1, nombre);
                pst.executeUpdate();
            }
            
            try (PreparedStatement pst = con.prepareStatement(sqlBorrarUsuario)) {
                pst.setString(1, nombre);
                int rows = pst.executeUpdate();
                con.commit(); 
                return rows > 0;
            }
            
        } catch (SQLException e) {
            System.err.println(">>> [ERROR] Fallo al eliminar cuenta: " + e.getMessage());
            return false;
        }
    }

    // --- GESTIÓN DE CONTENIDO ---

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
        }
    }

    public static ArrayList<MediaItem> cargarContenido() {
        ArrayList<MediaItem> lista = new ArrayList<>();
        String sql = "SELECT * FROM contenido";
        try (Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id"); 
                String titulo = rs.getString("titulo");
                String tipo = rs.getString("tipo");
                Genero genero = Genero.fromString(rs.getString("genero"));
                Categoria categoria = new Categoria(rs.getString("categoria"));
                String desc = rs.getString("descripcion");
                int dur = rs.getInt("duracion");
                double val = rs.getDouble("valoracion");
                
                MediaItem item;
                if ("Pelicula".equals(tipo)) {
                    item = new Pelicula(titulo, desc, genero, categoria, val, dur);
                } else {
                    item = new Serie(titulo, desc, genero, categoria, val, dur);
                }
                item.setId(id);
                lista.add(item);
            }
        } catch (SQLException e) { 
            System.err.println(">>> [ERROR] Al cargar contenido: " + e.getMessage()); 
        }
        return lista;
    }

    public static void cargarPeliculasDesdeCSV() {
        var stream = ConexionBD.class.getClassLoader().getResourceAsStream("peliculas.csv");
        if (stream == null) {
             try { stream = new java.io.FileInputStream("src/peliculas.csv"); } catch(Exception e) {}
        }
        if (stream != null) {
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
                System.err.println(">>> [ERROR] Leyendo CSV Películas: " + e.getMessage()); 
            }
        }
    }

    public static void cargarSeriesDesdeCSV() {
        var stream = ConexionBD.class.getClassLoader().getResourceAsStream("series.csv");
        if (stream == null) {
             try { stream = new java.io.FileInputStream("src/series.csv"); } catch(Exception e) {}
        }
        if (stream != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                br.readLine(); 
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (campos.length >= 6) {
                        String titulo = campos[0].trim();
                        String descripcion = campos[1].trim();
                        Genero genero = null;
                        try {
                            genero = Genero.valueOf(campos[2].trim().toUpperCase());
                        } catch (IllegalArgumentException ex) { genero = Genero.DRAMA; }
                        Categoria categoria = new Categoria(campos[3].trim());
                        int dur = Integer.parseInt(campos[4].trim());
                        double val = Double.parseDouble(campos[5].trim());
                        insertarContenido(new Serie(titulo, descripcion, genero, categoria, val, dur));
                    }
                }
            } catch (Exception e) { 
                System.err.println(">>> [ERROR] Leyendo CSV Series: " + e.getMessage()); 
            }
        }
    }

    // --- FAVORITOS Y VALORACIONES ---
    
    public static boolean esFavorito(String usuario, int idContenido) {
        String sql = "SELECT 1 FROM favoritos WHERE usuario_nombre = ? AND contenido_id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, usuario);
            pst.setInt(2, idContenido);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) { return false; }
    }

    //Pequeña ayuda de ia para este metodo
    public static void toggleFavorito(String usuario, int idContenido) {
        if (esFavorito(usuario, idContenido)) {
            String sql = "DELETE FROM favoritos WHERE usuario_nombre = ? AND contenido_id = ?";
            try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, usuario);
                pst.setInt(2, idContenido);
                pst.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        } else {
            String sql = "INSERT INTO favoritos (usuario_nombre, contenido_id) VALUES (?, ?)";
            try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, usuario);
                pst.setInt(2, idContenido);
                pst.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static ArrayList<Integer> obtenerIdsFavoritos(String usuario) {
        ArrayList<Integer> ids = new ArrayList<>();
        String sql = "SELECT contenido_id FROM favoritos WHERE usuario_nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, usuario);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) ids.add(rs.getInt("contenido_id"));
        } catch (SQLException e) { e.printStackTrace(); }
        return ids;
    }

    public static void vaciarFavoritos(String usuario) {
        String sql = "DELETE FROM favoritos WHERE usuario_nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, usuario);
            pst.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public static void valorarContenido(String usuario, int idContenido, int nota) {
        String sql = "INSERT OR REPLACE INTO valoraciones (usuario_nombre, contenido_id, nota) VALUES (?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, usuario);
            pst.setInt(2, idContenido);
            pst.setInt(3, nota);
            pst.executeUpdate();
            
            recalcularMedia(idContenido);
            
        } catch (SQLException e) { 
            System.err.println(">>> [ERROR] Al valorar contenido: " + e.getMessage()); 
        }
    }
    
    public static int obtenerMiValoracion(String usuario, int idContenido) {
        String sql = "SELECT nota FROM valoraciones WHERE usuario_nombre = ? AND contenido_id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, usuario);
            pst.setInt(2, idContenido);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("nota");
        } catch (SQLException e) { }
        return 0; 
    }
    
    private static void recalcularMedia(int idContenido) {
        String sqlAvg = "SELECT AVG(nota) as media FROM valoraciones WHERE contenido_id = ?";
        double mediaEstrellas = 0;
        
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sqlAvg)) {
            pst.setInt(1, idContenido);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) mediaEstrellas = rs.getDouble("media");
        } catch (SQLException e) { e.printStackTrace(); }
        
        double nuevaValoracionGlobal = mediaEstrellas * 2;
        
        String sqlUpdate = "UPDATE contenido SET valoracion = ? WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sqlUpdate)) {
            pst.setDouble(1, nuevaValoracionGlobal);
            pst.setInt(2, idContenido);
            pst.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public static double obtenerValoracionMedia(int idContenido) {
        String sql = "SELECT valoracion FROM contenido WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idContenido);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getDouble("valoracion");
        } catch (SQLException e) { }
        return 0.0;
    }

    public static String obtenerFotoUsuario(String nombre) {
        String sql = "SELECT foto FROM usuarios WHERE nombre = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getString("foto");
        } catch (SQLException e) {}
        return null; 
    }
    
    public static ArrayList<String[]> obtenerTodosUsuarios() {
        ArrayList<String[]> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, gmail, foto FROM usuarios ORDER BY nombre";
        try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(new String[]{ 
                    String.valueOf(rs.getInt("id")), rs.getString("nombre"), rs.getString("gmail"), rs.getString("foto") 
                });
            }
        } catch (SQLException e) {}
        return usuarios;
    }
    
    public static boolean eliminarContenido(int id) {
        String sql = "DELETE FROM contenido WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    public static boolean actualizarContenido(int id, String titulo, String tipo, String genero, String categoria, String descripcion, int duracion, double valoracion) {
        String sql = "UPDATE contenido SET titulo = ?, tipo = ?, genero = ?, categoria = ?, descripcion = ?, duracion = ?, valoracion = ? WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, titulo); pst.setString(2, tipo); pst.setString(3, genero); pst.setString(4, categoria);
            pst.setString(5, descripcion); pst.setInt(6, duracion); pst.setDouble(7, valoracion); pst.setInt(8, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    public static ArrayList<String[]> obtenerTodoContenidoConID() {
        ArrayList<String[]> contenidos = new ArrayList<>();
        String sql = "SELECT id, titulo, tipo, genero, categoria, duracion, valoracion FROM contenido ORDER BY titulo";
        try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                contenidos.add(new String[]{
                    String.valueOf(rs.getInt("id")), rs.getString("titulo"), rs.getString("tipo"), rs.getString("genero"),
                    rs.getString("categoria"), String.valueOf(rs.getInt("duracion")), String.format("%.1f", rs.getDouble("valoracion"))
                });
            }
        } catch (SQLException e) {}
        return contenidos;
    }

    public static boolean esAdmin(String nombre, String contrasenya) {
        return "admin".equals(nombre) && "admin".equals(contrasenya);
    }
}