package gui.avatar;

import gui.MainGuiWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VentanaSeleccionAvatar extends JFrame {

    public VentanaSeleccionAvatar(String usuario) {
        setTitle("Elige tu avatar");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Elige una foto de perfil", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        titulo.setOpaque(true);
        titulo.setBackground(new Color(25, 25, 25));
        add(titulo, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 15, 15));
        grid.setBackground(new Color(20, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<ImageIcon> opciones = cargarAvataresDesdeRecursos();
        if (opciones.isEmpty()) {
            opciones = crearAvatares(usuario); // fallback con inicial si no hay imágenes
        }
        for (ImageIcon icon : opciones) {
            JButton b = new JButton(escalarIcono(icon, 120, 120));
            b.setBackground(new Color(35, 35, 35));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            b.addActionListener(e -> {
                UserSession.set(usuario, icon);
                new MainGuiWindow(usuario, icon).setVisible(true);
                dispose();
            });
            grid.add(b);
        }

        add(grid, BorderLayout.CENTER);

        getContentPane().setBackground(new Color(20, 20, 20));
    }

    private List<ImageIcon> cargarAvataresDesdeRecursos() {
        String[] nombres = {
                "avatar1.png", "avatar2.png", "avatar3.png", "avatar4.png",
                "avatar5.png", "avatar6.png", "avatar7.png", "avatar8.png"
        };
        List<ImageIcon> lista = new ArrayList<>();
        ClassLoader cl = getClass().getClassLoader();
        for (String nombre : nombres) {
            String[] rutas = {
                    nombre,                       // si están directamente en resources/
                    "Imagenes/" + nombre,        // si están en resources/Imagenes/
                    "resources/" + nombre,       // por si el IDE copia con prefijo
                    "resources/Imagenes/" + nombre
            };
            for (String ruta : rutas) {
                var url = cl.getResource(ruta);
                if (url != null) {
                    lista.add(new ImageIcon(url));
                    break;
                }
            }
        }
        return lista;
    }

    private ImageIcon escalarIcono(ImageIcon icon, int w, int h) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) return icon;
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private List<ImageIcon> crearAvatares(String usuario) {
        String inicial = usuario != null && !usuario.isBlank() ? usuario.substring(0, 1).toUpperCase() : "U";
        Color[] colores = {
                new Color(0x5C7CFA),
                new Color(0xFF6B6B),
                new Color(0x51CF66),
                new Color(0xFCC419),
                new Color(0x845EF7),
                new Color(0x15AABF)
        };
        List<ImageIcon> lista = new ArrayList<>();
        for (Color c : colores) {
            lista.add(new ImageIcon(crearImagenAvatar(inicial, c)));
        }
        return lista;
    }

    private Image crearImagenAvatar(String letra, Color color) {
        int size = 96;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 46));
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(letra)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(letra, x, y);
        g2.dispose();
        return img;
    }
}
