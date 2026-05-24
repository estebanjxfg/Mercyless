import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Escenario1 extends JPanel implements KeyListener, Runnable {

    JFrame frame;

    Player player = new Player();

    BufferedImage background;

    // =========================
    // TAMAÑO FIJO DEL MUNDO
    // =========================
    final int WORLD_W = 1440;
    final int WORLD_H = 900;

    // =========================
    // PUERTA
    // =========================
    Rectangle puerta = new Rectangle(690, 900, 160, 160);

    boolean cercaPuerta = false;

    // =========================
    // FADE
    // =========================
    boolean fadeOut = false;
    float fadeAlpha = 0f;

    // =========================
    // ESC PARA CERRAR
    // =========================
    Timer escTimer;

    long escInicio = 0;

    boolean mostrandoBarraESC = false;

    // =========================
    // CONTROL CAMBIO ESCENA
    // =========================
    private boolean escenaCambiada = false;

    public Escenario1(JFrame frame) {

        this.frame = frame;

        setPreferredSize(new Dimension(WORLD_W, WORLD_H));

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

        // =========================
        // TIMER ESC
        // =========================
        escTimer = new Timer(16, e -> {

            long tiempo =
                    System.currentTimeMillis() - escInicio;

            if (tiempo >= 2000) {

                System.exit(0);
            }

            repaint();
        });

        new Thread(this).start();

        requestFocus();

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    // =========================
    // UPDATE
    // =========================
    public void update() {

        player.update();

        // =========================
        // COLISIONES FIJAS
        // =========================
        if (player.x < 70)
            player.x = 70;

        if (player.y < 240)
            player.y = 240;

        if (player.x > WORLD_W - 500)
            player.x = WORLD_W - 500;

        if (player.y > WORLD_H - 35)
            player.y = WORLD_H - 35;

        // =========================
        // HITBOX PLAYER
        // =========================
        Rectangle playerHitbox = new Rectangle(
                player.x,
                player.y,
                60,
                60
        );

        cercaPuerta = playerHitbox.intersects(puerta);

        // =========================
        // FADE
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
    // CAMBIO ESCENA
    // =========================
    public void cambiarEscena() {

        if (escenaCambiada)
            return;

        Escenario2 escena2 = new Escenario2(frame);

        frame.setContentPane(escena2);

        frame.revalidate();

        frame.repaint();

        escena2.requestFocusInWindow();

        escenaCambiada = true;
    }

    // =========================
    // DIBUJO
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
        // CENTRAR JUEGO
        // =========================
        int drawX = (getWidth() - WORLD_W) / 2;

        int drawY = (getHeight() - WORLD_H) / 2;

    // =========================
    // MAPA SIN DEFORMAR
    // =========================
    int bgW = background.getWidth();
    int bgH = background.getHeight();

        double scaleX = (double) getWidth() / bgW;
        double scaleY = (double) getHeight() / bgH;

        double scale = Math.min(scaleX, scaleY);

        int newW = (int) (bgW * scale);
        int newH = (int) (bgH * scale);

        drawX = (getWidth() - newW) / 2;
        drawY = (getHeight() - newH) / 2;

        g2.drawImage(
        background,
        drawX,
        drawY,
        newW,
        newH,
        null
);

        // =========================
        // DEBUG PUERTA
        // =========================
        g2.setColor(Color.RED);

        g2.drawRect(
                drawX + puerta.x,
                drawY + puerta.y,
                puerta.width,
                puerta.height
        );

        // =========================
        // PLAYER
        // =========================
        g2.drawImage(
                player.currentSprite,
                drawX + player.x,
                drawY + player.y,
                80,
                80,
                null
        );

        // =========================
        // FADE
        // =========================
        if (fadeOut) {

            g2.setColor(
                    new Color(0, 0, 0, fadeAlpha)
            );

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );
        }

        // =========================
        // BARRA ESC
        // =========================
        if (mostrandoBarraESC) {

            long tiempo =
                    System.currentTimeMillis()
                            - escInicio;

            int barra =
                    (int)((tiempo / 2000.0) * 150);

            if (barra > 300)
                barra = 300;

            g.setColor(Color.BLACK);

            g.fillRect(20, 20, 320, 30);

            g.setColor(Color.WHITE);

            g.drawRect(20, 20, 150, 20);

            g.fillRect(20, 20, barra, 20);
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

        // =========================
        // ESC PARA CERRAR
        // =========================
        if (key == KeyEvent.VK_ESCAPE) {

            if (!escTimer.isRunning()) {

                escInicio =
                        System.currentTimeMillis();

                mostrandoBarraESC = true;

                escTimer.start();
            }
        }

        if (key == KeyEvent.VK_W)
            player.up = true;

        if (key == KeyEvent.VK_S)
            player.down = true;

        if (key == KeyEvent.VK_A)
            player.left = true;

        if (key == KeyEvent.VK_D)
            player.right = true;

        // =========================
        // INTERACTUAR CON PUERTA
        // =========================
        if (key == KeyEvent.VK_E &&
                cercaPuerta &&
                !fadeOut) {

            fadeOut = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        // =========================
        // SOLTAR ESC
        // =========================
        if (key == KeyEvent.VK_ESCAPE) {

            escTimer.stop();

            mostrandoBarraESC = false;

            repaint();
        }

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
    public void keyTyped(KeyEvent e) {}
}