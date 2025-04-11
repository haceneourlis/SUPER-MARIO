package modele;

import java.util.logging.*;

/**
 * Cette classe est un thread qui va gérer la gravité de Mario en appliquant une
 * force
 * vers le bas (gravité).
 */
public class Descente extends Thread {

    // La force ici peut être positive (descente) comme négative (saut).
    public int force = 0;

    private static final int DELAY = 17;

    private GameCharacter gc;

    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public Descente(GameCharacter gc) {
        this.gc = gc;
    }

    @Override
    public void run() {
        while (true) {
            try {

                Thread.sleep(DELAY);

                // à chaque instant t , si le mario n'est pas sur le sol : CONSTANTS.LE_SOL
                // alors il doit descendre

                if (this.gc.allowedToFallDown) {
                    // On vérifie si la force est négative il descend
                    // sinon il saute.
                    if (force >= 0) {
                        this.gc.setDirection("down");
                    } else if (force < 0) {
                        this.gc.setDirection("up");
                    }

                    this.force += CONSTANTS.GRAVITY;
                    if (this.force >= CONSTANTS.FORCE_MAX) {
                        this.force = CONSTANTS.FORCE_MAX;
                    }

                    this.gc.setPositionY(this.force + this.gc.getPosition().y);
                }

            } catch (Exception e) {
                logger.log(Level.WARNING, "Erreur thread");
            }
        }
    }

}
