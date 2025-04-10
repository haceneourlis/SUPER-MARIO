package modele;

import java.awt.Point;
import java.util.logging.*;

/**
 * Cette classe est un thread qui va appliquer la < gravité > sur les coins
 * à chaque fois que une pièce sort des bloques , un thread est créé pour cette
 * pièce.
 * Il va appliquer la force de gravité sur la pièce jusqu'à ce qu'elle retombe
 * là , ou elle a été crée
 * 
 */
public class DescenteCoins extends Thread {

    public Coin coinToCatch;

    // La force ici peut être positive (descente) comme négative (saut).
    public int force_coin = 0;

    public boolean coinAllowedToFallDown = true;
    private static final int DELAY = 55;

    // La position de départ de la pièce.
    public Point positionDepart;

    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public DescenteCoins(Coin coinToCatch) {
        this.coinToCatch = coinToCatch;
        System.out.println("coinToCatch : " + coinToCatch);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(DELAY);
                // à chaque instant t , si le mario n'est pas sur le sol : CONSTANTS.LE_SOL
                // alors il doit descendre

                if (coinAllowedToFallDown && coinToCatch != null) {
                    System.out.println("heee");
                    // On vérifie si la force est négative il descend
                    // sinon il saute.
                    if (force_coin >= 0) {
                        coinToCatch.setDirection("down");
                    } else if (force_coin < 0) {
                        coinToCatch.setDirection("up");
                    }

                    // On vérifie si la force est négative il descend
                    // sinon il saute.
                    if (force_coin >= 0) {
                        logger.info("Coin going down : " + force_coin);
                    } else if (force_coin < 0) {
                        logger.info("Coin going up : " + force_coin);
                    }

                    coinToCatch.setPositionY(this.force_coin + coinToCatch.getPosition().y);
                    this.force_coin += CONSTANTS.GRAVITY;
                    if (this.force_coin >= CONSTANTS.FORCE_MAX_COIN) {
                        this.force_coin = CONSTANTS.FORCE_MAX_COIN;
                    }

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

    public void setTheCoinToCatch(Coin coinToCatch) {
        this.coinToCatch = coinToCatch;
    }

}
