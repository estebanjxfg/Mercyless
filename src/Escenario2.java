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

        // =========================
        // COLISIONES BORDES MAPA
        // =========================
        if (player.x < 60) player.x = 60;
        if (player.y < 160) player.y = 160;

        if (player.x > background.getWidth() - 220)
            player.x = background.getWidth() - 220;

        if (player.y > background.getHeight() - 310)
            player.y = background.getHeight() - 310;

        // =========================
        // CÁMARA
        // =========================
        cameraX = player.x - (getWidth() / 2.0);
        cameraY = player.y - (getHeight() / 2.0);

        cameraX = Math.max(0, Math.min(cameraX, background.getWidth() - getWidth()));
        cameraY = Math.max(0, Math.min(cameraY, background.getHeight() - getHeight()));
    }

    // =========================
    // DIBUJO (UNDERTALE STYLE)
    // =========================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // =========================
        // BORDES NEGROS (LETTERBOX)
        // =========================
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // =========================
        // ESCALAR MAPA
        // =========================
        int bgW = background.getWidth();
        int bgH = background.getHeight();

        double scaleX = (double) getWidth() / bgW;
        double scaleY = (double) getHeight() / bgH;
        double scale = Math.min(scaleX, scaleY);

        int newW = (int) (bgW * scale);
        int newH = (int) (bgH * scale);

        int drawX = (getWidth() - newW) / 2;
        int drawY = (getHeight() - newH) / 2;

        // =========================
        // APLICAR CÁMARA AL MUNDO
        // =========================
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
            } catch (Exception e) {}
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
    // MAIN (PRUEBA)
    // =========================
    public static void main(String[] args) {

        JFrame frame = new JFrame("Escenario 2");

        Escenario2 game = new Escenario2(frame);

        frame.add(game);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}