package modele;

import java.util.logging.*;

/**
 * Cette classe gère le saut du joueur.
 * Elle contient une méthode pour faire sauter le joueur, elle utilise donc une
 * instance de la classe descente et modifie son attribut force en lui affectant une impulsion vers le haut.
 */
public class Jumping{
    
    // Booléen qui indique si le joueur est en train de sauter. (plus vraiment nécessaire)
    private boolean is_jumping;

    // Constante de la puissance de l'impulsion à chaque saut
    public final int IMPULSION = 15;

    // Descente instance
    private Descente descente;
    
    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    // Constructeur qui initialise l'instance de la descente et le booléen de
    // saut.
    public Jumping(Descente descente) {
        this.is_jumping = false;
        this.descente = descente;
    }

    /**
     * Méthode pour faire sauter le joueur, il vérifie si le joueur n'est pas entrain de "tomber"
     * Si ce n'est pas le cas alors on applique une impulsion vers le haut et on dit que le joueur peut 
     * "tomber" maintenant.
     */
    public void jump() {
        if (!this.descente.allowedToFallDown) {
            this.descente.force = -IMPULSION;
            this.descente.allowedToFallDown = true;
        }
    }


    public void notjumping() {
        this.is_jumping = false;
    }

}
