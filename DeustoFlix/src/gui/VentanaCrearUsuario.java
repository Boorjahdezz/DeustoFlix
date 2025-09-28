package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaCrearUsuario extends JFrame {

public VentanaCrearUsuario() {
	setSize(1200,800);
	setTitle("Crear Usuario");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setLayout(new BorderLayout());
	
	
	JButton exit = new JButton("Exit");
	exit.setPreferredSize(new Dimension(75,25));
	exit.setFont(new Font("Arial", Font.BOLD, 16));
	exit.addActionListener(e ->{
		VentanaInicio ventanaInicio = new VentanaInicio();
		ventanaInicio.setVisible(true);
		dispose();
	});
	JPanel panelExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
	panelExit.setBackground(new Color(30,30,30));
	panelExit.add(exit);
	add(panelExit,(BorderLayout.NORTH));
	
}

	

}
