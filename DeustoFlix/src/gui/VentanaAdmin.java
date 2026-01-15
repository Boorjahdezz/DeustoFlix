package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import databases.ConexionBD;


public class VentanaAdmin extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    // Colores
    private final Color BG_DARK = new Color(20, 20, 25);
    private final Color BG_PANEL = new Color(30, 30, 35);
    private final Color ACCENT_RED = new Color(229, 9, 20);
    private final Color TEXT_WHITE = Color.WHITE;
    private final Color TEXT_GRAY = new Color(180, 180, 180);
    
    private JTable tablaUsuarios;
    private JTable tablaContenido;
    private DefaultTableModel modeloUsuarios;
    private DefaultTableModel modeloContenido;
    
    public VentanaAdmin() {
        setTitle("DeustoFlix - Panel Administrador");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);
        
        // Barra Superior
        JPanel topBar = crearBarraSuperior();
        add(topBar, BorderLayout.NORTH);
        
        // Panel con Pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_DARK);
        tabbedPane.setForeground(TEXT_WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Pestaña Gestión de Usuarios
        JPanel panelUsuarios = crearPanelGestionUsuarios();
        tabbedPane.addTab("👥 Gestión de Usuarios", panelUsuarios);
        
        // Pestaña Gestión de Contenido
        JPanel panelContenido = crearPanelGestionContenido();
        tabbedPane.addTab("🎬 Gestión de Contenido", panelContenido);
        
        // Pestaña Estadísticas
        JPanel panelEstadisticas = crearPanelEstadisticas();
        tabbedPane.addTab("📊 Estadísticas", panelEstadisticas);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Cargar datos iniciales
        refrescarTablaUsuarios();
        refrescarTablaContenido();
    }
    
    private JPanel crearBarraSuperior() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG_DARK);
        topBar.setPreferredSize(new Dimension(getWidth(), 70));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_RED));
        
        // Logo
        JLabel lblLogo = new JLabel("DEUSTOFLIX ADMIN");
        lblLogo.setFont(new Font("Arial Black", Font.BOLD, 24));
        lblLogo.setForeground(ACCENT_RED);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        // Botón cerrar sesión
        JButton btnCerrarSesion = new JButton("🚪 Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(50, 50, 55));
        btnCerrarSesion.setForeground(TEXT_WHITE);
        btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 12));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> {
            new VentanaInicio().setVisible(true);
            dispose();
        });
        
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerecha.setOpaque(false);
        panelDerecha.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        panelDerecha.add(btnCerrarSesion);
        
        topBar.add(lblLogo, BorderLayout.WEST);
        topBar.add(panelDerecha, BorderLayout.EAST);
        
        return topBar;
    }
    
    // PANEL GESTIÓN DE USUARIOS
    
    private JPanel crearPanelGestionUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("Gestión de Usuarios");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(TEXT_WHITE);
        
        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(BG_DARK);
        
        JButton btnRefrescar = crearBotonEstilizado("🔄 Refrescar", false);
        JButton btnEliminar = crearBotonEstilizado("🗑️ Eliminar Usuario", true);
        JButton btnBuscar = crearBotonEstilizado("🔍 Buscar", false);
        
        btnRefrescar.addActionListener(e -> refrescarTablaUsuarios());
        btnEliminar.addActionListener(e -> eliminarUsuarioSeleccionado());
        btnBuscar.addActionListener(e -> buscarUsuarios());
        
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(BG_DARK);
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        
        // Tabla de Usuarios
        String[] nombreColumnas = {"ID", "Usuario", "Email", "Avatar"};
        modeloUsuarios = new DefaultTableModel(nombreColumnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaUsuarios = crearTablaEstilizada(modeloUsuarios);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 55)));
        scrollPane.getViewport().setBackground(BG_PANEL);
        
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refrescarTablaUsuarios() {
        modeloUsuarios.setRowCount(0);
        ArrayList<String[]> usuarios = ConexionBD.obtenerTodosUsuarios();
        for (String[] usuario : usuarios) {
            modeloUsuarios.addRow(usuario);
        }
    }
    
    private void eliminarUsuarioSeleccionado() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un usuario para eliminar", "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreUsuario = (String) tablaUsuarios.getValueAt(filaSeleccionada, 1);
        
        int confirmar = JOptionPane.showConfirmDialog(this, 
            "¿Eliminar usuario '" + nombreUsuario + "'?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            if (ConexionBD.eliminarUsuario(nombreUsuario)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTablaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscarUsuarios() {
        String terminoBusqueda = JOptionPane.showInputDialog(this, "Ingrese nombre de usuario a buscar:", "Buscar Usuarios", JOptionPane.QUESTION_MESSAGE);
        if (terminoBusqueda != null && !terminoBusqueda.trim().isEmpty()) {
            ArrayList<String[]> todosUsuarios = ConexionBD.obtenerTodosUsuarios();
            modeloUsuarios.setRowCount(0);
            for (String[] usuario : todosUsuarios) {
                if (usuario[1].toLowerCase().contains(terminoBusqueda.toLowerCase())) {
                    modeloUsuarios.addRow(usuario);
                }
            }
        }
    }
    
    // PANEL GESTIÓN DE CONTENIDO
    
    private JPanel crearPanelGestionContenido() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("Gestión de Contenido");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(TEXT_WHITE);
        
        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(BG_DARK);
        
        JButton btnRefrescar = crearBotonEstilizado("🔄 Refrescar", false);
        JButton btnAgregarPelicula = crearBotonEstilizado("➕ Agregar Película", false);
        JButton btnAgregarSerie = crearBotonEstilizado("➕ Agregar Serie", false);
        JButton btnEliminar = crearBotonEstilizado("🗑️ Eliminar", true);
        
        btnRefrescar.addActionListener(e -> refrescarTablaContenido());
        btnAgregarPelicula.addActionListener(e -> new FormAgregarPelicula(this).setVisible(true));
        btnAgregarSerie.addActionListener(e -> new FormAgregarSerie(this).setVisible(true));
        btnEliminar.addActionListener(e -> eliminarContenidoSeleccionado());
        
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnAgregarPelicula);
        panelBotones.add(btnAgregarSerie);
        panelBotones.add(btnEliminar);
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(BG_DARK);
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        
        // Tabla de Contenido
        String[] nombreColumnas = {"ID", "Título", "Tipo", "Género", "Categoría", "Duración", "Valoración"};
        modeloContenido = new DefaultTableModel(nombreColumnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaContenido = crearTablaEstilizada(modeloContenido);
        JScrollPane scrollPane = new JScrollPane(tablaContenido);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 55)));
        scrollPane.getViewport().setBackground(BG_PANEL);
        
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void refrescarTablaContenido() {
        modeloContenido.setRowCount(0);
        ArrayList<String[]> contenido = ConexionBD.obtenerTodoContenidoConID();
        for (String[] item : contenido) {
            modeloContenido.addRow(item);
        }
    }
    
    private void eliminarContenidoSeleccionado() {
        int filaSeleccionada = tablaContenido.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona contenido para eliminar", "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) tablaContenido.getValueAt(filaSeleccionada, 0);
        String titulo = (String) tablaContenido.getValueAt(filaSeleccionada, 1);
        
        int confirmar = JOptionPane.showConfirmDialog(this, 
            "¿Eliminar contenido '" + titulo + "'?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            if (ConexionBD.eliminarContenido(Integer.parseInt(id))) {
                JOptionPane.showMessageDialog(this, "Contenido eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTablaContenido();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar contenido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // PANEL ESTADÍSTICAS
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        ArrayList<String[]> usuarios = ConexionBD.obtenerTodosUsuarios();
        ArrayList<String[]> contenido = ConexionBD.obtenerTodoContenidoConID();
        
        int totalUsuarios = usuarios.size();
        int totalContenido = contenido.size();
        int totalPeliculas = 0;
        int totalSeries = 0;
        
        for (String[] item : contenido) {
            if ("Pelicula".equals(item[2])) totalPeliculas++;
            else totalSeries++;
        }
        
        panel.add(crearTarjetaEstadistica("Total Usuarios", String.valueOf(totalUsuarios), "👥"));
        panel.add(crearTarjetaEstadistica("Total Contenido", String.valueOf(totalContenido), "🎬"));
        panel.add(crearTarjetaEstadistica("Películas", String.valueOf(totalPeliculas), "🎥"));
        panel.add(crearTarjetaEstadistica("Series", String.valueOf(totalSeries), "📺"));
        
        return panel;
    }
    
    private JPanel crearTarjetaEstadistica(String titulo, String valor, String icono) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(BG_PANEL);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 55), 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 48));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(TEXT_GRAY);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 36));
        lblValor.setForeground(ACCENT_RED);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel panelCentro = new JPanel(new GridLayout(3, 1, 0, 10));
        panelCentro.setOpaque(false);
        panelCentro.add(lblIcono);
        panelCentro.add(lblValor);
        panelCentro.add(lblTitulo);
        
        tarjeta.add(panelCentro, BorderLayout.CENTER);
        
        return tarjeta;
    }
    
    // MÉTODOS AUXILIARES
    
    private JButton crearBotonEstilizado(String texto, boolean esPeligro) {
        JButton btn = new JButton(texto);
        btn.setBackground(esPeligro ? new Color(200, 40, 40) : new Color(70, 70, 75));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(esPeligro ? new Color(220, 50, 50) : new Color(90, 90, 95));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(esPeligro ? new Color(200, 40, 40) : new Color(70, 70, 75));
            }
        });
        
        return btn;
    }
    
    private JTable crearTablaEstilizada(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setBackground(BG_PANEL);
        tabla.setForeground(TEXT_WHITE);
        tabla.setSelectionBackground(ACCENT_RED);
        tabla.setSelectionForeground(TEXT_WHITE);
        tabla.setShowGrid(true);
        tabla.setGridColor(new Color(50, 50, 55));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        
        tabla.getTableHeader().setBackground(new Color(40, 40, 45));
        tabla.getTableHeader().setForeground(TEXT_WHITE);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        return tabla;
    }
}