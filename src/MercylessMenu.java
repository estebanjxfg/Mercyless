import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MercylessMenu extends JFrame {

    // =========================
    // PANEL MENÚ
    // =========================
    private JPanel menuPanel;

    // =========================
    // BOTONES
    // =========================
    private JButton btnPlay;
    private JButton btnOptions;
    private JButton btnCredits;

    // =========================
    // PANTALLA COMPLETA
    // =========================
    private boolean pantallaCompleta = false;

    // =========================
    // CONSTRUCTOR
    // =========================
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

                // Fondo adaptado
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

        menuPanel.setLayout(null);

        // =========================
        // CREAR BOTONES
        // =========================
        btnPlay = crearBotonInvisible();

        btnOptions = crearBotonInvisible();

        btnCredits = crearBotonInvisible();

        // =========================
        // AGREGAR BOTONES
        // =========================
        menuPanel.add(btnPlay);

        menuPanel.add(btnOptions);

        menuPanel.add(btnCredits);

        // =========================
        // POSICIONES BOTONES
        // =========================
        actualizarBotones();

        // =========================
        // AJUSTAR AL REDIMENSIONAR
        // =========================
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {

                actualizarBotones();
            }
        });

        // =========================
        // BOTÓN PLAY
        // =========================
        btnPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // Crear juego
                PersonajeMov juego =
                        new PersonajeMov(MercylessMenu.this);

                // Cambiar contenido
                setContentPane(juego);

                revalidate();

                repaint();

                // Dar foco
                juego.requestFocusInWindow();
            }
        });

        // =========================
        // BOTÓN OPTIONS
        // =========================
        btnOptions.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ConfiguracionControles config =
                        new ConfiguracionControles(
                                MercylessMenu.this
                        );

                config.setVisible(true);

                setVisible(false);
            }
        });

        // =========================
        // BOTÓN CREDITS
        // =========================
        btnCredits.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(
                        null,
                        "MERCYLESS\n\nDesarrollado por Pandilla Momera Studios 😎"
                );
            }
        });

        // =========================
        // TECLAS
        // =========================
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                // =========================
                // F4 PANTALLA COMPLETA
                // =========================
                if (e.getKeyCode() == KeyEvent.VK_F4) {

                    pantallaCompleta = !pantallaCompleta;

                    if (pantallaCompleta) {

                        // Pantalla completa
                        setExtendedState(JFrame.MAXIMIZED_BOTH);

                    } else {

                        // Ventana normal
                        setExtendedState(JFrame.NORMAL);

                        setSize(1366, 768);

                        setLocationRelativeTo(null);
                    }

                    actualizarBotones();
                }

                // =========================
                // F3 CAMBIAR CONTROLES
                // =========================
                if (e.getKeyCode() == KeyEvent.VK_F3) {

                    ConfiguracionControles.cambiarControles();

                    String modo;

                    if (ConfiguracionControles.usarWASD) {

                        modo = "WASD";

                    } else {

                        modo = "FLECHAS";
                    }

                    JOptionPane.showMessageDialog(
                            null,
                            "Controles actuales: " + modo
                    );
                }
            }
        });

        setFocusable(true);

        // =========================
        // MOSTRAR MENÚ
        // =========================
        setContentPane(menuPanel);

        // IMPORTANTE
        requestFocusInWindow();
    }

    // =========================
    // BOTÓN INVISIBLE
    // =========================
    private JButton crearBotonInvisible() {

        JButton boton = new JButton();

        boton.setOpaque(false);

        boton.setContentAreaFilled(false);

        boton.setBorderPainted(false);

        boton.setFocusPainted(false);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    // =========================
    // POSICIONES BOTONES
    // =========================
    private void actualizarBotones() {

        int ancho = getWidth();

        int alto = getHeight();

        int x = (int) (ancho * 0.16);

        int anchoBoton = (int) (ancho * 0.28);

        int altoBoton = (int) (alto * 0.11);

        int yPlay = (int) (alto * 0.47);

        int yOptions = (int) (alto * 0.61);

        int yCredits = (int) (alto * 0.75);

        // PLAY
        btnPlay.setBounds(
                x,
                yPlay,
                anchoBoton,
                altoBoton
        );

        // OPTIONS
        btnOptions.setBounds(
                x,
                yOptions,
                anchoBoton,
                altoBoton
        );

        // CREDITS
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