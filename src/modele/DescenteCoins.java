package modele;

import java.awt.Point;
import java.util.logging.*;

/**
 * Cette classe gère la descente d'un coin.
 * Elle implémente un Thread.
 * Chaque coin a un thread de descente qui gère sa gravité.
 */
public class DescenteCoins extends Thread {

    // Le coin à gérer.
    public Coin coinToCatch;

    // La force ici peut être positive (descente) comme négative (saut).
    public int force_coin = 0;

    // Un booléen indiquant si le coin est autorisé à tomber.
    public boolean coinAllowedToFallDown = true;

    // La position de départ de la pièce.
    public Point positionDepart;

    // Logger pour debug
    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    /**
     * Constructeur de la classe DescenteCoins.
     * @param coinToCatch le coin à gérer.
     */
    public DescenteCoins(Coin coinToCatch) {
        this.coinToCatch = coinToCatch;
    }
    /**
     * Méthode pour modifier le coin concerné.
     * Utilisée plutot pour mettre ce dernier à null.
     * @param coinToCatch le noveau coin à gérer.
     */
    public void setTheCoinToCatch(Coin coinToCatch) {
        this.coinToCatch = coinToCatch;
    }

    /**
     * Méthode run du thread.
     */
    @Override
    public void run() {
        // On continue tant que le thread n'est pas interrompu.
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(CONSTANTS.DELAY_DESCENTE_COINS);
                

                // On vérifie si le coin est autorisé à tomber et qu'il existe toujours (pas mis à null).
                if (coinAllowedToFallDown && coinToCatch != null) {
                    // On vérifie si la force est négative il descend
                    // sinon il saute.
                    if (force_coin >= 0) {
                        coinToCatch.setDirection("down");
                    } else if (force_coin < 0) {
                        coinToCatch.setDirection("up");
                    }
                    // On ajoute à la force la gravité, et on vérifie si elle ne dépasse pas la force max.
                    this.force_coin += CONSTANTS.GRAVITY;
                    if (this.force_coin >= CONSTANTS.FORCE_MAX_COIN) {
                        this.force_coin = CONSTANTS.FORCE_MAX_COIN;
                    }

                    // On met à jour la position du coin.
                    coinToCatch.setPositionY(this.force_coin + coinToCatch.getPosition().y);


                    // On vérifie si la pièce est au sol, si oui on l'arrête
                    if (coinToCatch.getPosition().y >= coinToCatch.positionDepart.y) {
                        coinToCatch = null;
                        Collision.coinToCatch = null;
                        coinAllowedToFallDown = false;
                        // On arrête le thread de la pièce
                        this.interrupt();
                        // On remet la force à 0
                        this.force_coin = 0;

                    }
                }
            } catch (InterruptedException e) {
                logger.info("Thread interrupted, exiting...");
                break;
            }
        }

    }


}
