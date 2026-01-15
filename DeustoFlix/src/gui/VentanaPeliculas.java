package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class VentanaPeliculas extends JFrame {

    // Componentes principales
    private JTable tablaPeliculas;
    private DefaultTableModel modeloTabla;

    private JTextField txtTitulo;
    private JTextField txtGenero;
    private JTextField txtDuracion;
    private JTextField txtAnio;

    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JLabel lblEstado;

    public VentanaPeliculas() {

        // ================== CONFIGURACIÓN DE VENTANA ==================
        setTitle("Sistema de Películas - Java Swing");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================== MENÚ ==================
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemSalir);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de");

        itemAcerca.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Sistema de Películas\nJava Swing\n2026",
                        "Acerca de",
                        JOptionPane.INFORMATION_MESSAGE));

        menuAyuda.add(itemAcerca);

        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);

        // ================== TABLA ==================
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Título");
        modeloTabla.addColumn("Género");
        modeloTabla.addColumn("Duración (min)");
        modeloTabla.addColumn("Año");

        tablaPeliculas = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPeliculas);

        // Datos iniciales
        modeloTabla.addRow(new Object[]{"Matrix", "Ciencia Ficción", 136, 1999});
        modeloTabla.addRow(new Object[]{"El Padrino", "Drama", 175, 1972});
        modeloTabla.addRow(new Object[]{"Titanic", "Romance", 195, 1997});
        modeloTabla.addRow(new Object[]{"Inception", "Acción", 148, 2010});

        // ================== PANEL IZQUIERDO (FORMULARIO) ==================
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(9, 1, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la Película"));

        txtTitulo = new JTextField();
        txtGenero = new JTextField();
        txtDuracion = new JTextField();
        txtAnio = new JTextField();

        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(txtTitulo);

        panelFormulario.add(new JLabel("Género:"));
        panelFormulario.add(txtGenero);

        panelFormulario.add(new JLabel("Duración (min):"));
        panelFormulario.add(txtDuracion);

        panelFormulario.add(new JLabel("Año:"));
        panelFormulario.add(txtAnio);

        // ================== PANEL BOTONES ==================
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));

        btnAgregar = new JButton("Agregar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // ================== EVENTOS ==================
        btnAgregar.addActionListener(e -> agregarPelicula());

        btnEliminar.addActionListener(e -> eliminarPelicula());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaPeliculas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cargarDatosSeleccionados();
            }
        });

        // ================== PANEL ESTADO ==================
        lblEstado = new JLabel(" Listo.");
        lblEstado.setBorder(BorderFactory.createEtchedBorder());

        // ================== ARMADO FINAL ==================
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        add(panelIzquierdo, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }

    // ================== MÉTODOS ==================
    private void agregarPelicula() {
        String titulo = txtTitulo.getText();
        String genero = txtGenero.getText();
        String duracion = txtDuracion.getText();
        String anio = txtAnio.getText();

        if (titulo.isEmpty() || genero.isEmpty() || duracion.isEmpty() || anio.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Complete todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.addRow(new Object[]{
                titulo,
                genero,
                duracion,
                anio
        });

        lblEstado.setText(" Película agregada correctamente.");
        limpiarCampos();
    }

    private void eliminarPelicula() {
        int fila = tablaPeliculas.getSelectedRow();

        if (fila >= 0) {
            modeloTabla.removeRow(fila);
            lblEstado.setText(" Película eliminada.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una película",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtGenero.setText("");
        txtDuracion.setText("");
        txtAnio.setText("");
        lblEstado.setText(" Campos limpiados.");
    }

    private void cargarDatosSeleccionados() {
        int fila = tablaPeliculas.getSelectedRow();

        if (fila >= 0) {
            txtTitulo.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtGenero.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtDuracion.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtAnio.setText(modeloTabla.getValueAt(fila, 3).toString());
            lblEstado.setText(" Película seleccionada.");
        }
    }

    // ================== MAIN ==================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPeliculas().setVisible(true);
        });
    }
}
