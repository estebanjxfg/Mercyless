import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MercylessMenu extends JFrame {

    private JPanel menuPanel;
    private JPanel gamePanel;

    private JButton btnPlay;
    private JButton btnOptions;
    private JButton btnCredits;

    private boolean pantallaCompleta = false;

    public MercylessMenu() {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("Mercyless");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =========================
        // PANEL MENÚ
        // =========================
        menuPanel = new JPanel() {

            Image fondo = new ImageIcon(
                    getClass().getResource("/Menu/84789.jpeg")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Dibujar fondo adaptado
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        menuPanel.setLayout(null);

        // =========================
        // BOTONES TRANSPARENTES
        // =========================
        btnPlay = crearBotonInvisible();
        btnOptions = crearBotonInvisible();
        btnCredits = crearBotonInvisible();

        menuPanel.add(btnPlay);
        menuPanel.add(btnOptions);
        menuPanel.add(btnCredits);

        // =========================
        // POSICIONAR BOTONES
        // =========================
        actualizarBotones();

        // Reacomodar cuando cambie tamaño
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarBotones();
            }
        });

        // =========================
        // PANEL JUEGO
        // =========================
        gamePanel = new JPanel();
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setLayout(new BorderLayout());

        JLabel texto = new JLabel(
                "EL JUEGO HA COMENZADO",
                SwingConstants.CENTER
        );

        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("Arial", Font.BOLD, 40));

        gamePanel.add(texto, BorderLayout.CENTER);

        // =========================
        // BOTÓN PLAY
        // =========================
        btnPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setContentPane(gamePanel);

                revalidate();
                repaint();
            }
        });

        // =========================
        // F4 = PANTALLA COMPLETA
        // =========================
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_F4) {

                    pantallaCompleta = !pantallaCompleta;

                    dispose();

                    if (pantallaCompleta) {

                        setUndecorated(true);
                        setExtendedState(JFrame.MAXIMIZED_BOTH);

                    } else {

                        setUndecorated(false);
                        setSize(1366, 768);
                        setLocationRelativeTo(null);
                    }

                    setVisible(true);

                    actualizarBotones();
                }
            }
        });

        setFocusable(true);

        // Mostrar menú
        setContentPane(menuPanel);
    }

    // =========================
    // CREAR BOTÓN INVISIBLE
    // =========================
    private JButton crearBotonInvisible() {

        JButton boton = new JButton();

        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    // =========================
    // ACTUALIZAR POSICIÓN BOTONES
    // =========================
    private void actualizarBotones() {

        int ancho = getWidth();
        int alto = getHeight();

        /*
           Coordenadas proporcionales
           basadas en la imagen original
           1366 x 768
        */

        int x = (int) (ancho * 0.16);

        int anchoBoton = (int) (ancho * 0.28);
        int altoBoton = (int) (alto * 0.11);

        int yPlay = (int) (alto * 0.47);
        int yOptions = (int) (alto * 0.61);
        int yCredits = (int) (alto * 0.75);

        btnPlay.setBounds(x, yPlay, anchoBoton, altoBoton);

        btnOptions.setBounds(
                x,
                yOptions,
                anchoBoton,
                altoBoton
        );

        btnCredits.setBounds(
                x,
                yCredits,
                anchoBoton,
                altoBoton
        );
    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                new MercylessMenu().setVisible(true);
            }
        });
    }
}