package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Panel reutilizable de verificación CAPTCHA.
 * 
 * Funcionalidades:
 * - Genera CAPTCHA gráfico aleatorio
 * - Valida el texto ingresado
 * - Limita intentos fallidos
 * - Bloquea el panel tras varios errores
 * - Muestra cuenta regresiva con barra de progreso
 */
public class CaptchaVerificationPanel extends JPanel {

    /* =========================
       CONSTANTES Y ATRIBUTOS
       ========================= */

    private static final long serialVersionUID = 1L;

    // Control de intentos
    private int intentosFallidos = 0;
    private boolean bloqueado = false;

    // CAPTCHA
    private String captchaCorrecto;

    // Componentes gráficos
    private JTextField campoCaptcha;
    private JLabel labelCaptcha;
    private JProgressBar progressBar;

    // Temporizadores
    private Timer timer;

    /* =========================
       CONSTRUCTOR
       ========================= */

    public CaptchaVerificationPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        inicializarComponentes();
        generarCaptcha();
    }

    /* =========================
       INICIALIZACIÓN DE UI
       ========================= */

    /**
     * Crea y configura todos los componentes gráficos
     */
    private void inicializarComponentes() {

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);

        // Etiqueta donde se muestra el CAPTCHA
        labelCaptcha = new JLabel("", SwingConstants.CENTER);
        labelCaptcha.setFont(new Font("Verdana", Font.BOLD, 24));
        labelCaptcha.setPreferredSize(new Dimension(300, 60));
        panelCentral.add(labelCaptcha, BorderLayout.CENTER);

        // Campo de texto para ingresar CAPTCHA
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

        // Barra de progreso (tiempo / bloqueo)
        progressBar = new JProgressBar(0, 10);
        progressBar.setForeground(new Color(102, 204, 255));
        progressBar.setBackground(new Color(15, 32, 50));
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(300, 25));

        add(progressBar, BorderLayout.NORTH);
    }

    /* =========================
       GENERACIÓN DE CAPTCHA
       ========================= */

    /**
     * Genera un CAPTCHA gráfico con ruido visual
     */
    public void generarCaptcha() {

        captchaCorrecto = generarCodigoAleatorio(5);
        int ancho = 20 + captchaCorrecto.length() * 20;

        BufferedImage imagenCaptcha =
                new BufferedImage(ancho, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenCaptcha.createGraphics();

        // Fondo
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, ancho, 50);

        // Texto
        g2d.setFont(new Font("Verdana", Font.BOLD, 30));
        g2d.setColor(Color.BLACK);
        g2d.drawString(captchaCorrecto, 10, 35);

        // Ruido visual
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)));
            g2d.drawLine(
                    random.nextInt(ancho),
                    random.nextInt(50),
                    random.nextInt(ancho),
                    random.nextInt(50));
        }

        for (int i = 0; i < 100; i++) {
            g2d.fillRect(
                    random.nextInt(ancho),
                    random.nextInt(50),
                    1, 1);
        }

        g2d.dispose();
        labelCaptcha.setIcon(new ImageIcon(imagenCaptcha));
    }

    /**
     * Genera una cadena aleatoria alfanumérica
     */
    private String generarCodigoAleatorio(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            codigo.append(
                    caracteres.charAt(
                            random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }

    /* =========================
       VALIDACIÓN DEL CAPTCHA
       ========================= */

    /**
     * Verifica si el CAPTCHA ingresado es correcto
     * 
     * @return true si es correcto, false si falla o está bloqueado
     */
    public boolean verificarCaptcha() {

        if (bloqueado) {
            return false;
        }

        String captchaUsuario =
                campoCaptcha.getText().toUpperCase().trim();

        if (captchaUsuario.isEmpty()) {
            return false;
        }

        if (captchaUsuario.equals(captchaCorrecto)) {
            intentosFallidos = 0;
            campoCaptcha.setText("");
            return true;
        }

        // CAPTCHA incorrecto
        intentosFallidos++;
        campoCaptcha.setText("");

        if (intentosFallidos >= 3) {
            iniciarBloqueo(10);
        } else {
            generarCaptcha();
        }

        return false;
    }

    /* =========================
       BLOQUEO Y TEMPORIZADORES
       ========================= */

    /**
     * Bloquea el CAPTCHA durante X segundos
     */
    private void iniciarBloqueo(int segundos) {

        bloqueado = true;
        campoCaptcha.setEnabled(false);

        progressBar.setMaximum(segundos);
        progressBar.setVisible(true);

        Timer bloqueoTimer = new Timer(1000, new ActionListener() {
            private int tiempoRestante = segundos;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    progressBar.setValue(tiempoRestante);
                    progressBar.setString(
                            "Bloqueado. Desbloqueo en: " + tiempoRestante + "s");
                    tiempoRestante--;
                } else {
                    desbloquear();
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        bloqueoTimer.start();
    }

    /**
     * Desbloquea el CAPTCHA y reinicia estado
     */
    private void desbloquear() {
        bloqueado = false;
        intentosFallidos = 0;
        campoCaptcha.setEnabled(true);
        progressBar.setVisible(false);
        generarCaptcha();
    }

    /**
     * Temporizador simple de 10 segundos
     */
    public void iniciarTemporizador() {

        progressBar.setMaximum(10);
        progressBar.setVisible(true);

        timer = new Timer(1000, new ActionListener() {
            private int tiempoRestante = 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    progressBar.setValue(tiempoRestante);
                    progressBar.setString(
                            "Tiempo restante: " + tiempoRestante + "s");
                    tiempoRestante--;
                } else {
                    progressBar.setVisible(false);
                    timer.stop();
                }
            }
        });

        timer.start();
    }

    /* =========================
       MÉTODOS DE UTILIDAD
       ========================= */

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public int getIntentosRestantes() {
        return Math.max(0, 3 - intentosFallidos);
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void limpiar() {
        campoCaptcha.setText("");
    }

    /**
     * Reinicia el componente a su estado inicial
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
