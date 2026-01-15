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

  
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
              
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
        lblLogo.setForeground(new Color(229, 9, 20)); 
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
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JLabel lblFooter = new JLabel("© 2026 DeustoFlix - Entretenimiento sin límites");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(140, 140, 140));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        
        // ===== DISCREET ADMIN BUTTON =====
        JButton btnAdmin = new JButton("⚙");
        btnAdmin.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAdmin.setForeground(new Color(80, 80, 80)); 
        btnAdmin.setBackground(new Color(20, 20, 25));
        btnAdmin.setBorderPainted(false);
        btnAdmin.setFocusPainted(false);
        btnAdmin.setPreferredSize(new Dimension(35, 35));
        btnAdmin.setToolTipText("Admin Access");
        btnAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
    
        btnAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdmin.setForeground(new Color(229, 9, 20));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdmin.setForeground(new Color(80, 80, 80));
            }
        });
        
        btnAdmin.addActionListener(e -> mostrarLoginAdmin());
        
      
        JPanel footerContent = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerContent.setOpaque(false);
        footerContent.add(lblFooter);
        
        JPanel adminButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adminButtonPanel.setOpaque(false);
        adminButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
        adminButtonPanel.add(btnAdmin);
        
        panelFooter.add(footerContent, BorderLayout.CENTER);
        panelFooter.add(adminButtonPanel, BorderLayout.EAST);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelFooter, BorderLayout.SOUTH);

        add(panelPrincipal);
    }
    
    private void mostrarLoginAdmin() {
        JDialog loginDialog = new JDialog(this, "Admin Access", true);
        loginDialog.setSize(400, 250);
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 35));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
       
        JLabel lblTitle = new JLabel("🔐 Administrator Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(229, 9, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);
        
        
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblUser, gbc);
        
        JTextField txtUser = new JTextField(15);
        txtUser.setBackground(new Color(50, 50, 55));
        txtUser.setForeground(Color.WHITE);
        txtUser.setCaretColor(Color.WHITE);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 85)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtUser, gbc);
        
    
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPass, gbc);
        
        JPasswordField txtPass = new JPasswordField(15);
        txtPass.setBackground(new Color(50, 50, 55));
        txtPass.setForeground(Color.WHITE);
        txtPass.setCaretColor(Color.WHITE);
        txtPass.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 85)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtPass, gbc);
        
    
        JButton btnLogin = new JButton("ACCESS");
        btnLogin.setBackground(new Color(229, 9, 20));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(btnLogin, gbc);
        
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            
            if (ConexionBD.esAdmin(user, pass)) {
                loginDialog.dispose();
                new VentanaAdmin().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog, 
                    "Invalid credentials", 
                    "Access Denied", 
                    JOptionPane.ERROR_MESSAGE);
                txtPass.setText("");
            }
        });
        
    
        txtPass.addActionListener(e -> btnLogin.doClick());
        
        loginDialog.add(panel, BorderLayout.CENTER);
        loginDialog.setVisible(true);
    }


    private JButton crearBotonModerno(String texto, Color colorBase, boolean isPrimario) {
        JButton boton = new JButton(texto) {
            private Color colorActual = colorBase;
            private Color colorHover = isPrimario ? 
                new Color(200, 8, 18) : 
                new Color(70, 70, 70);  

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                
                g2d.setColor(colorActual);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                
                g2d.setColor(getForeground());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }

            {
                
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
       
        ConexionBD.inicializarBD();
        
       
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