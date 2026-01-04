package domain;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List; // Importante
import databases.*;

public class MediaRepository {
    private ArrayList<MediaItem> items;

    public MediaRepository() {
        items = new ArrayList<>();
        
        // 1. Cargar contenido de la Base de Datos
        items = ConexionBD.cargarContenido();
        
        // 2. VERIFICACIÓN: ¿Tenemos series?
        boolean haySeries = false;
        for (MediaItem item : items) {
            if (item instanceof Serie) {
                haySeries = true;
                break;
            }
        }

        // 3. PLAN DE RESCATE: Si no hay series, intentamos cargar CSV o generarlas a mano
        if (!haySeries) {
            System.out.println("⚠️ No se encontraron series en la BD. Intentando cargar CSV...");
            ConexionBD.cargarSeriesDesdeCSV(); // Intento 1: Leer archivo
            
            // Recargamos para ver si funcionó
            items = ConexionBD.cargarContenido();
            
            // Verificamos de nuevo
            haySeries = false;
            for (MediaItem item : items) if (item instanceof Serie) haySeries = true;

            // Intento 2: Si sigue sin haber series (falló el CSV), las creamos a mano
            if (!haySeries) {
                System.out.println("⚠️ Falló la carga del CSV. Generando series de respaldo...");
                generarSeriesDeRespaldo();
                items = ConexionBD.cargarContenido(); // Recarga final
            }
        }
        
        // 4. Generar imágenes para los items que no tengan (para que se vea bonito)
        for (MediaItem item : items) {
            if (item.getImagen() == null) {
                item.setImagen(crearImagenDemo(item));
            }
        }
    }

    // Método de respaldo para inyectar series si falla el archivo
    private void generarSeriesDeRespaldo() {
        Categoria catTop = new Categoria("Top Series");
        Categoria catSciFi = new Categoria("Sci-Fi Hits");
        
        // Insertamos manualmente algunas series famosas
        ConexionBD.insertarContenido(new Serie("Breaking Bad", "Profesor de química fabrica meta.", Genero.DRAMA, catTop, 9.5, 50));
        ConexionBD.insertarContenido(new Serie("Game of Thrones", "Lucha por el trono.", Genero.FANTASIA, catTop, 9.3, 60));
        ConexionBD.insertarContenido(new Serie("Stranger Things", "Niños vs Monstruos.", Genero.CIENCIA_FICCION, catSciFi, 8.7, 50));
        ConexionBD.insertarContenido(new Serie("The Office", "La vida en la oficina.", Genero.COMEDIA, new Categoria("Sitcom"), 8.9, 22));
        ConexionBD.insertarContenido(new Serie("The Mandalorian", "Star Wars western.", Genero.CIENCIA_FICCION, catSciFi, 8.7, 40));
        ConexionBD.insertarContenido(new Serie("The Boys", "Superhéroes corruptos.", Genero.ACCION, catTop, 8.7, 60));
        ConexionBD.insertarContenido(new Serie("Arcane", "Dos hermanas en guerra.", Genero.CIENCIA_FICCION, new Categoria("Animación"), 9.0, 40));
        ConexionBD.insertarContenido(new Serie("Friends", "Amigos en NY.", Genero.COMEDIA, new Categoria("Sitcom"), 8.9, 22));
    }

    public static ImageIcon crearImagenDemo(MediaItem item) {
        int w = 170;
        int h = 120;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo basado en el color del género (con fallback a GRIS si es null)
        Genero gen = item.getGenero();
        Color baseColor = (gen != null) ? gen.getColor() : Color.DARK_GRAY;
        
        GradientPaint gp = new GradientPaint(0, 0, baseColor, w, h, baseColor.darker());
        g.setPaint(gp);
        g.fillRect(0, 0, w, h);

        // Título
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String titulo = item.getTitulo();
        if (titulo.length() > 18) g.setFont(new Font("Arial", Font.BOLD, 11)); // Reducir si es muy largo
        
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(titulo)) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(titulo, Math.max(5, x), y - 10);

        // Género
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.setColor(new Color(240, 240, 240));
        String genTxt = (gen != null) ? gen.name() : "GENERAL";
        g.drawString(genTxt, 10, h - 10);

        g.dispose();
        return new ImageIcon(img);
    }

    public ArrayList<MediaItem> getAll() { 
        return items;
    }
    
    public ArrayList<MediaItem> getByTipo(String tipo) {
        ArrayList<MediaItem> res = new ArrayList<>();
        for (MediaItem item : items) {
            if (item.getTipo().equals(tipo)) {
                res.add(item);
            }
        }
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
        
        // Pre-rellenar categorías para mantener orden (opcional)
        for (Genero g : Genero.values()) map.put(g, new ArrayList<>());

        for (MediaItem item : items) {
            if (item.getTipo().equals(tipo)) {
                if (item.getGenero() != null) {
                    map.get(item.getGenero()).add(item);
                }
            }
        }
        
        // Limpiar categorías vacías para que no salgan filas vacías
        map.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        
        return map;
    }
    
    public Map<String, ArrayList<MediaItem>> agruparPorCategoria(ArrayList<MediaItem> items) {
        Map<String, ArrayList<MediaItem>> map = new LinkedHashMap<>();
        for (MediaItem item : items) {
            if (item.getCategoria() != null) {
                map.computeIfAbsent(item.getCategoria().getNombre(), k -> new ArrayList<>()).add(item);
            }
        }
        return map;
    }
}