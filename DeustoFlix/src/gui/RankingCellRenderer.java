package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer mejorado para tabla de ranking.
 * - Filas alternadas
 * - Borde inferior en cada celda
 * - Selección más visible
 * - Texto centrado y con padding
 * - Colores estilo moderno oscuro
 */
public class RankingCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        JLabel lbl = new JLabel(value == null ? "" : value.toString());
        lbl.setOpaque(true);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        // Colores alternados por fila
        if (!isSelected) {
            if (row % 2 == 0) {
                lbl.setBackground(new Color(40, 40, 40));  // fila par
            } else {
                lbl.setBackground(new Color(55, 55, 55));  // fila impar
            }
            lbl.setForeground(Color.WHITE);
        } else {
            // Color de selección
            lbl.setBackground(new Color(0, 120, 215)); // azul Netflix/Windows
            lbl.setForeground(Color.WHITE);
        }

        // Bordes y padding
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 80)), // línea inferior
                BorderFactory.createEmptyBorder(6, 8, 6, 8)  // padding
        ));

        // Destacar los primeros 3 puestos
        if (!isSelected && column == 0) {  // suponiendo que la primera columna es el ranking
            int puesto = 0;
            try {
                puesto = Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {}
            
            switch (puesto) {
                case 1 -> lbl.setForeground(new Color(255, 215, 0));  // Oro
                case 2 -> lbl.setForeground(new Color(192, 192, 192)); // Plata
                case 3 -> lbl.setForeground(new Color(205, 127, 50));  // Bronce
            }
        }

        return lbl;
    }
}
