package modele;

import java.util.logging.*;

/**
 * Cette classe gère le saut du joueur.
 * Elle contient une méthode pour faire sauter le joueur, elle utilise donc une
 * instance de la classe descente et modifie son attribut force en lui affectant
 * une impulsion vers le haut.
 */
public class Jumping {

    public final int IMPULSION = 15;
    // Descente instance
    private Descente descente;
    private DescenteCoins descenteCoins;
    // Instance de Mario
    private Mario mario = Mario.getInstance();

    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    // Constructeur qui initialise l'instance de la descente et le booléen de
    // saut.
    public Jumping(Descente descente) {
        this.descente = descente;
    }

    public void setThreadDecenteCoins(DescenteCoins descenteCoins) {
        this.descenteCoins = descenteCoins;
    }

    /**
     * Méthode pour faire sauter le joueur, il vérifie si le joueur n'est pas
     * entrain de "tomber"
     * Si ce n'est pas le cas alors on applique une impulsion vers le haut et on dit
     * que le joueur peut
     * "tomber" maintenant.
     */
    public void jump() {
        if (!this.mario.allowedToFallDown) {
            this.descente.force = -IMPULSION;
            this.mario.allowedToFallDown= true;
            mario.setDirection("up");
        }
    }

    public void jumpLaCoin() {
        if (!this.descenteCoins.coinAllowedToFallDown) {
            this.descenteCoins.force_coin = -CONSTANTS.IMPULSION_COIN;
            this.descenteCoins.coinAllowedToFallDown = true;
        }
    }

}
