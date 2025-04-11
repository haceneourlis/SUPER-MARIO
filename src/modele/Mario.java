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

    // Images de Mario (avec animation de walk)
    private BufferedImage[] image = new BufferedImage[4];

    // Mario's remaining number of lives
    private static int ViesMario = CONSTANTS.VIE_MAX_MARIO; // Début avec 3 vies

    // Booléen pour voir si mario est invincible ou non
    private boolean invincible = false;

    // Le début de son invincibilité
    private long invincibleStartTime = 0;
    // Le temps d'invincibilité
    private int invincibleDuration = 2000; // 2 secondes

    /**
     * Constructeur privé de la classe Mario.
     */
    private Mario() {
        // Appelle le constructeur de GameCharacter
        super();
        System.out.println("Création de Mario !");

        this.position = new Point(CONSTANTS.POSITION_X_ORIGINE_MARIO, CONSTANTS.LE_SOL);
        this.speed = 1; // vitesse initiale
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

    /**
     * Permet de créer une instance unique de la classe Mario (classe singleton)
     */
    public static Mario getInstance() {
        if (instance == null) {
            // Si il n'existe pas on le crée.
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

    public static void killMario() {
        VIVANT = false;
        ViesMario = 0; // Mario est mort
    }

    /**
     * Getter de la position en Y.
     * 
     * @return int position en Y de mario
     */
    public int getPositionY() {
        return this.position.y;
    }

    /**
     * Getter de la position en X.
     * 
     * @return int position en X de mario.
     */
    public int getPositionX() {
        return this.position.x;
    }

    /**
     * Setter de la position en Y de mario.
     * 
     * @param y la nouvelle position en Y de mario.
     */
    public void setPositionY(int y) {
        this.position.y = y;
    }

    /**
     * Méthode qui va incrémenter la vitesse du joueur.
     * Elle vérifie si la vitesse ne dépasse pas la constante vitesse_max.
     */
    public void increment_speed() {
        if (this.speed < CONSTANTS.VITESSE_MAX_MARIO) {
            this.speed += 1;
        }
    }

    /**
     * Méthode pour décrémenter la vitesse du joueur.
     * Si la vitesse est supérieure à 0, on la décrémente du facteur de
     * décélaration.
     */
    public void decelerer() {
        if (this.speed - CONSTANTS.DECELERATION_MARIO > 0) {
            this.speed -= CONSTANTS.DECELERATION_MARIO;
        } else {
            this.speed = 0;
        }
    }

    /**
     * Methode pour obtenir l'image du joueur
     * PAR DEFAUT METTRE L'INDEX A 0.
     * 
     * @return l'image du joueur
     */
    public BufferedImage getImage(int index) {
        return this.image[index];
    }

    /**
     * Methode pour savoir si le joueur est invincible ou pas.
     * 
     * @return un booléen
     */
    public boolean isInvincible() {
        return invincible;
    }

    /**
     * Méthode pour vérifier si la durée d'invincibilité est écoulée.
     * Si oui, on met le booléen "invincible"(présent dans la classe) à false.
     * 
     * @return ne retourne rien.
     */
    public void updateInvincibility() {
        // Vérifie si Mario est invincible et si le temps d'invincibilité est écoulé
        if (invincible && (System.currentTimeMillis() - invincibleStartTime > invincibleDuration)) {
            invincible = false;
        }
    }

    /**
     * Méthode pour obtenir le nombre de vies restantes de Mario.
     * 
     * @return le nombre de vies (un entier)
     */
    public static int getVies() {
        return Mario.ViesMario;
    }

    /**
     * Classe qui va gérer la perte de vie de Mario.
     * Si Mario est déjà invincible, il ne perd pas de vie.
     * Si Mario n'a plus de vie, on affiche un message de game over.
     * 
     * @param rien
     * @return rien
     */
    public void perdreVie() {
        // Si Mario n'est pas déjà invincible
        if (!invincible) {
            ViesMario--;
            invincible = true;
            // On enregistre le temps de début de l'invincibilité
            invincibleStartTime = System.currentTimeMillis();

        }
    }

    public void augmenterVie() {
        if ((ViesMario + 1) > 3) {
            // ne fait rien
        } else {
            ViesMario++;
        }
    }

    /**
     * Méthode pour remettre mario à sa position d'origine
     * et remettre sa vitesse à 0.
     * 
     * @param rien
     * @return rien
     */
    public void resetPosition() {
        this.position = new Point(CONSTANTS.POSITION_X_ORIGINE_MARIO, CONSTANTS.LE_SOL);
        this.speed = 1; // Reset de la vitesse si tu veux
        System.out.println("Mario revient au début !");
    }
}
