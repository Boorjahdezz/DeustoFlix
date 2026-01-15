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
        
        // 1. Cargar contenido de la Base de Datos
        items = ConexionBD.cargarContenido();
        
        // 2. verificacion de si hay seeries
        boolean haySeries = false;
        for (MediaItem item : items) {
            if (item instanceof Serie) {
                haySeries = true;
                break;
            }
        }

        // Si no hay series, intentamos cargar CSV o generarlas a mano
        if (!haySeries) {
            System.out.println("⚠️ No se encontraron series en la BD. Intentando cargar CSV...");
            ConexionBD.cargarSeriesDesdeCSV();
            
            items = ConexionBD.cargarContenido();       

            haySeries = false;
            for (MediaItem item : items) if (item instanceof Serie) haySeries = true;

            if (!haySeries) {
                System.out.println("⚠️ Falló la carga del CSV. Generando series de respaldo...");
                generarSeriesDeRespaldo();
                items = ConexionBD.cargarContenido();
            }
        }
       
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
        
        //pequeña ayuda de Gemini para poner fondo degradado
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
        

        for (Genero g : Genero.values()) map.put(g, new ArrayList<>());

        for (MediaItem item : items) {
            if (item.getTipo().equals(tipo)) {
                if (item.getGenero() != null) {
                    map.get(item.getGenero()).add(item);
                }
            }
        }
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
    public ArrayList<MediaItem> buscarPorTitulo(String texto) {
        return buscarRecursivo(items, texto.toLowerCase(), 0);
    }

    
     // Método PRIVADO recursivo.
     
    private ArrayList<MediaItem> buscarRecursivo(ArrayList<MediaItem> lista, String texto, int indice) {
        
        // --- 1. CASO BASE ---
        if (indice >= lista.size()) {
            return new ArrayList<>();
        }

        // --- 2. CASO RECURSIVO ---
        ArrayList<MediaItem> resultadosDelResto = buscarRecursivo(lista, texto, indice + 1);

        // --- 3. PROCESO ---
        MediaItem itemActual = lista.get(indice);
        if (itemActual.getTitulo().toLowerCase().contains(texto)) {
            resultadosDelResto.add(0, itemActual);
        }

        return resultadosDelResto;
    }
}