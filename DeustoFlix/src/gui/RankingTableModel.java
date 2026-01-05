package gui;

import domain.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList; // Necesario importar ArrayList
import java.util.List;

public class RankingTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private final String[] columnas = {"Título", "Género", "Categoría", "Valoración"};
    private List<MediaItem> datos;

    // --- 1. CONSTRUCTOR VACÍO (NUEVO) ---
    // Este es el que usa tu MainGuiWindow ahora
    public RankingTableModel() {
        this.datos = new ArrayList<>(); // Inicializamos lista vacía para que no de error
    }

    // --- 2. CONSTRUCTOR CON DATOS (EL QUE YA TENÍAS) ---
    public RankingTableModel(List<MediaItem> datos) {
        this.datos = datos;
    }

    // Método para actualizar los datos desde fuera (Buscador/Filtros)
    public void setDatos(List<MediaItem> nuevos) {
        this.datos = nuevos;
        fireTableDataChanged(); // Avisa a la tabla visual de que los datos han cambiado
    }
    
    // Método auxiliar para obtener el objeto al hacer doble click
    public MediaItem getItemAt(int row) {
        if (datos != null && row >= 0 && row < datos.size()) {
            return datos.get(row);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return datos != null ? datos.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (datos == null || row >= datos.size()) return "";
        
        MediaItem item = datos.get(row);

        switch (col) {
            case 0: return item.getTitulo();
            case 1: return (item.getGenero() != null) ? item.getGenero().name() : "Sin Genero";
            case 2: return (item.getCategoria() != null) ? item.getCategoria().getNombre() : "Sin Categ.";
            case 3:
                if (item instanceof Pelicula) return ((Pelicula) item).getValoracion();
                if (item instanceof Serie) return ((Serie) item).getValoracion();
                return "-";
            default:
                return "";
        }
    }
}