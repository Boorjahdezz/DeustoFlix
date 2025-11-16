package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class RankingCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        JLabel lbl = new JLabel(value == null ? "" : value.toString());

        lbl.setOpaque(true);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));

        
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        
        if (isSelected) {
            lbl.setBackground(new Color(70, 70, 70));
            lbl.setForeground(Color.WHITE);
        } else {
            lbl.setBackground(new Color(30, 30, 30));   // gris oscuro
            lbl.setForeground(Color.WHITE);
        }

        lbl.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        return lbl;
    }
}
