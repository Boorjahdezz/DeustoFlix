package gui;

import java.awt.*;
import javax.swing.*;

import databases.ConexionBD;

public class VentanaInicioSesion extends JFrame {

    private static final long serialVersionUID = 1L;

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

        JButton btnIniciar = new JButton("Iniciar Sesion");
        btnIniciar.setPreferredSize(new Dimension(200, 40));
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 16));

        btnIniciar.addActionListener(e -> {
            String nombre = nombreUsuario.getText();
            String pass = new String(contraseñaUsuario.getPassword());

            if (ConexionBD.loginUsuario(nombre, pass)) {
                JOptionPane.showMessageDialog(this, "Login exitoso");
                new MainGuiWindow().setVisible(true);
                dispose();
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
        gbc.gridy = 4;
        panelForm.add(btnIniciar, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelExit.setBackground(new Color(30, 30, 30));
        panelExit.add(exit);
        add(panelExit, BorderLayout.NORTH);
    }
}



