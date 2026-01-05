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
        
        // 1. AJUSTE: Altura máxima fija para evitar que se estiren verticalmente
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        setBackground(new Color(20, 20, 20));

        // 2. CAMBIO DE LAYOUT: Usamos GridBagLayout para pegar todo a la izquierda
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
        
        // Columna 0: Botón Anterior
        gbc.gridx = 0;
        gbc.weightx = 0; // No se estira
        add(btnPrev, gbc);

        // Columna 1: Panel de Cartas
        gbc.gridx = 1;
        gbc.weightx = 0; // El panel ocupa solo lo que necesitan las cartas
        add(cardsPanel, gbc);

        // Columna 2: Botón Siguiente (PEGADO A LAS CARTAS)
        gbc.gridx = 2;
        gbc.weightx = 0;
        add(btnNext, gbc);

        // Columna 3: Relleno Invisible (Empuja todo lo anterior a la izquierda)
        gbc.gridx = 3;
        gbc.weightx = 1.0; // Este componente absorbe todo el espacio sobrante
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
        btn.setPreferredSize(new Dimension(35, 120)); // Altura del botón
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
        // O si quieres que se vea siempre pero deshabilitado, cambia lógica. 
        // Aquí lo ocultamos para que no haya flecha si no hay nada más.
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