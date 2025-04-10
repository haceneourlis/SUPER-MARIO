package modele;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// classe Joueur qui va définir le joueur (mario) avec sa position en x et en y
public class Mario extends GameCharacter {

    // instance unique de la classe Mario
    private static Mario instance = null;

    // constante de coordonnées d'origine du joueur
    public final int X_ORIGINE = 50;

    // vitesse max constante
    public final int VITESSE_MAX = 6;

    // Images de Mario (avec animation de walk)
    private BufferedImage[] image = new BufferedImage[4];

    // bouger ou pas
    private boolean canMove = true;

    // Mario's remaining number of lives
    private int vies = 3; // Début avec 3 vies
    // Mario's invincibility
    private boolean invincible = false;
    private long invincibleStartTime = 0; //
    private int invincibleDuration = 2000; // 2 secondes

    // constructeur privé
    private Mario() {
        super();
        this.position = new Point(X_ORIGINE, CONSTANTS.LE_SOL);
        try {
            this.image[0] = ImageIO.read(new File("src/resources/mario_sprites/mario_idl.png"));
            this.image[1] = ImageIO.read(new File("src/resources/mario_sprites/mario_walk1.png"));
            this.image[2] = ImageIO.read(new File("src/resources/mario_sprites/mario_walk2.png"));
            this.image[3] = ImageIO.read(new File("src/resources/mario_sprites/mario_walk3.png"));
        } catch (IOException e) {
            System.out.println("Erreur : Impossible de charger l'image du joueur.");
            e.printStackTrace();
        }
    }

    // permet de créer une instance unique de la classe Mario (classe singleton)
    public static Mario getInstance() {
        if (instance == null) {
            instance = new Mario();
        }
        return instance;
    }

    // getter de la position en Y
    public int getPositionY() {
        return this.position.y;
    }

    // getter de la position en X
    public int getPositionX() {
        return this.position.x;
    }

    // setter de la position en Y
    public void setPositionY(int y) {
        this.position.y = y;
    }

    /**
     * Méthode qui va incrémenter la vitesse du joueur.
     * Elle vérifie si la vitesse ne dépasse pas la constante vitesse_max.
     */
    public void increment_speed() {
        if (this.speed < this.VITESSE_MAX) {
            this.speed += 1;
        }
    }

    
    /**
     * Méthode pour décrémenter la vitesse du joueur.
     * Si la vitesse est supérieure à 0, on la décrémente du facteur de
     * décélaration.
     */
    public void decelerer() {
        if (this.speed - CONSTANTS.DECELERATION > 0) {
            this.speed -= CONSTANTS.DECELERATION;
        } else {
            this.speed = 0;
        }
    }

    /**
     * Methode pour obtenir l'image du joueur
     *
     * @return l'image du joueur
     */
    public BufferedImage getImage(int index) {
        return this.image[index];
    }


    public boolean isInvincible() {
        return invincible;
    }

    public void updateInvincibility() {
        if (invincible && (System.currentTimeMillis() - invincibleStartTime > invincibleDuration)) {
            invincible = false;
            System.out.println("Mario is no longer invincible.");
        }
    }

    // Obtenir le nombre de vies restantes
    public int getVies() {
        return this.vies;
    }

    // Perdre une vie, et gérer le reset ou Game Over
    // TODO: Need to distinguish between different types of death (falling, enemy,
    // etc.)
    public void perdreVie() {
        if (!invincible) {

            this.vies--;
            invincible = true;
            invincibleStartTime = System.currentTimeMillis();
            System.out.println("Mario a perdu une vie ! Vies restantes : " + this.vies);
            if (this.vies <= 0) {
                // Game Over
                System.out.println("GAME OVER !");
                // TODO: Game Over screen
            }
        }
    }

    // Remettre Mario à sa position de départ
    public void resetPosition() {
        this.position = new Point(X_ORIGINE, CONSTANTS.LE_SOL);
        this.speed = 1; // Reset de la vitesse si tu veux
        System.out.println("Mario revient au début !");
    }
}
