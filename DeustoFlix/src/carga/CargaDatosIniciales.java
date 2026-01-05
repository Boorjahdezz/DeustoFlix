package carga;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import databases.ConexionBD;
import domain.Categoria;
import domain.Genero;
import domain.MediaItem;
import domain.Pelicula;

public class CargaDatosIniciales {

    private static final String CSV_FILE = "resources/peliculas.csv";

    public static void cargarPeliculasIniciales() {
        
        // 1. Verificación: ¿Ya existen datos en la BD?
        ArrayList<MediaItem> existentes = ConexionBD.cargarContenido();
        boolean hayPeliculas = false;
        
        for (MediaItem item : existentes) {
            if (item instanceof Pelicula) {
                hayPeliculas = true;
                break;
            }
        }

        if (hayPeliculas) {
            System.out.println("✅ La Base de Datos ya contiene películas. Se omite la carga del CSV.");
            return; 
        }

        // 2. Carga del archivo
        ClassLoader classLoader = CargaDatosIniciales.class.getClassLoader();
        var stream = classLoader.getResourceAsStream(CSV_FILE);
        
        if (stream == null) {
            // Intento fallback
            stream = classLoader.getResourceAsStream("peliculas.csv");
        }
        
        if (stream == null) {
            System.err.println("⚠️ No se encontró el archivo CSV de películas. Revisa la carpeta resources.");
            return;
        }

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            String line;
            br.readLine(); // Saltar encabezados

            System.out.println("Iniciando carga de películas desde CSV...");

            while ((line = br.readLine()) != null) {
                // EXPRESIÓN REGULAR para separar por comas ignorando las que están entre comillas
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Tu CSV tiene 7 columnas: Título, Tipo, Género, Categoría, Descripción, Duración, Valoración
                if (values.length >= 7) { 
                    try {
                        // --- ÍNDICES CORREGIDOS SEGÚN TU LOG DE ERROR ---
                        String titulo = values[0].replace("\"", "").trim();
                        // values[1] es "Pelicula", lo ignoramos porque ya sabemos que es película
                        String generoStr = values[2].replace("\"", "").trim();
                        String categoriaStr = values[3].replace("\"", "").trim();
                        String descripcion = values[4].replace("\"", "").trim(); // <--- Aquí estaba el error
                        int duracion = Integer.parseInt(values[5].replace("\"", "").trim());
                        double valoracion = Double.parseDouble(values[6].replace("\"", "").trim());

                        Genero genero = Genero.valueOf(generoStr.toUpperCase());
                        Categoria categoria = new Categoria(categoriaStr);

                        // Constructor: Titulo, Descripcion, Genero, Categoria, Valoracion, Duracion
                        Pelicula pelicula = new Pelicula(titulo, descripcion, genero, categoria, valoracion, duracion);
                        ConexionBD.insertarContenido(pelicula);

                    } catch (Exception e) {
                        System.err.println("Error procesando línea: " + line + " -> " + e.getMessage());
                    }
                }
            }
            System.out.println("¡Carga de películas finalizada correctamente!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}