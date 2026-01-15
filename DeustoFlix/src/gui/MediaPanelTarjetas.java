package gui;

import domain.MediaItem;
import databases.ConexionBD;
import gui.avatar.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MediaPanelTarjetas {
    
    public static JPanel createCard(MediaItem item) {
        // Usamos BorderLayout para la tarjeta completa
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(170, 240)); // Aumentado ligeramente para el pie
        card.setBackground(new Color(40, 40, 40));
        card.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2, true));

        // --- FUNCIONALIDAD DE CLIC (ABRIR DETALLE) ---
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Solo abre detalle si NO se pulsó el corazón
                // (Aunque el botón captura su propio evento, esto es seguridad)
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

        // 1. IMAGEN (Parte Superior)
        JLabel imgLabel = new JLabel(item.getImagen());
        imgLabel.setPreferredSize(new Dimension(170, 120));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imgLabel, BorderLayout.NORTH);

        // 2. PANEL DE TEXTO (Centro)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        
        JLabel title = new JLabel(item.getTitulo());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        textPanel.add(title);

        // Descripción corta truncada
        String descText = item.getDescripcion();
        if (descText.length() > 40) descText = descText.substring(0, 40) + "...";
        JLabel desc = new JLabel("<html><font color='#CCCCCC'>" + descText + "</font></html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 10));
        textPanel.add(desc);

        card.add(textPanel, BorderLayout.CENTER);

        // 3. PIE DE TARJETA CON CORAZÓN (Parte Inferior)
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(30, 30, 30));
        footerPanel.setBorder(new EmptyBorder(2, 5, 2, 5));

        JButton btnFav = new JButton("❤");
        btnFav.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnFav.setBorderPainted(false);
        btnFav.setContentAreaFilled(false);
        btnFav.setFocusPainted(false);
        btnFav.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // LÓGICA DE FAVORITOS
        String usuarioActual = UserSession.getUsuario();
        // Verificamos estado inicial
        if (ConexionBD.esFavorito(usuarioActual, item.getId())) {
            btnFav.setForeground(Color.RED);
        } else {
            btnFav.setForeground(Color.GRAY);
        }

        // Acción del botón
        btnFav.addActionListener(e -> {
            ConexionBD.toggleFavorito(usuarioActual, item.getId());
            boolean ahoraEsFav = ConexionBD.esFavorito(usuarioActual, item.getId());
            btnFav.setForeground(ahoraEsFav ? Color.RED : Color.GRAY);
            
            // Forzar repintado por si acaso
            btnFav.repaint();
        });

        footerPanel.add(new JLabel(" "), BorderLayout.CENTER); // Relleno
        footerPanel.add(btnFav, BorderLayout.EAST);
        
        card.add(footerPanel, BorderLayout.SOUTH);

        return card;
    }
}