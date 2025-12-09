package databases;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConexionBD {

    private static final String URL = "jdbc:sqlite:Basededatos/deustoflix.db";

    public static Connection conectar() {
        Connection con = null;
        try {
            // Cargar driver SQLite
            Class.forName("org.sqlite.JDBC");

            // Crear carpeta Basededatos si no existe
            File carpeta = new File("Basededatos");
            if (!carpeta.exists()) {
                boolean creada = carpeta.mkdirs();
                if (creada) System.out.println("Carpeta Basededatos creada correctamente.");
            }

            // Conectar o crear archivo DB
            con = DriverManager.getConnection(URL);
            System.out.println("Conexión a la base de datos exitosa.");

            // Crear tablas
            Statement st = con.createStatement();

            // Tabla usuarios
            st.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL
                );
            """);
            System.out.println("Tabla 'usuarios' creada correctamente.");

            // Tabla categorias
            st.execute("""
                CREATE TABLE IF NOT EXISTS categorias (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL UNIQUE
                );
            """);
            System.out.println("Tabla 'categorias' creada correctamente.");

            // Tabla generos
            st.execute("""
                CREATE TABLE IF NOT EXISTS generos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL UNIQUE
                );
            """);
            System.out.println("Tabla 'generos' creada correctamente.");

            // Tabla peliculas
            st.execute("""
                CREATE TABLE IF NOT EXISTS peliculas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    descripcion TEXT,
                    anio INTEGER,
                    duracion INTEGER,
                    id_genero INTEGER,
                    id_categoria INTEGER,
                    FOREIGN KEY(id_genero) REFERENCES generos(id),
                    FOREIGN KEY(id_categoria) REFERENCES categorias(id)
                );
            """);
            System.out.println("Tabla 'peliculas' creada correctamente.");

            // Tabla series
            st.execute("""
                CREATE TABLE IF NOT EXISTS series (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    descripcion TEXT,
                    anio INTEGER,
                    temporadas INTEGER,
                    id_genero INTEGER,
                    id_categoria INTEGER,
                    FOREIGN KEY(id_genero) REFERENCES generos(id),
                    FOREIGN KEY(id_categoria) REFERENCES categorias(id)
                );
            """);
            System.out.println("Tabla 'series' creada correctamente.");

            // Insertar datos de ejemplo
            st.execute("INSERT OR IGNORE INTO categorias(nombre) VALUES ('Películas'), ('Series');");
            st.execute("INSERT OR IGNORE INTO generos(nombre) VALUES ('Acción'), ('Comedia'), ('Drama'), ('Terror');");
            st.execute("""
                INSERT OR IGNORE INTO peliculas(titulo, descripcion, anio, duracion, id_genero, id_categoria)
                VALUES 
                ('Inception', 'Película de ciencia ficción', 2010, 148, 1, 1),
                ('The Mask', 'Comedia de Jim Carrey', 1994, 101, 2, 1);
            """);
            st.execute("""
                INSERT OR IGNORE INTO series(titulo, descripcion, anio, temporadas, id_genero, id_categoria)
                VALUES
                ('Breaking Bad', 'Serie dramática sobre un químico', 2008, 5, 3, 2),
                ('Friends', 'Comedia clásica', 1994, 10, 2, 2);
            """);
            System.out.println("Datos de ejemplo insertados en 'categorias', 'generos', 'peliculas' y 'series'.");

            st.close();

        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos:");
            e.printStackTrace();
        }

        return con;
    }
}
