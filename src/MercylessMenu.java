import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MercylessMenu extends JFrame {

    private JPanel menuPanel;

    private JButton btnPlay;
    private JButton btnOptions;
    private JButton btnCredits;
    
    // IMÁGENES PLAY
    private ImageIcon playNormal;
    private ImageIcon playHover;
    private ImageIcon playClick;

    private boolean pantallaCompleta = false;

    public MercylessMenu() {

        setTitle("Mercyless");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuPanel = new JPanel() {

            Image fondo = new ImageIcon(
                    getClass().getResource("/Menu/84789.jpeg")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        menuPanel.setLayout(null);

        btnPlay = crearBotonInvisible();
        btnOptions = crearBotonInvisible();
        btnCredits = crearBotonInvisible();

        menuPanel.add(btnPlay);
        menuPanel.add(btnOptions);
        menuPanel.add(btnCredits);

        actualizarBotones();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarBotones();
            }
        });

        // =========================
        // CLICK PLAY
        // =========================
        btnPlay.addActionListener(e -> {

            Escenario1 juego = new Escenario1(MercylessMenu.this);

            setContentPane(juego);
            revalidate();
            repaint();

            juego.requestFocusInWindow();
        });

        // =========================
        // HOVER / CLICK EFFECT PLAY
        // =========================
        btnPlay.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btnPlay.setIcon(playHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPlay.setIcon(playNormal);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btnPlay.setIcon(playClick);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btnPlay.setIcon(playHover);
            }
        });

        // =========================
        // TECLAS
        // =========================
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_F4) {

                    pantallaCompleta = !pantallaCompleta;

                    if (pantallaCompleta) {
                        setExtendedState(JFrame.MAXIMIZED_BOTH);
                    } else {
                        setExtendedState(JFrame.NORMAL);
                        setSize(1366, 768);
                        setLocationRelativeTo(null);
                    }

                    actualizarBotones();
                }
            }
        });

        setFocusable(true);

        setContentPane(menuPanel);
        requestFocusInWindow();
    }

    // =========================
    // BOTÓN INVISIBLE
    // =========================
    private JButton crearBotonInvisible() {

        JButton b = new JButton();
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // =========================
    // ESCALAR IMAGEN
    // =========================
    private ImageIcon escalarIcon(String ruta, int ancho, int alto) {

        Image img = new ImageIcon(getClass().getResource(ruta))
                .getImage()
                .getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

        return new ImageIcon(img);
    }

    // =========================
    // POSICIONES + IMÁGENES
    // =========================
    private void actualizarBotones() {

        int ancho = getWidth();
        int alto = getHeight();

        int x = (int) (ancho * 0.08);

        int anchoBoton = (int) (ancho * 0.31);
        int altoBoton = (int) (alto * 0.12);

        btnPlay.setBounds(x, (int)(alto * 0.41), anchoBoton, altoBoton);
        btnOptions.setBounds(x, (int)(alto * 0.61), anchoBoton, altoBoton);
        btnCredits.setBounds(x, (int)(alto * 0.75), anchoBoton, altoBoton);

        // =========================
        // CARGA IMÁGENES ESCALADAS
        // =========================
        playNormal = escalarIcon("/MenuBot/play_normal.png", anchoBoton, altoBoton);
        playHover  = escalarIcon("/MenuBot/play_hover.png", anchoBoton, altoBoton);
        playClick  = escalarIcon("/MenuBot/play_click.png", anchoBoton, altoBoton);

        btnPlay.setIcon(playNormal);
    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MercylessMenu().setVisible(true));
    }
}