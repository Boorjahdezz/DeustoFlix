package gui;

import domain.MediaItem;
import domain.Pelicula;
import domain.Serie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaDetalleMultimedia extends JFrame {

    public VentanaDetalleMultimedia(MediaItem item) {
        setTitle("Detalles: " + item.getTitulo());
        setSize(600, 450);
        setLocationRelativeTo(null);
        // DISPOSE_ON_CLOSE para que solo se cierre esta ventana y no toda la app
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

        // 1. Imagen (Izquierda)
        ImageIcon iconoOriginal = item.getImagen();
        // Escalamos la imagen para que se vea más grande en el detalle
        JLabel lblImagen = new JLabel(escalarImagen(iconoOriginal, 200, 280));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridheight = 5; // Ocupa varias filas de alto
        panelCentral.add(lblImagen, gbc);

        // 2. Datos (Derecha)
        gbc.gridx = 1; 
        gbc.gridheight = 1; // Reseteamos altura
        gbc.weightx = 1.0;  // Para que el texto ocupe el resto del ancho

        // Genero
        gbc.gridy = 0;
        panelCentral.add(crearEtiquetaDato("Género: ", item.getGenero() != null ? item.getGenero().name() : "Desconocido"), gbc);

        // Categoría
        gbc.gridy = 1;
        panelCentral.add(crearEtiquetaDato("Categoría: ", item.getCategoria() != null ? item.getCategoria().getNombre() : "General"), gbc);

        // Duración
        gbc.gridy = 2;
        panelCentral.add(crearEtiquetaDato("Duración: ", item.getDuracion() + " min"), gbc);

        // Valoración (Hay que comprobar si es Película o Serie)
        double valoracion = 0.0;
        if (item instanceof Pelicula) {
            valoracion = ((Pelicula) item).getValoracion();
        } else if (item instanceof Serie) {
            valoracion = ((Serie) item).getValoracion();
        }
        gbc.gridy = 3;
        // Mostramos la valoración con una estrella ★
        JLabel lblVal = crearEtiquetaDato("Valoración: ", String.format("%.1f / 10", valoracion));
        lblVal.setForeground(new Color(255, 100, 100)); // Rojo suave
        panelCentral.add(lblVal, gbc);

        // Descripción (Sinopsis)
        gbc.gridy = 4;
        gbc.weighty = 1.0; // Para que ocupe el espacio vertical sobrante
        JTextArea txtDesc = new JTextArea(item.getDescripcion());
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(new Color(40, 40, 40));
        txtDesc.setForeground(Color.LIGHT_GRAY);
        txtDesc.setFont(new Font("SansSerif", Font.ITALIC, 14));
        txtDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Lo metemos en un ScrollPane por si el texto es muy largo
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

    // --- MÉTODOS AUXILIARES QUE FALTABAN ---

    private JLabel crearEtiquetaDato(String titulo, String valor) {
        JLabel lbl = new JLabel("<html><b>" + titulo + "</b> " + valor + "</html>");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private ImageIcon escalarImagen(ImageIcon icon, int w, int h) {
        if (icon == null || icon.getIconWidth() <= 0) return null; // Evitar error si no hay imagen
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}