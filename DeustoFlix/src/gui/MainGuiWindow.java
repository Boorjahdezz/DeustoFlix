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

        // --- PANEL DE USUARIO (DERECHA) ---
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel(usuario);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        if (avatar != null) {
            userLabel.setIcon(escalarIcono(avatar, 36, 36));
            userLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        }

        // --- CREACIÓN DEL MENÚ DESPLEGABLE PERSONALIZADO ---
        JPopupMenu userMenu = new JPopupMenu();
        
        // Estilo Negro del Menú
        userMenu.setBackground(Color.BLACK);
        userMenu.setBorder(BorderFactory.createLineBorder(new Color(60,60,60)));

        JMenuItem itemEditar = new JMenuItem("Editar datos");
        estilizarItemMenu(itemEditar);

        JMenuItem itemCerrar = new JMenuItem("Cerrar sesión");
        estilizarItemMenu(itemCerrar);

        JMenuItem itemEliminar = new JMenuItem("Eliminar cuenta");
        estilizarItemMenu(itemEliminar);

        // Acciones
        itemEditar.addActionListener(e -> logicaEditarDatos());
        
        itemCerrar.addActionListener(e -> {
            new VentanaInicio().setVisible(true);
            dispose();
        });

        itemEliminar.addActionListener(e -> logicaEliminarCuenta());

        userMenu.add(itemEditar);
        // Separador oscuro si es posible, si no, añadimos un panel vacío pequeño
        JSeparator sep1 = new JSeparator(); sep1.setForeground(Color.GRAY); sep1.setBackground(Color.BLACK);
        userMenu.add(sep1);
        
        userMenu.add(itemCerrar);
        
        JSeparator sep2 = new JSeparator(); sep2.setForeground(Color.GRAY); sep2.setBackground(Color.BLACK);
        userMenu.add(sep2);

        userMenu.add(itemEliminar);

        // Evento para mostrar el menú DEBAJO de la imagen
        userLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // (0, height) hace que el menú aparezca justo debajo del componente userLabel
                userMenu.show(userLabel, 0, userLabel.getHeight());
            }
        });

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

        // Eventos navegación
        btnInicio.addActionListener(e -> mostrarInicio());
        btnPeliculas.addActionListener(e -> mostrarPeliculas());
        btnSeries.addActionListener(e -> mostrarSeries());
        btnRanking.addActionListener(e -> mostrarRanking());

        mostrarInicio();
    }

    // --- LÓGICA DE EDICIÓN DE DATOS ---
    private void logicaEditarDatos() {
        // 1. Recuperar datos actuales de la BD
        String[] datosActuales = ConexionBD.obtenerDatosUsuario(this.usuario);
        String currentGmail = (datosActuales != null) ? datosActuales[0] : "";
        String currentPass = (datosActuales != null) ? datosActuales[1] : "";

        // 2. Crear panel personalizado para el diálogo
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBackground(new Color(40,40,40)); // Fondo oscuro
        
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

        // UIManager temporal para que el JOptionPane sea oscuro
        UIManager.put("OptionPane.background", new Color(40,40,40));
        UIManager.put("Panel.background", new Color(40,40,40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Editar Datos de Usuario", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        // Restaurar UIManager (opcional, por si afecta a otros diálogos)
        // (En una app real configurarías un LookAndFeel global)

        if (result == JOptionPane.OK_OPTION) {
            String nuevoGmail = txtGmail.getText();
            String nuevaPass = new String(txtPass.getPassword());

            if (!nuevoGmail.isEmpty() && !nuevaPass.isEmpty()) {
                boolean exito = ConexionBD.actualizarUsuario(this.usuario, nuevoGmail, nuevaPass);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Los campos no pueden estar vacíos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // --- LÓGICA DE ELIMINACIÓN DE CUENTA ---
    private void logicaEliminarCuenta() {
        // Mismo estilo oscuro para el diálogo
        UIManager.put("OptionPane.background", new Color(40,40,40));
        UIManager.put("Panel.background", new Color(40,40,40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "¿Estás seguro de que quieres eliminar tu cuenta?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = ConexionBD.eliminarUsuario(this.usuario);
            if (eliminado) {
                mostrarProgresoYSalir();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarProgresoYSalir() {
        JDialog dialogoCarga = new JDialog(this, "Eliminando cuenta...", true);
        dialogoCarga.setSize(300, 100);
        dialogoCarga.setLocationRelativeTo(this);
        dialogoCarga.setLayout(new BorderLayout());
        dialogoCarga.setUndecorated(true); 

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel lblInfo = new JLabel("Borrando datos...", SwingConstants.CENTER);
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(lblInfo, BorderLayout.NORTH);

        JProgressBar barra = new JProgressBar(0, 100);
        barra.setStringPainted(true);
        barra.setBackground(new Color(50, 50, 50));
        barra.setForeground(Color.RED);
        panel.add(barra, BorderLayout.CENTER);

        dialogoCarga.add(panel);

        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    final int valor = i;
                    SwingUtilities.invokeLater(() -> barra.setValue(valor));
                    Thread.sleep(20); 
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                dialogoCarga.dispose();
                this.dispose(); 
                new VentanaInicio().setVisible(true); 
            });
        }).start();

        dialogoCarga.setVisible(true); 
    }

    // Helper para estilizar los items del menú en negro
    private void estilizarItemMenu(JMenuItem item) {
        item.setBackground(Color.BLACK);
        item.setForeground(Color.WHITE);
        item.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        item.setOpaque(true);
    }

    private void estilizarBoton(JButton b, Dimension d) {
        b.setPreferredSize(d);
        b.setBackground(new Color(30,30,30));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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

    private void mostrarRanking() {
        contentPanel.removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder());

        String[] opciones = {"Novedades semanales", "Top 25 Series", "Top 25 Películas"};
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
            modelo.setDatos(lista.subList(0, Math.min(25, lista.size())));
        } else if (sel.contains("Series")) {
            List<MediaItem> lista = repo.getByTipo("Serie");
            lista.sort((a,b) -> Double.compare(((Serie)b).getValoracion(), ((Serie)a).getValoracion()));
            modelo.setDatos(lista.subList(0, Math.min(25, lista.size())));
        } else {
            List<MediaItem> lista = repo.getByTipo("Pelicula");
            lista.sort((a,b) -> Double.compare(((Pelicula)b).getValoracion(), ((Pelicula)a).getValoracion()));
            modelo.setDatos(lista.subList(0, Math.min(25, lista.size())));
        }
    }
}