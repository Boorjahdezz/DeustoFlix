package gui;

import java.awt.*;
import javax.swing.*;

import databases.ConexionBD;

public class VentanaInicio extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaInicio() {
        setTitle("Inicio");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(30, 30, 30));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);

        JButton btnCrearCuenta = new JButton("Crear Cuenta");
        JButton btnInicioSesion = new JButton("Iniciar Sesion");

        Dimension tamanoBoton = new Dimension(400, 100);
        btnCrearCuenta.setPreferredSize(tamanoBoton);
        btnInicioSesion.setPreferredSize(tamanoBoton);
        btnCrearCuenta.setMaximumSize(tamanoBoton);
        btnInicioSesion.setMaximumSize(tamanoBoton);
        btnCrearCuenta.setMinimumSize(tamanoBoton);
        btnInicioSesion.setMinimumSize(tamanoBoton);

        btnCrearCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInicioSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCrearCuenta.setBackground(Color.WHITE);
        btnInicioSesion.setBackground(Color.WHITE);

        panelBotones.add(btnInicioSesion);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 30)));
        panelBotones.add(btnCrearCuenta);

        btnInicioSesion.addActionListener(e -> {
            new VentanaInicioSesion().setVisible(true);
            dispose();
        });

        btnCrearCuenta.addActionListener(e -> {
            new VentanaCrearUsuario().setVisible(true);
            dispose();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(panelBotones, gbc);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Inicializamos la base de datos
        ConexionBD.inicializarBD();
        SwingUtilities.invokeLater(() -> new VentanaInicio().setVisible(true));
    }
}


