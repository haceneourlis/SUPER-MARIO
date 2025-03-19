package modele;

import vue.Affichage;

public class Descente extends Thread {

    // cette classe est un thread qui va gérer la gravité au sens réel du terme dans
    // ce jeu
    // ça veut dire que le mario sera sous l'effet de la gravité et il va descendre
    // tout seul

    private Mario mario;
    private Affichage gp;
    private Jumping jumpingThread;

    /*
     * leSol est la position y du sol, ce " sol " pourrait etre le vrai sol (
     * CONSTANTS.LE_SOL )
     * ou bien une briquette ou un tuyau ou un bloc de brique ...
     */
    private int leSol = CONSTANTS.LE_SOL;
    public boolean allowedToFallDown = true;

    private static boolean canFallDown = true;

    public Descente(Affichage affichage, Jumping jumpingThread) {
        gp = affichage;
        this.mario = Mario.getInstance();
        this.jumpingThread = jumpingThread;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(16);

                // à chaque instant t , si le mario n'est pas sur le sol : CONSTANTS.LE_SOL
                // alors il doit descendre

                if (allowedToFallDown) {

                    if (mario.getPosition().y < leSol) {
                        mario.setDirection("down");
                        mario.setPositionY(CONSTANTS.GRAVITY + mario.getPosition().y);
                        // System.out.println("Mario is falling down");
                    }
                    if (mario.getPosition().y >= leSol) {
                        mario.position.y = leSol; // on remet le mario sur le sol

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
