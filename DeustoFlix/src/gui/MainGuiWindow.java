package gui;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainGuiWindow extends JFrame {

	private final JPanel contentPanel;
    private final JButton btnPeliculas, btnSeries, btnInicio;
    private final MediaRepository repo;
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainGuiWindow() {
        setTitle("DeustoFlix");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navBar.setBackground(new Color(30, 30, 30));

        btnInicio = crearBotonNav("Inicio");
        btnPeliculas = crearBotonNav("Películas");
        btnSeries = crearBotonNav("Series");

        navBar.add(btnInicio);
        navBar.add(btnPeliculas);
        navBar.add(btnSeries);
        
        add(navBar, BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        repo = new MediaRepository();

        btnInicio.addActionListener(e -> mostrarVistaPrincipal());
        btnPeliculas.addActionListener(e -> mostrarPorTipo("Pelicula"));
        btnSeries.addActionListener(e -> mostrarPorTipo("Serie"));

        mostrarVistaPrincipal();
	}

        private JButton crearBotonNav(String text) {
            JButton btn = new JButton(text);
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(50, 50, 50));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            return btn;
        }

        // Vista principal: mezcla por categoría
        private void mostrarVistaPrincipal() {
            contentPanel.removeAll();
            Map<String, ArrayList<MediaItem>> categorias = repo.agruparPorCategoria(repo.getAll());
            for (String categoria : categorias.keySet()) {
                JLabel lbl = new JLabel("  " + categoria);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
                contentPanel.add(lbl);
                contentPanel.add(new MediaPanelFilas(categorias.get(categoria)));
                contentPanel.add(Box.createVerticalStrut(20));
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        // Por tipo, agrupado por género
        private void mostrarPorTipo(String tipo) {
            contentPanel.removeAll();
            Map<Genero, ArrayList<MediaItem>> generos = repo.agruparPorGenero(tipo);
            for (Genero genero : generos.keySet()) {
                if (generos.get(genero).isEmpty()) continue;
                JLabel lbl = new JLabel("  " + genero.name());
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
                contentPanel.add(lbl);
                contentPanel.add(new MediaPanelFilas(generos.get(genero)));
                contentPanel.add(Box.createVerticalStrut(15));
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new MainGuiWindow().setVisible(true));
        }
}