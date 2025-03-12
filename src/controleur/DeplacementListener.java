package controleur;

import modele.*;

public class DeplacementListener extends Thread {
    private MouvementJoueur mj;
    private Mario j;
    private Jumping jumpin;
    // delay
    public final int DELAY = 30;

    public DeplacementListener(MouvementJoueur mj, Mario j, Jumping jumpin) {
        this.mj = mj;
        this.j = j;
        this.jumpin = jumpin;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mj.isLeft_pressed()) {
                j.deplacer_gauche();
            }
            if (mj.isRight_pressed()) {
                j.deplacer_droite();
            }
            if (mj.isSpace_pressed()) {
                jumpin.jump();
            }
        }
    }

}
