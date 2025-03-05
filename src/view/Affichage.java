package View;
import Model.Ground;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Affichage extends JPanel {
    private BufferedImage groundImage; // Store the ground image
    private Ground ground;

    public Affichage() {
        setPreferredSize(new Dimension(500, 300)); // Set window size
        ground = new Ground(getPreferredSize().height);

        try {
            // Load the ground image from the images folder
            groundImage = ImageIO.read(new File("images/brick.png"));
        } catch (IOException e) {
            System.out.println("Error loading ground image.");
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw sky background
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw the ground using the image
        int groundY = ground.getYPosition();
        int tileSize = ground.getTileSize();

        for (int i = 0; i < getWidth(); i += tileSize) {
            if (groundImage != null) {
                g.drawImage(groundImage, i, groundY, tileSize, tileSize, null);
            } else {
                g.setColor(new Color(139, 69, 19)); // Fallback color if image fails
                g.fillRect(i, groundY, tileSize, tileSize);
            }
        }
    }
}
