package gui;

import domain.MediaItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MediaPanelFilas extends JPanel {
    private static final long serialVersionUID = 1L;
    private int startIdx = 0;
    private final List<MediaItem> items;
    private final JPanel cardsPanel;
    private final JButton btnPrev, btnNext;
    private final int maxCards = 8; // Número de tarjetas visibles por página

    public MediaPanelFilas(List<MediaItem> items) {
        this.items = items;
        

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        setBackground(new Color(20, 20, 20));


        setLayout(new GridBagLayout());

        cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        cardsPanel.setBackground(new Color(20, 20, 20));

        btnPrev = crearBoton("<");
        btnNext = crearBoton(">");

        btnPrev.setVisible(false);
        btnPrev.addActionListener(e -> moverIzq());
        btnNext.addActionListener(e -> moverDer());

        // --- CONFIGURACIÓN DE POSICIONES (GridBag) ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER; // Centrado verticalmente
        
       
        gbc.gridx = 0;
        gbc.weightx = 0; 
        add(btnPrev, gbc);

  
        gbc.gridx = 1;
        gbc.weightx = 0;
        add(cardsPanel, gbc);

    
        gbc.gridx = 2;
        gbc.weightx = 0;
        add(btnNext, gbc);


        gbc.gridx = 3;
        gbc.weightx = 1.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        add(filler, gbc);

        actualizarTarjetas();
    }

    private JButton crearBoton(String txt) {
        JButton btn = new JButton(txt);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setFocusable(false);
        btn.setPreferredSize(new Dimension(35, 120));
        btn.setBackground(new Color(50,50,50));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder());
        
        // Efecto hover simple
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(80, 80, 80));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(50, 50, 50));
            }
        });
        
        return btn;
    }

    private void actualizarTarjetas() {
        cardsPanel.removeAll();
        int endIdx = Math.min(startIdx + maxCards, items.size());
        
        // Añadimos las tarjetas al panel
        for (int i = startIdx; i < endIdx; i++) {
            cardsPanel.add(MediaPanelTarjetas.createCard(items.get(i)));
        }
        
        // Control de visibilidad de botones
        btnPrev.setVisible(startIdx > 0);
        
        // Importante: Si no hay más items después, ocultamos el botón siguiente
        btnNext.setVisible(endIdx < items.size());

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void moverIzq() {
        startIdx = Math.max(0, startIdx - maxCards);
        actualizarTarjetas();
    }

    private void moverDer() {
        if (startIdx + maxCards < items.size())
            startIdx += maxCards;
        actualizarTarjetas();
    }
}