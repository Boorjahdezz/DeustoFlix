package gui;

import java.awt.*;
import javax.swing.*;

// IMPORTANTE: Importamos la ventana del siguiente paso
import gui.avatar.VentanaSeleccionAvatar; 

public class VentanaCrearUsuario extends JFrame {

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

        // BOTÓN SIGUIENTE
        JButton btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setPreferredSize(new Dimension(200, 40));
        btnSiguiente.setFont(new Font("Arial", Font.BOLD, 16));

        btnSiguiente.addActionListener(e -> {
            String nombre = nombreUsuario.getText();
            String gmail = gmailUsuario.getText();
            String pass = new String(contraseñaUsuario.getPassword());

            // Validación simple antes de pasar al siguiente paso
            if(nombre.isEmpty() || gmail.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // AQUÍ ESTÁ EL CAMBIO:
            // No guardamos todavía. Abrimos la selección de avatar pasando los datos.
            new VentanaSeleccionAvatar(nombre, gmail, pass).setVisible(true);
            dispose(); // Cerramos esta ventana
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
        gbc.gridy = 6; panelForm.add(btnSiguiente, gbc);

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