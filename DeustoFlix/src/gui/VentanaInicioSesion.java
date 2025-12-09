package gui;

import java.awt.BorderLayout;import java.awt.Color;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VentanaInicioSesion extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaInicioSesion() {
        setSize(1200, 800);
        setTitle("Inicio Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setLayout(new BorderLayout());

        // Campos y etiquetas
        JTextField nombreUsuario = new JTextField();
        nombreUsuario.setPreferredSize(new Dimension(300, 40));
        nombreUsuario.setBackground(new Color(50, 50, 50));
        nombreUsuario.setForeground(Color.WHITE);
        nombreUsuario.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel nombreUsuarioLabel = new JLabel("Nombre de Usuario");
        nombreUsuarioLabel.setForeground(Color.WHITE);
        nombreUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        
        JPanel panelPassword = new JPanel(new BorderLayout());
        panelPassword.setPreferredSize(new Dimension(300, 40)); 
        panelPassword.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 

        
        JPasswordField contenedorContrasenya = new JPasswordField();
        contenedorContrasenya.setBackground(new Color(50, 50, 50));
        contenedorContrasenya.setForeground(Color.WHITE);
        contenedorContrasenya.setCaretColor(Color.WHITE);
        contenedorContrasenya.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        contenedorContrasenya.setFont(new Font("Arial", Font.PLAIN, 18));

        
        JButton btnVer = new JButton("👁");
        btnVer.setPreferredSize(new Dimension(50, 40));
        btnVer.setBackground(new Color(60, 60, 60));
        btnVer.setForeground(Color.WHITE);
        btnVer.setBorderPainted(false);
        btnVer.setFocusPainted(false);
        btnVer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnVer.addActionListener(e -> {
            char c = contenedorContrasenya.getEchoChar();
            if (c == '*') {
                contenedorContrasenya.setEchoChar((char) 0);
                btnVer.setBackground(new Color(100, 100, 100)); 
            } else {
                contenedorContrasenya.setEchoChar('•');
                btnVer.setBackground(new Color(60, 60, 60));
            }
        });
        
        contenedorContrasenya.setEchoChar('*'); 

        
        panelPassword.add(contenedorContrasenya, BorderLayout.CENTER);
        panelPassword.add(btnVer, BorderLayout.EAST);
        
        //JPasswordField contraseña = new JPasswordField();
        //contraseña.setPreferredSize(new Dimension(300, 40));
        //contraseña.setBackground(new Color(50, 50, 50));
        //contraseña.setForeground(Color.WHITE);
        //contraseña.setFont(new Font("Arial", Font.PLAIN, 18));
        


        JLabel contraseñaLabel = new JLabel("Contraseña");
        contraseñaLabel.setForeground(Color.WHITE);
        contraseñaLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonIniciar = new JButton("Iniciar Sesion");
        botonIniciar.setPreferredSize(new Dimension(200, 40));
        botonIniciar.setFont(new Font("Arial", Font.BOLD, 16));
        botonIniciar.addActionListener(e -> {
        	VentanaCarga carga = new VentanaCarga();

            carga.iniciarCarga(() -> {
                new MainGuiWindow().setVisible(true);
                dispose(); 
            });
        });
        
        JButton exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(75,25));
        exit.setFont(new Font("Arial", Font.BOLD, 16));
        exit.addActionListener(e ->{
        	
        	VentanaInicio ventanaInicio = new VentanaInicio();
        	ventanaInicio.setVisible(true);
        	dispose();
        });

        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        /*GridBagConstraints gbcExit = new GridBagConstraints();
        gbcExit.insets = new Insets(15, 10, 15, 10);
        gbcExit.gridx = 0;
        gbcExit.fill = GridBagConstraints.NONE;
        gbcExit.anchor = GridBagConstraints.NORTHWEST;
        */
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

    
        gbc.gridy = 0;
        panel.add(nombreUsuarioLabel, gbc);

  
        gbc.gridy = 1;
        panel.add(nombreUsuario, gbc);

      
        gbc.gridy = 2;
        panel.add(contraseñaLabel, gbc);

 
        gbc.gridy = 3;
        panel.add(panelPassword, gbc);

        gbc.gridy = 4;
        panel.add(botonIniciar, gbc);
        /*
        gbcExit.gridy= 0;
        panel.add(exit, gbcExit);
        */
        add(panel);
        
        JPanel panelexit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelexit.setBackground(new Color(30,30,30));
        panelexit.add(exit);
        add(panelexit,BorderLayout.NORTH);

        getContentPane().setBackground(new Color(30,30,30)); 
    }
}
