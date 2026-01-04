package gui;

import domain.*;
import databases.ConexionBD;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.awt.GridLayout;
import java.awt.Cursor; // Importante para el cursor de mano

public class MainGuiWindow extends JFrame {

    private JPanel contentPanel;
    private MediaRepository repo;
    private final String usuario;
    // userLabel global para poder actualizar el icono al cambiar foto
    private JLabel userLabel; 
    private final ImageIcon avatar;

    // --- COLORES ---
    private final Color COLOR_NORMAL = new Color(30, 30, 30);
    private final Color COLOR_ACTIVO = new Color(80, 80, 80);

    public MainGuiWindow() {
        this("Invitado", null);
    }

    public MainGuiWindow(String usuario, ImageIcon avatar) {
        this.usuario = usuario;
        this.avatar = avatar; // Guardamos el avatar inicial

        setTitle("DeustoFlix");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        repo = new MediaRepository();

        // --- BARRA SUPERIOR ---
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

        // --- PANEL DE USUARIO ---
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        userPanel.setOpaque(false);
        
        userLabel = new JLabel(usuario);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        // Si hay avatar inicial, lo ponemos
        if (avatar != null) {
            userLabel.setIcon(escalarIcono(avatar, 36, 36));
            userLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        }

        // --- MENÚ DESPLEGABLE ---
        JPopupMenu userMenu = new JPopupMenu();
        userMenu.setBackground(Color.BLACK);
        userMenu.setBorder(BorderFactory.createLineBorder(new Color(60,60,60)));

        JMenuItem itemEditar = new JMenuItem("Editar datos");
        estilizarItemMenu(itemEditar);
        
        JMenuItem itemFoto = new JMenuItem("Cambiar foto de perfil");
        estilizarItemMenu(itemFoto);

        JMenuItem itemCerrar = new JMenuItem("Cerrar sesión");
        estilizarItemMenu(itemCerrar);
        
        JMenuItem itemEliminar = new JMenuItem("Eliminar cuenta");
        estilizarItemMenu(itemEliminar);

        // Acciones
        itemEditar.addActionListener(e -> logicaEditarDatos());
        itemFoto.addActionListener(e -> logicaCambiarFoto()); 
        
        itemCerrar.addActionListener(e -> {
            new VentanaInicio().setVisible(true);
            dispose();
        });
        
        itemEliminar.addActionListener(e -> logicaEliminarCuenta());

        userMenu.add(itemEditar);
        userMenu.add(itemFoto);
        userMenu.add(new JSeparator());
        userMenu.add(itemCerrar);
        userMenu.add(new JSeparator());
        userMenu.add(itemEliminar);

        userLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userMenu.show(userLabel, 0, userLabel.getHeight());
            }
        });

        userPanel.add(userLabel);
        topBar.add(navPanel, BorderLayout.WEST);
        topBar.add(userPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.BLACK);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);

        // --- GESTIÓN DE BOTONES ACTIVOS ---
        JButton[] todosLosBotones = {btnInicio, btnPeliculas, btnSeries, btnRanking};

        btnInicio.addActionListener(e -> {
            mostrarInicio();
            actualizarSeleccionBoton(btnInicio, todosLosBotones);
        });
        btnPeliculas.addActionListener(e -> {
            mostrarPeliculas();
            actualizarSeleccionBoton(btnPeliculas, todosLosBotones);
        });
        btnSeries.addActionListener(e -> {
            mostrarSeries();
            actualizarSeleccionBoton(btnSeries, todosLosBotones);
        });
        btnRanking.addActionListener(e -> {
            mostrarRanking();
            actualizarSeleccionBoton(btnRanking, todosLosBotones);
        });

        mostrarInicio();
        actualizarSeleccionBoton(btnInicio, todosLosBotones);
    }

    // ================================================================
    // NUEVA LÓGICA: CAMBIAR FOTO DE PERFIL (CORREGIDA)
    // ================================================================
    private void logicaCambiarFoto() {
        JDialog dialog = new JDialog(this, "Elige un nuevo avatar", true);
        dialog.setSize(500, 200); // Un poco más ancha para que quepan bien
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panelAvatares = new JPanel(new GridLayout(1, 4, 10, 10));
        panelAvatares.setBackground(new Color(30, 30, 30));
        panelAvatares.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // TUS NOMBRES REALES DE ARCHIVO (según tu carpeta resources)
        String[] opciones = {"perfil1.png", "perfil2.png", "perfil3.png", "perfil4.png"};

        for (String nombreArchivo : opciones) {
            // Cargar imagen: Buscamos en la carpeta 'resources'
            ImageIcon icon = null;
            
            // Intento 1: Carga directa desde classpath (si resources es source folder)
            java.net.URL url = getClass().getClassLoader().getResource("resources/" + nombreArchivo);
            
            // Intento 2: Carga fallback por si 'resources' está en la raíz pero no es source folder
            if (url == null) {
                try {
                     url = new java.io.File("src/resources/" + nombreArchivo).toURI().toURL();
                } catch (Exception ex) { }
            }

            if (url != null) {
                icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            } else {
                System.err.println("No se pudo cargar: " + nombreArchivo);
            }
            
            JButton btnAvatar = new JButton();
            btnAvatar.setBackground(new Color(50, 50, 50));
            btnAvatar.setFocusPainted(false);
            
            if (icon != null) {
                btnAvatar.setIcon(icon);
            } else {
                btnAvatar.setText("?"); 
                btnAvatar.setForeground(Color.WHITE);
            }
            
            // Acción al hacer clic
            final ImageIcon iconoFinal = icon;
            btnAvatar.addActionListener(e -> {
                boolean exito = ConexionBD.actualizarFotoUsuario(this.usuario, nombreArchivo);
                if (exito) {
                    if (iconoFinal != null) {
                        Image imgPeque = iconoFinal.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
                        userLabel.setIcon(new ImageIcon(imgPeque));
                        userLabel.revalidate(); // Asegurar que se repinte
                        userLabel.repaint();
                    }
                    JOptionPane.showMessageDialog(dialog, "Foto de perfil actualizada.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al guardar en base de datos.");
                }
            });
            
            panelAvatares.add(btnAvatar);
        }

        dialog.add(panelAvatares, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // ================================================================
    // RESTO DE MÉTODOS
    // ================================================================

    private void actualizarSeleccionBoton(JButton activo, JButton[] todos) {
        for (JButton btn : todos) {
            if (btn == activo) btn.setBackground(COLOR_ACTIVO);
            else btn.setBackground(COLOR_NORMAL);
        }
    }

    private void estilizarBoton(JButton b, Dimension d) {
        b.setPreferredSize(d);
        b.setBackground(COLOR_NORMAL);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private JLabel crearTitulo(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0)); 
        return lbl;
    }

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

    private void mostrarSeries() {
        contentPanel.removeAll();
        List<MediaItem> todos = repo.getAll();
        Map<Genero, List<MediaItem>> agrupado = new HashMap<>();
        for (MediaItem item : todos) {
            if (item instanceof Serie) {
                Genero g = item.getGenero();
                if (g == null) g = Genero.DRAMA;
                agrupado.putIfAbsent(g, new ArrayList<>());
                agrupado.get(g).add(item);
            }
        }
        List<Genero> generosOrdenados = new ArrayList<>(agrupado.keySet());
        Collections.sort(generosOrdenados);
        for (Genero g : generosOrdenados) {
            JLabel lbl = crearTitulo(g.name().replace("_", " "));
            contentPanel.add(lbl);
            contentPanel.add(new MediaPanelFilas(agrupado.get(g)));
        }
        if (agrupado.isEmpty()) {
            JLabel vacio = new JLabel("No se encontraron series.");
            vacio.setForeground(Color.GRAY);
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(vacio);
        }
        refrescar();
    }

    private void mostrarPeliculas() {
        contentPanel.removeAll();
        List<MediaItem> todos = repo.getAll();
        Map<Genero, List<MediaItem>> agrupado = new HashMap<>();
        for (MediaItem item : todos) {
            if (item instanceof Pelicula) {
                Genero g = item.getGenero();
                if (g == null) g = Genero.DRAMA;
                agrupado.putIfAbsent(g, new ArrayList<>());
                agrupado.get(g).add(item);
            }
        }
        List<Genero> generosOrdenados = new ArrayList<>(agrupado.keySet());
        Collections.sort(generosOrdenados);
        for (Genero g : generosOrdenados) {
            JLabel lbl = crearTitulo(g.name().replace("_", " "));
            contentPanel.add(lbl);
            contentPanel.add(new MediaPanelFilas(agrupado.get(g)));
        }
        refrescar();
    }

    // --- RANKING (CON ESTILO OSCURO) ---
    private void mostrarRanking() {
        contentPanel.removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] opciones = {"Novedades", "Top Series", "Top Películas"};
        JComboBox<String> combo = new JComboBox<>(opciones);
        combo.setBackground(new Color(30,30,30));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("SansSerif", Font.BOLD, 14));
        combo.setFocusable(false);
        
        panel.add(combo, BorderLayout.NORTH);

        RankingTableModel modelo = new RankingTableModel(new ArrayList<>());
        JTable tabla = new JTable(modelo);
        
        tabla.setRowHeight(35);
        tabla.setBackground(new Color(20, 20, 20));
        tabla.setForeground(Color.WHITE);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setFillsViewportHeight(true);
        tabla.setDefaultRenderer(Object.class, new RankingCellRenderer());

        tabla.getTableHeader().setBackground(new Color(45, 45, 45));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());
        scrollTabla.getViewport().setBackground(new Color(20, 20, 20));
        
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        combo.addActionListener(e -> actualizarRanking(combo, modelo));
        contentPanel.add(panel);
        
        actualizarRanking(combo, modelo);
        refrescar();
    }

    private void actualizarRanking(JComboBox<String> combo, RankingTableModel modelo) {
        String sel = combo.getSelectedItem().toString();
        List<MediaItem> lista;
        if (sel.contains("Series")) {
            lista = repo.getByTipo("Serie");
            lista.sort((a,b) -> Double.compare(((Serie)b).getValoracion(), ((Serie)a).getValoracion()));
        } else if (sel.contains("Películas")) {
            lista = repo.getByTipo("Pelicula");
            lista.sort((a,b) -> Double.compare(((Pelicula)b).getValoracion(), ((Pelicula)a).getValoracion()));
        } else {
            lista = repo.getAll();
            Collections.shuffle(lista);
        }
        if(lista.size() > 25) lista = lista.subList(0, 25);
        modelo.setDatos(lista);
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

    private void estilizarItemMenu(JMenuItem item) {
        item.setBackground(Color.BLACK);
        item.setForeground(Color.WHITE);
        item.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        item.setOpaque(true);
    }

    private void logicaEditarDatos() {
        String[] datosActuales = ConexionBD.obtenerDatosUsuario(this.usuario);
        String currentGmail = (datosActuales != null) ? datosActuales[0] : "";
        String currentPass = (datosActuales != null) ? datosActuales[1] : "";

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBackground(new Color(40,40,40)); 
        
        JLabel lblGmail = new JLabel("Gmail:");
        lblGmail.setForeground(Color.WHITE);
        JTextField txtGmail = new JTextField(currentGmail);
        
        JLabel lblPass = new JLabel("Nueva Contraseña:");
        lblPass.setForeground(Color.WHITE);
        JPasswordField txtPass = new JPasswordField(currentPass);

        panel.add(lblGmail);
        panel.add(txtGmail);
        panel.add(lblPass);
        panel.add(txtPass);

        UIManager.put("OptionPane.background", new Color(40,40,40));
        UIManager.put("Panel.background", new Color(40,40,40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Editar Datos", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String nuevoGmail = txtGmail.getText();
            String nuevaPass = new String(txtPass.getPassword());
            if (!nuevoGmail.isEmpty() && !nuevaPass.isEmpty()) {
                ConexionBD.actualizarUsuario(this.usuario, nuevoGmail, nuevaPass);
            }
        }
    }

    private void logicaEliminarCuenta() {
        UIManager.put("OptionPane.background", new Color(40,40,40));
        UIManager.put("Panel.background", new Color(40,40,40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Borrar cuenta?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (ConexionBD.eliminarUsuario(this.usuario)) mostrarProgresoYSalir();
        }
    }

    private void mostrarProgresoYSalir() {
        JDialog dialogoCarga = new JDialog(this, true);
        dialogoCarga.setSize(300, 100);
        dialogoCarga.setLocationRelativeTo(this);
        dialogoCarga.setUndecorated(true); 
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setStringPainted(true);
        panel.add(new JLabel("Borrando...", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(barra, BorderLayout.CENTER);
        dialogoCarga.add(panel);

        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    final int v = i;
                    SwingUtilities.invokeLater(() -> barra.setValue(v));
                    Thread.sleep(20); 
                }
            } catch (Exception e) {}
            SwingUtilities.invokeLater(() -> {
                dialogoCarga.dispose();
                dispose(); 
                new VentanaInicio().setVisible(true); 
            });
        }).start();
        dialogoCarga.setVisible(true); 
    }
}