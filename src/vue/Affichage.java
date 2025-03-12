package vue;
import modele.*;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Affichage extends JPanel {
    private BufferedImage groundImage; // Store the ground image
    private Ground ground;
    private Joueur joueur;
    private BufferedImage ennemiImage;
    private Ennemi ennemi;
    private int x_joueur;
    private int y_joueur;
    private final int RATIO_X = 2;
    private final int RATIO_Y = 3;

    public Affichage(Joueur j) {
        setPreferredSize(new Dimension(500, 300)); // Set window size
        ground = new Ground(getPreferredSize().height);
        this.joueur = j;
        try {
            // Load the ground image from the images folder
            groundImage = ImageIO.read(new File("images/brick.png"));
        } catch (IOException e) {
            System.out.println("Error loading ground image.");
            e.printStackTrace();
        }

        //
        try {
            ennemiImage = ImageIO.read(new File("images/turtle.png"));
        } catch (IOException e) {
            System.out.println("Erreur : Impossible de charger l'image de l'ennemi.");
            e.printStackTrace();
        }
        // Initialiser l'ennemi (au-dessus du sol)
        ennemi = new Ennemi(400, ground.getYPosition()-50, 20, 30, 5, 0, 500, true);
        ennemi.start();

        // Mettre à jour l'affichage toutes les 50ms
        Timer timer = new Timer(50, e -> repaint());
        timer.start();
    }



    public void transformFromModelToView(){
        this.y_joueur = (joueur.HMAX + joueur.getPositionY())*RATIO_Y;
        this.x_joueur = -joueur.BEFORE*RATIO_X + joueur.getPositionX()*RATIO_X;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        transformFromModelToView();

        g.drawRect(this.x_joueur, this.y_joueur, 60, 60);


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


        // Dessiner l'ennemi avec rotation
        Graphics2D g2d = (Graphics2D) g;
        if (ennemiImage != null) {
            int x = ennemi.getX();
            int y = ennemi.getY();
            int width = ennemi.getWidth();
            int height = ennemi.getHeight();

            AffineTransform transform = new AffineTransform();
            if (!ennemi.isMovingRight()) {
                // Si l'ennemi va vers la gauche, on le retourne horizontalement
                transform.translate(x + width, y);
                transform.scale(-1, 1);
            } else {
                transform.translate(x, y);
            }

            g2d.drawImage(ennemiImage, transform, this);
        } else {
            // Si l'image ne charge pas, afficher un carré rouge
            g.setColor(Color.RED);
            g.fillRect(ennemi.getX(), ennemi.getY(), ennemi.getWidth(), ennemi.getHeight());
        }

        // Afficher le joueur (comme étant un rectangle pour le moment)


    }
}
