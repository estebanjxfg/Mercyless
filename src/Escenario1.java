import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Escenario1 extends JPanel implements KeyListener, Runnable {

    JFrame frame;

    Player player = new Player();

    BufferedImage background;

    boolean fullscreen = false;

    // =========================
    // PUERTA
    // =========================
    Rectangle puerta = new Rectangle(460, 600, 120, 160);

    boolean cercaPuerta = false;

    // =========================
    // FADE SYSTEM
    // =========================
    boolean fadeOut = false;
    float fadeAlpha = 0f;

    public Escenario1(JFrame frame) {

        this.frame = frame;

        setPreferredSize(new Dimension(1440, 900));
        setBackground(Color.BLACK);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(this);

        try {
            background = ImageIO.read(
                    getClass().getResource("/Sprites/Escenario1.jpeg")
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
        // COLISIONES MAPA
        // =========================
        if (player.x < 40) player.x = 40;
        if (player.y < 160) player.y = 160;

        if (player.x > background.getWidth() - 650)
            player.x = background.getWidth() - 650;

        if (player.y > background.getHeight() - 700)
            player.y = background.getHeight() - 700;

        // =========================
        // HITBOX PLAYER
        // =========================
        Rectangle playerHitbox = new Rectangle(
                (int) player.x,
                (int) player.y,
                60,
                60
        );

        cercaPuerta = playerHitbox.intersects(puerta);

        // =========================
        // FADE LOGIC
        // =========================
        if (fadeOut) {

            fadeAlpha += 0.05f;

            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                cambiarEscena();
            }
        }
    }

    // =========================
    // CAMBIO DE ESCENA
    // =========================
    private boolean escenaCambiada = false;

public void cambiarEscena() {
    // 1. Verificas si ya se cambió la escena
    if (escenaCambiada) {
        return; // Termina la ejecución del método aquí
    }

    // 2. Si no se ha cambiado, ejecutas tu código normal
    Escenario2 escena2 = new Escenario2(frame);

    frame.setContentPane(escena2);
    frame.revalidate();
    frame.repaint();

    escena2.requestFocusInWindow();

    // 3. Marcas la bandera como verdadera para bloquear futuros intentos
    escenaCambiada = true; 
}

    // =========================
    // DIBUJO
    // =========================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // fondo negro (bordes estilo Undertale)
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int bgW = background.getWidth();
        int bgH = background.getHeight();

        double scaleX = (double) getWidth() / bgW;
        double scaleY = (double) getHeight() / bgH;
        double scale = Math.min(scaleX, scaleY);

        int newW = (int) (bgW * scale);
        int newH = (int) (bgH * scale);

        int drawX = (getWidth() - newW) / 2;
        int drawY = (getHeight() - newH) / 2;

        // mapa
        g2.drawImage(background, drawX, drawY, newW, newH, null);

        // puerta (DEBUG rojo)
        g2.setColor(Color.RED);
        g2.drawRect(drawX + puerta.x, drawY + puerta.y, puerta.width, puerta.height);

        // jugador
        g2.drawImage(
                player.currentSprite,
                drawX + (int) player.x,
                drawY + (int) player.y,
                80,
                80,
                null
        );

        // =========================
        // FADE NEGRO
        // =========================
        if (fadeOut) {

            g2.setColor(new Color(0, 0, 0, fadeAlpha));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
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

        // =========================
        // INTERACCIÓN E
        // =========================
        if (key == KeyEvent.VK_E && cercaPuerta && !fadeOut) {
            fadeOut = true;
        }

        if (key == KeyEvent.VK_F4) toggleFullscreen();
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
    // FULLSCREEN
    // =========================
    public void toggleFullscreen() {

        GraphicsDevice device =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice();

        fullscreen = !fullscreen;

        frame.dispose();

        if (fullscreen) {

            frame.setUndecorated(true);
            device.setFullScreenWindow(frame);

        } else {

            device.setFullScreenWindow(null);

            frame.setUndecorated(false);
            frame.setSize(1440, 900);
            frame.setLocationRelativeTo(null);
        }

        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }
}