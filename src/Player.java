import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player {

    // Posición
    public int x = 500;
    public int y = 500;

    // Velocidad
    public int speed = 4;

    // Movimiento
    public boolean up, down, left, right;

    // Sprites
    BufferedImage upSprite;
    BufferedImage downSprite;
    BufferedImage leftSprite;
    BufferedImage rightSprite;

    // Sprite actual
    BufferedImage currentSprite;

    public Player() {

        try {

            upSprite = ImageIO.read(
                    getClass().getResource("/Sprites/player_up.png")
            );

            downSprite = ImageIO.read(
                    getClass().getResource("/Sprites/player_down.png")
            );

            leftSprite = ImageIO.read(
                    getClass().getResource("/Sprites/player_left.png")
            );

            rightSprite = ImageIO.read(
                    getClass().getResource("/Sprites/player_right.png")
            );

            currentSprite = downSprite;

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // UPDATE MOVIMIENTO
    // =========================
    public void update() {

        if (up) {

            y -= speed;

            currentSprite = upSprite;
        }

        if (down) {

            y += speed;

            currentSprite = downSprite;
        }

        if (left) {

            x -= speed;

            currentSprite = leftSprite;
        }

        if (right) {

            x += speed;

            currentSprite = rightSprite;
        }
    }

    // =========================
    // DIBUJAR
    // =========================
    public void draw(Graphics g, int cameraX, int cameraY) {

        g.drawImage(
                currentSprite,
                x - cameraX,
                y - cameraY,
                64,
                64,
                null
        );
    }
}