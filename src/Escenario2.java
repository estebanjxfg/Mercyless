import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Escenario2 extends JPanel implements KeyListener, Runnable {

    JFrame frame;

    Player player = new Player();

    BufferedImage background;

    // =========================
    // CÁMARA
    // =========================
    double cameraX = 0;
    double cameraY = 0;

    public Escenario2(JFrame frame) {

        this.frame = frame;

        setPreferredSize(new Dimension(1440, 900));
        setBackground(Color.BLACK);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(this);

        try {
            background = ImageIO.read(
                    getClass().getResource("/Sprites/Escenario2.jpeg")
            );
        } catch (Exception e) {
            System.out.println("Error cargando Escenario2.jpeg");
            e.printStackTrace();
        }

        new Thread(this).start();

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    // =========================
    // UPDATE
    // =========================
    public void update() {

        player.update();

        if (background == null) return; // evita crashes

        // =========================
        // COLISIONES BORDES MAPA
        // =========================
        player.x = Math.max(60, player.x);
        player.y = Math.max(160, player.y);

        player.x = Math.min(player.x, background.getWidth() - 220);
        player.y = Math.min(player.y, background.getHeight() - 310);

        // =========================
        // CÁMARA
        // =========================
        cameraX = player.x - (getWidth() / 2.0);
        cameraY = player.y - (getHeight() / 2.0);

        cameraX = Math.max(0, Math.min(cameraX, background.getWidth() - getWidth()));
        cameraY = Math.max(0, Math.min(cameraY, background.getHeight() - getHeight()));
    }

    // =========================
    // DIBUJO
    // =========================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (background == null) return;

        int bgW = background.getWidth();
        int bgH = background.getHeight();

        double scaleX = (double) getWidth() / bgW;
        double scaleY = (double) getHeight() / bgH;
        double scale = Math.min(scaleX, scaleY);

        int newW = (int) (bgW * scale);
        int newH = (int) (bgH * scale);

        int drawX = (getWidth() - newW) / 2;
        int drawY = (getHeight() - newH) / 2;

        int worldX = drawX - (int) cameraX;
        int worldY = drawY - (int) cameraY;

        g2.drawImage(background, worldX, worldY, newW, newH, null);

        // =========================
        // JUGADOR
        // =========================
        int playerSize = (int) (164 * scale);

        int playerX = (int) (player.x - cameraX);
        int playerY = (int) (player.y - cameraY);

        g2.drawImage(player.currentSprite, playerX, playerY, playerSize, playerSize, null);
    }

    // =========================
    // LOOP
    // =========================
    @Override
    public void run() {

        while (true) {
            update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (Exception ignored) {}
        }
    }

    // =========================
    // INPUT
    // =========================
    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) player.up = true;
        if (key == KeyEvent.VK_S) player.down = true;
        if (key == KeyEvent.VK_A) player.left = true;
        if (key == KeyEvent.VK_D) player.right = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) player.up = false;
        if (key == KeyEvent.VK_S) player.down = false;
        if (key == KeyEvent.VK_A) player.left = false;
        if (key == KeyEvent.VK_D) player.right = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // =========================
    // MAIN SOLO PARA PRUEBA
    // =========================
    public static void main(String[] args) {

        JFrame frame = new JFrame("Escenario 2");

        Escenario2 game = new Escenario2(frame);

        frame.setContentPane(game); // mejor práctica
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}