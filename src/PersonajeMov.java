import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PersonajeMov extends JPanel implements KeyListener, Runnable {

    // Tamaño ventana
    final int screenWidth = 640;
    final int screenHeight = 480;

    // Tamaño mapa/fondo
    final int worldWidth = 2000;
    final int worldHeight = 2000;

    // Posición del personaje en el mundo
    // SPAWN NUEVO (fuera de la caseta)
    int x = 500;
    int y = 450;

    // Cámara
    int cameraX = 0;
    int cameraY = 0;

    // Velocidad
    int speed = 4;

    // Controles
    boolean up, down, left, right;

    // Pantalla completa
    boolean fullscreen = false;

    // Ventana
    JFrame frame;

    // Fondo
    BufferedImage background;

    // Sprites del personaje
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

    // BORDES MAPA
    // Pared izquierda MÁS DELGADA

    new Rectangle(1950, 0, 50, 2000),       // derecha
    new Rectangle(0, 0, 2000, 60),          // arriba
    new Rectangle(0, 1940, 2000, 60),       // abajo

    // REJAS CANCHA
   new Rectangle(125, 360, 18, 1200),      // izquierda ajustada
    new Rectangle(1860, 330, 30, 1260),     // derecha
    new Rectangle(140, 300, 1720, 35),      // arriba
    new Rectangle(140, 1590, 1720, 35),     // abajo

    // MURO SUPERIOR
    new Rectangle(620, 170, 1240, 40),

    // PLATAFORMA CENTRAL
    new Rectangle(860, 430, 620, 55),

    // BARRAS ARRIBA CANCHA
    new Rectangle(120, 540, 760, 40),
    new Rectangle(980, 540, 760, 40),

    // PARTE CIRCULAR ABAJO
    new Rectangle(720, 1720, 600, 90)
};

    public PersonajeMov(JFrame frame) {

        this.frame = frame;

        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Cargar imágenes
        try {

            // Fondo grande
            background = ImageIO.read(getClass().getResource("/sprites/background.jpeg"));

            // Sprites
            playerUp = ImageIO.read(getClass().getResource("/sprites/player_up.png"));

            playerDown = ImageIO.read(getClass().getResource("/sprites/player_down.png"));

            playerLeft = ImageIO.read(getClass().getResource("/sprites/player_left.png"));

            playerRight = ImageIO.read(getClass().getResource("/sprites/player_right.png"));

            // Sprite inicial
            currentSprite = playerDown;

        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar fondo con cámara
        g.drawImage(background,
                -cameraX,
                -cameraY,
                worldWidth,
                worldHeight,
                null);

        // Dibujar personaje
        g.drawImage(currentSprite,
                x - cameraX,
                y - cameraY,
                64,
                64,
                null);

        // =========================
        // DEBUG COLISIONES
        // =========================
        /*
        g.setColor(Color.RED);

        for (Rectangle wall : walls) {

            g.drawRect(
                    wall.x - cameraX,
                    wall.y - cameraY,
                    wall.width,
                    wall.height
            );
        }
        */
    }

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

        // Hitbox del personaje
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

        // Límites del mundo
        if (x < 0)
            x = 0;

        if (y < 0)
            y = 0;

        if (x > worldWidth - 64)
            x = worldWidth - 64;

        if (y > worldHeight - 64)
            y = worldHeight - 64;

        // Cámara sigue al jugador
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

    // Cambiar pantalla completa
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
            frame.setSize(screenWidth, screenHeight);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    @Override
    public void run() {

        while (true) {

            update();
            repaint();

            try {
                Thread.sleep(16); // 60 FPS aprox
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

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

            // F4 pantalla completa
            case KeyEvent.VK_F4:
                toggleFullscreen();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {

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
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

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
// prueba paput