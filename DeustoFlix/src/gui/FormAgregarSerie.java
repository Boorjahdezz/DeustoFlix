package gui;

import javax.swing.*;
import java.awt.*;
import databases.ConexionBD;
import domain.*;

public class FormAgregarSerie extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private final Color BG_DARK = new Color(25, 25, 30);
    private final Color BG_FIELD = new Color(40, 40, 45);
    private final Color ACCENT_RED = new Color(229, 9, 20);
    private final Color TEXT_WHITE = Color.WHITE;
    
    private JTextField txtTitulo, txtDuracion;
    private JTextArea txtDescripcion;
    private JComboBox<String> cboGenero, cboCategoria;
    private JSpinner spnValoracion;
    private VentanaAdmin parent;
    
    public FormAgregarSerie(VentanaAdmin parent) {
        super(parent, "Agregar Nueva Serie", true);
        this.parent = parent;
        
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);
        
        // Panel de Título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(BG_DARK);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel lblTitulo = new JLabel("📺 Agregar Nueva Serie");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(ACCENT_RED);
        panelTitulo.add(lblTitulo);
        
        // Panel de Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(BG_DARK);
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campo Título
        panelForm.add(crearEtiqueta("Título:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTitulo = crearCampoTexto();
        panelForm.add(txtTitulo, gbc);
        
        // Campo Descripción
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        panelForm.add(crearEtiqueta("Descripción:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setBackground(BG_FIELD);
        txtDescripcion.setForeground(TEXT_WHITE);
        txtDescripcion.setCaretColor(TEXT_WHITE);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(txtDescripcion.getBorder());
        panelForm.add(scrollDesc, gbc);
        
        // Género
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(crearEtiqueta("Género:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] generos = {"ACCION", "COMEDIA", "DRAMA", "TERROR", "CIENCIA_FICCION", "ROMANCE", "DOCUMENTAL"};
        cboGenero = crearComboBox(generos);
        panelForm.add(cboGenero, gbc);
        
        // Categoría
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panelForm.add(crearEtiqueta("Categoría:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] categorias = {"Trending", "New Releases", "Popular", "Classic", "Binge-Worthy"};
        cboCategoria = crearComboBox(categorias);
        panelForm.add(cboCategoria, gbc);
        
        // Duración (promedio por episodio)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panelForm.add(crearEtiqueta("Duración promedio (min):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtDuracion = crearCampoTexto();
        panelForm.add(txtDuracion, gbc);
        
        // Valoración
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panelForm.add(crearEtiqueta("Valoración (0-10):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        SpinnerModel model = new SpinnerNumberModel(5.0, 0.0, 10.0, 0.1);
        spnValoracion = new JSpinner(model);
        spnValoracion.setFont(new Font("Arial", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) spnValoracion.getEditor()).getTextField().setBackground(BG_FIELD);
        ((JSpinner.DefaultEditor) spnValoracion.getEditor()).getTextField().setForeground(TEXT_WHITE);
        panelForm.add(spnValoracion, gbc);
        
        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setBackground(BG_DARK);
        
        JButton btnGuardar = new JButton("💾 Guardar Serie");
        JButton btnCancelar = new JButton("❌ Cancelar");
        
        estilizarBoton(btnGuardar, ACCENT_RED);
        estilizarBoton(btnCancelar, new Color(80, 80, 85));
        
        btnGuardar.addActionListener(e -> guardarSerie());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        // Agregar todo al diálogo
        add(panelTitulo, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void guardarSerie() {
        try {
            String titulo = txtTitulo.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String generoStr = (String) cboGenero.getSelectedItem();
            String categoria = (String) cboCategoria.getSelectedItem();
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            double valoracion = (Double) spnValoracion.getValue();
            
            if (titulo.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos requeridos", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Genero genero = Genero.valueOf(generoStr);
            Categoria cat = new Categoria(categoria);
            Serie serie = new Serie(titulo, descripcion, genero, cat, valoracion, duracion);
            
            ConexionBD.insertarContenido(serie);
            
            JOptionPane.showMessageDialog(this, "¡Serie agregada correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            parent.refrescarTablaContenido();
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato de duración inválido", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(TEXT_WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }
    
    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(20);
        txt.setBackground(BG_FIELD);
        txt.setForeground(TEXT_WHITE);
        txt.setCaretColor(TEXT_WHITE);
        txt.setFont(new Font("Arial", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return txt;
    }
    
    private JComboBox<String> crearComboBox(String[] items) {
        JComboBox<String> cbo = new JComboBox<>(items);
        cbo.setBackground(BG_FIELD);
        cbo.setForeground(TEXT_WHITE);
        cbo.setFont(new Font("Arial", Font.PLAIN, 14));
        return cbo;
    }
    
    private void estilizarBoton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(TEXT_WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}