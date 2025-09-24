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
    private final int maxCards = 10;

    public MediaPanelFilas(List<MediaItem> items) {
        this.items = items;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 20));

        cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        cardsPanel.setBackground(new Color(20, 20, 20));

        btnPrev = crearBoton("<");
        btnNext = crearBoton(">");

        btnPrev.setVisible(false);
        btnPrev.addActionListener(e -> moverIzq());
        btnNext.addActionListener(e -> moverDer());

        add(btnPrev, BorderLayout.WEST);
        add(cardsPanel, BorderLayout.CENTER);
        add(btnNext, BorderLayout.EAST);

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
        return btn;
    }

    private void actualizarTarjetas() {
        cardsPanel.removeAll();
        int endIdx = Math.min(startIdx + maxCards, items.size());
        for (int i = startIdx; i < endIdx; i++) {
            cardsPanel.add(MediaPanelTarjetas.createCard(items.get(i)));
        }
        btnPrev.setVisible(startIdx > 0);
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