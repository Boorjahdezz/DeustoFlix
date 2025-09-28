package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VentanaCrearUsuario extends JFrame {

    public VentanaCrearUsuario() {
        setSize(1200, 800);
        setTitle("Crear Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // SOLO BorderLayout en el JFrame

        // -------- BOTÓN EXIT ARRIBA IZQUIERDA --------
        JButton exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(75,25));
        exit.setFont(new Font("Arial", Font.BOLD, 16));
        exit.addActionListener(e -> {
            VentanaInicio ventanaInicio = new VentanaInicio();
            ventanaInicio.setVisible(true);
            dispose();
        });

        JPanel panelExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelExit.setBackground(new Color(30,30,30));
        panelExit.add(exit);

        add(panelExit, BorderLayout.NORTH);

        // -------- PANEL CENTRAL CON FORMULARIO CENTRADO --------
        JTextField nombreUsuario = new JTextField();
        nombreUsuario.setPreferredSize(new Dimension(300,40));
        nombreUsuario.setBackground(new Color(30,30,30));
        nombreUsuario.setForeground(Color.WHITE);
        nombreUsuario.setFont(new Font("Arial",Font.PLAIN, 18));

        JLabel nombreUsuarioLabel = new JLabel("Nombre de Usuario");
        nombreUsuarioLabel.setForeground(Color.WHITE);
        nombreUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPasswordField contraseñaUsuario = new JPasswordField();
        contraseñaUsuario.setPreferredSize(new Dimension(300,40));
        contraseñaUsuario.setBackground(new Color(30,30,30));
        contraseñaUsuario.setForeground(Color.WHITE);
        contraseñaUsuario.setFont(new Font("Arial",Font.PLAIN, 18));

        JLabel contraseñaUsuarioLabel = new JLabel("Contraseña");
        contraseñaUsuarioLabel.setForeground(Color.WHITE);
        contraseñaUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField gmailUsuario = new JTextField();
        gmailUsuario.setPreferredSize(new Dimension(300,40));
        gmailUsuario.setBackground(new Color(30,30,30));
        gmailUsuario.setForeground(Color.WHITE);
        gmailUsuario.setFont(new Font("Arial",Font.PLAIN, 18));

        JLabel gmailUsuarioLabel = new JLabel("Gmail");
        gmailUsuarioLabel.setForeground(Color.WHITE);
        gmailUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botoncrearUsuario = new JButton("Crear Usuario");
        botoncrearUsuario.setPreferredSize(new Dimension(200,40));
        botoncrearUsuario.setFont(new Font("Arial",Font.BOLD, 16));
        botoncrearUsuario.addActionListener(e -> {
            MainGuiWindow mainWindow = new MainGuiWindow();
            mainWindow.setVisible(true);
            dispose();
        });

        // PANEL DEL FORMULARIO CENTRADO
        JPanel panelParaRellenar = new JPanel(new GridBagLayout());
        panelParaRellenar.setBackground(new Color(30,30,30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        panelParaRellenar.add(nombreUsuarioLabel, gbc);

        gbc.gridy = 1;
        panelParaRellenar.add(nombreUsuario, gbc);

        gbc.gridy = 2;
        panelParaRellenar.add(contraseñaUsuarioLabel, gbc);

        gbc.gridy = 3;
        panelParaRellenar.add(contraseñaUsuario, gbc);

        gbc.gridy = 4;
        panelParaRellenar.add(gmailUsuarioLabel, gbc);

        gbc.gridy = 5;
        panelParaRellenar.add(gmailUsuario, gbc);

        gbc.gridy = 6;
        panelParaRellenar.add(botoncrearUsuario, gbc);

        add(panelParaRellenar, BorderLayout.CENTER); // Panel centrado

        // Fondo general de la ventana (opcional, para gris claro fuera del panel central)
        getContentPane().setBackground(new Color(220,220,220));
    }
}
