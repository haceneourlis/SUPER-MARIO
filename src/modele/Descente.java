package modele;

import java.util.logging.*;

/**
 * Cette classe est un thread qui va gérer la gravité de Mario en appliquant une
 * force
 * vers le bas (gravité).
 */
public class Descente extends Thread {

    private Mario mario;
    // La force ici peut être positive (descente) comme négative (saut).
    public int force_mario = 0;

    public boolean marioAllowedToFallDown = true;
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

                if (marioAllowedToFallDown) {
                    // On vérifie si la force est négative il descend
                    // sinon il saute.
                    if (force_mario >= 0) {
                        mario.setDirection("down");
                    } else if (force_mario < 0) {
                        mario.setDirection("up");
                    }

                    this.force_mario += CONSTANTS.GRAVITY;
                    if (this.force_mario >= CONSTANTS.FORCE_MAX) {
                        this.force_mario = CONSTANTS.FORCE_MAX;
                    }

                    mario.setPositionY(this.force_mario + mario.getPosition().y);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
