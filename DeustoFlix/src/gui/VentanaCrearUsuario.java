package gui;

import java.awt.*;
import javax.swing.*;


import gui.avatar.VentanaSeleccionAvatar;
import gui.CaptchaVerificationPanel;

public class VentanaCrearUsuario extends JFrame {

    /**
	 * 
	 */
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

        // Campos
        JTextField nombreUsuario = new JTextField();
        estilarCampo(nombreUsuario);

        JLabel nombreUsuarioLabel = new JLabel("Nombre de Usuario");
        estilarLabel(nombreUsuarioLabel);

        JPasswordField contraseñaUsuario = new JPasswordField();
        estilarCampo(contraseñaUsuario);

        JLabel contraseñaUsuarioLabel = new JLabel("Contraseña");
        estilarLabel(contraseñaUsuarioLabel);

        JTextField gmailUsuario = new JTextField();
        estilarCampo(gmailUsuario);

        JLabel gmailUsuarioLabel = new JLabel("Gmail");
        estilarLabel(gmailUsuarioLabel);
        
        JLabel captchaLabel = new JLabel("Verificación CAPTCHA");
        captchaLabel.setForeground(Color.WHITE);
        captchaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        CaptchaVerificationPanel panelCaptcha = new CaptchaVerificationPanel();

        // BOTÓN SIGUIENTE
        JButton btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setPreferredSize(new Dimension(200, 40));
        btnSiguiente.setFont(new Font("Arial", Font.BOLD, 16));

        btnSiguiente.addActionListener(e -> {
            String nombre = nombreUsuario.getText();
            String gmail = gmailUsuario.getText();
            String pass = new String(contraseñaUsuario.getPassword());

          
            if(nombre.isEmpty() || gmail.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
      
            new VentanaSeleccionAvatar(nombre, gmail, pass).setVisible(true);
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
        gbc.gridy = 5; panelForm.add(gmailUsuario, gbc);
        gbc.gridy = 6; panelForm.add(captchaLabel, gbc);
        gbc.gridy = 7; panelForm.add(panelCaptcha, gbc);
        gbc.gridy = 8; panelForm.add(btnSiguiente, gbc);

        add(panelForm, BorderLayout.CENTER);
    }

    // Métodos auxiliares para no repetir código de estilo
    private void estilarLabel(JLabel lbl) {
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
    }
    private void estilarCampo(JTextField txt) {
        txt.setPreferredSize(new Dimension(300, 40));
        txt.setBackground(new Color(30, 30, 30));
        txt.setForeground(Color.WHITE);
        txt.setFont(new Font("Arial", Font.PLAIN, 18));
        txt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
}