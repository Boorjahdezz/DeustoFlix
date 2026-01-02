package gui.avatar;

import gui.MainGuiWindow;    // <--- IMPORTANTE: Importamos la ventana principal
import databases.ConexionBD;

import javax.swing.*;
import java.awt.*;

public class VentanaSeleccionAvatar extends JFrame {

    private String nombreTemp;
    private String gmailTemp;
    private String passTemp;

    public VentanaSeleccionAvatar(String nombre, String gmail, String pass) {
        this.nombreTemp = nombre;
        this.gmailTemp = gmail;
        this.passTemp = pass;

        setTitle("Paso 2: Elige tu avatar");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Elige una foto de perfil para terminar", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        titulo.setOpaque(true);
        titulo.setBackground(new Color(25, 25, 25));
        add(titulo, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 15, 15));
        grid.setBackground(new Color(20, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Nombres de los archivos en tu carpeta resources
        String[] nombresArchivos = {
                "perfil1.png", "perfil2.png", "perfil3.png", "perfil4.png",
                "perfil5.png", "perfil6.png", "perfil7.png", "perfil8.png"
        };

        ClassLoader cl = getClass().getClassLoader();

        for (String nombreArchivo : nombresArchivos) {
            // Cargar imagen
            ImageIcon icon = null;
            java.net.URL url = cl.getResource(nombreArchivo);
            
            if (url == null) url = cl.getResource("Imagenes/" + nombreArchivo);

            if (url != null) {
                icon = new ImageIcon(url);
            } else {
                System.err.println("No se encontró imagen: " + nombreArchivo);
                continue; 
            }

            // Guardamos una referencia final para usarla dentro del botón
            final ImageIcon iconFinal = icon;

            JButton b = new JButton(escalarIcono(icon, 120, 120));
            b.setBackground(new Color(35, 35, 35));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            
            b.addActionListener(e -> {
                // 1. Guardar en Base de Datos
                boolean exito = ConexionBD.crearUsuario(nombreTemp, gmailTemp, passTemp, nombreArchivo);

                if (exito) {
                    // --- CAMBIO CLAVE AQUÍ ---
                    JOptionPane.showMessageDialog(this, "¡Bienvenido a DeustoFlix, " + nombreTemp + "!");

                    // 2. Establecer la sesión (usando tu clase UserSession)
                    UserSession.set(nombreTemp, iconFinal); 

                    // 3. Abrir DIRECTAMENTE la aplicación principal
                    new MainGuiWindow(nombreTemp, iconFinal).setVisible(true);
                    
                    // Cerrar esta ventana de registro
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear usuario. Quizá el nombre ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            grid.add(b);
        }

        add(grid, BorderLayout.CENTER);
        getContentPane().setBackground(new Color(20, 20, 20));
    }

    private ImageIcon escalarIcono(ImageIcon icon, int w, int h) {
        if (icon == null || icon.getIconWidth() <= 0) return icon;
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}