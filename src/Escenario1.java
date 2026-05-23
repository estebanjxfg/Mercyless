import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Escenario1 extends JPanel
        implements KeyListener, Runnable {

    // =========================
    // VENTANA
    // =========================
    JFrame frame;

    // =========================
    // JUGADOR
    // =========================
    Player player = new Player();

    // =========================
    // FONDO
    // =========================
    BufferedImage background;

    // =========================
    // PANTALLA COMPLETA
    // =========================
    boolean fullscreen = false;

    public Escenario1(JFrame frame) {

        this.frame = frame;

        setPreferredSize(new Dimension(640, 480));

        setBackground(Color.BLACK);

        setFocusable(true);

        addKeyListener(this);

        try {

            // =========================
            // ESCENARIO
            // =========================
            background = ImageIO.read(
                    getClass().getResource("/Sprites/Escenario1.jpeg")
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        Thread gameThread = new Thread(this);

        gameThread.start();
    }

    // =========================
    // UPDATE
    // =========================
    public void update() {

        player.update();
    }

    // =========================
    // PANTALLA COMPLETA
    // =========================
    public void toggleFullscreen() {

        GraphicsDevice device =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice();

        fullscreen = !fullscreen;

        frame.dispose();

        if (fullscreen) {

            frame.setUndecorated(true);

            device.setFullScreenWindow(frame);

        } else {

            device.setFullScreenWindow(null);

            frame.setUndecorated(false);

            frame.setSize(640, 480);

            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        }
    }

    // =========================
    // DIBUJAR
    // =========================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // =========================
        // FONDO NEGRO
        // =========================
        g2.setColor(Color.BLACK);

        g2.fillRect(0, 0, getWidth(), getHeight());

        // =========================
        // TAMAÑO ORIGINAL ESCENARIO
        // =========================
        int bgWidth = background.getWidth();

        int bgHeight = background.getHeight();

        // =========================
        // ESCALA PROPORCIONAL
        // =========================
        double scaleX = (double) getWidth() / bgWidth;

        double scaleY = (double) getHeight() / bgHeight;

        double scale = Math.min(scaleX, scaleY);

        // =========================
        // NUEVO TAMAÑO
        // =========================
        int newWidth = (int) (bgWidth * scale);

        int newHeight = (int) (bgHeight * scale);

        // =========================
        // CENTRAR
        // =========================
        int drawX = (getWidth() - newWidth) / 2;

        int drawY = (getHeight() - newHeight) / 2;

        // =========================
        // DIBUJAR ESCENARIO
        // =========================
        g2.drawImage(
                background,
                drawX,
                drawY,
                newWidth,
                newHeight,
                null
        );

        // =========================
        // ESCALA DEL JUGADOR
        // =========================
        int playerSize = (int) (164 * scale);

        // =========================
        // POSICIÓN ESCALADA
        // =========================
        int playerX = drawX + (int)(player.x * scale);

        int playerY = drawY + (int)(player.y * scale);

        // =========================
        // DIBUJAR JUGADOR
        // =========================
        g2.drawImage(
                player.currentSprite,
                playerX,
                playerY,
                playerSize,
                playerSize,
                null
        );
    }

    // =========================
    // GAME LOOP
    // =========================
    @Override
    public void run() {

        while (true) {

            update();

            repaint();

            try {

                Thread.sleep(16);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    // =========================
    // TECLAS
    // =========================
    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        // Movimiento
        if (key == KeyEvent.VK_W)
            player.up = true;

        if (key == KeyEvent.VK_S)
            player.down = true;

        if (key == KeyEvent.VK_A)
            player.left = true;

        if (key == KeyEvent.VK_D)
            player.right = true;

        // Pantalla completa
        if (key == KeyEvent.VK_F4)
            toggleFullscreen();
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W)
            player.up = false;

        if (key == KeyEvent.VK_S)
            player.down = false;

        if (key == KeyEvent.VK_A)
            player.left = false;

        if (key == KeyEvent.VK_D)
            player.right = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {

        JFrame frame = new JFrame("Escenario 1");

        Escenario1 game = new Escenario1(frame);

        frame.add(game);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);

        frame.setVisible(true);
    }
}
    
