package gui;

import domain.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RankingTableModel extends AbstractTableModel {

    private final String[] columnas = {"Título", "Género", "Categoría", "Valoración"};
    private List<MediaItem> datos;

    public RankingTableModel(List<MediaItem> datos) {
        this.datos = datos;
    }

    public void setDatos(List<MediaItem> nuevos) {
        this.datos = nuevos;
        fireTableDataChanged();
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
        MediaItem item = datos.get(row);

        switch (col) {
            case 0: return item.getTitulo();
            case 1: return item.getGenero().name();
            case 2: return item.getCategoria().getNombre();
            case 3:
                if (item instanceof Pelicula) return ((Pelicula) item).getValoracion();
                if (item instanceof Serie) return ((Serie) item).getValoracion();
                return "-";
            default:
                return "";
        }
    }
}
