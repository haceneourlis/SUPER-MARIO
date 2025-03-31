package modele;

import java.util.logging.*;
import vue.Affichage;

/**
 * Cette classe est un thread qui va gérer la gravité de Mario en appliquant une
 * force
 * vers le bas (gravité).
 */
public class Descente extends Thread {

    private Mario mario;
    // La force ici peut être positive (descente) comme négative (saut).
    public int force = 0;

    public boolean allowedToFallDown = true;

    private static final int DELAY = 17;

    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public Descente() {
        this.mario = Mario.getInstance();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(DELAY);

                // à chaque instant t , si le mario n'est pas sur le sol : CONSTANTS.LE_SOL
                // alors il doit descendre

                if (allowedToFallDown) {
                    // On vérifie si la force est négative il descend
                    // sinon il saute.
                    if (force >= 0) {
                        mario.setDirection("down");
                    } else if (force < 0) {
                        mario.setDirection("up");
                    }

                    mario.setPositionY(this.force + mario.getPosition().y);
                    this.force += CONSTANTS.GRAVITY;

                    logger.log(Level.WARNING, "mettre une valeur maximale à la force ici ");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
