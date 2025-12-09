package gui;

import java.awt.*;
import javax.swing.*;

import databases.ConexionBD;

public class VentanaCrearUsuario extends JFrame {

    public VentanaCrearUsuario() {
        setSize(1200, 800);
        setTitle("Crear Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

        JTextField nombreUsuario = new JTextField();
        nombreUsuario.setPreferredSize(new Dimension(300, 40));
        nombreUsuario.setBackground(new Color(30, 30, 30));
        nombreUsuario.setForeground(Color.WHITE);
        nombreUsuario.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel nombreUsuarioLabel = new JLabel("Nombre de Usuario");
        nombreUsuarioLabel.setForeground(Color.WHITE);
        nombreUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPasswordField contraseñaUsuario = new JPasswordField();
        contraseñaUsuario.setPreferredSize(new Dimension(300, 40));
        contraseñaUsuario.setBackground(new Color(30, 30, 30));
        contraseñaUsuario.setForeground(Color.WHITE);
        contraseñaUsuario.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel contraseñaUsuarioLabel = new JLabel("Contraseña");
        contraseñaUsuarioLabel.setForeground(Color.WHITE);
        contraseñaUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField gmailUsuario = new JTextField();
        gmailUsuario.setPreferredSize(new Dimension(300, 40));
        gmailUsuario.setBackground(new Color(30, 30, 30));
        gmailUsuario.setForeground(Color.WHITE);
        gmailUsuario.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel gmailUsuarioLabel = new JLabel("Gmail");
        gmailUsuarioLabel.setForeground(Color.WHITE);
        gmailUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnCrear = new JButton("Crear Usuario");
        btnCrear.setPreferredSize(new Dimension(200, 40));
        btnCrear.setFont(new Font("Arial", Font.BOLD, 16));

        btnCrear.addActionListener(e -> {
            String nombre = nombreUsuario.getText();
            String gmail = gmailUsuario.getText();
            String pass = new String(contraseñaUsuario.getPassword());

            if (ConexionBD.crearUsuario(nombre, gmail, pass)) {
                JOptionPane.showMessageDialog(this, "Usuario creado correctamente");
                new VentanaInicioSesion().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

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
        gbc.gridy = 6; panelForm.add(btnCrear, gbc);

        add(panelForm, BorderLayout.CENTER);
    }
}

