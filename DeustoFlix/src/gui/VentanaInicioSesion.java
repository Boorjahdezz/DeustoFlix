package gui;

import java.awt.*;
import javax.swing.*;

import databases.ConexionBD;
import gui.avatar.UserSession;

public class VentanaInicioSesion extends JFrame {

    private static final long serialVersionUID = 1L;
    private CaptchaVerificationPanel panelCaptcha;

    public VentanaInicioSesion() {
        setSize(1200, 800);
        setTitle("Inicio Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextField nombreUsuario = new JTextField();
        nombreUsuario.setPreferredSize(new Dimension(300, 40));
        nombreUsuario.setBackground(new Color(50, 50, 50));
        nombreUsuario.setForeground(Color.WHITE);
        nombreUsuario.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel nombreUsuarioLabel = new JLabel("Nombre de Usuario");
        nombreUsuarioLabel.setForeground(Color.WHITE);
        nombreUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPasswordField contraseñaUsuario = new JPasswordField();
        contraseñaUsuario.setPreferredSize(new Dimension(300, 40));
        contraseñaUsuario.setBackground(new Color(50, 50, 50));
        contraseñaUsuario.setForeground(Color.WHITE);
        contraseñaUsuario.setFont(new Font("Arial", Font.PLAIN, 18));
        contraseñaUsuario.setEchoChar('*');

        JLabel contraseñaLabel = new JLabel("Contraseña");
        contraseñaLabel.setForeground(Color.WHITE);
        contraseñaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel captchaLabel = new JLabel("Verificación CAPTCHA");
        captchaLabel.setForeground(Color.WHITE);
        captchaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        CaptchaVerificationPanel panelCaptcha = new CaptchaVerificationPanel();

        JButton btnIniciar = new JButton("Iniciar Sesion");
        btnIniciar.setPreferredSize(new Dimension(200, 40));
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 16));

        // --- ACCIÓN DEL BOTÓN INICIAR SESIÓN ---
        btnIniciar.addActionListener(e -> {
            String nombre = nombreUsuario.getText();
            String pass = new String(contraseñaUsuario.getPassword());
            
            if (nombre.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor complete todos los campos", 
                    "Campos incompletos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (panelCaptcha.isBloqueado()) {
                JOptionPane.showMessageDialog(this, 
                    "Demasiados intentos fallidos. Por favor espere.", 
                    "Bloqueado", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!panelCaptcha.verificarCaptcha()) {
                int intentosRestantes = panelCaptcha.getIntentosRestantes();
                
                if (panelCaptcha.isBloqueado()) {
                    // Se alcanzó el límite de intentos, el componente se bloqueó automáticamente
                    JOptionPane.showMessageDialog(this, 
                        "Has alcanzado el número máximo de intentos. Espera para continuar.", 
                        "Bloqueado", 
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    // Aún quedan intentos
                    JOptionPane.showMessageDialog(this, 
                        "Código de verificación incorrecto. Intentos restantes: " + intentosRestantes, 
                        "Error CAPTCHA", 
                        JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            if (ConexionBD.loginUsuario(nombre, pass)) {
                // 1. Recuperamos el nombre del archivo de la foto
                String fotoFile = ConexionBD.obtenerFotoUsuario(nombre);
                
                // 2. Cargamos la imagen desde los recursos
                ImageIcon iconoUsuario = null;
                if (fotoFile != null && !fotoFile.isEmpty()) {
                    // Buscamos la imagen en el classpath (igual que en selección de avatar)
                    java.net.URL url = getClass().getClassLoader().getResource(fotoFile);
                    if (url == null) url = getClass().getClassLoader().getResource("Imagenes/" + fotoFile);
                    
                    if (url != null) {
                        iconoUsuario = new ImageIcon(url);
                        // Escalamos un poco la imagen para que no sea gigante
                        Image img = iconoUsuario.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                        iconoUsuario = new ImageIcon(img);
                    }
                }

                // 3. Guardamos sesión y abrimos la ventana principal
                UserSession.set(nombre, iconoUsuario);
                new MainGuiWindow(nombre, iconoUsuario).setVisible(true);
                
                dispose(); // Cerramos la ventana de login
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(75, 25));
        exit.setFont(new Font("Arial", Font.BOLD, 16));
        exit.addActionListener(e -> {
            new VentanaInicio().setVisible(true);
            dispose();
        });

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        panelForm.add(nombreUsuarioLabel, gbc);
        gbc.gridy = 1;
        panelForm.add(nombreUsuario, gbc);
        gbc.gridy = 2;
        panelForm.add(contraseñaLabel, gbc);
        gbc.gridy = 3;
        panelForm.add(contraseñaUsuario, gbc);
        // CAPTCHA Label
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 5, 10);
        panelForm.add(captchaLabel, gbc);
        
        // CAPTCHA Panel (con toda la funcionalidad de bloqueo)
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 10, 10);
        panelForm.add(panelCaptcha, gbc);
        
        // Botón iniciar
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        panelForm.add(btnIniciar, gbc);
        add(panelForm, BorderLayout.CENTER);

        JPanel panelExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelExit.setBackground(new Color(30, 30, 30));
        panelExit.add(exit);
        add(panelExit, BorderLayout.NORTH);
    }
}