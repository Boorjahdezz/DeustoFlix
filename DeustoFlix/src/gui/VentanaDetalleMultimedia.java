package gui;

import domain.MediaItem;
import domain.Pelicula;
import domain.Serie;
import gui.avatar.UserSession;
import databases.ConexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaDetalleMultimedia extends JFrame {

    private JButton[] estrellas; // Array para las 5 estrellas
    private JLabel lblVal;       // Etiqueta global para actualizarla

    public VentanaDetalleMultimedia(MediaItem item) {
        setTitle("Detalles: " + item.getTitulo());
        setSize(600, 550); // Un poco más alto para las estrellas
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 30));

        // --- PANEL SUPERIOR (TÍTULO) ---
        JLabel lblTitulo = new JLabel(item.getTitulo(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(255, 215, 0)); // Color dorado
        lblTitulo.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(lblTitulo, BorderLayout.NORTH);

        // --- PANEL CENTRAL (IMAGEN + DATOS) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(30, 30, 30));
        add(panelCentral, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // Imagen
        ImageIcon iconoOriginal = item.getImagen();
        JLabel lblImagen = new JLabel(escalarImagen(iconoOriginal, 200, 280));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridheight = 6; // Aumentado para acomodar estrellas
        panelCentral.add(lblImagen, gbc);

        // Datos derecha
        gbc.gridx = 1; 
        gbc.gridheight = 1; 
        gbc.weightx = 1.0;  

        // Género
        gbc.gridy = 0;
        panelCentral.add(crearEtiquetaDato("Género: ", item.getGenero() != null ? item.getGenero().name() : "Desconocido"), gbc);

        // Categoría
        gbc.gridy = 1;
        panelCentral.add(crearEtiquetaDato("Categoría: ", item.getCategoria() != null ? item.getCategoria().getNombre() : "General"), gbc);

        // Duración
        gbc.gridy = 2;
        panelCentral.add(crearEtiquetaDato("Duración: ", item.getDuracion() + " min"), gbc);

        // Valoración Global
        double valoracion = obtenerValoracionActual(item);
        gbc.gridy = 3;
        lblVal = crearEtiquetaDato("Valoración Global: ", String.format("%.1f / 10", valoracion));
        lblVal.setForeground(new Color(255, 100, 100)); // Rojo suave
        panelCentral.add(lblVal, gbc);

        // --- PANEL DE ESTRELLAS (NUEVO) ---
        gbc.gridy = 4;
        JPanel panelEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelEstrellas.setBackground(new Color(30, 30, 30));
        panelEstrellas.setBorder(BorderFactory.createTitledBorder(null, "Tu Valoración", 0, 0, new Font("Arial", Font.BOLD, 12), Color.GRAY));
        
        estrellas = new JButton[5];
        for (int i = 0; i < 5; i++) {
            final int nota = i + 1; // 1 a 5
            estrellas[i] = new JButton("★");
            estrellas[i].setFont(new Font("SansSerif", Font.BOLD, 24));
            estrellas[i].setForeground(Color.GRAY); // Desactivada por defecto
            estrellas[i].setBackground(new Color(30, 30, 30));
            estrellas[i].setBorderPainted(false);
            estrellas[i].setFocusPainted(false);
            estrellas[i].setContentAreaFilled(false);
            estrellas[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Acción al clicar
            estrellas[i].addActionListener(e -> valorar(nota, item));
            
            panelEstrellas.add(estrellas[i]);
        }
        
        // Cargar valoración existente si el usuario ya votó
        String usuario = UserSession.getUsuario();
        int miNota = ConexionBD.obtenerMiValoracion(usuario, item.getId());
        pintarEstrellas(miNota);

        panelCentral.add(panelEstrellas, gbc);
        // ----------------------------------

        // Descripción (Sinopsis)
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        JTextArea txtDesc = new JTextArea(item.getDescripcion());
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(new Color(40, 40, 40));
        txtDesc.setForeground(Color.LIGHT_GRAY);
        txtDesc.setFont(new Font("SansSerif", Font.ITALIC, 14));
        txtDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        scrollDesc.setBorder(BorderFactory.createTitledBorder(null, "Sinopsis", 0, 0, new Font("SansSerif", Font.BOLD, 12), Color.WHITE));
        scrollDesc.setOpaque(false);
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setBackground(new Color(30,30,30));

        panelCentral.add(scrollDesc, gbc);

        // --- BOTÓN CERRAR (ABAJO) ---
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(200, 50, 50));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());
        
        JPanel panelBtn = new JPanel();
        panelBtn.setBackground(new Color(30, 30, 30));
        panelBtn.add(btnCerrar);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private void valorar(int nota, MediaItem item) {
        // 1. Efecto visual
        pintarEstrellas(nota);
        
        // 2. Guardar en BD
        String usuario = UserSession.getUsuario();
        ConexionBD.valorarContenido(usuario, item.getId(), nota);
        
        // 3. Actualizar la media global visualmente
        double nuevaMedia = ConexionBD.obtenerValoracionMedia(item.getId());
        
        // Actualizamos el objeto local también por si acaso
        if (item instanceof Pelicula) ((Pelicula)item).setValoracion(nuevaMedia);
        else if (item instanceof Serie) ((Serie)item).setValoracion(nuevaMedia);
        
        lblVal.setText("<html><b>Valoración Global: </b> <font color='#FF6464'>" + String.format("%.1f / 10", nuevaMedia) + "</font></html>");
        
        JOptionPane.showMessageDialog(this, "¡Valoración de " + nota + " estrellas guardada!");
    }

    private void pintarEstrellas(int nota) {
        // Recorre las 5 estrellas
        for (int i = 0; i < 5; i++) {
            if (i < nota) {
                estrellas[i].setForeground(Color.YELLOW); // Seleccionada
            } else {
                estrellas[i].setForeground(Color.GRAY);   // Deseleccionada
            }
        }
        // Truco para repintar el componente
        repaint();
    }
    
    private double obtenerValoracionActual(MediaItem item) {
        if (item instanceof Pelicula) return ((Pelicula) item).getValoracion();
        if (item instanceof Serie) return ((Serie) item).getValoracion();
        return 0.0;
    }

    private JLabel crearEtiquetaDato(String titulo, String valor) {
        JLabel lbl = new JLabel("<html><b>" + titulo + "</b> " + valor + "</html>");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private ImageIcon escalarImagen(ImageIcon icon, int w, int h) {
        if (icon == null || icon.getIconWidth() <= 0) return null;
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}