import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConfiguracionControles extends JFrame {

    // =========================
    // CONTROLES
    // =========================
    public static boolean usarWASD = true;

    // =========================
    // BOTONES
    // =========================
    JButton btnRegresar;

    // =========================
    // PANEL
    // =========================
    JPanel panel;

    public ConfiguracionControles(JFrame menuFrame) {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("Opciones");

        setSize(1366, 768);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =========================
        // PANEL CON FONDO
        // =========================
        panel = new JPanel() {

            Image fondo = new ImageIcon(
                    getClass().getResource("/Menu/Controles2.png")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                g.drawImage(
                        fondo,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );
            }
        };

        panel.setLayout(null);

        

        // =========================
        // TEXTO CONTROLES
        // =========================
        JLabel texto = new JLabel();

        texto.setForeground(Color.WHITE);

        texto.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        actualizarTexto(texto);

        texto.setBounds(430, 300, 700, 50);

        panel.add(texto);

        // =========================
        // TEXTO F3
        // =========================
        JLabel cambiar = new JLabel(
                "Presiona F3 para cambiar"
        );

        cambiar.setForeground(Color.LIGHT_GRAY);

        cambiar.setFont(
                new Font("Arial", Font.PLAIN, 22)
        );

        cambiar.setBounds(450, 380, 500, 40);

        panel.add(cambiar);

        // =========================
        // BOTÓN REGRESAR
        // =========================
        btnRegresar = new JButton("←");

        btnRegresar.setBounds(20, 20, 60, 40);

        btnRegresar.setFocusPainted(false);

        btnRegresar.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        panel.add(btnRegresar);

        btnRegresar.addActionListener(e -> {

            dispose();

            menuFrame.setVisible(true);
        });

        // =========================
        // TECLAS
        // =========================
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                // F3 CAMBIAR CONTROLES
                if (e.getKeyCode() == KeyEvent.VK_F3) {

                    cambiarControles();

                    actualizarTexto(texto);
                }
            }
        });

        setFocusable(true);

        setContentPane(panel);

        requestFocusInWindow();
    }

    // =========================
    // ACTUALIZAR TEXTO
    // =========================
    private void actualizarTexto(JLabel texto) {

        if (usarWASD) {

            texto.setText(
                    "Controles actuales: WASD"
            );

        } else {

            texto.setText(
                    "Controles actuales: FLECHAS"
            );
        }
    }

    // =========================
    // CAMBIAR CONTROLES
    // =========================
    public static void cambiarControles() {

        usarWASD = !usarWASD;
    }
}
// hola papus, me olvide en cambiar el nombre del commit xdxdxd