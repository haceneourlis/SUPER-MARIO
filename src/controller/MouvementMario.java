package controleur;


import view.Mario;
import model.Jumping;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MouvementMario implements KeyListener {
    private Mario Mario;
    private Jumping jumpin;

    public MouvementMario(Mario Mario, Jumping jumpin) {
        this.Mario = Mario;
        this.jumpin = jumpin;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_RIGHT) { // Flèche droite
            this.Mario.deplacer_droite();
            // faire un print de debug
            System.out.println("Droite");

        } else if (keyCode == KeyEvent.VK_LEFT) { // Flèche gauche
            this.Mario.deplacer_gauche();
        } else if (keyCode == KeyEvent.VK_SPACE) {
            System.out.println("Espace");
            this.jumpin.jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // si la touche est relachée on doit réinitialiser la vitesse à 0
        Mario.reinitialiserVitesse();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Rien à faire ici pour le moment
    }
}
