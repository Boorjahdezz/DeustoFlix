package gui;

import javax.swing.*;
import java.awt.*;

public class MainGuiWindow extends JFrame {

    public MainGuiWindow() {
        setTitle("DeustoFlix");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior (Header)
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 30, 30));
        JLabel title = new JLabel("DeustoFlix");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Panel lateral (Menú)
        JPanel sideMenu = new JPanel();
        sideMenu.setBackground(new Color(40, 40, 40));
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        String[] menuItems = {"Inicio", "Películas", "Series", "Mi Perfil", "Salir"};
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sideMenu.add(btn);
            sideMenu.add(Box.createVerticalStrut(10));
        }
        add(sideMenu, BorderLayout.WEST);

        // Panel central (Contenido)
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(50, 50, 50));
        JLabel contentLabel = new JLabel("Bienvenido a DeustoFlix");
        contentLabel.setForeground(Color.WHITE);
        contentLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        contentPanel.add(contentLabel);
        add(contentPanel, BorderLayout.CENTER);

        // Panel inferior (Footer)
        JPanel footer = new JPanel();
        footer.setBackground(new Color(30, 30, 30));
        JLabel footerLabel = new JLabel("© 2025 DeustoFlix");
        footerLabel.setForeground(Color.WHITE);
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGuiWindow().setVisible(true);
        });
    }
}