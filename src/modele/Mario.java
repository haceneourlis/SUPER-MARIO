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

    public static boolean VIVANT = true; // Mario est vivant par défaut

    // vitesse max constante
    public final int VITESSE_MAX = 6;
    // Max de vies
    public final int VIE_MAX = 3;

    // Images de Mario (avec animation de walk)
    private BufferedImage[] image = new BufferedImage[4];

    // Mario's remaining number of lives
    private static int ViesMario; // Début avec 3 vies
    // Mario's invincibility
    private boolean invincible;
    private long invincibleStartTime; //
    private int invincibleDuration; // 2 secondes

    // constructeur privé
    private Mario() {
        super();
        System.out.println("Création de Mario !");

        this.position = new Point(CONSTANTS.X_ORIGINE, CONSTANTS.LE_SOL);
        this.speed = 1; // vitesse initiale
        ViesMario = VIE_MAX; // Initialiser le nombre de vies à 3
        this.invincible = false; // Mario n'est pas invincible au départ
        this.invincibleStartTime = 0; // Temps de début d'invincibilité
        this.invincibleDuration = 2000; // Durée d'invincibilité de 2 secondes
        this.canMove = true; // Mario peut se déplacer par défaut
        VIVANT = true; // Mario est vivant par défaut
        this.canMove = true; // Mario peut se déplacer par défaut
        this.allowedToFallDown = false; // Réinitialiser le booléen de saut
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

    public static void reborn() {
        Mario.VIVANT = true; // Mario est vivant
        Mario.ViesMario = 3; // Réinitialiser le nombre de vies
        instance.position = null;
        instance.resetPosition(); // Remettre Mario à sa position de départ

        instance.speed = 1; // Réinitialiser la vitesse
        instance.invincible = false; // Mario n'est pas invincible au départ
        instance.invincibleStartTime = 0; // Temps de début d'invincibilité
        instance.invincibleDuration = 2000; // Durée d'invincibilité de 2 secondes
        instance.canMove = true; // Mario peut se déplacer par défaut
        instance.allowedToFallDown = false; // Réinitialiser le booléen de saut

        System.out.println("Mario est de retour !");
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
    public static int getViesMario() {
        return ViesMario;
    }

    public static void resetVies() {
        ViesMario = 3; // Reset to 3 lives
        System.out.println("Mario's lives have been reset to 3.");
    }

    // Perdre une vie, et gérer le reset ou Game Over
    // TODO: Need to distinguish between different types of death (falling, enemy,
    // etc.)
    public void perdreVie() {
        if (!invincible) {

            this.ViesMario--;
            invincible = true;
            invincibleStartTime = System.currentTimeMillis();
            System.out.println("Mario a perdu une vie ! Vies restantes : " + this.ViesMario);
            if (this.ViesMario <= 0) {
                // Game Over
                System.out.println("GAME OVER !");
                // TODO: Game Over screen
            }
        }
    }

    public static void killMario() {
        VIVANT = false;
        ViesMario = 0; // Mario est mort
    }

    public void augmenterVie() {
        if ((this.getViesMario() + 1) > 3) {
            // ne fait rien
        } else {
            this.ViesMario++;
        }
    }

    // Remettre Mario à sa position de départ
    public void resetPosition() {
        this.position = new Point(CONSTANTS.X_ORIGINE, CONSTANTS.LE_SOL);
        this.speed = 1; // Reset de la vitesse si tu veux
        System.out.println("Mario revient au début !");
    }
}
