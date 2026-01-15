package gui;

import domain.MediaItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MediaPanelTarjetas {
    
    public static JPanel createCard(MediaItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(170, 230));
        card.setBackground(new Color(40, 40, 40));
        card.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2, true));

        // --- FUNCIONALIDAD DE CLIC ---
     
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

       
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            
                new VentanaDetalleMultimedia(item).setVisible(true);
            }

        
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(60, 60, 60)); // Más claro
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(40, 40, 40)); // Color original
            }
        });
        // -----------------------------

        JLabel imgLabel = new JLabel(item.getImagen());
        imgLabel.setPreferredSize(new Dimension(170, 120));
        card.add(imgLabel, BorderLayout.NORTH);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
       
        textPanel.setOpaque(false); 
        
        JLabel title = new JLabel(item.getTitulo());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        textPanel.add(title);

        JLabel desc = new JLabel("<html><font color='#CCCCCC'>" + item.getDescripcion() + "</font></html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        textPanel.add(desc);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
}