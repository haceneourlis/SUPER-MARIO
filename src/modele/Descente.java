package modele;

import java.util.logging.*;

/**
 * Cette classe gère la descente d'un gamecharacter.
 * Elle implémente un Thread.
 * Chaque gamecharacter a un thread de descente qui gère sa gravité.
 */
public class Descente extends Thread {

    // La force ici peut être positive (descente) comme négative (saut).
    public int force = 0;

    // Le gamecharacter à gérer.
    private GameCharacter gc;

    // Un logger pour debug.
    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    /**
     * Constructeur de la classe Descente.
     * @param gc le gamecharacter à gérer.
     */
    public Descente(GameCharacter gc) {
        this.gc = gc;
    }

    /**
     * Méthode run qui lance le thread.
     */
    @Override
    public void run() {
        while (true) {
            try {
                
                Thread.sleep(CONSTANTS.DELAY_DESCENTE);

                // On vérifie si le gamecharacter est autorisé à tomber.
                if (this.gc.allowedToFallDown) {
                    // On vérifie si la force est négative il descend
                    // sinon il saute.
                    if (force >= 0) {
                        this.gc.setDirection("down");
                    } else if (force < 0) {
                        this.gc.setDirection("up");
                    }

                    // On ajoute à la force la gravité, et on vérifie si elle ne dépasse pas la force max.
                    this.force += CONSTANTS.GRAVITY;
                    if (this.force >= CONSTANTS.FORCE_MAX) {
                        this.force = CONSTANTS.FORCE_MAX;
                    }
                    // On met à jour la position du gamecharacter.
                    this.gc.setPositionY(this.force + this.gc.getPosition().y);
                }
                

            } catch (Exception e) {
                logger.log(Level.WARNING, "Erreur thread");
            }
        }
    }

}
