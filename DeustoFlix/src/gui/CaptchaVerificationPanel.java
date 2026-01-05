package gui;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Componente de verificación CAPTCHA reutilizable con sistema de bloqueo
 */
public class CaptchaVerificationPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private int intentosFallidos = 0;
    private String captchaCorrecto;
    private JTextField campoCaptcha;
    private JLabel labelCaptcha;
    private JProgressBar progressBar;
    private Timer timer;
    private boolean bloqueado = false;

    public CaptchaVerificationPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        inicializarComponentes();
        generarCaptcha();
    }

    private void inicializarComponentes() {
        // Panel central con CAPTCHA
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);

        // Captcha label
        labelCaptcha = new JLabel("", SwingConstants.CENTER);
        labelCaptcha.setFont(new Font("Verdana", Font.BOLD, 24));
        labelCaptcha.setPreferredSize(new Dimension(300, 60));
        panelCentral.add(labelCaptcha, BorderLayout.CENTER);

        // Campo de texto
        campoCaptcha = new JTextField(10);
        campoCaptcha.setFont(new Font("Verdana", Font.PLAIN, 16));
        campoCaptcha.setHorizontalAlignment(SwingConstants.CENTER);
        campoCaptcha.setPreferredSize(new Dimension(200, 40));
        campoCaptcha.setBackground(new Color(50, 50, 50));
        campoCaptcha.setForeground(Color.WHITE);
        
        JPanel panelCampo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCampo.setOpaque(false);
        panelCampo.add(campoCaptcha);
        panelCentral.add(panelCampo, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);

        // Barra de progreso
        progressBar = new JProgressBar(0, 10);
        progressBar.setForeground(new Color(102, 204, 255));
        progressBar.setBackground(new Color(15, 32, 50));
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBar.setString("Tiempo restante: 10s");
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(300, 25));
        add(progressBar, BorderLayout.NORTH);
    }

    public void generarCaptcha() {
        captchaCorrecto = generarCodigoAleatorio(5);
        int ancho = 20 + captchaCorrecto.length() * 20;
        BufferedImage imagenCaptcha = new BufferedImage(ancho, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenCaptcha.createGraphics();

        // Fondo del Captcha
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, ancho, 50);

        // Texto del Captcha
        g2d.setFont(new Font("Verdana", Font.BOLD, 30));
        g2d.setColor(Color.BLACK);
        g2d.drawString(captchaCorrecto, 10, 35);

        // Ruido gráfico
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            int x1 = random.nextInt(ancho);
            int y1 = random.nextInt(50);
            int x2 = random.nextInt(ancho);
            int y2 = random.nextInt(50);
            g2d.drawLine(x1, y1, x2, y2);
        }
        for (int i = 0; i < 100; i++) {
            g2d.fillRect(random.nextInt(ancho), random.nextInt(50), 1, 1);
        }

        g2d.dispose();
        labelCaptcha.setIcon(new ImageIcon(imagenCaptcha));
    }

    private String generarCodigoAleatorio(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            captcha.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return captcha.toString();
    }

    /**
     * Verifica el CAPTCHA ingresado
     * @return true si es correcto, false si es incorrecto o está bloqueado
     */
    public boolean verificarCaptcha() {
        if (bloqueado) {
            return false;
        }

        String captchaUsuario = campoCaptcha.getText().toUpperCase().trim();
        
        if (captchaUsuario.isEmpty()) {
            return false;
        }

        if (captchaUsuario.equals(captchaCorrecto)) {
            // CAPTCHA correcto - resetear intentos
            intentosFallidos = 0;
            campoCaptcha.setText("");
            return true;
        } else {
            // CAPTCHA incorrecto
            intentosFallidos++;
            campoCaptcha.setText("");
            
            if (intentosFallidos >= 3) {
                // Bloquear después de 3 intentos fallidos
                iniciarBloqueo(10); // 10 segundos de bloqueo
            } else {
                // Regenerar CAPTCHA
                generarCaptcha();
            }
            
            return false;
        }
    }

    /**
     * Obtiene el número de intentos fallidos actuales
     */
    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    /**
     * Obtiene el número de intentos restantes
     */
    public int getIntentosRestantes() {
        return Math.max(0, 3 - intentosFallidos);
    }

    /**
     * Verifica si el componente está bloqueado
     */
    public boolean isBloqueado() {
        return bloqueado;
    }

    /**
     * Inicia un temporizador de bloqueo
     */
    private void iniciarBloqueo(int segundos) {
        bloqueado = true;
        campoCaptcha.setEnabled(false);
        progressBar.setMaximum(segundos);
        
        Timer bloqueoTimer = new Timer(1000, new ActionListener() {
            private int tiempoRestante = segundos;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    progressBar.setValue(tiempoRestante);
                    progressBar.setString("Bloqueado. Desbloqueo en: " + tiempoRestante + "s");
                    tiempoRestante--;
                } else {
                    // Desbloquear
                    bloqueado = false;
                    campoCaptcha.setEnabled(true);
                    progressBar.setVisible(false);
                    intentosFallidos = 0; // Resetear intentos
                    generarCaptcha(); // Generar nuevo CAPTCHA
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        progressBar.setVisible(true);
        bloqueoTimer.start();
    }

    /**
     * Inicia un temporizador 
     */
    public void iniciarTemporizador() {
        timer = new Timer(1000, new ActionListener() {
            private int tiempoRestante = 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    progressBar.setValue(tiempoRestante);
                    progressBar.setString("Tiempo restante: " + tiempoRestante + "s");
                    tiempoRestante--;
                } else {
                    campoCaptcha.setEnabled(true);
                    progressBar.setVisible(false);
                    timer.stop();
                }
            }
        });
        progressBar.setMaximum(10);
        progressBar.setVisible(true);
        timer.start();
    }

    /**
     * Limpia el campo de entrada
     */
    public void limpiar() {
        campoCaptcha.setText("");
    }

    /**
     * Resetea el componente a su estado inicial
     */
    public void reset() {
        intentosFallidos = 0;
        bloqueado = false;
        campoCaptcha.setEnabled(true);
        campoCaptcha.setText("");
        progressBar.setVisible(false);
        if (timer != null) {
            timer.stop();
        }
        generarCaptcha();
    }
}