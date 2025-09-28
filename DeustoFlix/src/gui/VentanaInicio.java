package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VentanaInicio extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 public VentanaInicio() {
		 
	        setTitle("Inicio");
	        setSize(1200, 800);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLayout(new BorderLayout());

	        // Panel principal con GridBagLayout para centrar contenido
	        JPanel panelPrincipal = new JPanel(new GridBagLayout());
	        panelPrincipal.setBackground(new Color(30, 30, 30)); // fondo oscuro

	        // Panel de botones (vertical)
	        JPanel panelBotones = new JPanel();
	        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
	        panelBotones.setOpaque(false);
	        /*
	        //imagen logo
	        ImageIcon imagen = new ImageIcon(getClass().getResource("/gui/Imagenes/DeustoFlixLogo.jpg"));
	        JLabel etiquetaImagen = new JLabel(imagen);
	        etiquetaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panelBotones.add(etiquetaImagen);
	        panelBotones.add(Box.createRigidArea(new Dimension(0,40)));
	        */
	        // Botones
	        JButton CrearCuenta = new JButton("Crear Cuenta");
	        JButton InicioSesion = new JButton("Iniciar Sesion");
	       // CrearCuenta.setForeground(Color.WHITE);
	        CrearCuenta.setBackground(new Color(50, 50, 50));
	        CrearCuenta.setFocusPainted(false);
	        CrearCuenta.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
           // InicioSesion.setForeground(Color.WHITE);
            InicioSesion.setBackground(new Color(50, 50, 50));
            InicioSesion.setFocusPainted(false);
            InicioSesion.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

	        Dimension tamanoBoton = new Dimension(400, 100); // ancho=400, alto=100
	        CrearCuenta.setPreferredSize(tamanoBoton);
	        CrearCuenta.setMaximumSize(tamanoBoton);
	        CrearCuenta.setMinimumSize(tamanoBoton);
	        InicioSesion.setPreferredSize(tamanoBoton);
	        InicioSesion.setMaximumSize(tamanoBoton);
	        InicioSesion.setMinimumSize(tamanoBoton);
	        CrearCuenta.setBackground(Color.WHITE);
	        InicioSesion.setBackground(Color.WHITE);

	        CrearCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
	        InicioSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        // Añadir botones y espacio entre ellos
	        panelBotones.add(InicioSesion);
	        panelBotones.add(Box.createRigidArea(new Dimension(0, 30))); // espacio entre botones
	        panelBotones.add(CrearCuenta);
	
	        
	        //funcionalidades a los botones
	        InicioSesion.addActionListener(e -> {
	        	VentanaInicioSesion ventanaInicioSesion = new VentanaInicioSesion();
	            ventanaInicioSesion.setVisible(true);
	            dispose();
	        });
	        CrearCuenta.addActionListener(e ->{
	        	VentanaCrearUsuario ventanaCrearUsuario = new VentanaCrearUsuario();
	        	ventanaCrearUsuario.setVisible(true);
	        	dispose();
	        	});

	        // Centrar el panel de botones en el panel principal
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.anchor = GridBagConstraints.CENTER;
	        panelPrincipal.add(panelBotones, gbc);

	        // Añadir al frame
	        add(panelPrincipal, BorderLayout.CENTER);
	    }
	}


