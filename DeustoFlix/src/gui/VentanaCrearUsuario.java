package gui;

import java.awt.*;
import java.awt.event.KeyAdapter; // Necesario para detectar teclas
import java.awt.event.KeyEvent;   // Necesario para detectar teclas
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import gui.avatar.VentanaSeleccionAvatar;

public class VentanaCrearUsuario extends JFrame {

    private static final long serialVersionUID = 1L;
    private CaptchaVerificationPanel panelCaptcha; 

    public VentanaCrearUsuario() {
        setSize(1200, 800);
        setTitle("Crear Usuario - Paso 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Botón Salir
        JButton exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(75, 25));
        exit.setFont(new Font("Arial", Font.BOLD, 16));
        exit.addActionListener(e -> {
            new VentanaInicio().setVisible(true);
            dispose();
        });

        JPanel panelExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelExit.setBackground(new Color(30, 30, 30));
        panelExit.add(exit);
        add(panelExit, BorderLayout.NORTH);

        // --- CAMPOS DEL FORMULARIO ---

        // 1. Nombre
        JTextField nombreUsuario = new JTextField();
        estilarCampo(nombreUsuario);
        JLabel nombreUsuarioLabel = new JLabel("Nombre de Usuario");
        estilarLabel(nombreUsuarioLabel);

        // 2. Contraseña
        JPasswordField contraseñaUsuario = new JPasswordField();
        estilarCampo(contraseñaUsuario);
        JLabel contraseñaUsuarioLabel = new JLabel("Contraseña");
        estilarLabel(contraseñaUsuarioLabel);

        // 3. Gmail (CON BLOQUEO DE ARROBA)
        JPanel panelEmailContainer = new JPanel(new BorderLayout());
        panelEmailContainer.setPreferredSize(new Dimension(300, 40));
        panelEmailContainer.setBackground(new Color(30, 30, 30));
        panelEmailContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 

        JTextField gmailUsuario = new JTextField();
        estilarCampo(gmailUsuario);
        gmailUsuario.setBorder(new EmptyBorder(0, 5, 0, 0)); 
        gmailUsuario.setPreferredSize(null); 
        
        // --- AQUÍ ESTÁ EL CAMBIO: KeyListener para bloquear '@' ---
        gmailUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Si el carácter escrito es una arroba...
                if (e.getKeyChar() == '@') {
                    e.consume(); // ¡BLOQUEAR! Evita que se escriba en la caja
                    // Avisar al usuario
                    JOptionPane.showMessageDialog(VentanaCrearUsuario.this, 
                        "¡No hace falta! El '@gmail.com' ya está puesto a la derecha.", 
                        "Aviso", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        // ----------------------------------------------------------
        
        JLabel lblDominio = new JLabel("@gmail.com");
        lblDominio.setForeground(Color.LIGHT_GRAY);
        lblDominio.setFont(new Font("Arial", Font.PLAIN, 18));
        lblDominio.setBorder(new EmptyBorder(0, 0, 0, 10)); 

        panelEmailContainer.add(gmailUsuario, BorderLayout.CENTER);
        panelEmailContainer.add(lblDominio, BorderLayout.EAST);

        JLabel gmailUsuarioLabel = new JLabel("Gmail");
        estilarLabel(gmailUsuarioLabel);
        
        // 4. Captcha
        JLabel captchaLabel = new JLabel("Verificación CAPTCHA");
        estilarLabel(captchaLabel);
        
        panelCaptcha = new CaptchaVerificationPanel();

        // BOTÓN SIGUIENTE
        JButton btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setPreferredSize(new Dimension(200, 40));
        btnSiguiente.setFont(new Font("Arial", Font.BOLD, 16));

        btnSiguiente.addActionListener(e -> {
            String nombre = nombreUsuario.getText().trim();
            String parteUsuarioEmail = gmailUsuario.getText().trim();
            String pass = new String(contraseñaUsuario.getPassword());

            if(nombre.isEmpty() || parteUsuarioEmail.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Construimos el correo completo
            String gmailCompleto = parteUsuarioEmail + "@gmail.com";

            // Validaciones Captcha
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
                    JOptionPane.showMessageDialog(this, 
                        "Has alcanzado el número máximo de intentos. Espera para continuar.", 
                        "Bloqueado", 
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Código de verificación incorrecto. Intentos restantes: " + intentosRestantes, 
                        "Error CAPTCHA", 
                        JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
      
            new VentanaSeleccionAvatar(nombre, gmailCompleto, pass).setVisible(true);
            dispose(); 
        });

        // Layout
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0; panelForm.add(nombreUsuarioLabel, gbc);
        gbc.gridy = 1; panelForm.add(nombreUsuario, gbc);
        gbc.gridy = 2; panelForm.add(contraseñaUsuarioLabel, gbc);
        gbc.gridy = 3; panelForm.add(contraseñaUsuario, gbc);
        gbc.gridy = 4; panelForm.add(gmailUsuarioLabel, gbc);
        gbc.gridy = 5; panelForm.add(panelEmailContainer, gbc); // Añadimos el panel compuesto
        gbc.gridy = 6; panelForm.add(captchaLabel, gbc);
        gbc.gridy = 7; panelForm.add(panelCaptcha, gbc);
        gbc.gridy = 8; panelForm.add(btnSiguiente, gbc);

        add(panelForm, BorderLayout.CENTER);
    }

    // Métodos auxiliares
    private void estilarLabel(JLabel lbl) {
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
    }
    
    private void estilarCampo(JTextField txt) {
        txt.setPreferredSize(new Dimension(300, 40));
        txt.setBackground(new Color(30, 30, 30));
        txt.setForeground(Color.WHITE);
        txt.setFont(new Font("Arial", Font.PLAIN, 18));
        txt.setCaretColor(Color.WHITE); 
        txt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
}