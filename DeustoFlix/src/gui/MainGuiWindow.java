package gui;

import domain.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


import java.util.*;

public class MainGuiWindow extends JFrame {

    private JPanel contentPanel;
    private MediaRepository repo;
    private final String usuario;
    private final ImageIcon avatar;

    public MainGuiWindow() {
        this("Invitado", null);
    }

    public MainGuiWindow(String usuario, ImageIcon avatar) {
        this.usuario = usuario;
        this.avatar = avatar;

        setTitle("DeustoFlix");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        repo = new MediaRepository();

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(20, 20, 20));

        Dimension tamañoBoton = new Dimension(130, 35);

        JButton btnInicio = new JButton("Inicio");
        JButton btnPeliculas = new JButton("Películas");
        JButton btnSeries = new JButton("Series");
        JButton btnRanking = new JButton("Tabla de Ranking");

        estilizarBoton(btnInicio, tamañoBoton);
        estilizarBoton(btnPeliculas, tamañoBoton);
        estilizarBoton(btnSeries, tamañoBoton);
        estilizarBoton(btnRanking, tamañoBoton);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        navPanel.setOpaque(false);
        navPanel.add(btnInicio);
        navPanel.add(btnPeliculas);
        navPanel.add(btnSeries);
        navPanel.add(btnRanking);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel(usuario);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        if (avatar != null) {
            userLabel.setIcon(escalarIcono(avatar, 36, 36));
            userLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        }
        userPanel.add(userLabel);

        topBar.add(navPanel, BorderLayout.WEST);
        topBar.add(userPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        //panel centro
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.BLACK);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        btnInicio.addActionListener(e -> mostrarInicio());
        btnPeliculas.addActionListener(e -> mostrarPeliculas());
        btnSeries.addActionListener(e -> mostrarSeries());
        btnRanking.addActionListener(e -> mostrarRanking());

        mostrarInicio();
    }

    private void estilizarBoton(JButton b, Dimension d) {
        b.setPreferredSize(d);
        b.setBackground(new Color(30,30,30));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    //funciones para mostrar movies y series
    private void mostrarInicio() {
        contentPanel.removeAll();
        ArrayList<MediaItem> lista = repo.getAll();
        Map<String, ArrayList<MediaItem>> cats = repo.agruparPorCategoria(lista);

        for (String cat : cats.keySet()) {
            JLabel lbl = crearTitulo(cat);
            contentPanel.add(lbl);
            contentPanel.add(new MediaPanelFilas(cats.get(cat)));
        }

        refrescar();
    }

    private void mostrarPeliculas() {
        contentPanel.removeAll();
        Map<Genero, ArrayList<MediaItem>> generos = repo.agruparPorGenero("Pelicula");

        for (Genero g : generos.keySet()) {
            JLabel lbl = crearTitulo(g.name());
            contentPanel.add(lbl);
            contentPanel.add(new MediaPanelFilas(generos.get(g)));
        }

        refrescar();
    }

    private void mostrarSeries() {
        contentPanel.removeAll();
        Map<Genero, ArrayList<MediaItem>> generos = repo.agruparPorGenero("Serie");

        for (Genero g : generos.keySet()) {
            JLabel lbl = crearTitulo(g.name());
            contentPanel.add(lbl);
            contentPanel.add(new MediaPanelFilas(generos.get(g)));
        }

        refrescar();
    }

   
    private JLabel crearTitulo(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private void refrescar() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private ImageIcon escalarIcono(ImageIcon icon, int w, int h) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) return null;
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    //tabla para las mejores peliculaws  y eso
    private void mostrarRanking() {

        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder());

        String[] opciones = {
                "Novedades semanales",
                "Top 25 Series",
                "Top 25 Películas"
        };

        JComboBox<String> combo = new JComboBox<>(opciones);
        combo.setBackground(new Color(20,20,20));
        combo.setForeground(Color.WHITE);
        combo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        combo.setFocusable(false);

        panel.add(combo, BorderLayout.NORTH);

        RankingTableModel modelo = new RankingTableModel(new ArrayList<>());
        JTable tabla = new JTable(modelo);

        tabla.setRowHeight(28);
        tabla.setBackground(new Color(20,20,20));
        tabla.setForeground(Color.WHITE);
        tabla.setFillsViewportHeight(true);
        tabla.setDefaultRenderer(Object.class, new RankingCellRenderer());

        tabla.getTableHeader().setBackground(new Color(45,45,45));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBackground(Color.BLACK);
        scrollTabla.getViewport().setOpaque(true);
        scrollTabla.getViewport().setBackground(Color.BLACK);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollTabla, BorderLayout.CENTER);

        combo.addActionListener(e -> actualizarRanking(combo, modelo));

        contentPanel.add(panel);
        refrescar();
    }

    private void actualizarRanking(JComboBox<String> combo, RankingTableModel modelo) {

        String sel = combo.getSelectedItem().toString();

        if (sel.contains("Novedades")) {
            List<MediaItem> lista = repo.getAll();
            Collections.shuffle(lista);
            modelo.setDatos(lista.subList(0, 25));
        }
        else if (sel.contains("Series")) {
            List<MediaItem> lista = repo.getByTipo("Serie");
            lista.sort((a,b) -> Double.compare(((Serie)b).getValoracion(), ((Serie)a).getValoracion()));
            modelo.setDatos(lista.subList(0, 25));
        }
        else {
            List<MediaItem> lista = repo.getByTipo("Pelicula");
            lista.sort((a,b) -> Double.compare(((Pelicula)b).getValoracion(), ((Pelicula)a).getValoracion()));
            modelo.setDatos(lista.subList(0, 25));
        }
    }
}
