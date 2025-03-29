package modele;

import java.util.logging.*;
import vue.Affichage;


/**
 * Cette classe est un thread qui va gérer la gravité de Mario en appliquant une force
 * vers le bas (gravité).
 */
public class Descente extends Thread {


    private Mario mario;
    private Affichage gp;

    /*
     * leSol est la position y du sol, ce " sol " pourrait etre le vrai sol (
     * CONSTANTS.LE_SOL )
     * ou bien une briquette ou un tuyau ou un bloc de brique ...
     */
    private int leSol = CONSTANTS.LE_SOL;

    // La force ici peut être positive (descente) comme négative (saut).
    public int force = 0;

    public boolean allowedToFallDown = true;

    private static boolean canFallDown = true;

    private static final int DELAY = 17;

    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public Descente(Affichage affichage) {
        gp = affichage;
        this.mario = Mario.getInstance();
        logger.log(Level.WARNING, "Hacene supprime la méthode getCanFallDown si tu vois que tu n'en as pas besoin s'il te plait");
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
                    } else if (force < 0){
                        mario.setDirection("up");
                    }  

                    mario.setPositionY(this.force + mario.getPosition().y);
                    this.force += CONSTANTS.GRAVITY;
                    
                    if (mario.getPosition().y >= leSol) {
                        this.force = 0;
                        mario.position.y = leSol; // on remet le mario sur le sol
                        this.allowedToFallDown = false;
                    }
                } 
                

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSol(int newSol) {
        leSol = newSol;
    }
    
    public static boolean getCanFallDown() {
        return canFallDown;
    }
}
