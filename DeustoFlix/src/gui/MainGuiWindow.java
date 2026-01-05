package gui;

import domain.*;
import databases.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class MainGuiWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPanel;
    private MediaRepository repo;
    private final String usuario;
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
        this.avatar = avatar;

        setTitle("DeustoFlix");
        
        // --- TAMAÑO GRANDE ---
        setSize(1600, 1100); 
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        repo = new MediaRepository();

        // --- BARRA SUPERIOR (NAVBAR) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(20, 20, 20));
        topBar.setPreferredSize(new Dimension(getWidth(), 60));

        Dimension tamañoBoton = new Dimension(160, 40);
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
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        if (avatar != null) {
            userLabel.setIcon(escalarIcono(avatar, 40, 40));
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
            mostrarInicio(null);
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

        // Carga inicial
        mostrarInicio(null);
        
        actualizarSeleccionBoton(btnInicio, todosLosBotones);
    }

    // ================================================================
    //  INICIO CON BUSCADOR + RECOMENDACIONES
    // ================================================================
    private void mostrarInicio(String terminoBusqueda) {
        contentPanel.removeAll();
        
        // Panel del Buscador
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchContainer.setBackground(Color.BLACK);
        searchContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 

        JLabel lblIcon = new JLabel("🔍");
        lblIcon.setForeground(Color.GRAY);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        
        JTextField txtBuscar = new JTextField(50); 
        txtBuscar.setPreferredSize(new Dimension(600, 40)); 
        txtBuscar.setBackground(new Color(50, 50, 50));
        txtBuscar.setForeground(Color.WHITE);
        txtBuscar.setCaretColor(Color.WHITE);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80,80,80)), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10))
        );
        
        if (terminoBusqueda != null) {
            txtBuscar.setText(terminoBusqueda);
        }

        JButton btnBuscar = new JButton("BUSCAR");
        estilizarBotonPequeño(btnBuscar);
        btnBuscar.setPreferredSize(new Dimension(120, 40));

        btnBuscar.addActionListener(e -> mostrarInicio(txtBuscar.getText().trim()));
        txtBuscar.addActionListener(e -> mostrarInicio(txtBuscar.getText().trim()));

        searchContainer.add(lblIcon);
        searchContainer.add(txtBuscar);
        searchContainer.add(btnBuscar);

        contentPanel.add(searchContainer);


        // --- LÓGICA DE CONTENIDO ---
        if (terminoBusqueda == null || terminoBusqueda.isEmpty()) {
            // MOSTRAR TODO (POR CATEGORÍAS)
            ArrayList<MediaItem> lista = repo.getAll();
            Map<String, ArrayList<MediaItem>> cats = repo.agruparPorCategoria(lista);

            for (String cat : cats.keySet()) {
                JLabel lbl = crearTitulo(cat);
                contentPanel.add(lbl);
                contentPanel.add(new MediaPanelFilas(cats.get(cat)));
            }
        } else {
            // MOSTRAR RESULTADOS DE BÚSQUEDA
            ArrayList<MediaItem> resultados = repo.buscarPorTitulo(terminoBusqueda);
            
            JLabel lblResultados = crearTitulo("Resultados para: \"" + terminoBusqueda + "\"");
            lblResultados.setForeground(new Color(255, 0, 0));
            contentPanel.add(lblResultados);
            

            if (!resultados.isEmpty()) {
                MediaPanelFilas panelResultados = new MediaPanelFilas(resultados);
                contentPanel.add(panelResultados);
                
                // --- NUEVO: RECOMENDACIONES BASADAS EN LA BÚSQUEDA ---
                List<MediaItem> recomendados = obtenerRecomendaciones(resultados);
                
                if (!recomendados.isEmpty()) {
                    // Separador visual o espacio
                    contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
                    
                    JLabel lblRecomendados = crearTitulo("Porque buscaste esto (Recomendados):");
                    lblRecomendados.setForeground(new Color(255, 0, 0)); // Un poco más gris para diferenciar
                    contentPanel.add(lblRecomendados);
                    contentPanel.add(new MediaPanelFilas(recomendados));
                }
                // -----------------------------------------------------

            } else {
                JLabel lblVacio = new JLabel("No se encontraron coincidencias.");
                lblVacio.setForeground(Color.GRAY);
                lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 18));
                lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblVacio.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
                contentPanel.add(lblVacio);
            }
        }
        
        refrescar();
    }
    
    /**
     * Método inteligente que busca películas del mismo género que los resultados encontrados.
     */
    private List<MediaItem> obtenerRecomendaciones(List<MediaItem> resultadosBusqueda) {
        List<MediaItem> recomendados = new ArrayList<>();
        List<MediaItem> todoElCatalogo = repo.getAll();
        
        // 1. Identificar qué géneros hemos encontrado
        Set<Genero> generosEncontrados = new HashSet<>();
        for (MediaItem item : resultadosBusqueda) {
            if (item.getGenero() != null) {
                generosEncontrados.add(item.getGenero());
            }
        }
        
        // 2. Buscar en el catálogo cosas que coincidan con esos géneros
        for (MediaItem item : todoElCatalogo) {
            // Si el género coincide Y NO está ya en la lista de resultados (para no repetir)
            if (generosEncontrados.contains(item.getGenero()) && !resultadosBusqueda.contains(item)) {
                recomendados.add(item);
            }
        }
        
        // 3. Mezclar para variedad y limitar a 15
        Collections.shuffle(recomendados);
        if (recomendados.size() > 15) {
            return recomendados.subList(0, 15);
        }
        
        return recomendados;
    }


    // ================================================================
    //  RESTO DEL CÓDIGO (SIN CAMBIOS)
    // ================================================================

    private void estilizarBotonPequeño(JButton b) {
        b.setBackground(new Color(229, 9, 20));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorder(BorderFactory.createEmptyBorder());
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

        tabla.getTableHeader().setBackground(new Color(229, 9, 20));
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

    private void logicaCambiarFoto() {
        JDialog dialog = new JDialog(this, "Elige un nuevo avatar", true);
        dialog.setSize(500, 200); 
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panelAvatares = new JPanel(new GridLayout(1, 4, 10, 10));
        panelAvatares.setBackground(new Color(30, 30, 30));
        panelAvatares.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] opciones = {"perfil1.png", "perfil2.png", "perfil3.png", "perfil4.png"};

        for (String nombreArchivo : opciones) {
            ImageIcon icon = null;
            java.net.URL url = getClass().getClassLoader().getResource("resources/" + nombreArchivo);
            
            if (url == null) {
                try {
                     url = new java.io.File("src/resources/" + nombreArchivo).toURI().toURL();
                } catch (Exception ex) { }
            }

            if (url != null) {
                icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
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
            
            final ImageIcon iconoFinal = icon;
            btnAvatar.addActionListener(e -> {
                boolean exito = ConexionBD.actualizarFotoUsuario(this.usuario, nombreArchivo);
                if (exito) {
                    if (iconoFinal != null) {
                        Image imgPeque = iconoFinal.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
                        userLabel.setIcon(new ImageIcon(imgPeque));
                        userLabel.revalidate(); 
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
        lbl.setHorizontalAlignment(SwingConstants.LEFT); 
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 40, 5, 0)); 
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
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