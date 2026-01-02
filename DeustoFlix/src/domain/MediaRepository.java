package domain;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import databases.*;

public class MediaRepository {
    private ArrayList<MediaItem> items;

    public MediaRepository() {
        items = new ArrayList<>();
        // 1. Intentamos cargar de la BD
        items = ConexionBD.cargarContenido();
        
        // 2. Si la BD está vacía, generamos datos y volvemos a cargar
        if(items.isEmpty()) {
            generarDatosIniciales();
            items = ConexionBD.cargarContenido();
        }
    }

    private void generarDatosIniciales() {
        Genero[] generos = Genero.values();
        Categoria[] categorias = {
            new Categoria("TOP TV"),
            new Categoria("Novedades"),
            new Categoria("DeustoFlix Exclusive"),
            new Categoria("Trending")
        };
        Random rnd = new Random();

        for (int i = 0; i < 100; i++) {
            boolean esPelicula = rnd.nextBoolean();
            Genero genero = generos[rnd.nextInt(generos.length)];
            Categoria categoria = categorias[rnd.nextInt(categorias.length)];
            String titulo = (esPelicula ? "Pelicula " : "Serie ") + (i+1);
            String descripcion = "Descripción de " + titulo + " (" + genero + ")";
            
            // La imagen no la persistimos en BD en este ejemplo simple, 
            // pero el objeto Java la tendrá al crearse la imagen demo.
            
            // CAMBIO: Crear el objeto y guardarlo en BD
            MediaItem itemNuevo;
            if (esPelicula) {
                // Constructor: titulo, descripcion, genero, categoria, valoracion, duracion
                itemNuevo = new Pelicula(titulo, descripcion, genero, categoria, 5.0 + rnd.nextDouble()*5, 90 + rnd.nextInt(60));
            } else {
                itemNuevo = new Serie(titulo, descripcion, genero, categoria, 5.0 + rnd.nextDouble()*5, 30 + rnd.nextInt(30));
            }

            // ¡IMPORTANTE!: Guardar en BD para que no se pierda
            ConexionBD.insertarContenido(itemNuevo);
        }
        System.out.println("Datos iniciales generados y guardados en BD.");
    }

    public static ImageIcon crearImagenDemo(Genero genero, int idx) {
        BufferedImage img = new BufferedImage(170, 120, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        Color color = switch (genero) {
            case COMEDIA -> new Color(255, 220, 100);
            case TERROR -> new Color(160, 0, 0);
            case THRILLER -> new Color(80, 80, 80);
            case ACCION -> new Color(0, 80, 180);
            case DRAMA -> new Color(100, 0, 100);
            case ROMANCE -> new Color(255, 120, 180);
            default -> new Color(100, 100, 100); // Color por defecto para evitar errores
        };
        g.setColor(color); g.fillRect(0, 0, 170, 120);
        g.setColor(Color.WHITE); g.drawString(genero.name(), 10, 20);
        g.drawString("IMG " + idx, 60, 60);
        g.dispose();
        return new ImageIcon(img);
    }

    public ArrayList<MediaItem> getAll() { 
        return items;
    }
    public ArrayList<MediaItem> getByTipo(String tipo) {
        ArrayList<MediaItem> res = new ArrayList<>();
        for (MediaItem item : items) if (item.getTipo().equals(tipo)) res.add(item);
        return res;
    }
    public ArrayList<MediaItem> getByGenero(String tipo, Genero genero) {
        ArrayList<MediaItem> res = new ArrayList<>();
        for (MediaItem item : items)
            if (item.getTipo().equals(tipo) && item.getGenero() == genero)
                res.add(item);
        return res;
    }
    public Map<Genero, ArrayList<MediaItem>> agruparPorGenero(String tipo) {
        Map<Genero, ArrayList<MediaItem>> map = new LinkedHashMap<>();
        for (Genero g : Genero.values()) map.put(g, new ArrayList<>());
        for (MediaItem item : items)
            if (item.getTipo().equals(tipo))
                map.get(item.getGenero()).add(item);
        return map;
    }
    public Map<String, ArrayList<MediaItem>> agruparPorCategoria(ArrayList<MediaItem> items) {
        Map<String, ArrayList<MediaItem>> map = new LinkedHashMap<>();
        for (MediaItem item : items)
            map.computeIfAbsent(item.getCategoria().getNombre(), k -> new ArrayList<>()).add(item);
        return map;
    }
}