package databases;

import java.sql.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

import domain.Categoria;
import domain.Genero;
import domain.MediaItem;
import domain.Pelicula;
import domain.Serie;

public class GestionBaseDeDatos {

    private static Connection con;

    // ----------------- CONEXIÓN -----------------
    public static void conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BaseDeDatos/DeustoFlix.db");
            // System.out.println("Conexión establecida.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }

    public static void desconectar() {
        try {
            if (con != null) {
                con.close();
                // System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    // ----------------- CREAR TABLAS -----------------
    public static void crearTablas() {
        if (con == null) conectar();

        try (Statement st = con.createStatement()) {
            // Tabla GENERO
            String sqlGenero = "CREATE TABLE IF NOT EXISTS GENERO (" +
                    "ID_G INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NOMBRE TEXT UNIQUE);";
            st.execute(sqlGenero);

            // Tabla CONTENIDO (Añadido CATEGORIA y RUTA_IMAGEN)
            String sqlContenido = "CREATE TABLE IF NOT EXISTS CONTENIDO (" +
                    "ID_CONT INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "TIPO TEXT," +
                    "TITULO TEXT NOT NULL," +
                    "DIRECTOR TEXT," +
                    "ANYO_ESTRENO INTEGER," +
                    "DESCRIPCION TEXT," +
                    "DURACION INTEGER," +
                    "CATEGORIA TEXT," +      // Nueva columna
                    "RUTA_IMAGEN TEXT);";    // Nueva columna
            st.execute(sqlContenido);

            // Tabla Relacional
            String sqlContenidoGenero = "CREATE TABLE IF NOT EXISTS CONTENIDO_GENEROS (" +
                    "ID_C INTEGER," +
                    "ID_G INTEGER," +
                    "PRIMARY KEY(ID_C, ID_G)," +
                    "FOREIGN KEY(ID_C) REFERENCES CONTENIDO(ID_CONT)," +
                    "FOREIGN KEY(ID_G) REFERENCES GENERO(ID_G));";
            st.execute(sqlContenidoGenero);

            // System.out.println("Tablas verificadas/creadas correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----------------- MÉTODOS DE LECTURA (SELECT) -----------------
    
    public static ArrayList<MediaItem> obtenerTodosLosContenidos() {
        ArrayList<MediaItem> lista = new ArrayList<>();
        if (con == null) conectar();

        // Hacemos un JOIN para traer también el nombre del Género
        String sql = "SELECT c.*, g.NOMBRE as GENERO_NOMBRE " +
                     "FROM CONTENIDO c " +
                     "LEFT JOIN CONTENIDO_GENEROS cg ON c.ID_CONT = cg.ID_C " +
                     "LEFT JOIN GENERO g ON cg.ID_G = g.ID_G";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String tipo = rs.getString("TIPO");
                String titulo = rs.getString("TITULO");
                String descripcion = rs.getString("DESCRIPCION");
                String catNombre = rs.getString("CATEGORIA");
                String generoNombre = rs.getString("GENERO_NOMBRE");
                String rutaImagen = rs.getString("RUTA_IMAGEN");
                int duracion = rs.getInt("DURACION"); // Por si lo usas en el futuro

                // Reconstruir objetos
                Genero genero = Genero.valueOf(generoNombre); // Asume que el nombre en BD es igual al Enum
                Categoria categoria = new Categoria(catNombre);
                
                // Gestionar Imagen (Si es "GENERATED" creamos la demo, si no, intentamos cargarla)
                ImageIcon imagen;
                if ("GENERATED".equals(rutaImagen) || rutaImagen == null) {
                    imagen = crearImagenDemo(genero, titulo); // Método helper abajo
                } else {
                    imagen = new ImageIcon(rutaImagen); // Carga real si hubiera ruta
                }

                MediaItem item;
                if ("Pelicula".equalsIgnoreCase(tipo)) {
                    item = new Pelicula(titulo, descripcion, genero, categoria, imagen);
                } else {
                    item = new Serie(titulo, descripcion, genero, categoria, imagen);
                    // Nota: Si Serie tuviera campos extra en BD, se leerían aquí
                }
                
                lista.add(item);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar contenidos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // ----------------- MÉTODOS DE ESCRITURA (INSERT) -----------------

    public static void registrarGenero(Genero genero) {
        String sql = "INSERT OR IGNORE INTO GENERO (NOMBRE) VALUES (?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, genero.name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método mejorado para recibir todos los datos necesarios
    public static void registrarContenidoCompleto(MediaItem item) {
        if (con == null) conectar();
        
        // 1. Asegurar que el género existe
        registrarGenero(item.getGenero());

        // 2. Insertar Contenido
        String sqlInsert = "INSERT INTO CONTENIDO (TIPO, TITULO, DIRECTOR, ANYO_ESTRENO, DESCRIPCION, DURACION, CATEGORIA, RUTA_IMAGEN) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            String tipo = (item instanceof Pelicula) ? "Pelicula" : "Serie";
            
            stmt.setString(1, tipo);
            stmt.setString(2, item.getTitulo());
            stmt.setString(3, "Director Desconocido"); // Default o podrías añadirlo a MediaItem
            stmt.setInt(4, 2023); // Default o añadir field
            stmt.setString(5, item.getDescripcion());
            stmt.setInt(6, (int)item.getDuracion());
            stmt.setString(7, item.getCategoria().getNombre());
            stmt.setString(8, "GENERATED"); // Por ahora usamos imágenes generadas

            stmt.executeUpdate();

            // 3. Obtener ID generado y enlazar con Género
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idContenido = rs.getInt(1);
                    enlazarGenero(idContenido, item.getGenero().name());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void enlazarGenero(int idContenido, String nombreGenero) throws SQLException {
        // Obtener ID del género
        String sqlGetIdG = "SELECT ID_G FROM GENERO WHERE NOMBRE = ?";
        int idGenero = -1;
        try (PreparedStatement pst = con.prepareStatement(sqlGetIdG)) {
            pst.setString(1, nombreGenero);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) idGenero = rs.getInt(1);
        }

        if (idGenero != -1) {
            String sqlLink = "INSERT INTO CONTENIDO_GENEROS (ID_C, ID_G) VALUES (?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sqlLink)) {
                pst.setInt(1, idContenido);
                pst.setInt(2, idGenero);
                pst.executeUpdate();
            }
        }
    }
    
    // ----------------- HELPER IMAGEN (Copiado/Adaptado para BD) -----------------
    private static ImageIcon crearImagenDemo(Genero genero, String texto) {
        BufferedImage img = new BufferedImage(170, 120, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        Color color = switch (genero) {
            case COMEDIA -> new Color(255, 220, 100);
            case TERROR -> new Color(160, 0, 0);
            case THRILLER -> new Color(80, 80, 80);
            case ACCION -> new Color(0, 80, 180);
            case DRAMA -> new Color(100, 0, 100);
            case ROMANCE -> new Color(255, 120, 180);
            default -> Color.GRAY;
        };
        g.setColor(color); g.fillRect(0, 0, 170, 120);
        g.setColor(Color.WHITE); 
        g.drawString(genero.name(), 10, 20);
        // Pintamos un poco del título para diferenciar
        if(texto.length() > 10) texto = texto.substring(0, 10) + "...";
        g.drawString(texto, 10, 60);
        g.dispose();
        return new ImageIcon(img);
    }
}


