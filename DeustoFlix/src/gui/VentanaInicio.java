package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import databases.ConexionBD;

public class VentanaInicio extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaInicio() {
        setTitle("DeustoFlix");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal con gradiente
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            
        	// CODIGO GENERADO CON IAG 
        	//Claude-4.5 Sin modificar
        	@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradiente de fondo oscuro moderno
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(10, 10, 15),
                    0, getHeight(), new Color(25, 25, 35)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Logo/Título en la parte superior
        JPanel panelHeader = new JPanel();
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(40, 0, 20, 0));
        
        JLabel lblLogo = new JLabel("DEUSTOFLIX");
        lblLogo.setFont(new Font("Arial Black", Font.BOLD, 56));
        lblLogo.setForeground(new Color(229, 9, 20)); // Netflix red
        panelHeader.add(lblLogo);

        // Panel central con los botones
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);

        // Texto de bienvenida
        JLabel lblBienvenida = new JLabel("Bienvenido a tu plataforma de streaming");
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 20));
        lblBienvenida.setForeground(new Color(220, 220, 220));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(lblBienvenida);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 50)));

        // Botón Iniciar Sesión
        JButton btnInicioSesion = crearBotonModerno("Iniciar Sesión", new Color(229, 9, 20), true);
        panelBotones.add(btnInicioSesion);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón Crear Cuenta
        JButton btnCrearCuenta = crearBotonModerno("Crear Cuenta", new Color(50, 50, 50), false);
        panelBotones.add(btnCrearCuenta);

        // Acciones de los botones
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
        panelCentro.add(panelBotones, gbc);

        // Panel footer con información adicional
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JLabel lblFooter = new JLabel("© 2026 DeustoFlix - Entretenimiento sin límites");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(140, 140, 140));
        panelFooter.add(lblFooter);

        // Añadir paneles al frame
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelFooter, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    /**
     * Crea un botón con estilo moderno y efectos hover
     */
    private JButton crearBotonModerno(String texto, Color colorBase, boolean isPrimario) {
        JButton boton = new JButton(texto) {
            private Color colorActual = colorBase;
            private Color colorHover = isPrimario ? 
                new Color(200, 8, 18) : // Rojo más oscuro para hover
                new Color(70, 70, 70);  // Gris más claro para hover

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar fondo redondeado
                g2d.setColor(colorActual);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Dibujar texto
                g2d.setColor(getForeground());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }

            {
                // Efectos hover
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        colorActual = colorHover;
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                        repaint();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        colorActual = colorBase;
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        repaint();
                    }
                });
            }
        };

        Dimension tamanoBoton = new Dimension(400, 65);
        boton.setPreferredSize(tamanoBoton);
        boton.setMaximumSize(tamanoBoton);
        boton.setMinimumSize(tamanoBoton);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setForeground(Color.WHITE);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);

        return boton;
    }


    public static void main(String[] args) {
        // Inicializamos la base de datos
        ConexionBD.inicializarBD();
        
        // Look and Feel moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            VentanaInicio ventana = new VentanaInicio();
            ventana.setVisible(true);
        });
    }
}