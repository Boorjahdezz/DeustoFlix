package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public class MainGuiWindow extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainGuiWindow() {
        setTitle("DeustoFlix");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Barra de navegación superior
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navBar.setBackground(new Color(30, 30, 30));
        String[] navItems = {"Inicio", "Películas", "Series", "Mi Perfil"};
        for (String item : navItems) {
            JButton btn = new JButton(item);
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(50, 50, 50));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            navBar.add(btn);
        }
        add(navBar, BorderLayout.NORTH);

        // Panel de tarjetas en grid
        JPanel cardsPanel = new JPanel(new GridLayout(2, 5, 20, 20));
        cardsPanel.setBackground(new Color(20, 20, 20));
        List<CardInfo> shows = getSampleShows();

        for (CardInfo show : shows) {
            JPanel card = createShowCard(show);
            cardsPanel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Clase para almacenar la info de cada tarjeta
    private static class CardInfo {
        String title; 
        String description;
        ImageIcon image;
        CardInfo(String t, String d, ImageIcon i) { 
        	title = t;
        	description = d;
        	image = i;
        }
    }

    // Tarjetas de ejemplo
    private List<CardInfo> getSampleShows() {
        List<CardInfo> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new CardInfo(
                "Serie " + i,
                "Descripción breve de la serie " + i + ".",
                new ImageIcon(new BufferedImage(400, 200, BufferedImage.TYPE_INT_RGB))
            ));
        }
        return list;
    }

    // Método para crear una tarjeta tipo Netflix
    private JPanel createShowCard(CardInfo info) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(400, 300));
        card.setBackground(new Color(40, 40, 40));
        card.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2, true));

        // Imagen
        JLabel imgLabel = new JLabel(info.image);
        imgLabel.setPreferredSize(new Dimension(400, 200));
        card.add(imgLabel, BorderLayout.NORTH);

        // Título y descripción
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(card.getBackground());

        JLabel title = new JLabel(info.title);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        textPanel.add(title);

        JLabel desc = new JLabel("<html><font color='#CCCCCC'>" + info.description + "</font></html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textPanel.add(desc);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGuiWindow().setVisible(true);
        });
    }
}