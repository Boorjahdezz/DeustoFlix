package gui;

import java.awt.BorderLayout;import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.tools.StandardJavaFileManager;

public class VentanaInicioSesion extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  VentanaInicioSesion() {
		// TODO Auto-generated constructor stub
		setSize(1200,800);
		setTitle("Inicio Sesion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());
		setBackground(new Color(30,30,30));

		
		JTextField nombreUsuario = new JTextField(20);
		nombreUsuario.setPreferredSize(new Dimension(300,30));
		JLabel nombreUsiarioLabel = new JLabel("Nombre de Usuario");
		nombreUsiarioLabel.setForeground(Color.WHITE);
		JPasswordField contraseña = new JPasswordField(20);
		contraseña.setPreferredSize(new Dimension(300,30));
		JLabel contraseñaLabel = new JLabel("Contraseña");
		contraseñaLabel.setForeground(Color.WHITE);
		JButton botonIniciar = new JButton("Iniciar Sesion");
		botonIniciar.addActionListener(e -> {
			MainGuiWindow mainWindow = new MainGuiWindow();
			mainWindow.setVisible(true);
			dispose();
			});
		
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(nombreUsiarioLabel, gbc);
		
		gbc.insets = new Insets(10,10,10,10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(nombreUsuario, gbc);
		
		gbc.insets = new Insets(10,10,10,10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(contraseñaLabel, gbc);
		
		gbc.insets = new Insets(10,10,10,10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(nombreUsuario, gbc);
		
		gbc.insets = new Insets(10,10,10,10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(botonIniciar, gbc);
		
		
		
		panel.setBackground(new Color(30,30,30));
		panel.add(nombreUsiarioLabel);
		panel.add(nombreUsuario);
		panel.add(contraseñaLabel);
		panel.add(contraseña);
		panel.add(botonIniciar);
	
		add(panel);
		
	}
}
