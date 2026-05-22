import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PersonajeMov extends JPanel implements KeyListener, Runnable {

    // =========================
    // TAMAÑO VENTANA
    // =========================
    final int screenWidth = 640;
    final int screenHeight = 480;

    // =========================
    // TAMAÑO MAPA
    // =========================
    final int worldWidth = 2000;
    final int worldHeight = 2000;

    // =========================
    // POSICIÓN PERSONAJE
    // =========================
    int x = 500;
    int y = 450;

    // =========================
    // CÁMARA
    // =========================
    int cameraX = 0;
    int cameraY = 0;

    // =========================
    // VELOCIDAD
    // =========================
    int speed = 4;

    // =========================
    // CONTROLES
    // =========================
    boolean up, down, left, right;

    // =========================
    // PANTALLA COMPLETA
    // =========================
    boolean fullscreen = false;

    // =========================
    // VENTANA
    // =========================
    JFrame frame;

    // =========================
    // BOTÓN REGRESAR
    // =========================
    JButton btnRegresar;

    // =========================
    // FONDO
    // =========================
    BufferedImage background;

    // =========================
    // SPRITES
    // =========================
    BufferedImage playerUp;
    BufferedImage playerDown;
    BufferedImage playerLeft;
    BufferedImage playerRight;

    // Sprite actual
    BufferedImage currentSprite;

    // =========================
    // COLISIONES
    // =========================
    Rectangle[] walls = {

        // Bordes mapa
        new Rectangle(1950, 0, 50, 2000),
        new Rectangle(0, 0, 2000, 60),
        new Rectangle(0, 1940, 2000, 60),

        // Rejas cancha
        new Rectangle(125, 360, 18, 1200),
        new Rectangle(1860, 330, 30, 1260),
        new Rectangle(140, 300, 1720, 35),
        new Rectangle(140, 1590, 1720, 35),

        // Muro superior
        new Rectangle(620, 170, 1240, 40),

        // Plataforma central
        new Rectangle(860, 430, 620, 55),

        // Barras arriba cancha
        new Rectangle(120, 540, 760, 40),
        new Rectangle(980, 540, 760, 40),

        // Parte circular abajo
        new Rectangle(720, 1720, 600, 90)
    };

    // =========================
    // CONSTRUCTOR
    // =========================
    public PersonajeMov(JFrame frame) {

        this.frame = frame;

        setPreferredSize(new Dimension(screenWidth, screenHeight));

        setBackground(Color.BLACK);

        setFocusable(true);

        addKeyListener(this);

        setLayout(null);

        // =========================
        // BOTÓN REGRESAR
        // =========================
        btnRegresar = new JButton("←");

        btnRegresar.setBounds(10, 10, 50, 30);

        add(btnRegresar);

        btnRegresar.addActionListener(e -> {

            MercylessMenu menu = new MercylessMenu();

            frame.dispose();

            menu.setVisible(true);
        });

        // =========================
        // CARGAR IMÁGENES
        // =========================
        try {

            background = ImageIO.read(
                    getClass().getResource("/Sprites/background.jpeg")
            );

            playerUp = ImageIO.read(
                    getClass().getResource("/Sprites/player_up.png")
            );

            playerDown = ImageIO.read(
                    getClass().getResource("/Sprites/player_down.png")
            );

            playerLeft = ImageIO.read(
                    getClass().getResource("/Sprites/player_left.png")
            );

            playerRight = ImageIO.read(
                    getClass().getResource("/Sprites/player_right.png")
            );

            currentSprite = playerDown;

        } catch (Exception e) {

            e.printStackTrace();
        }

        Thread gameThread = new Thread(this);

        gameThread.start();
    }

    // =========================
    // DIBUJAR
    // =========================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Fondo
        g.drawImage(
                background,
                -cameraX,
                -cameraY,
                worldWidth,
                worldHeight,
                null
        );

        // Personaje
        g.drawImage(
                currentSprite,
                x - cameraX,
                y - cameraY,
                64,
                64,
                null
        );
    }

    // =========================
    // UPDATE
    // =========================
    public void update() {

        int nextX = x;

        int nextY = y;

        // Movimiento
        if (up) {

            nextY -= speed;

            currentSprite = playerUp;
        }

        if (down) {

            nextY += speed;

            currentSprite = playerDown;
        }

        if (left) {

            nextX -= speed;

            currentSprite = playerLeft;
        }

        if (right) {

            nextX += speed;

            currentSprite = playerRight;
        }

        // Hitbox personaje
        Rectangle playerHitbox =
                new Rectangle(nextX, nextY, 40, 50);

        boolean collision = false;

        // Detectar colisiones
        for (Rectangle wall : walls) {

            if (playerHitbox.intersects(wall)) {

                collision = true;

                break;
            }
        }

        // Solo mover si NO hay colisión
        if (!collision) {

            x = nextX;

            y = nextY;
        }

        // Límites mundo
        if (x < 0)
            x = 0;

        if (y < 0)
            y = 0;

        if (x > worldWidth - 64)
            x = worldWidth - 64;

        if (y > worldHeight - 64)
            y = worldHeight - 64;

        // Cámara sigue jugador
        cameraX = x - getWidth() / 2 + 32;

        cameraY = y - getHeight() / 2 + 32;

        // Limitar cámara
        if (cameraX < 0)
            cameraX = 0;

        if (cameraY < 0)
            cameraY = 0;

        if (cameraX > worldWidth - getWidth())
            cameraX = worldWidth - getWidth();

        if (cameraY > worldHeight - getHeight())
            cameraY = worldHeight - getHeight();
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

            frame.setSize(screenWidth, screenHeight);

            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        }

        // RECUPERAR FOCO
        requestFocusInWindow();
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

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }

    // =========================
    // KEY PRESSED
    // =========================
    @Override
    public void keyPressed(KeyEvent e) {

        int tecla = e.getKeyCode();

        // =========================
        // CONTROLES WASD
        // =========================
        if (ConfiguracionControles.usarWASD) {

            switch (tecla) {

                case KeyEvent.VK_W:
                    up = true;
                    break;

                case KeyEvent.VK_S:
                    down = true;
                    break;

                case KeyEvent.VK_A:
                    left = true;
                    break;

                case KeyEvent.VK_D:
                    right = true;
                    break;
            }

        } else {

            // =========================
            // CONTROLES FLECHAS
            // =========================
            switch (tecla) {

                case KeyEvent.VK_UP:
                    up = true;
                    break;

                case KeyEvent.VK_DOWN:
                    down = true;
                    break;

                case KeyEvent.VK_LEFT:
                    left = true;
                    break;

                case KeyEvent.VK_RIGHT:
                    right = true;
                    break;
            }
        }

        // =========================
        // F4 PANTALLA COMPLETA
        // =========================
        if (tecla == KeyEvent.VK_F4) {

            toggleFullscreen();

            // RECUPERAR FOCO
            requestFocusInWindow();
        }
    }

    // =========================
    // KEY RELEASED
    // =========================
    @Override
    public void keyReleased(KeyEvent e) {

        int tecla = e.getKeyCode();

        // =========================
        // CONTROLES WASD
        // =========================
        if (ConfiguracionControles.usarWASD) {

            switch (tecla) {

                case KeyEvent.VK_W:
                    up = false;
                    break;

                case KeyEvent.VK_S:
                    down = false;
                    break;

                case KeyEvent.VK_A:
                    left = false;
                    break;

                case KeyEvent.VK_D:
                    right = false;
                    break;
            }

        } else {

            // =========================
            // CONTROLES FLECHAS
            // =========================
            switch (tecla) {

                case KeyEvent.VK_UP:
                    up = false;
                    break;

                case KeyEvent.VK_DOWN:
                    down = false;
                    break;

                case KeyEvent.VK_LEFT:
                    left = false;
                    break;

                case KeyEvent.VK_RIGHT:
                    right = false;
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {

        JFrame frame = new JFrame("Movimiento tipo Undertale");

        PersonajeMov game = new PersonajeMov(frame);

        frame.add(game);

        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.setResizable(false);

        frame.setVisible(true);
    }
}