package resources;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import databases.ConexionBD;
import domain.Categoria;
import domain.Genero;
import domain.Pelicula;

public class CargaDatosIniciales {

    private static final String CSV_FILE = "peliculas.csv";

    public static void cargarPeliculasIniciales() {
        // Obtenemos el ClassLoader para buscar el archivo en 'src/resources'
        ClassLoader classLoader = CargaDatosIniciales.class.getClassLoader();

        // ** OPCIONAL: Revisar si ya hay contenido para no duplicar**
        // Podrías añadir una lógica aquí para verificar si ya hay películas cargadas.
        
        var stream = classLoader.getResourceAsStream(CSV_FILE);
        if (stream == null) {
            throw new IllegalStateException("No se encontró '" + CSV_FILE + "' en el classpath. Asegura incluir src/resources en el classpath de ejecución.");
        }

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(stream,
                StandardCharsets.UTF_8))) {

            String line;
            // Saltar la primera línea de encabezados
            br.readLine();

            System.out.println("Iniciando carga de películas desde " + CSV_FILE + "...");

            while ((line = br.readLine()) != null) {
                // EXPRESIÓN REGULAR: Divide por comas, ignorando las comas dentro de comillas dobles.
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (values.length == 7) {
                    try {
                        // 1. Limpiar y Parsear valores
                        String titulo = values[0].replace("\"", "");
                        String generoStr = values[2].replace("\"", "");
                        String categoriaStr = values[3].replace("\"", "");
                        String descripcion = values[4].replace("\"", "");
                        int duracion = Integer.parseInt(values[5].replace("\"", ""));
                        double valoracion = Double.parseDouble(values[6].replace("\"", ""));

                        // 2. Crear objetos de Dominio
                        // Se asume que Genero es un Enum
                        Genero genero = Genero.valueOf(generoStr.toUpperCase());
                        // Se asume que Categoria tiene un constructor con el nombre
                        Categoria categoria = new Categoria(categoriaStr);

                        // 3. Crear el objeto Pelicula
                        // ¡IMPORTANTE! Ajusta este constructor si tu clase Pelicula usa otros parámetros.
                        // Asumo: Pelicula(título, descripción, género, categoría, valoración, duración)
                        Pelicula pelicula = new Pelicula(titulo, descripcion, genero, categoria, valoracion, duracion);

                        // 4. Persistir en la BD
                        ConexionBD.insertarContenido(pelicula);

                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico en la línea: " + line);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: El Genero '" + values[2] + "' o Tipo de Enum no existe.");
                    }
                } else {
                    System.err.println("Error: La línea no tiene 7 campos: " + line);
                }
            }
            System.out.println("¡Carga de 100 datos de películas finalizada!");

        } catch (Exception e) {
            System.err.println("ERROR FATAL al cargar el CSV: El archivo puede no estar en src/resources/ o faltan dependencias.");
            e.printStackTrace();
        }
    }
}
