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

        ClassLoader classLoader = CargaDatosIniciales.class.getClassLoader();
        var stream = classLoader.getResourceAsStream(CSV_FILE);
        
        if (stream == null) {

            stream = classLoader.getResourceAsStream("peliculas.csv");
        }
        
        if (stream == null) {
            System.err.println("⚠️ No se encontró el archivo CSV de películas. Revisa la carpeta resources.");
            return;
        }

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            String line;
            br.readLine(); 

            System.out.println("Iniciando carga de películas desde CSV...");

            while ((line = br.readLine()) != null) {
         
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

         
                if (values.length >= 7) { 
                    try {
                        // --- ÍNDICES CORREGIDOS SEGÚN TU LOG DE ERROR ---
                        String titulo = values[0].replace("\"", "").trim();
                      
                        String generoStr = values[2].replace("\"", "").trim();
                        String categoriaStr = values[3].replace("\"", "").trim();
                        String descripcion = values[4].replace("\"", "").trim(); 
                        int duracion = Integer.parseInt(values[5].replace("\"", "").trim());
                        double valoracion = Double.parseDouble(values[6].replace("\"", "").trim());

                        Genero genero = Genero.valueOf(generoStr.toUpperCase());
                        Categoria categoria = new Categoria(categoriaStr);

             
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