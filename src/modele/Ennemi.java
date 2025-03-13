package modele;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// package caractere;

// -------------------------------------------------------
// un ennemi qui bouge de gauche à droite (position initiale se trouve à droite de la fenêtre)
// si l'ennemi atteint la frontière, il change de direction

// definir les coordonnées et la taille de l'ennemi
// definir la vitesse de l'ennemi
// definir les frontières de la fenêtre
// definir une methode pour faire bouger l'ennemi
// definir un thread pour gérer le mouvement de l'ennemi

// -------------------------------------------------------

// une classe qui hérite de la classe Thread pour gérer les ennemis
// l'enemi bouge de droite à gauche et ne peut pas sortir de la fenêtre
public class Ennemi extends GameCharacter implements Runnable {

    // les frontières
    private int leftBorder;
    private int rightBorder;

    public Thread thread;
    private boolean running = true;
    private boolean movingRight; // Indique si l'ennemi va à droite

    // images de l'ennemi
    private BufferedImage[] image;

    public Ennemi(int x, int width, int height, int speed, int leftBorder, int rightBorder,
            boolean movingRight) {
        super();
        this.image = new BufferedImage[3];
        try {
            this.image[0] = ImageIO.read(getClass().getResourceAsStream("/resources/koopa_sprites/koopa2.png"));
            this.image[1] = ImageIO.read(getClass().getResourceAsStream("/resources/koopa_sprites/koopa1.png"));
            this.image[2] = ImageIO.read(getClass().getResourceAsStream("/resources/koopa_sprites/koopa2.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.position.x = x;
        // affiche dans la console le height de l'image 0
        System.out.println(this.image[0].getHeight());
        this.position.y = CONSTANTS.LE_SOL;

        this.solidArea.x = CONSTANTS.slidAreaDefaultX;
        this.solidArea.y = CONSTANTS.slidAreaDefaultY;
        this.solidArea.height = CONSTANTS.TAILLE_CELLULE;
        this.solidArea.width = CONSTANTS.TAILLE_CELLULE;

        this.speed = speed;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;

        this.movingRight = movingRight; // l'ennemi commence par aller à droite

        

        thread = new Thread(this);
    }

    @Override
    // getter de image en fonction de l'index
    public BufferedImage getImage(int index) {
        return this.image[index];
    }

    public boolean isMovingRight() {
        return movingRight;
    } // Indique si l'ennemi va à droite

    // faire bouger l'ennemi
    public void moveEnnemi() {
        if (movingRight) {
            this.position.x += speed;
            if (this.position.x + width >= rightBorder) { // si l'ennemi atteint la frontière droite
                this.position.x = rightBorder - width; // le mettre à la frontière droite
                movingRight = false; // changer la direction
            }
        } else {
            this.position.x -= speed;
            if (this.position.x <= leftBorder) {
                this.position.x = leftBorder;
                movingRight = true;
            }
        }
    }

    // demarrer le thread
    @Override
    public void run() {
        while (running) {
            moveEnnemi();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // arreter le thread
    public void stopMoving() {
        running = false;
    }
}
