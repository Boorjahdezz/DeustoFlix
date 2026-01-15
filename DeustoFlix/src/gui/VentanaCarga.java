package gui;

import javax.swing.*;
import java.awt.*;

public class VentanaCarga extends JDialog {

    private static final long serialVersionUID = 1L;
    private JProgressBar barra;

    public VentanaCarga() {
        super((Frame) null, true); 

        setSize(400, 150);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 20, 20));

        JLabel lbl = new JLabel("Iniciando sesión...");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        barra = new JProgressBar(0, 100);
        barra.setStringPainted(true);
        barra.setBackground(new Color(40, 40, 40));
        barra.setForeground(new Color(180, 0, 0));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(barra, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    public void iniciarCarga(Runnable alFinalizar) {

        
        SwingUtilities.invokeLater(() -> setVisible(true));

       
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    final int v = i;
                    SwingUtilities.invokeLater(() -> barra.setValue(v));
                    Thread.sleep(18);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            
            SwingUtilities.invokeLater(() -> {
                dispose();
                if (alFinalizar != null) alFinalizar.run();
            });
        }).start();
    }
}

