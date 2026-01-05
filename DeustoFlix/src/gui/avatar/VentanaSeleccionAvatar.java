package gui.avatar;

import gui.MainGuiWindow;
import gui.VentanaInicioSesion; 
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
        setSize(800, 550); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- TÍTULO ---
        JLabel titulo = new JLabel("Elige una foto de perfil para terminar", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        titulo.setOpaque(true);
        titulo.setBackground(new Color(25, 25, 25));
        add(titulo, BorderLayout.NORTH);

        // --- GRID DE FOTOS ---
        JPanel grid = new JPanel(new GridLayout(2, 4, 15, 15));
        grid.setBackground(new Color(20, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] nombresArchivos = {
                "perfil1.png", "perfil2.png", "perfil3.png", "perfil4.png",
                "perfil5.png", "perfil6.png", "perfil7.png", "perfil8.png"
        };

        ClassLoader cl = getClass().getClassLoader();

        for (String nombreArchivo : nombresArchivos) {
            ImageIcon icon = null;
            java.net.URL url = cl.getResource(nombreArchivo);
            
            if (url == null) url = cl.getResource("Imagenes/" + nombreArchivo);

            if (url != null) {
                icon = new ImageIcon(url);
            } else {
                System.err.println("Imagen no encontrada: " + nombreArchivo);
                continue; 
            }

            // CORRECCIÓN: Descomentamos esta línea porque la necesitamos
            final ImageIcon iconFinal = icon; 

            JButton b = new JButton(escalarIcono(icon, 120, 120));
            b.setBackground(new Color(35, 35, 35));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            
            // ACCIÓN: CREAR USUARIO Y ENTRAR
            b.addActionListener(e -> {
                boolean exito = ConexionBD.crearUsuario(nombreTemp, gmailTemp, passTemp, nombreArchivo);

                if (exito) {
                    JOptionPane.showMessageDialog(this, "¡Bienvenido a DeustoFlix, " + nombreTemp + "!");
                    
                    // --- CORRECCIÓN AQUÍ: Pasamos nombre e icono ---
                    new MainGuiWindow(nombreTemp, iconFinal).setVisible(true);
                    // -----------------------------------------------
                    
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: El usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            grid.add(b);
        }

        add(grid, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(25, 25, 25));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnExit = new JButton("Cancelar y Volver al Login");
        btnExit.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExit.setBackground(new Color(180, 50, 50)); 
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setPreferredSize(new Dimension(250, 40));
        
        btnExit.addActionListener(e -> {
            new VentanaInicioSesion().setVisible(true);
            dispose();
        });

        panelSur.add(btnExit);
        add(panelSur, BorderLayout.SOUTH);
        
        getContentPane().setBackground(new Color(20, 20, 20));
    }

    private ImageIcon escalarIcono(ImageIcon icon, int w, int h) {
        if (icon == null || icon.getIconWidth() <= 0) return icon;
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}