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

    // =========================
    // PUERTA REGRESO
    // =========================
    Rectangle puertaRegreso =
            new Rectangle(1300, 50, 200, 200);

    boolean cercaPuertaRegreso = false;

    // =========================
    // FADE
    // =========================
    boolean fadeOut = false;

    float fadeAlpha = 0f;

    // =========================
    // CONTROL CAMBIO ESCENA
    // =========================
    boolean escenaCambiada = false;

    public Escenario2(JFrame frame) {

        this.frame = frame;

        setPreferredSize(new Dimension(1440, 900));

        setBackground(Color.BLACK);

        setFocusable(true);

        setFocusTraversalKeysEnabled(false);

        addKeyListener(this);

        try {

            background = ImageIO.read(
                    getClass().getResource(
                            "/Sprites/Escenario2.jpeg"
                    )
            );

        } catch (Exception e) {

            System.out.println(
                    "Error cargando Escenario2.jpeg"
            );

            e.printStackTrace();
        }

        new Thread(this).start();

        SwingUtilities.invokeLater(
                () -> requestFocusInWindow()
        );
    }

    // =========================
    // UPDATE
    // =========================
    public void update() {

        player.update();

        if (background == null)
            return;

        // =========================
        // COLISIONES
        // =========================
        player.x = Math.max(230, player.x);

        player.y = Math.max(80, player.y);
        
        //abajo
        player.x = Math.min(
                player.x,
                background.getWidth() - 1
        );
        //derecha
        player.y = Math.min(
                player.y,
                background.getHeight() - 220
        );

        // =========================
        // HITBOX PLAYER
        // =========================
        Rectangle playerHitbox =
                new Rectangle(
                        player.x,
                        player.y,
                        60,
                        60
                );

        cercaPuertaRegreso =
                playerHitbox.intersects(
                        puertaRegreso
                );

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

        // =========================
        // CÁMARA
        // =========================
        cameraX =
                player.x - (getWidth() / 2.0);

        cameraY =
                player.y - (getHeight() / 2.0);

        cameraX = Math.max(
                0,
                Math.min(
                        cameraX,
                        background.getWidth()
                                - getWidth()
                )
        );

        cameraY = Math.max(
                0,
                Math.min(
                        cameraY,
                        background.getHeight()
                                - getHeight()
                )
        );
    }

    // =========================
    // CAMBIAR ESCENA
    // =========================
    public void cambiarEscena() {

        if (escenaCambiada)
            return;

        Escenario1 escena1 =
                new Escenario1(frame);

        frame.setContentPane(escena1);

        frame.revalidate();

        frame.repaint();

        escena1.requestFocusInWindow();

        escenaCambiada = true;
    }

    // =========================
    // DIBUJO
    // =========================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);

        g2.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        if (background == null)
            return;

        int bgW = background.getWidth();

        int bgH = background.getHeight();

        double scaleX =
                (double) getWidth() / bgW;

        double scaleY =
                (double) getHeight() / bgH;

        double scale =
                Math.min(scaleX, scaleY);

        int newW = (int) (bgW * scale);

        int newH = (int) (bgH * scale);

        int drawX =
                (getWidth() - newW) / 2;

        int drawY =
                (getHeight() - newH) / 2;

        int worldX =
                drawX - (int) cameraX;

        int worldY =
                drawY - (int) cameraY;

        // =========================
        // MAPA
        // =========================
        g2.drawImage(
                background,
                worldX,
                worldY,
                newW,
                newH,
                null
        );

        // =========================
        // DEBUG PUERTA
        // =========================
        // =========================
// DEBUG PUERTA
// =========================
int puertaX =
        worldX + (int)(puertaRegreso.x * scale);

int puertaY =
        worldY + (int)(puertaRegreso.y * scale);

int puertaW =
        (int)(puertaRegreso.width * scale);

int puertaH =
        (int)(puertaRegreso.height * scale);

g2.setColor(Color.RED);

g2.drawRect(
        puertaX,
        puertaY,
        puertaW,
        puertaH
);

        // =========================
        // JUGADOR
        // =========================
        int playerSize =
                (int) (164 * scale);

        int playerX =
                (int) (player.x - cameraX);

        int playerY =
                (int) (player.y - cameraY);

        g2.drawImage(
                player.currentSprite,
                playerX,
                playerY,
                playerSize,
                playerSize,
                null
        );

        // =========================
        // FADE NEGRO
        // =========================
        if (fadeOut) {

            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            fadeAlpha
                    )
            );

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );
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

            } catch (Exception ignored) {}
        }
    }

    // =========================
    // INPUT
    // =========================
    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W)
            player.up = true;

        if (key == KeyEvent.VK_S)
            player.down = true;

        if (key == KeyEvent.VK_A)
            player.left = true;

        if (key == KeyEvent.VK_D)
            player.right = true;

        // =========================
        // INTERACTUAR PUERTA
        // =========================
        if (key == KeyEvent.VK_E
                && cercaPuertaRegreso
                && !fadeOut) {

            fadeOut = true;
        }
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
    public void keyTyped(KeyEvent e) {}

    // =========================
    // MAIN SOLO PRUEBA
    // =========================
    public static void main(String[] args) {

        JFrame frame =
                new JFrame("Escenario 2");

        Escenario2 game =
                new Escenario2(frame);

        frame.setContentPane(game);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setResizable(false);

        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}